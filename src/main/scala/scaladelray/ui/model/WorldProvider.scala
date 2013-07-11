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

import scaladelray.{World, Color}
import scala.collection.mutable
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class WorldProvider extends TableModel {

  var backgroundColor = Color( 0, 0, 0 )
  var ambientLight = Color( 0, 0, 0 )
  var geometryProvider = mutable.MutableList[GeometryProvider]()
  var lightDescriptionProvider = mutable.MutableList[LightDescriptionProvider]()
  var cameraProvider : Option[CameraProvider] = None
  var indexOfRefraction = 1.0

  def createWorld = {
    val geometries = for( gp <- geometryProvider ) yield gp.createGeometry
    val lightDescriptions = for( ld <- lightDescriptionProvider ) yield ld.createLightDescription

    World( backgroundColor, geometries.toSet, ambientLight, lightDescriptions.toSet, indexOfRefraction )
  }

  def getRowCount: Int = 3

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
          "Background color"
        case 1 =>
          "Ambient light"
        case 2 =>
          "Index of refraction"
      }
    case 1 =>
      row match {
        case 0 =>
          "" + backgroundColor.r + " " + backgroundColor.g + " " + backgroundColor.b
        case 1 =>
          "" + ambientLight.r + " " + ambientLight.g + " " + ambientLight.b
        case 2 =>
          new java.lang.Double( indexOfRefraction )
      }
  }

  def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          backgroundColor = Color( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          val v = obj.asInstanceOf[String].split( " " )
          ambientLight = Color( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 2 =>
          indexOfRefraction = obj.asInstanceOf[String].toDouble
      }
    } catch {
      case _ : Throwable =>
    }

  }

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}

  override def toString: String = "World"
}
