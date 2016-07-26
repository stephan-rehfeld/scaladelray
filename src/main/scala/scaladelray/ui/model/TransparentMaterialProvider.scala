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

import java.lang.Double
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

import scaladelray.Color
import scaladelray.material.Material
import scaladelray.material.bsdf.PerfectTransparentBTDF
import scaladelray.texture.SingleColorTexture

class TransparentMaterialProvider extends MaterialProvider with TableModel {

  var indexOfRefraction = 1.5

  override def createMaterial( l : () => Unit ) : Material = {
    l()
    val e = if( this.emission.isDefined ) Some( emission.get.createEmission( l ) ) else None
    Material( e, (1.0, SingleColorTexture( Color( 1, 1, 1 ) ), PerfectTransparentBTDF( indexOfRefraction ) ) )
  }

  override def remove(obj: AnyRef) {
    if( emission.isDefined && emission.get == obj ) emission = None
  }

  override def getRowCount: Int = 1

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
          "Infex of Refraction"

      }
    case 1 =>
      row match {
        case 0 =>
          new Double( indexOfRefraction )

      }
  }

  override def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].toDouble
          indexOfRefraction = v

      }
    } catch {
      case _ : Throwable =>
    }

  }

  override def addTableModelListener(p1: TableModelListener) {}

  override def removeTableModelListener(p1: TableModelListener) {}

  override def isReady: Boolean = true

  override def toString: String = "Transparent material"

  override def count = 1 + (if( emission.isDefined ) emission.get.count else 0)

}
