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

case class StartBeacon( beaconPort : Int, servicePort : Int )
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
          println( "Receiving" )
          socket.receive(packet)
          println( "Received" )
          val data = new String( packet.getData, 0, packet.getLength )
          if( data == "ScalaDelRay:1.0" ) {
            val address = packet.getAddress
            val port = packet.getPort

            println( "Got request from " + address + ":" + port )
            val portBuf = new Array[Byte]( 4 )
            portBuf(0) = ((m.servicePort & 0xff000000) >> 24).asInstanceOf[Byte]
            portBuf(1) = ((m.servicePort & 0xff0000) >> 16).asInstanceOf[Byte]
            portBuf(2) = ((m.servicePort & 0xff00) >> 8).asInstanceOf[Byte]
            portBuf(3) = (m.servicePort & 0xff).asInstanceOf[Byte]

            packet.setData( portBuf )

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

    val actorSystem = ActorSystem( "renderNode" )
    val beaconActor = actorSystem.actorOf( Props( new BeaconActor ) )
    beaconActor ! StartBeacon( 12345, 4444 )

  }
}
