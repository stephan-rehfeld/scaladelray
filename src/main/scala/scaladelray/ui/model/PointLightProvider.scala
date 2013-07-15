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
import javax.swing.event.TableModelListener
import scaladelray.light.{PointLight, LightDescription}
import java.lang
import java.lang.Double

class PointLightProvider extends LightDescriptionProvider with TableModel {

  var color = Color( 1, 1, 1 )
  var position = Point3( 0, 0, 0 )
  var castsShadows = true
  var constantAttenuation = 1.0
  var linearAttenuation = 0.0
  var quadraticAttenuation = 0.0


  def createLightDescription: LightDescription = new PointLight( color, position, castsShadows, constantAttenuation, linearAttenuation, quadraticAttenuation )

  def getRowCount: Int = 6

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
          "Position"
        case 2 =>
          "Casts shadows"
        case 3 =>
          "Constant Attenuation"
        case 4 =>
          "Linear Attenuation"
        case 5 =>
          "Quadratic Attenuation"

      }
    case 1 =>
      row match {
        case 0 =>
          "" + color.r + " " + color.g + " " + color.b
        case 1 =>
          "" + position.x + " " + position.y + " " + position.z
        case 2 =>
          new lang.Boolean( castsShadows )
        case 3 =>
          new Double( constantAttenuation )
        case 4 =>
          new Double( linearAttenuation )
        case 5 =>
          new Double( quadraticAttenuation )
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
          position = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 2 =>
          val v = obj.asInstanceOf[Boolean]
         castsShadows = v
        case 3 =>
          val v = obj.asInstanceOf[String].toDouble
          constantAttenuation = v
        case 4 =>
          val v = obj.asInstanceOf[String].toDouble
          linearAttenuation = v
        case 5 =>
          val v = obj.asInstanceOf[String].toDouble
          quadraticAttenuation = v

      }
    } catch {
      case _ : Throwable =>
    }

  }

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}

  override def toString: String = "Point Light"
}
