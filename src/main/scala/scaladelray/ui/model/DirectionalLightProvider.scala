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

package scaladelray.ui.model

import javax.swing.table.TableModel
import scaladelray.Color
import scaladelray.math.{Vector3, Point3}
import scaladelray.light.{DirectionalLight, SpotLight, LightDescription}
import java.lang.Double
import javax.swing.event.TableModelListener

class DirectionalLightProvider extends LightDescriptionProvider with TableModel {

  var color = Color( 1, 1, 1 )
  var direction = Vector3( 0, 0, -1 )

  def createLightDescription: LightDescription = new DirectionalLight( color, direction )

  def getRowCount: Int = 2

  def getColumnCount: Int = 2

  def getColumnName( column : Int): String = column match {
    case 0 => "Property"
    case 1 => "Value"
  }

  def getColumnClass(row: Int): Class[_] = classOf[String]

  def isCellEditable(row: Int, column: Int): Boolean = column match {
    case 0 => false
    case 1 => true
  }

  def getValueAt(row: Int, column: Int): AnyRef = column match {
    case 0 =>
      row match {
        case 0 =>
          "Color"
        case 1 =>
          "Direction"

      }
    case 1 =>
      row match {
        case 0 =>
          "" + color.r + " " + color.g + " " + color.b
        case 1 =>
          "" + direction.x + " " + direction.y + " " + direction.z
      }
  }

  def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          color = Color( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          val v = obj.asInstanceOf[String].split( " " )
          direction = Vector3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
      }
    } catch {
      case _ : Throwable =>
    }

  }

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}

  override def toString: String = "Directional Light"

}
