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
        case 1 => "Renderables"
        case 2 => "Lights"
      }
    case "Renderables" =>
      worldProvider.renderableProvider (index )
    case "Lights" =>
      worldProvider.lightDescriptionProvider( index )
    case pp : PlaneProvider =>
      if( pp.materialProvider.isDefined ) pp.materialProvider.get else "<Material>"
    case sp : SphereProvider =>
      if( sp.materialProvider.isDefined ) sp.materialProvider.get else "<Material>"
    case bp : AxisAlignedBoxProvider =>
      if( bp.materialProvider.isDefined ) bp.materialProvider.get else "<Material>"
    case tp : TriangleProvider =>
      if( tp.materialProvider.isDefined ) tp.materialProvider.get else "<Material>"
    case mp : ModelProvider =>
      if( mp.materialProvider.isDefined ) mp.materialProvider.get else "<Material>"
    case np : NodeProvider =>
      np.childNodes(index)
    case ocp : OrthograpicCameraProvider =>
      if( ocp.samplingPatternProvider.isDefined ) ocp.samplingPatternProvider.get else "<Anti-Aliasing Sampling Pattern>"
    case pcp : PerspectiveCameraProvider =>
      if( pcp.samplingPatternProvider.isDefined ) pcp.samplingPatternProvider.get else "<Anti-Aliasing Sampling Pattern>"
    case dcp : DOFCameraProvider =>
      index match {
        case 0 => if( dcp.aaSamplingPatternProvider.isDefined ) dcp.aaSamplingPatternProvider.get else "<Anti-Aliasing Sampling Pattern>"
        case 1 => if( dcp.lensSamplingPatternProvider.isDefined ) dcp.lensSamplingPatternProvider.get else "<Lens Sampling Pattern>"
      }
    case lp : LambertMaterialProvider =>
      if( lp.diffuseTextureProvider.isDefined ) lp.diffuseTextureProvider.get else "<Diffuse Texture>"
    case pp : PhongMaterialProvider =>
      index match {
        case 0 => if( pp.diffuseTextureProvider.isDefined ) pp.diffuseTextureProvider.get else "<Diffuse Texture>"
        case 1 => if( pp.specularTextureProvider.isDefined ) pp.specularTextureProvider.get else "<Specular Texture>"
      }
    case pp : ReflectiveMaterialProvider =>
      index match {
        case 0 => if( pp.diffuseTextureProvider.isDefined ) pp.diffuseTextureProvider.get else "<Diffuse Texture>"
        case 1 => if( pp.specularTextureProvider.isDefined ) pp.specularTextureProvider.get else "<Specular Texture>"
        case 2 => if( pp.reflectionTextureProvider.isDefined ) pp.reflectionTextureProvider.get else "<Reflection Texture>"
      }
  }

  def getChildCount( parent: Any ): Int = parent match {
    case wp : WorldProvider => 3
    case "Renderables" => worldProvider.renderableProvider.size
    case "Lights" => worldProvider.lightDescriptionProvider.size
    case pp : PlaneProvider => 1
    case sp : SphereProvider => 1
    case bp : AxisAlignedBoxProvider => 1
    case tp : TriangleProvider => 1
    case np : NodeProvider => np.childNodes.size
    case mp : ModelProvider => 1
    case ocp : OrthograpicCameraProvider => 1
    case pcp : PerspectiveCameraProvider => 1
    case dcp : DOFCameraProvider => 2
    case lp : LambertMaterialProvider => 1
    case pp : PhongMaterialProvider => 2
    case rp : ReflectiveMaterialProvider => 3
    case _ => 0
  }

  def isLeaf(node: Any): Boolean = node match {
    case wp : WorldProvider => false
    case "<Camera>" => true
    case "Renderables" => worldProvider.renderableProvider.size == 0
    case "Lights" => worldProvider.lightDescriptionProvider.size == 0
    case pp : PlaneProvider => false
    case sp : SphereProvider => false
    case bp : AxisAlignedBoxProvider => false
    case tp : TriangleProvider => false
    case np : NodeProvider => np.childNodes.isEmpty
    case mp : ModelProvider => false
    case ocp : OrthograpicCameraProvider => false
    case pcp : PerspectiveCameraProvider => false
    case dcp : DOFCameraProvider => false
    case lp : LambertMaterialProvider => false
    case pp : PhongMaterialProvider => false
    case rp : ReflectiveMaterialProvider => false
    case _ => true
  }

  def valueForPathChanged( path: TreePath, newValue: Any ) {}

  def getIndexOfChild( parent: Any, child: Any ): Int = parent match {
    case wp : WorldProvider =>
      child match {
        case "<Camera>" => 0
        case "Renderables" => 1
        case "Lights" => 2
        case ocp : OrthograpicCameraProvider => 0
        case pcp : PerspectiveCameraProvider => 0
        case dcp : DOFCameraProvider => 0
      }
    case "Renderables" => worldProvider.renderableProvider.indexOf( child )
    case "Lights" => worldProvider.lightDescriptionProvider.indexOf( child )
    case pp : PlaneProvider => 0
    case sp : SphereProvider => 0
    case bp : AxisAlignedBoxProvider => 0
    case tp : TriangleProvider => 0
    case np : NodeProvider => np.childNodes.indexOf( child )
    case mp : ModelProvider => 0
    case ocp : OrthograpicCameraProvider => 0
    case pcp : PerspectiveCameraProvider => 0
    case dcp : DOFCameraProvider =>
      child match {
        case "<Anti-Aliasing Sampling Pattern>" => 0
        case "<Lens Sampling Pattern>" => 1
        case spp : SamplingPatternProvider =>
          if( dcp.aaSamplingPatternProvider.isDefined && dcp.aaSamplingPatternProvider.get == spp ) 0 else 1
      }
    case lp : LambertMaterialProvider => 0
    case pp : PhongMaterialProvider =>
      child match {
        case "<Diffuse Texture>" => 0
        case "<Specular Texture>" => 1
        case tp : TextureProvider =>
          if(pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == tp ) 0 else 1
      }
    case rp : ReflectiveMaterialProvider =>
      child match {
        case "<Diffuse Texture>" => 0
        case "<Specular Texture>" => 1
        case "<Reflection Texture>" => 2
        case tp : TextureProvider =>
          if(rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == tp ) {0}
          else if(rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == tp) 1 else 2
      }

  }

  def addTreeModelListener(l: TreeModelListener) {}

  def removeTreeModelListener(l: TreeModelListener) {}
}
