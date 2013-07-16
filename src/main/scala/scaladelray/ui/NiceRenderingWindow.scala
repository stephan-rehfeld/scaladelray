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
import java.awt.{Graphics2D, Dimension}
import akka.actor.{Actor, Props, ActorSystem}
import akka.routing.RoundRobinRouter
import java.awt.image.BufferedImage
import scala.concurrent.{Await, Future}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scaladelray.World
import javax.imageio.ImageIO

case class StartRendering()

class NiceRenderingWindow( world : World, camera : (Int,Int) => Camera, s : Dimension, actors : Int ) extends Frame {

  title = "Rendering"
  size = s
  minimumSize = s
  resizable = false
  visible = true

  implicit val timeout = Timeout(5 hours)

  val win = this

  private val actorSystem = ActorSystem("Rendering")
  val targets = actorSystem.actorOf( Props( new RenderingActor( world, 0 ) ).withRouter( RoundRobinRouter( nrOfInstances = Runtime.getRuntime.availableProcessors() ) ) )

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
    def receive = {
      case msg : StartRendering =>
        var futures = scala.collection.mutable.MutableList[Future[Any]]()

        for( i <- 0 until s.height ) {
          val ftr = targets ? Render( 0, i, s.width,  i+1, cam )
          futures += ftr
        }

        for( future <- futures ) {
          val pixel = Await.result( future, timeout.duration ).asInstanceOf[List[(Int,Int,Int)]]

          for( (x,y,c) <- pixel ) {
            raster.setDataElements(x, s.height-1-y, model.getDataElements(c, null))
            win.repaint()
          }
        }
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

    actorSystem.shutdown()
  }
}
