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
import javax.swing.event.TableModelListener
import scaladelray.texture.{ChessboardTexture, Texture}

class ChessboardTextureProvider extends TextureProvider with TableModel {

  var x = 1
  var y = 1

  override def createTexture( l : () => Unit ) : Texture = {
    l()
    ChessboardTexture( x, y )
  }

  override def getRowCount: Int = 2

  override def getColumnCount: Int = 2

  override def getColumnName(column: Int): String = column match {
    case 0 => "Property"
    case 1 => "Value"
  }

  override def getColumnClass(p1: Int): Class[_] = classOf[String]

  override def isCellEditable(row: Int, column: Int): Boolean = column == 1

  override def getValueAt(row: Int, column: Int): AnyRef = column match {
    case 0 => row match {
      case 0 => "x"
      case 1 => "y"
    }
    case 1 => row match {
      case 0 => new Integer(x)
      case 1 => new Integer(y)
    }
  }

  override def setValueAt( obj : Any, row: Int, column: Int) {

    row match {
      case 0 => x = obj.asInstanceOf[String].toInt
      case 1 => y = obj.asInstanceOf[String].toInt
    }

  }

  override def addTableModelListener(p1: TableModelListener) {}

  override def removeTableModelListener(p1: TableModelListener) {}

  override def isReady: Boolean = true

  override def toString: String = "Chessboard texture"

  override def count = 1

}
