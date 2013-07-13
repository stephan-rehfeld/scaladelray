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

import javax.swing.tree.{TreePath, TreeModel}
import javax.swing.event.TreeModelListener

class SceneGraphTreeModel( worldProvider : WorldProvider ) extends TreeModel {

  def getRoot: AnyRef = worldProvider

  def getChild( parent: Any, index: Int ): AnyRef = parent match {
    case wp : WorldProvider =>
      index match {
        case 0 => if( wp.cameraProvider.isDefined ) wp.cameraProvider.get else "<Camera>"
        case 1 => "Geometries"
        case 2 => "Lights"
      }
    case "Geometries" =>
      worldProvider.geometryProvider (index )
    case "Lights" =>
      worldProvider.lightDescriptionProvider( index )
    case pp : PlaneProvider =>
      if( pp.materialProvider.isDefined ) pp.materialProvider.get else "<Material>"
    case sp : SphereProvider =>
      if( sp.materialProvider.isDefined ) sp.materialProvider.get else "<Material>"
    case ocp : OrthograpicCameraProvider =>
      if( ocp.samplingPatternProvider.isDefined ) ocp.samplingPatternProvider.get else "<Anti-Aliasing Sampling Pattern>"
    case pcp : PerspectiveCameraProvider =>
      if( pcp.samplingPatternProvider.isDefined ) pcp.samplingPatternProvider.get else "<Anti-Aliasing Sampling Pattern>"
    case dcp : DOFCameraProvider =>
      index match {
        case 0 => if( dcp.aaSamplingPatternProvider.isDefined ) dcp.aaSamplingPatternProvider.get else "<Anti-Aliasing Sampling Pattern>"
        case 1 => if( dcp.lensSamplingPatternProvider.isDefined ) dcp.lensSamplingPatternProvider.get else "<Lens Sampling Pattern>"
      }

  }

  def getChildCount( parent: Any ): Int = parent match {
    case wp : WorldProvider => 3
    case "Geometries" => worldProvider.geometryProvider.size
    case "Lights" => worldProvider.lightDescriptionProvider.size
    case pp : PlaneProvider => 1
    case sp : SphereProvider => 1
    case ocp : OrthograpicCameraProvider => 1
    case pcp : PerspectiveCameraProvider => 1
    case dcp : DOFCameraProvider => 2
    case _ => 0
  }

  def isLeaf(node: Any): Boolean = node match {
    case wp : WorldProvider => false
    case "<Camera>" => true
    case "Geometries" => worldProvider.geometryProvider.size == 0
    case "Lights" => worldProvider.lightDescriptionProvider.size == 0
    case pp : PlaneProvider => false
    case pp : SphereProvider => false
    case ocp : OrthograpicCameraProvider => false
    case pcp : PerspectiveCameraProvider => false
    case dcp : DOFCameraProvider => false
    case _ => true
  }

  def valueForPathChanged( path: TreePath, newValue: Any ) {}

  def getIndexOfChild( parent: Any, child: Any ): Int = parent match {
    case wp : WorldProvider =>
      child match {
        case "<Camera>" => 0
        case "Geometries" => 1
        case "Lights" => 2
        case ocp : OrthograpicCameraProvider => 0
        case pcp : PerspectiveCameraProvider => 0
        case dcp : DOFCameraProvider => 0
      }
    case "Geometries" => worldProvider.geometryProvider.indexOf( child )
    case "Lights" => worldProvider.lightDescriptionProvider.indexOf( child )
    case pp : PlaneProvider => 0
    case sp : SphereProvider => 0

    case ocp : OrthograpicCameraProvider => 0
    case pcp : PerspectiveCameraProvider => 0
    case dcp : DOFCameraProvider =>
      child match {
        case "<Anti-Aliasing Sampling Pattern>" => 0
        case "<Lens Sampling Pattern>" => 1
        case spp : SamplingPatternProvider =>
          if( dcp.aaSamplingPatternProvider.isDefined && dcp.aaSamplingPatternProvider.get == spp ) 0 else 1
      }

  }

  def addTreeModelListener(l: TreeModelListener) {}

  def removeTreeModelListener(l: TreeModelListener) {}
}
