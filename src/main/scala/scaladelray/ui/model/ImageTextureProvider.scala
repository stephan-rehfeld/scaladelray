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
import scaladelray.texture.{ImageTexture, ChessboardTexture, Texture}
import javax.swing.event.{TableModelEvent, TableModelListener}
import java.io.File
import scala.collection.mutable

class ImageTextureProvider( tml : TableModelListener ) extends TextureProvider with TableModel {

  var fileName = ""

  var listener = mutable.Set[TableModelListener]()

  listener += tml


  def createTexture: Texture = ImageTexture(fileName )

  def getRowCount: Int = 1

  def getColumnCount: Int = 2

  def getColumnName(column: Int): String = column match {
    case 0 => "Property"
    case 1 => "Value"
  }

  def getColumnClass(p1: Int): Class[_] = classOf[String]

  def isCellEditable(row: Int, column: Int): Boolean = column == 1

  def getValueAt(row: Int, column: Int): AnyRef = column match {
    case 0 => row match {
      case 0 => "file"
    }
    case 1 => row match {
      case 0 => fileName
    }
  }

  def setValueAt( obj : Any, row: Int, column: Int) {

    row match {
      case 0 =>
        fileName = obj.asInstanceOf[String]
        for( l <- listener ) l.tableChanged( new TableModelEvent( this ) )
    }

  }

  def addTableModelListener( l : TableModelListener) {
    listener += l
  }

  def removeTableModelListener( l : TableModelListener) {
    listener -= l
  }


  def isReady: Boolean = new File( fileName ).exists()

  override def toString: String = "Image texture"

}
