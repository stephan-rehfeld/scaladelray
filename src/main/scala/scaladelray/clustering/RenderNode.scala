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

package scaladelray.clustering

import java.net._
import akka.actor.{Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory

case class StartBeacon( beaconPort : Int, servicePort : Int, cores : Int )
case class BeaconSocket( socket : MulticastSocket )

class BeaconActor extends Actor {

  def receive = {
    case m : StartBeacon =>
      val socket = new MulticastSocket( m.beaconPort )
      val group = InetAddress.getByName("228.5.6.7")
      socket.joinGroup( group )
      sender ! BeaconSocket( socket )

      var running = true
      while( running ) {
        val buf = new Array[Byte]( 256 )
        val packet = new DatagramPacket(buf, buf.size)

        try {
          socket.receive(packet)
          val data = new String( packet.getData, 0, packet.getLength )
          if( data == "ScalaDelRay:1.0" ) {
            val address = packet.getAddress
            val port = packet.getPort

            val dataBuffer = new Array[Byte]( 8 )
            dataBuffer(0) = ((m.servicePort & 0xff000000) >> 24).asInstanceOf[Byte]
            dataBuffer(1) = ((m.servicePort & 0xff0000) >> 16).asInstanceOf[Byte]
            dataBuffer(2) = ((m.servicePort & 0xff00) >> 8).asInstanceOf[Byte]
            dataBuffer(3) = (m.servicePort & 0xff).asInstanceOf[Byte]

            dataBuffer(4) = ((m.cores & 0xff000000) >> 24).asInstanceOf[Byte]
            dataBuffer(5) = ((m.cores & 0xff0000) >> 16).asInstanceOf[Byte]
            dataBuffer(6) = ((m.cores & 0xff00) >> 8).asInstanceOf[Byte]
            dataBuffer(7) = (m.cores & 0xff).asInstanceOf[Byte]

            packet.setData( dataBuffer )

            socket.send( packet )

          }
        } catch {
          case ex : SocketException => running = false
        }
      }
  }
}

object RenderNode {
  def main( args : Array[String] ) = {

    val port = 4444

    val config = ConfigFactory.parseString("""
      akka {
        actor {
          provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "127.0.0.1"
            port = """ + port + """
          }
        }
      }""")

    val actorSystem = ActorSystem( "renderNode", config )
    val beaconActor = actorSystem.actorOf( Props[BeaconActor] )
    beaconActor ! StartBeacon( 12345, port, Runtime.getRuntime.availableProcessors() )

  }
}
