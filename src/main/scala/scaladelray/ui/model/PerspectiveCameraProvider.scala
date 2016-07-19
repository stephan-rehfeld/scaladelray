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

import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

import scaladelray.camera.PerspectiveCamera
import scaladelray.math.d.{Direction3, Point3}


class PerspectiveCameraProvider extends CameraProvider with TableModel {

  var position = Point3( 0, 0, 0 )
  var gazeDirection = Direction3( 0, 0, -1 )
  var upVector = Direction3( 0, 1, 0 )
  var imagePlaneFormat = (0.036,0.024)
  var focalLength = 0.05
  var fNumber = 4.0
  var depthOfField = Double.PositiveInfinity

  override def createCamera( l : () => Unit ) = {
    l()
    PerspectiveCamera( position, gazeDirection, upVector, imagePlaneFormat, focalLength, fNumber, depthOfField )
  }

  override def getRowCount: Int = 7

  override def getColumnCount: Int = 2

  override def getColumnName( column : Int): String = column match {
    case 0 => "Property"
    case 1 => "Value"
  }

  override def getColumnClass(row: Int): Class[_] = classOf[String]

  override def isCellEditable(row: Int, column: Int): Boolean = column match {
    case 0 => false
    case 1 => true
  }

  override def getValueAt(row: Int, column: Int): AnyRef = column match {
    case 0 =>
      row match {
        case 0 =>
          "Position"
        case 1 =>
          "Gaze direction"
        case 2 =>
          "Up vector"
        case 3 =>
          "Image Plane Format"
        case 4 =>
          "Focal Length"
        case 5 =>
          "F Number"
        case 6 =>
          "Depth of Field"
      }
    case 1 =>
      row match {
        case 0 =>
          "" + position.x + " " + position.y + " " + position.z
        case 1 =>
          "" + gazeDirection.x + " " + gazeDirection.y + " " + gazeDirection.z
        case 2 =>
          "" + upVector.x + " " + upVector.y + " " + upVector.z
        case 3 =>
          "" + imagePlaneFormat._1 + " " + imagePlaneFormat._2
        case 4 =>
          new java.lang.Double( focalLength )
        case 5 =>
          new java.lang.Double( fNumber )
        case 6 =>
          new java.lang.Double( depthOfField )
      }
  }


  override def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          position = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          val v = obj.asInstanceOf[String].split( " " )
          gazeDirection = Direction3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 2 =>
          val v = obj.asInstanceOf[String].split( " " )
          upVector = Direction3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 3 =>
          val v = obj.asInstanceOf[String].split( " " )
          imagePlaneFormat = (v(0).toDouble,v(1).toDouble)
        case 4 =>
          focalLength = obj.asInstanceOf[String].toDouble
        case 5 =>
          fNumber = obj.asInstanceOf[String].toDouble
        case 6 =>
          depthOfField = obj.asInstanceOf[String].toDouble
      }
    } catch {
      case _ : Throwable =>
    }
  }

  override def addTableModelListener(p1: TableModelListener) {}

  override def removeTableModelListener(p1: TableModelListener) {}

  override def remove(obj: AnyRef) {

  }

  override def isReady: Boolean = true

  override def toString: String = "Perspective Camera"

  override def count = 1

}
