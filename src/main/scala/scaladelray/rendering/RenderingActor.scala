package scaladelray.rendering

import scaladelray.camera.Camera
import scaladelray.{Tracer, Color, World}
import akka.actor.Actor

case class Render( startX : Int, startY : Int, width : Int, height : Int, cam : Camera )

class RenderingActor( world : World, id : Int, recursionDepth : Int ) extends Actor {

  def receive = {
    case msg : Render =>
      val pixel = scala.collection.mutable.MutableList[(Int,Int,Int)]()
      for( x <- msg.startX until msg.width ) {
        for( y <- msg.startY until msg.height ) yield {
          val rays = msg.cam( x, y )
          var c = Color( 0, 0, 0 )
          for( ray <- rays ) c = c + Tracer.standardTracer( ray, world, recursionDepth )

          c = c / rays.size

          pixel += ((x,y,c.rgbInteger))
        }
      }
      sender ! pixel.toList

  }

}
