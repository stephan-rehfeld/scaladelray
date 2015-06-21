/*
 * Copyright 2015 Stephan Rehfeld
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

import scaladelray.material.{SpotEmission, Emission}
import scaladelray.Color
import javax.swing.table.TableModel
import javax.swing.event.TableModelListener
import java.lang.Double

class SpotEmissionProvider extends EmissionProvider with TableModel {

  var color  = Color( 1, 1, 1 )
  var halfAngle = math.Pi / 4.0

  override def createEmission( l : () => Unit ): Emission = {
    l()
    SpotEmission( color, halfAngle )
  }

  override def remove( obj : AnyRef ) {}
  override def isReady = true
  override def count = 1

  override def getRowCount: Int = 2

  override def getColumnCount: Int = 2

  override def getColumnName( columnIndex: Int ): String = columnIndex match {
    case 0 => "Property"
    case 1 => "Value"
  }

  override def getColumnClass(columnIndex: Int): Class[_] = classOf[String]

  override def isCellEditable( rowIndex: Int, columnIndex: Int ): Boolean = columnIndex match {
    case 0 => false
    case 1 => true
  }

  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = columnIndex match {
    case 0 => rowIndex match {
      case 0 => "Color"
      case 1 => "Half angle"
    }
    case 1 => rowIndex match {
      case 0 => color
      case 1 => new Double( math.toDegrees( halfAngle ))
    }
  }

  override def setValueAt( aValue: scala.Any, rowIndex: Int, columnIndex: Int) {
    try {
      rowIndex match {
        case 0 => color = aValue.asInstanceOf[Color]
        case 1 =>
          val v = aValue.asInstanceOf[String].toDouble
          halfAngle = math.toRadians( v )
      }
    } catch {
      case _ : Throwable =>
    }
  }

  override def addTableModelListener(l: TableModelListener) {}

  override def removeTableModelListener(l: TableModelListener) {}

  override def toString: String = "Spot emission"

}
