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

package scaladelray

import camera.Camera
import swing.{Component, MainFrame, SimpleSwingApplication}
import java.awt.{Graphics2D, Dimension}
import java.awt.image.BufferedImage
import akka.actor.{Props, Actor, ActorSystem}
import scaladelray.math.Ray

import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{Future, Await}
import akka.routing.RoundRobinRouter

case class Render( startX : Int, startY : Int, width : Int, height : Int, cam : Camera )

class RenderingActor( world : World, id : Int ) extends Actor {

  println( "Rendering Actor Started: " + id )
  def receive = {
    case msg : Render =>
      val pixel = scala.collection.mutable.MutableList[(Int,Int,Int)]()
      for( x <- msg.startX until msg.width ) {
        for( y <- msg.startY until msg.height ) yield {
          val ray = msg.cam( x, y )
          val v = (x,y,Tracer.standardTracer( ray, world, 9 ).rgbInteger)
          pixel += v
        }
      }
      sender ! pixel.toList
  }

}

abstract class WindowedRayTracer extends SimpleSwingApplication {

  private val actorSystem = ActorSystem("Rendering")
  def world : World
  def camera : ((Int,Int) => Camera)

  //val actors = for( i <- 1 to 8 ) yield actorSystem.actorOf( Props( new RenderingActor(( world ), i )) )
  val targets = actorSystem.actorOf( Props( new RenderingActor( world, 0 ) ).withRouter( RoundRobinRouter( nrOfInstances = 8 ) ) )


  implicit val timeout = Timeout(5 minutes)


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

        print( "Creating jobs: " )
        val linesPerActor = this.size.getHeight.asInstanceOf[Int] / 8
        for( i <- 0 until 8 ) {
          val ftr = targets ? Render( 0, i * linesPerActor, this.size.getWidth.asInstanceOf[Int],  (i+1) * linesPerActor, cam )
          futures += ftr
        }


        println( "done" )

        for( future <- futures ) {
          val pixel = Await.result( future, timeout.duration ).asInstanceOf[List[(Int,Int,Int)]]

          for( (x,y,c) <- pixel ) {
            raster.setDataElements(x, this.size.getHeight.asInstanceOf[Int]-1-y, model.getDataElements(c, null))
          }


        }

        val end = System.currentTimeMillis()
        println( "Rendering took : " + ((end-begin).asInstanceOf[Double] / 1000.0) + " seconds.")
        //raster.setDataElements(x, y, model.getDataElements((if (x == y) java.awt.Color.RED.getRGB else java.awt.Color.BLACK.getRGB), null))
        g.drawImage(image, 0, 0, null)
      }
    }
  }

}
