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

import scaladelray.geometry.Triangle
import scaladelray.math.Transform
import scaladelray.math.d.{Normal3, Point3}
import scaladelray.rendering.Renderable
import scaladelray.texture.TexCoord2D

class TriangleProvider extends RenderableProvider with TableModel {

  var materialProvider : Option[MaterialProvider] = None

  var vertexA = Point3( 0, 0, 0 )
  var vertexB = Point3( 0, 1, 0 )
  var vertexC = Point3( 1, 0, 0 )

  var normalA = Normal3( 0, 0, 1 )
  var normalB = Normal3( 0, 0, 1 )
  var normalC = Normal3( 0, 0, 1 )

  var texCoordA = TexCoord2D( 0, 0 )
  var texCoordB = TexCoord2D( 0, 1 )
  var texCoordC = TexCoord2D( 1, 0 )

  override def createRenderable( l : () => Unit ) = {
    l()
    val g = Triangle( vertexA, vertexB, vertexC, normalA, normalB, normalC, texCoordA, texCoordB, texCoordC, None )
    val m = materialProvider.get.createMaterial( l )
    Set( Renderable( Transform(), g, m ) )
  }

  override def getRowCount: Int = 9

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
          "Vertex A"
        case 1 =>
          "Vertex B"
        case 2 =>
          "Vertex C"
        case 3 =>
          "Normal A"
        case 4 =>
          "Normal B"
        case 5 =>
          "Normal C"
        case 6 =>
          "Texture coordinate A"
        case 7 =>
          "Texture coordinate B"
        case 8 =>
          "Texture coordinate C"
      }
    case 1 =>
      row match {
        case 0 =>
          "" + vertexA.x + " " + vertexA.y + " " + vertexA.z
        case 1 =>
          "" + vertexB.x + " " + vertexB.y + " " + vertexB.z
        case 2 =>
          "" + vertexC.x + " " + vertexC.y + " " + vertexC.z
        case 3 =>
          "" + normalA.x + " " + normalA.y + " " + normalA.z
        case 4 =>
          "" + normalB.x + " " + normalB.y + " " + normalB.z
        case 5 =>
          "" + normalC.x + " " + normalC.y + " " + normalC.z
        case 6 =>
          "" + texCoordA.u + " " + texCoordA.v
        case 7 =>
          "" + texCoordB.u + " " + texCoordB.v
        case 8 =>
          "" + texCoordC.u + " " + texCoordC.v
      }
  }

  override def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          vertexA = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          val v = obj.asInstanceOf[String].split( " " )
          vertexB = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 2 =>
          val v = obj.asInstanceOf[String].split( " " )
          vertexC = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 3 =>
          val v = obj.asInstanceOf[String].split( " " )
          normalA = Normal3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 4 =>
          val v = obj.asInstanceOf[String].split( " " )
          normalB = Normal3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 5 =>
          val v = obj.asInstanceOf[String].split( " " )
          normalC = Normal3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 6 =>
          val v = obj.asInstanceOf[String].split( " " )
          texCoordA = TexCoord2D( v(0).toDouble, v(1).toDouble )
        case 7 =>
          val v = obj.asInstanceOf[String].split( " " )
          texCoordB = TexCoord2D( v(0).toDouble, v(1).toDouble )
        case 8 =>
          val v = obj.asInstanceOf[String].split( " " )
          texCoordC = TexCoord2D( v(0).toDouble, v(1).toDouble )
      }
    } catch {
      case _ : Throwable =>
    }

  }

  override def remove(obj: AnyRef) {
    if( materialProvider.isDefined && materialProvider.get == obj ) materialProvider = None
    if( materialProvider.isDefined ) materialProvider.get.remove( obj )
  }

  override def addTableModelListener(p1: TableModelListener) {}

  override def removeTableModelListener(p1: TableModelListener) {}

  override def isReady: Boolean = if( materialProvider.isDefined ) materialProvider.get.isReady else false

  override def toString: String = "Triangle"

  override def count = 1 + materialProvider.get.count

}
