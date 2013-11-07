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
import akka.pattern.ask
import com.typesafe.config.ConfigFactory
import scala.swing._
import scala.swing.GridBagPanel.{Anchor, Fill}
import java.awt.Dimension
import javax.swing.table.TableModel
import javax.swing.event.{TableModelEvent, TableModelListener}
import scala.collection.mutable
import java.util.Date
import java.text.SimpleDateFormat
import scala.swing.event.ButtonClicked
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await


case class StartBeacon( beaconPort : Int, interface : String, servicePort : Int, cores : Int )
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

object RenderNode extends SimpleSwingApplication {


  var remoteActorSystem : Option[ActorSystem] = None
  var beaconSocket : Option[MulticastSocket] = None

  Logging.loggingActor ! LogMessage( new Date, "Application started" )

  def top = new MainFrame {
    title = "ScalaDelRay rendering node"
    contents = ui
    size = new Dimension( 320, 480 )
    resizable = false
  }

  lazy val ui = new GridBagPanel {
    val c = new Constraints


    val interfaceLabel = new Label( "Interface: " )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 0
    c.gridy = 0
    layout( interfaceLabel ) = c

    val addresses = mutable.MutableList[String]()
    val nets = NetworkInterface.getNetworkInterfaces
    while( nets.hasMoreElements ) {
      val net = nets.nextElement
      val iNetAddresses = net.getInetAddresses
      while( iNetAddresses.hasMoreElements ) {
        val iNetAddress = iNetAddresses.nextElement
        addresses += iNetAddress.getHostAddress
      }

    }

    val interfaceComboBox = new ComboBox( addresses )
    c.gridx = 1
    layout( interfaceComboBox ) = c


    val portLabel = new Label( "Port:" )
    c.gridx = 0
    c.gridy = 1
    layout( portLabel ) = c


    val portTextField = new TextField( "7983" )
    c.gridx = 1
    layout( portTextField ) = c


    val threadsLabel = new Label( "Threads:" )
    c.gridx = 0
    c.gridy = 2
    layout( threadsLabel ) = c

    val threadsTextField = new TextField( Runtime.getRuntime.availableProcessors().toString )
    c.gridx = 1
    layout( threadsTextField ) = c

    val startStopButton = new Button( "Start" )
    startStopButton.reactions += {
      case ButtonClicked(_) =>

        if( remoteActorSystem.isEmpty ) {
          startStopButton.enabled = false
          startStopButton.text = "Stop"
          interfaceComboBox.enabled = false
          portTextField.enabled = false
          threadsTextField.enabled = false
          val config = ConfigFactory.parseString("""
            akka {
              actor {
                provider = "akka.remote.RemoteActorRefProvider"
              }
              remote {
                enabled-transports = ["akka.remote.netty.tcp"]
                netty.tcp {
                  hostname = """ + '"' + interfaceComboBox.selection.item + '"' + """
                  port = """ + portTextField.text + """
                }
              }
            }""")

          remoteActorSystem = Some( ActorSystem( "renderNode", config ) )
          Logging.loggingActor ! LogMessage( new Date(), "Started rendering node service" )

          val beaconActor = remoteActorSystem.get.actorOf( Props[BeaconActor] )
          implicit val timeout = Timeout(5 seconds)

          val future = beaconActor ? StartBeacon( 12345, interfaceComboBox.selection.item, portTextField.text.toInt, threadsTextField.text.toInt )
          val bs = Await.result(future, timeout.duration).asInstanceOf[BeaconSocket]
          beaconSocket = Some( bs.socket )

          startStopButton.enabled = true
        } else {
          startStopButton.enabled = false
          beaconSocket.get.close()
          beaconSocket = None
          remoteActorSystem.get.shutdown()
          remoteActorSystem = None
          startStopButton.text = "Start"
          interfaceComboBox.enabled = true
          portTextField.enabled = true
          threadsTextField.enabled = true
          Logging.loggingActor ! LogMessage( new Date(), "Stopped rendering node service" )
          startStopButton.enabled = true

        }
    }
    c.gridx = 0
    c.gridy = 3
    c.gridwidth = 2
    layout( startStopButton ) = c

    val logTable = new Table
    Logging.loggingActor ! PromoteTable( logTable )
    val logTableScrollPane = new ScrollPane {
      contents = logTable
    }

    c.gridy = 4
    c.ipady = 200
    c.weighty = 1.0
    c.anchor = Anchor.PageEnd
    c.fill = Fill.Both
    layout( logTableScrollPane ) = c

    val dummyLabel = new Label( "" )
    c.fill = Fill.Horizontal
    c.ipady = 0
    c.weighty = 0.0
    c.gridx = 1
    c.gridwidth = 1
    c.gridy = 5
    layout(dummyLabel) = c

  }
}

case class LogMessage( date : Date, message : String )
case class PromoteTable( table : Table )

class LoggingActor extends Actor {


  val listener = mutable.MutableList[TableModelListener]()
  var table : Option[Table] = None
  val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
  val tableModel = new mutable.MutableList[(String,String)] with TableModel {

    def getRowCount: Int = this.size

    def getColumnCount: Int = 2

    def getColumnName( column : Int): String = column match {
      case 0 => "Time"
      case 1 => "Event"
    }

    def getColumnClass(row: Int): Class[_] = classOf[String]

    def isCellEditable(row: Int, column: Int): Boolean = false

    def getValueAt(row: Int, column: Int): AnyRef = {
      val (time,message) = this(row)
      column match {
        case 0 => time
        case 1 => message
      }
    }

    def setValueAt(obj: Any, row: Int, column: Int) {}

    def addTableModelListener(p1: TableModelListener) {
      listener += p1
    }

    def removeTableModelListener(p1: TableModelListener) {}

    override def +=(elem : (String,String) ) = {
      super.+=(elem)
      for( l <- listener ) l.tableChanged( new TableModelEvent( this ) )
      this
    }
  }

  override def receive = {
    case LogMessage( date, message ) =>
      tableModel += ((dateFormat.format(date),message))
    case PromoteTable( promotedTable ) =>
      this.table = Some( promotedTable )
      promotedTable.model = tableModel

  }
}


object Logging {
  lazy val loggingActorSystem = ActorSystem( "logging" )
  lazy val loggingActor = loggingActorSystem.actorOf( Props[LoggingActor ] )

}