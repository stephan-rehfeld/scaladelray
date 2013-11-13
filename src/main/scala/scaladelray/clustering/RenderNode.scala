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
import akka.actor.{Props, ActorSystem}
import akka.pattern.ask
import com.typesafe.config.ConfigFactory
import scala.swing._
import scala.swing.GridBagPanel.{Anchor, Fill}
import java.awt.Dimension
import scala.collection.mutable
import java.util.Date
import scala.swing.event.ButtonClicked
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await


/**
 * The render node application. It opens a windows where the user can configure the interface, the port, and the number
 * of cores that are provided. The service can be started and stopped in the UI.
 */
object RenderNode extends SimpleSwingApplication {

  /**
   * The actor system where the rendering actors run in.
   */
  private var remoteActorSystem : Option[ActorSystem] = None

  /**
   * The socket of the [[scaladelray.clustering.BeaconActor]]. It is used to close the multicast socket and shutdown
   * the BeaconNActor properly.
   */
  private var beaconSocket : Option[MulticastSocket] = None

  Logging.loggingActor ! LogMessage( new Date, "Application started" )

  override def top = new MainFrame {
    title = "ScalaDelRay rendering node"
    contents = ui
    size = new Dimension( 320, 480 )
    resizable = false
  }

  /**
   * The UI.
   */
  private lazy val ui = new GridBagPanel {

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


