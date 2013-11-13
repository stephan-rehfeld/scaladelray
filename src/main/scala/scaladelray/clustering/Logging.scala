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

import akka.actor.{Actor, Props, ActorSystem}
import java.util.Date
import scala.swing.Table
import scala.collection.mutable
import javax.swing.event.{TableModelEvent, TableModelListener}
import java.text.SimpleDateFormat
import javax.swing.table.TableModel

/**
 * This message adds a logged event to the database of the [[scaladelray.clustering.LoggingActor]].
 *
 * @param date The date and time of the event.
 * @param message A message that describes the event.
 */
case class LogMessage( date : Date, message : String )

/**
 * This message promotes a table to the [[scaladelray.clustering.LoggingActor]]. The LoggingActor writes is database
 * to this table.
 *
 * @param table The table where the databse of the LoggingActor is showed in.
 */
case class PromoteTable( table : Table )

/**
 * The LoggingActor processes [[scaladelray.clustering.LogMessage]]s and stores the events in a database. If a Table
 * has been promoted to the LoggingActor via a [[scaladelray.clustering.PromoteTable]] message, the database of events
 * is displayed in this table.
 */
class LoggingActor extends Actor {

  /**
   * The table model listener that needs to be updates when a new event occurred.
   */
  private val listener = mutable.MutableList[TableModelListener]()

  /**
   * The table where the database is displayed in.
   */
  private var table : Option[Table] = None

  /**
   * The format of the date and time in the table.
   */
  private val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

  /**
   * The database and table model. A mutable List extended by the TableModel interface.
   */
  private val tableModel = new mutable.MutableList[(String,String)] with TableModel {

    override def getRowCount: Int = this.size

    override def getColumnCount: Int = 2

    override def getColumnName( column : Int): String = column match {
      case 0 => "Time"
      case 1 => "Event"
    }

    override def getColumnClass(row: Int): Class[_] = classOf[String]

    override def isCellEditable(row: Int, column: Int): Boolean = false

    override def getValueAt(row: Int, column: Int): AnyRef = {
      val (time,message) = this(row)
      column match {
        case 0 => time
        case 1 => message
      }
    }

    override def setValueAt(obj: Any, row: Int, column: Int) {}

    override def addTableModelListener(p1: TableModelListener) {
      listener += p1
    }

    override def removeTableModelListener(p1: TableModelListener) {}

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

/**
 * This singleton holds the actor system for the logging and an instance of the [[scaladelray.clustering.LoggingActor]].
 */
object Logging {

  /**
   * The actor system for logging.
   */
  private lazy val loggingActorSystem = ActorSystem( "logging" )

  /**
   * The logging actor.
   */
  lazy val loggingActor = loggingActorSystem.actorOf( Props[LoggingActor ] )
}
