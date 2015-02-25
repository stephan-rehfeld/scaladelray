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

import scaladelray.Color
import scala.collection.mutable
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel
import scaladelray.world.World

class WorldProvider extends TableModel {

  var backgroundProvider : Option[BackgroundProvider] = None
  var ambientLight = Color( 0, 0, 0 )
  var renderableProvider = mutable.MutableList[RenderableProvider]()
  var lightDescriptionProvider = mutable.MutableList[LightDescriptionProvider]()
  var cameraProvider : Option[CameraProvider] = None
  var indexOfRefraction = 1.0

  def remove( obj : Any ) {
    obj match {
      case bp : BackgroundProvider =>
        backgroundProvider = None
      case cp : CameraProvider =>
        cameraProvider = None
      case lp : LightDescriptionProvider =>
        lightDescriptionProvider = lightDescriptionProvider.filterNot( _ == lp )
      case rp : RenderableProvider =>
        if( renderableProvider.contains( rp ) ) renderableProvider = renderableProvider.filterNot( _ == rp ) else for( v <- renderableProvider ) v.remove( rp )
      case spp : SamplingPatternProvider =>
        if( cameraProvider.isDefined ) cameraProvider.get.remove( spp )
      case mp : MaterialProvider =>
        for( v <- renderableProvider ) v.remove( mp )
      case tp : TextureProvider =>
        if( backgroundProvider.isDefined ) backgroundProvider.get.remove( tp )
        for( v <- renderableProvider ) v.remove( tp )
      case s: String =>
      case _ =>
    }
  }

  def createWorld = {
    val renderables = for( rp <- renderableProvider ) yield rp.createRenderable
    val lightDescriptions = for( ld <- lightDescriptionProvider ) yield ld.createLightDescription

    (cameraProvider.get.createCamera,World( backgroundProvider.get.createBackground, renderables.toSet, ambientLight, lightDescriptions.toSet, indexOfRefraction ))
  }

  def getRowCount: Int = 2

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
          "Ambient light"
        case 1 =>
          "Index of refraction"
      }
    case 1 =>
      row match {
        case 0 =>
          "" + ambientLight.r + " " + ambientLight.g + " " + ambientLight.b
        case 1 =>
          new java.lang.Double( indexOfRefraction )
      }
  }

  def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          ambientLight = Color( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          indexOfRefraction = obj.asInstanceOf[String].toDouble
      }
    } catch {
      case _ : Throwable =>
    }

  }

  def isReady = (if( backgroundProvider.isDefined ) backgroundProvider.get.isReady else false) && (if( cameraProvider.isDefined ) cameraProvider.get.isReady else false) && renderableProvider.find( (rp) => !rp.isReady ).isEmpty && lightDescriptionProvider.find( (lp) => !lp.isReady ).isEmpty

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}

  override def toString: String = "World"
}
