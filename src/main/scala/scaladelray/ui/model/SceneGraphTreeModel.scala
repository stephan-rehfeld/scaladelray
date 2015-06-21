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
        case 0 => if( wp.backgroundProvider.isDefined ) wp.backgroundProvider.get else "<Background>"
        case 1 => if( wp.cameraProvider.isDefined ) wp.cameraProvider.get else "<Camera>"
        case 2 => "Renderables"
        case 3 => "Lights"
      }
    case sbp : SkyboxProvider =>
      index match {
        case 0 => sbp.frontTextureProvider.getOrElse( "<Front Texture>")
        case 1 => sbp.backTextureProvider.getOrElse( "<Back Texture>")
        case 2 => sbp.leftTextureProvider.getOrElse( "<Left Texture>")
        case 3 => sbp.rightTextureProvider.getOrElse( "<Right Texture>")
        case 4 => sbp.topTextureProvider.getOrElse( "<Top Texture>")
        case 5 => sbp.bottomTextureProvider.getOrElse( "<Bottom Texture>")
      }
    case "Renderables" =>
      worldProvider.renderableProvider (index )
    case "Lights" =>
      worldProvider.lightDescriptionProvider( index )
    case pp : PlaneProvider =>
      index match {
        case 0 => if( pp.materialProvider.isDefined ) pp.materialProvider.get else "<Material>"
        case 1 => if( pp.normalMapProvider.isDefined ) pp.normalMapProvider.get else "<Normal Map>"
      }
    case dp : DiscProvider =>
      index match {
        case 0 => if( dp.materialProvider.isDefined ) dp.materialProvider.get else "<Material>"
        case 1 => if( dp.normalMapProvider.isDefined ) dp.normalMapProvider.get else "<Normal Map>"
      }
    case rp : RectangleProvider =>
      index match {
        case 0 => if( rp.materialProvider.isDefined ) rp.materialProvider.get else "<Material>"
        case 1 => if( rp.normalMapProvider.isDefined ) rp.normalMapProvider.get else "<Normal Map>"
      }
    case sp : SphereProvider =>
      index match {
        case 0 => if( sp.materialProvider.isDefined ) sp.materialProvider.get else "<Material>"
        case 1 => if( sp.normalMapProvider.isDefined ) sp.normalMapProvider.get else "<Normal Map>"
      }
    case bp : AxisAlignedBoxProvider =>
      index match {
        case 0 => if( bp.materialProvider.isDefined ) bp.materialProvider.get else "<Material>"
        case 1 => if( bp.normalMapProvider.isDefined ) bp.normalMapProvider.get else "<Normal Map>"
      }
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
      index match {
        case 0 => if( lp.diffuseTextureProvider.isDefined ) lp.diffuseTextureProvider.get else "<Diffuse Texture>"
        case 1 => if( lp.emission.isDefined ) lp.emission.get else "<Emission>"
      }
    case pp : PhongMaterialProvider =>
      index match {
        case 0 => if( pp.diffuseTextureProvider.isDefined ) pp.diffuseTextureProvider.get else "<Diffuse Texture>"
        case 1 => if( pp.specularTextureProvider.isDefined ) pp.specularTextureProvider.get else "<Specular Texture>"
        case 2 => if( pp.emission.isDefined ) pp.emission.get else "<Emission>"
      }
    case pp : ReflectiveMaterialProvider =>
      index match {
        case 0 => if( pp.diffuseTextureProvider.isDefined ) pp.diffuseTextureProvider.get else "<Diffuse Texture>"
        case 1 => if( pp.specularTextureProvider.isDefined ) pp.specularTextureProvider.get else "<Specular Texture>"
        case 2 => if( pp.reflectionTextureProvider.isDefined ) pp.reflectionTextureProvider.get else "<Reflection Texture>"
        case 3 => if( pp.emission.isDefined ) pp.emission.get else "<Emission>"
      }
  }

  def getChildCount( parent: Any ): Int = parent match {
    case wp : WorldProvider => 4
    case sbp : SkyboxProvider => 6
    case "Renderables" => worldProvider.renderableProvider.size
    case "Lights" => worldProvider.lightDescriptionProvider.size
    case pp : PlaneProvider => 2
    case dp : DiscProvider => 2
    case rp : RectangleProvider => 2
    case sp : SphereProvider => 2
    case bp : AxisAlignedBoxProvider => 2
    case tp : TriangleProvider => 1
    case np : NodeProvider => np.childNodes.size
    case mp : ModelProvider => 1
    case ocp : OrthograpicCameraProvider => 1
    case pcp : PerspectiveCameraProvider => 1
    case dcp : DOFCameraProvider => 2
    case lp : LambertMaterialProvider => 2
    case pp : PhongMaterialProvider => 3
    case rp : ReflectiveMaterialProvider => 4
    case _ => 0
  }

  def isLeaf(node: Any): Boolean = node match {
    case wp : WorldProvider => false
    case sbp : SkyboxProvider => false
    case "<Background>" => true
    case "<Camera>" => true
    case "Renderables" => worldProvider.renderableProvider.size == 0
    case "Lights" => worldProvider.lightDescriptionProvider.size == 0
    case pp : PlaneProvider => false
    case dp : DiscProvider => false
    case rp : RectangleProvider => false
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
        case "<Background>" => 0
        case "<Camera>" => 1
        case "Renderables" => 2
        case "Lights" => 3
        case bg : BackgroundProvider => 0
        case ocp : OrthograpicCameraProvider => 1
        case pcp : PerspectiveCameraProvider => 1
        case dcp : DOFCameraProvider => 1
      }
    case sbp : SkyboxProvider =>
      child match {
        case "<Front Texture>" => 0
        case "<Back Texture>" => 1
        case "<Left Texture>" => 2
        case "<Right Texture>" => 3
        case "<Top Texture>" => 4
        case "<Bottom Texture>" => 5
        case tp : TextureProvider => if( sbp.frontTextureProvider.isDefined && sbp.frontTextureProvider.get == tp ) 0
                                     else if( sbp.backTextureProvider.isDefined && sbp.backTextureProvider.get == tp ) 1
                                     else if( sbp.leftTextureProvider.isDefined && sbp.leftTextureProvider.get == tp ) 2
                                     else if( sbp.rightTextureProvider.isDefined && sbp.rightTextureProvider.get == tp ) 3
                                     else if( sbp.topTextureProvider.isDefined && sbp.topTextureProvider.get == tp ) 4
                                     else 5
      }
    case "Renderables" => worldProvider.renderableProvider.indexOf( child )
    case "Lights" => worldProvider.lightDescriptionProvider.indexOf( child )
    case pp : PlaneProvider =>
      child match {
        case "<Material>" => 0
        case mp : MaterialProvider => 0
        case "<Normal Map>" => 1
        case nm : TextureProvider => 1
      }
    case dp : DiscProvider =>
      child match {
        case "<Material>" => 0
        case mp : MaterialProvider => 0
        case "<Normal Map>" => 1
        case nm : TextureProvider => 1
      }
    case rp : RectangleProvider =>
      child match {
        case "<Material>" => 0
        case mp : MaterialProvider => 0
        case "<Normal Map>" => 1
        case nm : TextureProvider => 1
      }
    case sp : SphereProvider =>
      child match {
        case "<Material>" => 0
        case mp : MaterialProvider => 0
        case "<Normal Map>" => 1
        case nm : TextureProvider => 1
      }
    case bp : AxisAlignedBoxProvider =>
      child match {
        case "<Material>" => 0
        case mp : MaterialProvider => 0
        case "<Normal Map>" => 1
        case nm : TextureProvider => 1
      }
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
    case lp : LambertMaterialProvider =>
      child match {
        case "<Diffuse Texture>" => 0
        case "<Emission>" => 1
        case tp : TextureProvider => 0
        case e : EmissionProvider => 1

      }
    case pp : PhongMaterialProvider =>
      child match {
        case "<Diffuse Texture>" => 0
        case "<Specular Texture>" => 1
        case "<Emission>" => 2
        case tp : TextureProvider =>
          if(pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == tp ) 0 else 1
        case e : EmissionProvider => 2
      }
    case rp : ReflectiveMaterialProvider =>
      child match {
        case "<Diffuse Texture>" => 0
        case "<Specular Texture>" => 1
        case "<Reflection Texture>" => 2
        case "<Emission>" => 3
        case tp : TextureProvider =>
          if(rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == tp ) {0}
          else if(rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == tp) 1 else 2
        case e : EmissionProvider => 2
      }

  }

  def addTreeModelListener(l: TreeModelListener) {}

  def removeTreeModelListener(l: TreeModelListener) {}
}
