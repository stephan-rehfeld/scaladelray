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
import scaladelray.math.{Transform, Vector3, Point3}
import scaladelray.geometry.{Node, Geometry}
import scaladelray.loader.OBJLoader
import javax.swing.event.{TableModelEvent, TableModelListener}
import java.io.File
import scala.collection.mutable

class ModelProvider( tml : TableModelListener ) extends GeometryProvider with TableModel {

  var fileName = ""
  var materialProvider : Option[MaterialProvider] = None
  var translate = Point3( 0, 0, 0 )
  var scale = Vector3( 1, 1, 1 )
  var rotate = Vector3( 0, 0, 0 )
  var octreeRecursionDepth = 3
  var octreeFacesLimit = -1

  var listener = mutable.Set[TableModelListener]()

  listener += tml

  private def subDivideFunction( maxRecursions : Int, facesLimit : Int, recursion : Int, faces : Int ) =
    (maxRecursions == -1 || recursion < maxRecursions) && (facesLimit == -1 || faces > facesLimit)


  def createGeometry: Geometry = {
    val loader = new OBJLoader
    val m = loader.load( fileName, materialProvider.get.createMaterial, subDivideFunction( octreeRecursionDepth, octreeFacesLimit, _ , _ ) )
    val t = Transform.translate( translate ).rotateZ( rotate.z ).rotateY(rotate.y ).rotateX( rotate.x ).scale( scale.x, scale.y, scale.z )
    new Node( t, m  )
  }

  def remove(obj: AnyRef) {
    obj match {
      case mp : MaterialProvider =>
        if( materialProvider.isDefined && materialProvider.get == mp ) materialProvider = None
      case _ =>
    }
    if( materialProvider.isDefined ) materialProvider.get.remove( obj )
  }

  def getRowCount: Int = 6

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
          "Translate"
        case 1 =>
          "Scale"
        case 2 =>
          "Rotate"
        case 3 =>
          "File"
        case 4 =>
          "Octree recursion depth"
        case 5 =>
          "Octree faces limit"
      }
    case 1 =>
      row match {
        case 0 =>
          "" + translate.x + " " + translate.y + " " + translate.z
        case 1 =>
          "" + scale.x + " " + scale.y + " " + scale.z
        case 2 =>
          "" + math.toDegrees( rotate.x ) + " " + math.toDegrees( rotate.y ) + " " + math.toDegrees( rotate.z )
        case 3 =>
          fileName
        case 4 =>
          new Integer( octreeRecursionDepth )
        case 5 =>
          new Integer( octreeFacesLimit )
      }
  }

  def setValueAt(obj: Any, row: Int, column: Int) {
    try {
      row match {
        case 0 =>
          val v = obj.asInstanceOf[String].split( " " )
          translate = Point3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 1 =>
          val v = obj.asInstanceOf[String].split( " " )
          scale = Vector3( v(0).toDouble, v(1).toDouble, v(2).toDouble )
        case 2 =>
          val v = obj.asInstanceOf[String].split( " " )
          rotate = Vector3( math.toRadians( v(0).toDouble ), math.toRadians( v(1).toDouble ), math.toRadians( v(2).toDouble ) )
        case 3 =>
          fileName = obj.asInstanceOf[String]
          for( l <- listener ) l.tableChanged( new TableModelEvent( this ) )
        case 4 =>
          octreeRecursionDepth = obj.asInstanceOf[String].toInt
        case 5 =>
          octreeFacesLimit = obj.asInstanceOf[String].toInt
      }
    } catch {
      case _ : Throwable =>
    }

  }


  def isReady: Boolean = {
    (if( materialProvider.isDefined ) materialProvider.get.isReady else false) && new File( fileName ).exists() && !(octreeRecursionDepth == -1 && octreeFacesLimit == -1)
  }

  def addTableModelListener( l : TableModelListener) {
    listener += l
  }

  def removeTableModelListener( l : TableModelListener) {
    listener -= l
  }

  override def toString: String = "Model"

}
