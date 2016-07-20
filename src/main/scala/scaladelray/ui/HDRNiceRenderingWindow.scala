/*
 * Copyright 2015 Stephan Rehfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scaladelray.ui

import java.awt.image.BufferedImage
import java.awt.{Dimension, Graphics2D}
import javax.imageio.ImageIO

import akka.actor._
import akka.pattern.ask
import akka.remote.RemoteScope
import akka.routing._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.reflectiveCalls
import scala.swing.GridBagPanel.Fill
import scala.swing._
import scaladelray.HDRImage
import scaladelray.camera.Camera
import scaladelray.math.i.{Point2, Rectangle, Size2}
import scaladelray.rendering.{Algorithm, HDRRender, HDRRenderingActor}

case class StartRendering()

class HDRNiceRenderingWindow( camera : Camera, s : Dimension, actors : Int, clusterNodes : List[(String,Int,Int)], algorithm : Algorithm ) extends Frame {

  title = "Rendering"
  size = s
  minimumSize = s
  resizable = false
  visible = true


  implicit val timeout = Timeout(5 hours)

  val win = this

  val config = ConfigFactory.parseString("""
      akka {
        actor {
          provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "127.0.0.1"
            port = 4441
          }
        }
      }""")//.withFallback(ConfigFactory.load())

  private val actorSystem = if( clusterNodes.isEmpty ) ActorSystem("Rendering") else ActorSystem("Rendering",config)

  val targets = createRenderNodes

  val originalHDRImage = HDRImage( Size2( this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int] ) )
  val _adjustedHDRImage = HDRImage( Size2( this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int] ) )

  def adjustedHDRImage = _adjustedHDRImage
  def adjustedHDRImage_=( newImage : HDRImage ) {
    for{
      x <- 0 until newImage.size.width
      y <- 0 until newImage.size.height
    } _adjustedHDRImage.set( x, y, newImage( x, y ) )
    win.repaint()
  }

  val brightnessAdjustmentWindow = new BrightnessAdjustmentWindow( originalHDRImage, this )


  menuBar = new MenuBar
  val fileMenu = new Menu( "FILE" )
  val saveMenuItem = new MenuItem( Action("Save") {
    val fsd = new FileChooser
    val result = fsd.showSaveDialog( null )
    if( result == FileChooser.Result.Approve ) {
      val file = fsd.selectedFile
      val extension = file.getName.split( '.' ).last
      val awtImage = new BufferedImage( _adjustedHDRImage.size.width,  _adjustedHDRImage.size.height, BufferedImage.TYPE_INT_ARGB)

      val model = awtImage.getColorModel
      val raster = awtImage.getRaster
      for {
        x <- 0 until _adjustedHDRImage.size.width
        y <- 0 until _adjustedHDRImage.size.height } {
        val c = _adjustedHDRImage( x, y )
        val color = new java.awt.Color( math.min(c.r.asInstanceOf[Float], 1.0f), math.min(c.g.asInstanceOf[Float], 1.0f), math.min(c.b.asInstanceOf[Float], 1.0f) )
        raster.setDataElements(x, _adjustedHDRImage.size.height-1-y, model.getDataElements(color.getRGB, null))
      }
      ImageIO.write( awtImage, extension, file )

    }
  })
  saveMenuItem.enabled = false

  fileMenu.contents += saveMenuItem
  menuBar.contents += fileMenu

  val a = actorSystem.actorOf( Props( new Actor {

    val begin = System.currentTimeMillis()

    def receive = {
      case msg : StartRendering =>
        var futures = scala.collection.mutable.MutableList[Future[Any]]()

        /*for( i <- 0 until s.height ) {
          val ftr = targets ? HDRRender( HDRImage.Rectangle( 0, i, s.width,  i+1) , cam )
          futures += ftr
        }*/
        val tiles = 10
        val tileWidth = originalHDRImage.size.width / tiles
        val tileHeight = originalHDRImage.size.height / tiles

        for {
          tileX <- 0 until tiles
          tileY <- 0 until tiles
        } {
          val ftr = targets ? HDRRender( camera, originalHDRImage.size, Rectangle( Point2( tileX * tileWidth, tileY * tileHeight ), Size2( tileWidth, tileHeight) ) )
          futures += ftr
        }

        var futuresResolved = 0

        val start = System.currentTimeMillis()

        val infoWindowUpdateActor = actorSystem.actorOf( Props( new Actor {
          this.context.setReceiveTimeout( 1 second )
          def receive = {
            case m : ReceiveTimeout =>

              var time = (System.currentTimeMillis() - start) / 1000
              var month = time / (60*60*24*30)
              time = time % (60*60*24*30)
              var days = time / (60*60*24)
              time = time % (60*60*24)
              var hours = time / (60*60)
              time = time % (60*60)
              var minutes = time / 60
              var seconds = time % 60

              infoWindow.infoUI.timeInfoLabel.text = "" + month + " month " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds"

              if( futuresResolved > 0 ) {
                time = (System.currentTimeMillis() - start) / 1000

                time = time * futures.size / futuresResolved

                month = time / (60*60*24*30)
                time = time % (60*60*24*30)
                days = time / (60*60*24)
                time = time % (60*60*24)
                hours = time / (60*60)
                time = time % (60*60)
                minutes = time / 60
                seconds = time % 60

                infoWindow.infoUI.estimatedInfoLabel.text = "" + month + " month " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds"
              }



              infoWindow.infoUI.progressBar.value = futuresResolved * 100 / futures.size

          }
        }))

        for( future <- futures ) {
          val (tileRectangle,tileImage) = Await.result( future, timeout.duration ).asInstanceOf[(Rectangle,HDRImage)]

          for { x <- tileRectangle.corner.x until tileRectangle.corner.x + tileRectangle.size.width
                y <- tileRectangle.corner.y until tileRectangle.corner.y + tileRectangle.size.height
          } {
            originalHDRImage.set( x, y, tileImage( x - tileRectangle.corner.x, y - tileRectangle.corner.y ) )

          }
          brightnessAdjustmentWindow.img = originalHDRImage


          futuresResolved = futuresResolved + 1

        }
        infoWindow.infoUI.estimatedInfoLabel.text = infoWindow.infoUI.timeInfoLabel.text
        infoWindow.infoUI.progressBar.value = 100
        infoWindow.title = "Finished"
        infoWindowUpdateActor ! PoisonPill
        saveMenuItem.enabled = true
    }
  }))

  contents = new Component {
    override def paintComponent( g: Graphics2D ) = {
      super.paintComponent( g )
      val awtImage = new BufferedImage( _adjustedHDRImage.size.width,  _adjustedHDRImage.size.height, BufferedImage.TYPE_INT_ARGB)

      val model = awtImage.getColorModel
      val raster = awtImage.getRaster
      for {
        x <- 0 until _adjustedHDRImage.size.width
        y <- 0 until _adjustedHDRImage.size.height } {
        val c = _adjustedHDRImage( x, y )
        val color = new java.awt.Color( math.min(c.r.asInstanceOf[Float], 1.0f), math.min(c.g.asInstanceOf[Float], 1.0f), math.min(c.b.asInstanceOf[Float], 1.0f) )
        raster.setDataElements(x, _adjustedHDRImage.size.height-1-y, model.getDataElements(color.getRGB, null))
      }
      g.drawImage(awtImage, 0, 0, null)
    }
  }

  override def closeOperation() {
    super.closeOperation()
    infoWindow.close()
    targets ! Broadcast( PoisonPill )
    Thread.sleep( 1000 )
    actorSystem.terminate()
  }

  val infoWindow = new Frame {
    title = "Rendering..."

    val infoUI = new GridBagPanel {
      val c = new Constraints

      val timeLabel = new Label( "Time:" )

      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 0
      c.gridy = 0
      layout( timeLabel ) = c

      val timeInfoLabel = new Label( "0 month 0 days 0 hours 0 minutes 0 seconds")

      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 1
      c.gridy = 0
      layout( timeInfoLabel ) = c

      val estimatedLabel = new Label( "Estimated:" )

      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 0
      c.gridy = 1
      layout( estimatedLabel ) = c

      val estimatedInfoLabel = new Label( "?")

      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 1
      c.gridy = 1
      layout( estimatedInfoLabel ) = c

      val progressBar = new ProgressBar{
        min = 0
        max = 100
        value = 0
        label = "Rendering..."
      }

      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 0
      c.gridy = 2
      c.gridwidth = 2
      layout( progressBar ) = c
    }

    contents = infoUI
    visible = true
  }

  private def createRenderNodes : ActorRef = {
    val targets = mutable.MutableList[Routee]()

    for( i <- 1 to Runtime.getRuntime.availableProcessors() ) {
      targets += ActorRefRoutee( actorSystem.actorOf( Props( classOf[HDRRenderingActor], algorithm ) ) )
    }

    for( (hostname,port,threads) <- clusterNodes ) {
      val address = Address("akka.tcp", "renderNode", hostname, port )
      for( i <- 1 to threads ) {
        targets += ActorRefRoutee( actorSystem.actorOf( Props( classOf[HDRRenderingActor], algorithm ).withDeploy(Deploy(scope = RemoteScope(address))) ) )
      }
    }

    val router = Router( RoundRobinRoutingLogic(), targets.toIndexedSeq )
    actorSystem.actorOf( Props.apply( new Actor {
      override def receive: Receive = {
        case msg  => router.route( msg, sender() )
      }
    }))
  }
}

