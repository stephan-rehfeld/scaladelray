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
import scaladelray.material.{ReflectiveMaterial, PhongMaterial, Material}
import javax.swing.event.TableModelListener

class ReflectiveMaterialProvider extends MaterialProvider with TableModel {
  var phongExponent = 1
  var diffuseTextureProvider : Option[TextureProvider] = None
  var specularTextureProvider : Option[TextureProvider] = None
  var reflectionTextureProvider : Option[TextureProvider] = None

  def createMaterial: Material = ReflectiveMaterial( diffuseTextureProvider.get.createTexture, specularTextureProvider.get.createTexture, phongExponent, reflectionTextureProvider.get.createTexture )

  def remove(obj: AnyRef) {
    if( diffuseTextureProvider.isDefined && diffuseTextureProvider.get == obj ) diffuseTextureProvider = None
    if( specularTextureProvider.isDefined && specularTextureProvider.get == obj ) specularTextureProvider = None
  }

  def getRowCount: Int = 1

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
          "Phong exponent"

      }
    case 1 =>
      row match {
        case 0 =>
          new Integer( phongExponent )

      }
  }

  def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].toInt
          phongExponent = v

      }
    } catch {
      case _ : Throwable =>
    }

  }

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}

  override def toString: String = "Reflective material"
}