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

import javax.swing.table.TableModel
import scaladelray.world.{Skybox, Background}
import javax.swing.event.TableModelListener

class SkyboxProvider extends BackgroundProvider with TableModel {

  var frontTextureProvider : Option[TextureProvider] = None
  var backTextureProvider : Option[TextureProvider] = None
  var leftTextureProvider : Option[TextureProvider] = None
  var rightTextureProvider : Option[TextureProvider] = None
  var topTextureProvider : Option[TextureProvider] = None
  var bottomTextureProvider : Option[TextureProvider] = None

  override def getRowCount: Int = 0

  override def getColumnCount: Int = 0

  override def getColumnName(columnIndex: Int): String = columnIndex match {
    case 0 => "Property"
    case 1 => "Value"
  }

  override def getColumnClass(columnIndex: Int): Class[_] = classOf[String]

  override def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = columnIndex match {
    case 0 => false
    case 1 => true
  }

  override def getValueAt( rowIndex: Int, columnIndex: Int ): AnyRef = "???"

  override def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int): Unit = {}

  override def addTableModelListener(l: TableModelListener): Unit = {}

  override def removeTableModelListener(l: TableModelListener): Unit = {}

  override def createBackground: Background = new Skybox( frontTextureProvider.get.createTexture, backTextureProvider.get.createTexture, leftTextureProvider.get.createTexture, rightTextureProvider.get.createTexture, topTextureProvider.get.createTexture, bottomTextureProvider.get.createTexture )
  override def isReady: Boolean = frontTextureProvider.isDefined && frontTextureProvider.get.isReady && backTextureProvider.isDefined && backTextureProvider.get.isReady && leftTextureProvider.isDefined && leftTextureProvider.get.isReady && rightTextureProvider.isDefined && rightTextureProvider.get.isReady && topTextureProvider.isDefined && topTextureProvider.get.isReady && bottomTextureProvider.isDefined && bottomTextureProvider.get.isReady
  override def remove( obj : AnyRef ) {
    if( frontTextureProvider.isDefined && frontTextureProvider.get == obj ) frontTextureProvider = None
    if( backTextureProvider.isDefined && backTextureProvider.get == obj ) backTextureProvider = None
    if( leftTextureProvider.isDefined && leftTextureProvider.get == obj ) leftTextureProvider = None
    if( rightTextureProvider.isDefined && rightTextureProvider.get == obj ) rightTextureProvider = None
    if( topTextureProvider.isDefined && topTextureProvider.get == obj ) topTextureProvider = None
    if( bottomTextureProvider.isDefined && bottomTextureProvider.get == obj ) bottomTextureProvider = None

  }

  override def toString: String = "Skybox"

}
