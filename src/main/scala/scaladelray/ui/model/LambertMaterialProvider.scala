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
import scaladelray.material.{LambertBRDF, Material, LambertOldMaterial, OldMaterial}
import javax.swing.event.TableModelListener

class LambertMaterialProvider extends MaterialProvider with TableModel {

  var diffuseTextureProvider : Option[TextureProvider] = None

  override def createMaterial( l : () => Unit ) : (Material,OldMaterial) = {
    l()
    val o = LambertOldMaterial( diffuseTextureProvider.get.createTexture( l ) )
    val m = Material( None, (1.0, diffuseTextureProvider.get.createTexture( l ), LambertBRDF() ) )
    (m,o)
  }

  override def remove(obj: AnyRef) {
    if( diffuseTextureProvider.isDefined && diffuseTextureProvider.get == obj ) diffuseTextureProvider = None
  }

  override def getRowCount: Int = 0

  override def getColumnCount: Int = 2

  override def getColumnName( column : Int): String = column match {
    case 0 => "Property"
    case 1 => "Value"
  }

  override def getColumnClass(row: Int): Class[_] = classOf[String]

  override def isCellEditable(row: Int, column: Int): Boolean = false

  override def getValueAt(row: Int, column: Int): AnyRef = ""

  override def setValueAt(obj: Any, row: Int, column: Int) {}

  override def addTableModelListener(p1: TableModelListener) {}

  override def removeTableModelListener(p1: TableModelListener) {}

  override def isReady: Boolean = if( diffuseTextureProvider.isDefined ) diffuseTextureProvider.get.isReady else false

  override def toString: String = "Lambert material"

  override def count = 1 + diffuseTextureProvider.get.count

}
