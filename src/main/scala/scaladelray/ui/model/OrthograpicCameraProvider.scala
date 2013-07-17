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
import scaladelray.camera.{OrthographicCamera, Camera}
import javax.swing.event.TableModelListener
import scaladelray.math.{Vector3, Point3}

class OrthograpicCameraProvider extends CameraProvider with TableModel {

  var position = Point3( 0, 0, 0 )
  var gazeDirection = Vector3( 0, 0, -1 )
  var upVector = Vector3( 0, 1, 0 )
  var size = 1.0
  var samplingPatternProvider : Option[SamplingPatternProvider] = Some( new RegularSamplingPatternProvider )

  def createCamera = OrthographicCamera( position, gazeDirection, upVector, _, _, size, samplingPatternProvider.get.createSamplingPattern )

  def getRowCount: Int = 4

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
          "Position"
        case 1 =>
          "Gaze direction"
        case 2 =>
          "Up vector"
        case 3 =>
          "Size"
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
          new java.lang.Double( size )
      }
  }


  def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          position = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          val v = obj.asInstanceOf[String].split( " " )
          gazeDirection = Vector3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 2 =>
          val v = obj.asInstanceOf[String].split( " " )
          upVector = Vector3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 3 =>
          size = obj.asInstanceOf[String].toDouble
      }
    } catch {
      case _ : Throwable =>
    }
  }

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}

  def remove(obj: AnyRef) {
    if( samplingPatternProvider.isDefined && obj == samplingPatternProvider.get ) samplingPatternProvider = None
  }


  def isReady: Boolean = samplingPatternProvider.isDefined

  override def toString: String = "Orthographic Camera"
}
