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
import scaladelray.light.{AreaLight, LightDescription}
import scaladelray.Color
import scaladelray.math.{Vector3, Point3}
import javax.swing.event.TableModelListener
import java.lang.Double

class AreaLightProvider extends LightDescriptionProvider with TableModel {

  var color = Color( 1, 1, 1 )
  var position = Point3( 0, 0, 0 )
  var direction = Vector3( 0, 0, -1 )
  var upVector = Vector3( 0, 1, 0 )
  var size = 1.0
  var samplingPoints = 255
  var constantAttenuation = 1.0
  var linearAttenuation = 0.0
  var quadraticAttenuation = 0.0


  def createLightDescription: LightDescription = new AreaLight( color, position, direction, upVector, size, samplingPoints, constantAttenuation, linearAttenuation, quadraticAttenuation )

  def getRowCount: Int = 9

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
          "Direction"
        case 3 =>
          "Up vector"
        case 4 =>
          "Size"
        case 5 =>
          "Sampling points"
        case 6 =>
          "Constant Attenuation"
        case 7 =>
          "Linear Attenuation"
        case 8 =>
          "Quadratic Attenuation"

      }
    case 1 =>
      row match {
        case 0 =>
          "" + color.r + " " + color.g + " " + color.b
        case 1 =>
          "" + position.x + " " + position.y + " " + position.z
        case 2 =>
          "" + direction.x + " " + direction.y + " " + direction.z
        case 3 =>
          "" + upVector.x + " " + upVector.y + " " + upVector.z
        case 4 =>
          new Double( size )
        case 5 =>
          new Integer( samplingPoints )
        case 6 =>
          new Double( constantAttenuation )
        case 7 =>
          new Double( linearAttenuation )
        case 8 =>
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
          val v = obj.asInstanceOf[String].split( " " )
          direction = Vector3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 3 =>
          val v = obj.asInstanceOf[String].split( " " )
          upVector = Vector3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 4 =>
          val v = obj.asInstanceOf[String].toDouble
          size = v
        case 5 =>
          val v = obj.asInstanceOf[String].toInt
          samplingPoints = v
        case 6 =>
          val v = obj.asInstanceOf[String].toDouble
          constantAttenuation = v
        case 7 =>
          val v = obj.asInstanceOf[String].toDouble
          linearAttenuation = v
        case 8 =>
          val v = obj.asInstanceOf[String].toDouble
          quadraticAttenuation = v

      }
    } catch {
      case _ : Throwable =>
    }

  }

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}


  def isReady: Boolean = true

  override def toString: String = "Area Light"

}
