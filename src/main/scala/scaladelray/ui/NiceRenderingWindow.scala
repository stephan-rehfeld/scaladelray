/*
 * Copyright 2013 Stephan Rehfeld
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

import scala.swing._
import scaladelray.camera.Camera
import scaladelray.World
import java.awt.{GridBagLayout, Graphics2D, Dimension}
import akka.actor._
import akka.routing.RoundRobinRouter
import java.awt.image.BufferedImage
import scala.concurrent.{Await, Future}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scaladelray.World
import javax.imageio.ImageIO
import scala.swing.GridBagPanel.Fill
import scaladelray.World
import scaladelray.rendering.{Render, RenderingActor}
import scala.collection.mutable
import akka.remote.RemoteScope
import com.typesafe.config.ConfigFactory

case class StartRendering()

class TestActor extends Actor {
  println( "Created a test actor" )
  def receive: Actor.Receive = {
    case _ =>
  }
}

class NiceRenderingWindow( world : World, camera : (Int,Int) => Camera, s : Dimension, actors : Int, recursionDepth : Int, clusterNodes : List[(String,Int,Int)] ) extends Frame {

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

  private val actorSystem = ActorSystem("Rendering",config)



  val targets = createRenderNodes

  val image = new BufferedImage(s.width, s.height, BufferedImage.TYPE_INT_ARGB)
  val model = image.getColorModel
  val raster = image.getRaster
  val cam = camera( this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int] )



  menuBar = new MenuBar
  val fileMenu = new Menu( "FILE" )
  val saveMenuItem = new MenuItem( Action("Save") {
    val fsd = new FileChooser
    val result = fsd.showSaveDialog( null )
    if( result == FileChooser.Result.Approve ) {
      val file = fsd.selectedFile
      val extension = file.getName.split( '.' ).last
      ImageIO.write( image, extension, file )

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

        for( i <- 0 until s.height ) {
          val ftr = targets ? Render( 0, i, s.width,  i+1, cam )
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
          val pixel = Await.result( future, timeout.duration ).asInstanceOf[List[(Int,Int,Int)]]

          for( (x,y,c) <- pixel ) {
            raster.setDataElements(x, s.height-1-y, model.getDataElements(c, null))
            win.repaint()
          }

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
      g.drawImage(image, 0, 0, null)
    }
  }

  override def closeOperation() {
    super.closeOperation()
    infoWindow.close()
    targets ! PoisonPill
    actorSystem.shutdown()
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
    val targets = mutable.MutableList[ActorRef]()

    for( i <- 1 to Runtime.getRuntime.availableProcessors() ) {
      targets += actorSystem.actorOf( Props( classOf[RenderingActor], world, 0, recursionDepth ) )
    }

    for( (hostname,port,threads) <- clusterNodes ) {
      val address = Address("akka.tcp", "renderNode", hostname, port )
      for( i <- 1 to threads ) {
        targets += actorSystem.actorOf( Props( classOf[RenderingActor], world, 0, recursionDepth ).withDeploy(Deploy(scope = RemoteScope(address))) )
      }
    }

    actorSystem.actorOf(Props.empty.withRouter( RoundRobinRouter(routees = targets.toList)) )

  }
}
