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

import swing.{Component, MainFrame, SimpleSwingApplication}
import java.awt.{Graphics2D, Dimension}
import java.awt.image.BufferedImage
import akka.actor.{Props, Actor, ActorSystem}

import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{Future, Await}
import akka.routing.RoundRobinRouter
import scaladelray.{Tracer, Color, World}
import scaladelray.camera.Camera

case class Render( startX : Int, startY : Int, width : Int, height : Int, cam : Camera )

class RenderingActor( world : World, id : Int ) extends Actor {

  def receive = {
    case msg : Render =>
      val pixel = scala.collection.mutable.MutableList[(Int,Int,Int)]()
      for( x <- msg.startX until msg.width ) {
        for( y <- msg.startY until msg.height ) yield {
          val rays = msg.cam( x, y )
          var c = Color( 0, 0, 0 )
          for( ray <- rays ) c = c + Tracer.standardTracer( ray, world, 10 )

          c = c / rays.size

          pixel += ((x,y,c.rgbInteger))
        }
      }
      sender ! pixel.toList
  }

}

abstract class WindowedRayTracer extends SimpleSwingApplication {

  private val actorSystem = ActorSystem("Rendering")
  def world : World
  def camera : ((Int,Int) => Camera)
  println( "Running with " + Runtime.getRuntime.availableProcessors() + " processors" )
  val targets = actorSystem.actorOf( Props( new RenderingActor( world, 0 ) ).withRouter( RoundRobinRouter( nrOfInstances = Runtime.getRuntime.availableProcessors() ) ) )


  implicit val timeout = Timeout(5 hours)


  def top = new MainFrame {

    title = "WindowedRayTracer"
    minimumSize = new Dimension( 640, 480 )

    contents = new Component {
      override def paintComponent( g: Graphics2D ) = {
        val image = new BufferedImage(this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int], BufferedImage.TYPE_INT_ARGB)
        val model = image.getColorModel
        val raster = image.getRaster
        val cam = camera( this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int] )
        val begin = System.currentTimeMillis()

        var futures = scala.collection.mutable.MutableList[Future[Any]]()

        for( i <- 0 until this.size.getHeight.asInstanceOf[Int] ) {
          val ftr = targets ? Render( 0, i, this.size.getWidth.asInstanceOf[Int],  i+1, cam )
          futures += ftr
        }

        for( future <- futures ) {
          val pixel = Await.result( future, timeout.duration ).asInstanceOf[List[(Int,Int,Int)]]

          for( (x,y,c) <- pixel ) {
            raster.setDataElements(x, this.size.getHeight.asInstanceOf[Int]-1-y, model.getDataElements(c, null))
          }
        }

        val end = System.currentTimeMillis()
        println( "Rendering took : " + ((end-begin).asInstanceOf[Double] / 1000.0) + " seconds.")
        g.drawImage(image, 0, 0, null)
      }
    }
  }

}
