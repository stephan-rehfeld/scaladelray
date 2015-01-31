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

import java.net.{SocketException, DatagramPacket, InetAddress, MulticastSocket}
import akka.actor.Actor

/**
 * If a [[scaladelray.clustering.BeaconActor]] receives this message it opens a port to listen on broadcast messages
 * and replies to them by transmitting information on how to connect to this render node. The BeaconActor replies with
 * a [[scaladelray.clustering.BeaconSocket]] message.
 *
 * @param beaconPort The port where the actor listens for broadcast messages.
 * @param interface The interface where the actor listens for broadcast messages.
 * @param servicePort The port of the actor subsystems.
 * @param cores The number of cores that the render nodes provides for rendering.
 */
case class StartBeacon( beaconPort : Int, interface : String, servicePort : Int, cores : Int )

/**
 * This message is sent by the [[scaladelray.clustering.BeaconActor]] and contains the multicast port. This port
 * is required to shutdown the actor.
 *
 * @param socket The socket that was creates by the BeaconActor and where it listens for broadcast messages.
 */
case class BeaconSocket( socket : MulticastSocket )

/**
 * The BeaconActor opens a multicast port and listens for broadcast messages of the render node discovery service.
 * It replies on broadcast message and transmits information on how to connect to this node.
 *
 */
class BeaconActor extends Actor {

  override def receive = {
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
            val interfaceData = m.interface.getBytes

            val address = packet.getAddress
            val port = packet.getPort

            val dataBuffer = new Array[Byte]( interfaceData.size + 8 )

            dataBuffer(0) = ((m.servicePort & 0xff000000) >> 24).asInstanceOf[Byte]
            dataBuffer(1) = ((m.servicePort & 0xff0000) >> 16).asInstanceOf[Byte]
            dataBuffer(2) = ((m.servicePort & 0xff00) >> 8).asInstanceOf[Byte]
            dataBuffer(3) = (m.servicePort & 0xff).asInstanceOf[Byte]

            dataBuffer(4) = ((m.cores & 0xff000000) >> 24).asInstanceOf[Byte]
            dataBuffer(5) = ((m.cores & 0xff0000) >> 16).asInstanceOf[Byte]
            dataBuffer(6) = ((m.cores & 0xff00) >> 8).asInstanceOf[Byte]
            dataBuffer(7) = (m.cores & 0xff).asInstanceOf[Byte]

            for( i <- 0 until interfaceData.size ) dataBuffer( i+8 ) = interfaceData( i )

            val answerBuf = new Array[Byte]( 256 )
            val answer = new DatagramPacket(answerBuf, answerBuf.size)
            answer.setAddress( address )
            answer.setPort( port )
            answer.setData( dataBuffer )

            socket.send( answer )



          }
        } catch {
          case ex : SocketException => running = false
        }
      }
  }
}

