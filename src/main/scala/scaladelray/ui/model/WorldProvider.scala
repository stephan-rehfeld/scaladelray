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

import scaladelray.{World, Color}
import scala.collection.mutable
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class WorldProvider extends TableModel {

  var backgroundColor = Color( 0, 0, 0 )
  var ambientLight = Color( 0, 0, 0 )
  var geometryProvider = mutable.MutableList[GeometryProvider]()
  var lightDescriptionProvider = mutable.MutableList[LightDescriptionProvider]()
  var cameraProvider : Option[CameraProvider] = None
  var indexOfRefraction = 1.0

  def remove( obj : Any ) {
    obj match {
      case cp : CameraProvider =>
        cameraProvider = None
      case lp : LightDescriptionProvider =>
        lightDescriptionProvider = lightDescriptionProvider.filterNot( _ == lp )
      case gp : GeometryProvider =>
        if( geometryProvider.contains( gp ) ) geometryProvider = geometryProvider.filterNot( _ == gp ) else for( v <- geometryProvider ) v.remove( gp )
      case spp : SamplingPatternProvider =>
        if( cameraProvider.isDefined ) cameraProvider.get.remove( spp )
      case mp : MaterialProvider =>
        for( v <- geometryProvider ) v.remove( mp )
      case tp : TextureProvider =>
        for( v <- geometryProvider ) v.remove( tp )
      case s: String =>
      case _ =>
    }
  }

  def createWorld( l : () => Unit ) = {
    l()
    val geometries = for( gp <- geometryProvider ) yield gp.createGeometry( l )
    val lightDescriptions = for( ld <- lightDescriptionProvider ) yield ld.createLightDescription( l )

    (cameraProvider.get.createCamera( l ),World( backgroundColor, geometries.toSet, ambientLight, lightDescriptions.toSet, indexOfRefraction ))
  }

  override def getRowCount: Int = 3

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
          "Background color"
        case 1 =>
          "Ambient light"
        case 2 =>
          "Index of refraction"
      }
    case 1 =>
      row match {
        case 0 =>
          backgroundColor
        case 1 =>
          ambientLight
        case 2 =>
          new java.lang.Double( indexOfRefraction )
      }
  }

  override def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          backgroundColor = obj.asInstanceOf[Color]
        case 1 =>
          ambientLight = obj.asInstanceOf[Color]
        case 2 =>
          indexOfRefraction = obj.asInstanceOf[String].toDouble
      }
    } catch {
      case _ : Throwable =>
    }

  }

  def isReady = (if( cameraProvider.isDefined ) cameraProvider.get.isReady else false) && geometryProvider.find( (gp) => !gp.isReady ).isEmpty && lightDescriptionProvider.find( (lp) => !lp.isReady ).isEmpty

  override def addTableModelListener(p1: TableModelListener) {}

  override def removeTableModelListener(p1: TableModelListener) {}

  override def toString: String = "World"

  def count = 1 + this.geometryProvider.foldLeft( 0 )( (v : Int, gp : GeometryProvider) => v + gp.count ) +  this.lightDescriptionProvider.foldLeft( 0 )( (v : Int, ldp : LightDescriptionProvider) => v + ldp.count ) + this.cameraProvider.get.count
}
