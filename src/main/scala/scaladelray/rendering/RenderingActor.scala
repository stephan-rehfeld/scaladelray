package scaladelray.rendering

import akka.actor.SupervisorStrategy.{Escalate, Stop}
import akka.actor.{Actor, ActorKilledException, OneForOneStrategy, SupervisorStrategy}

import scala.concurrent.duration._
import scaladelray.camera.OldCamera
import scaladelray.world.World
import scaladelray.{Color, Tracer}

case class Render( startX : Int, startY : Int, width : Int, height : Int, cam : OldCamera )

class RenderingActor( world : World, id : Int, recursionDepth : Int ) extends Actor {


  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ActorKilledException  => Stop
    case _: Exception             => Escalate
  }

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
