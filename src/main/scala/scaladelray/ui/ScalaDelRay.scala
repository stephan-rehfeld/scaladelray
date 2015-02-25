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

package scaladelray.ui

import scala.swing._
import scala.swing.GridBagPanel.{Anchor, Fill}
import javax.swing.table.TableModel
import scaladelray.ui.model._
import javax.swing.{SwingUtilities, JMenuItem, JPopupMenu, JTree}
import javax.swing.tree.TreeSelectionModel
import javax.swing.event.{TableModelEvent, TableModelListener, TreeSelectionEvent, TreeSelectionListener}
import java.awt.event._
import scaladelray.Constants
import scala.swing.TabbedPane.Page
import scala.collection.mutable
import akka.actor._
import scala.concurrent.duration._
import scaladelray.math.Vector3
import scaladelray.math.Point3
import scala.Some
import scala.swing.event.ButtonClicked
import scaladelray.Color
import java.net.{SocketException, DatagramPacket, DatagramSocket, InetAddress}
import scala.language.reflectiveCalls

case class StartDiscovery()

object ScalaDelRay extends SimpleSwingApplication {

  var worldProvider = createStandardScene
  var renderingWindowsSize = new Dimension( 640, 480 )
  var recursionDepth = 10
  var clusterNodes = mutable.ListBuffer[(String,Int,Int)]()
  private val actorSystem = ActorSystem("ui")

  lazy val ui : GridBagPanel with TableModelListener = new GridBagPanel with TableModelListener {


    def tableChanged(e: TableModelEvent) {
      updateUI()
    }

    val c = new Constraints
    val shouldFill = true

    if( shouldFill ) c.fill = Fill.Horizontal

    val newPlaneButton = new Button {
      text = "Plane"
      reactions += {
        case ButtonClicked(_) =>
          val pp = new PlaneProvider
          val node = sceneGraphTree.getLastSelectedPathComponent
          if( node != null ) {
            node match {
              case np : NodeProvider =>
                np.childNodes += pp

              case _ =>
                worldProvider.renderableProvider += pp

            }
          } else {
            worldProvider.renderableProvider += pp

          }
          updateUI()
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 0
    c.gridy = 0
    layout( newPlaneButton ) = c

    val newSphereButton = new Button {
      text = "Sphere"
      reactions += {
        case ButtonClicked(_) =>
          val sp = new SphereProvider
          val node = sceneGraphTree.getLastSelectedPathComponent
          if( node != null ) {
            node match {
              case np : NodeProvider =>
                np.childNodes += sp

              case _ =>
                worldProvider.renderableProvider += sp

            }
          } else {
            worldProvider.renderableProvider += sp

          }
          updateUI()
      }
    }
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 1
    c.gridy = 0
    layout( newSphereButton ) = c

    val newBoxButton = new Button {
      text = "Box"
      reactions += {
        case ButtonClicked(_) =>
          val sp = new AxisAlignedBoxProvider
          val node = sceneGraphTree.getLastSelectedPathComponent
          if( node != null ) {
            node match {
              case np : NodeProvider =>
                np.childNodes += sp

              case _ =>
                worldProvider.renderableProvider += sp

            }
          } else {
            worldProvider.renderableProvider += sp

          }
          updateUI()
      }
    }
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 2
    c.gridy = 0
    layout( newBoxButton ) = c

    val newTriangleButton = new Button {
      text = "Triangle"
      reactions += {
        case ButtonClicked(_) =>
          val sp = new TriangleProvider
          val node = sceneGraphTree.getLastSelectedPathComponent
          if( node != null ) {
            node match {
              case np : NodeProvider =>
                np.childNodes += sp

              case _ =>
                worldProvider.renderableProvider += sp

            }
          } else {
            worldProvider.renderableProvider += sp

          }
          updateUI()
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 3
    c.gridy = 0
    layout( newTriangleButton ) = c

    val newNodeButton = new Button {
      text = "Node"
      reactions += {
        case ButtonClicked(_) =>
          val sp = new NodeProvider
          val node = sceneGraphTree.getLastSelectedPathComponent
          if( node != null ) {
            node match {
              case np : NodeProvider =>
                np.childNodes += sp

              case _ =>
                worldProvider.renderableProvider += sp

            }
          } else {
            worldProvider.renderableProvider += sp

          }
          updateUI()
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 4
    c.gridy = 0
    layout( newNodeButton ) = c

    val newModelButton : Button = new Button {
      text = "Model"
      reactions += {
        case ButtonClicked(_) =>
          val sp = new ModelProvider( ui )
          val node = sceneGraphTree.getLastSelectedPathComponent
          if( node != null ) {
            node match {
              case np : NodeProvider =>
                np.childNodes += sp

              case _ =>
                worldProvider.renderableProvider += sp

            }
          } else {
            worldProvider.renderableProvider += sp

          }
          updateUI()
      }
    }
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 5
    c.gridy = 0
    layout( newModelButton ) = c

    val lights = new GridBagPanel {
      val c = new Constraints

      val newPointLightButton = new Button {
        text = "Point Light"
        reactions += {
          case ButtonClicked(_) =>
            val sp = new PointLightProvider
            worldProvider.lightDescriptionProvider += sp
            updateUI()
        }
      }
      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 0
      c.gridy = 1
      layout( newPointLightButton ) = c

      val newSpotLightButton = new Button {
        text = "Spot Light"
        reactions += {
          case ButtonClicked(_) =>
            val sp = new SpotLightProvider
            worldProvider.lightDescriptionProvider += sp
            updateUI()
        }
      }
      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 1
      c.gridy = 1
      layout( newSpotLightButton ) = c

      val newDirectionalLightButton = new Button {
        text = "Directional Light"
        reactions += {
          case ButtonClicked(_) =>
            val sp = new DirectionalLightProvider
            worldProvider.lightDescriptionProvider += sp
            updateUI()
        }
      }
      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 2
      c.gridy = 1
      layout( newDirectionalLightButton ) = c

      val newAreaLightButton = new Button {
        text = "Area light"
        reactions += {
          case ButtonClicked(_) =>
            val sp = new AreaLightProvider
            worldProvider.lightDescriptionProvider += sp
            updateUI()
        }
      }
      c.fill = Fill.Horizontal
      c.weightx = 0.5
      c.gridx = 3
      c.gridy = 1
      layout( newAreaLightButton ) = c
    }
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 0
    c.gridy = 1
    c.gridwidth = 6
    layout( lights ) = c

    val backgroundPopupMenu = new JPopupMenu

    val createSingleBackgroundColorMenuItem = new JMenuItem( "Single Background Color" )
    createSingleBackgroundColorMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.backgroundProvider = Some( new SingleBackgroundColorProvider)
        updateUI()
      }
    })

    val createSkyboxMenuItem = new JMenuItem( "Skybox" )
    createSkyboxMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.backgroundProvider = Some( new SkyboxProvider )
        updateUI()
      }
    })
    backgroundPopupMenu.add( createSingleBackgroundColorMenuItem )
    backgroundPopupMenu.add( createSkyboxMenuItem )

    val cameraPopupMenu = new JPopupMenu
    val createOrthographicCameraMenuItem = new JMenuItem( "Orthographic Camera" )
    createOrthographicCameraMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.cameraProvider = Some( new OrthograpicCameraProvider )
        updateUI()
      }
    })
    val createPerspectiveCameraMenuItem = new JMenuItem( "Perspective Camera" )
    createPerspectiveCameraMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.cameraProvider = Some( new PerspectiveCameraProvider )
        updateUI()
      }
    })
    val createDOFCameraMenuItem = new JMenuItem( "DOF Camera" )
    createDOFCameraMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.cameraProvider = Some( new DOFCameraProvider )
        updateUI()
      }
    })

    cameraPopupMenu.add( createOrthographicCameraMenuItem )
    cameraPopupMenu.add( createPerspectiveCameraMenuItem )
    cameraPopupMenu.add( createDOFCameraMenuItem )

    var selectionParent : Option[AnyRef] = None
    var selection : Option[AnyRef] = None

    val samplingPatternPopupMenu = new JPopupMenu
    val regularSamplingPatternMenuItem = new JMenuItem( "Regular sampling pattern" )
    regularSamplingPatternMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( ocp : OrthograpicCameraProvider ) =>
            ocp.samplingPatternProvider = Some( new RegularSamplingPatternProvider )
          case Some( pcp : PerspectiveCameraProvider ) =>
            pcp.samplingPatternProvider = Some( new RegularSamplingPatternProvider )
          case Some( dcp : DOFCameraProvider ) =>
            selection match {
              case Some( "<Anti-Aliasing Sampling Pattern>" ) =>
                dcp.aaSamplingPatternProvider = Some( new RegularSamplingPatternProvider )
              case Some( "<Lens Sampling Pattern>" ) =>
                dcp.lensSamplingPatternProvider = Some( new RegularSamplingPatternProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( dcp.aaSamplingPatternProvider.isDefined && dcp.aaSamplingPatternProvider.get == spp ) dcp.aaSamplingPatternProvider = Some( new RegularSamplingPatternProvider )
                if( dcp.lensSamplingPatternProvider.isDefined && dcp.lensSamplingPatternProvider.get == spp ) dcp.lensSamplingPatternProvider = Some( new RegularSamplingPatternProvider )
              case Some( _ ) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( _ ) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    samplingPatternPopupMenu.add( regularSamplingPatternMenuItem )

    val materialPopupMenu = new JPopupMenu
    val newSingleColorMaterialMenuItem = new JMenuItem( "Single color material" )
    newSingleColorMaterialMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( pp : PlaneProvider ) =>
            pp.materialProvider = Some( new SingleColorMaterialProvider )
          case Some( sp : SphereProvider ) =>
            sp.materialProvider = Some( new SingleColorMaterialProvider )
          case Some( bp : AxisAlignedBoxProvider ) =>
            bp.materialProvider = Some( new SingleColorMaterialProvider )
          case Some( tp : TriangleProvider ) =>
            tp.materialProvider = Some( new SingleColorMaterialProvider )
          case Some( mp : ModelProvider ) =>
            mp.materialProvider = Some( new SingleColorMaterialProvider )
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newLambertMaterialMenuItem = new JMenuItem( "Lambert material" )
    newLambertMaterialMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( pp : PlaneProvider ) =>
            pp.materialProvider = Some( new LambertMaterialProvider )
          case Some( sp : SphereProvider ) =>
            sp.materialProvider = Some( new LambertMaterialProvider )
          case Some( bp : AxisAlignedBoxProvider ) =>
            bp.materialProvider = Some( new LambertMaterialProvider )
          case Some( tp : TriangleProvider ) =>
            tp.materialProvider = Some( new LambertMaterialProvider )
          case Some( mp : ModelProvider ) =>
            mp.materialProvider = Some( new LambertMaterialProvider )
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newPhongMaterialMenuItem = new JMenuItem( "Phong material" )
    newPhongMaterialMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( pp : PlaneProvider ) =>
            pp.materialProvider = Some( new PhongMaterialProvider )
          case Some( sp : SphereProvider ) =>
            sp.materialProvider = Some( new PhongMaterialProvider )
          case Some( bp : AxisAlignedBoxProvider ) =>
            bp.materialProvider = Some( new PhongMaterialProvider )
          case Some( tp : TriangleProvider ) =>
            tp.materialProvider = Some( new PhongMaterialProvider )
          case Some( mp : ModelProvider ) =>
            mp.materialProvider = Some( new PhongMaterialProvider )
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newReflectiveMaterialMenuItem = new JMenuItem( "Reflective material" )
    newReflectiveMaterialMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( pp : PlaneProvider ) =>
            pp.materialProvider = Some( new ReflectiveMaterialProvider )
          case Some( sp : SphereProvider ) =>
            sp.materialProvider = Some( new ReflectiveMaterialProvider )
          case Some( bp : AxisAlignedBoxProvider ) =>
            bp.materialProvider = Some( new ReflectiveMaterialProvider )
          case Some( tp : TriangleProvider ) =>
            tp.materialProvider = Some( new ReflectiveMaterialProvider )
          case Some( mp : ModelProvider ) =>
            mp.materialProvider = Some( new ReflectiveMaterialProvider )
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newTransparentMaterialMenuItem = new JMenuItem( "Transparent material" )
    newTransparentMaterialMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( pp : PlaneProvider ) =>
            pp.materialProvider = Some( new TransparentMaterialProvider )
          case Some( sp : SphereProvider ) =>
            sp.materialProvider = Some( new TransparentMaterialProvider )
          case Some( bp : AxisAlignedBoxProvider ) =>
            bp.materialProvider = Some( new TransparentMaterialProvider )
          case Some( tp : TriangleProvider ) =>
            tp.materialProvider = Some( new TransparentMaterialProvider )
          case Some( mp : ModelProvider ) =>
            mp.materialProvider = Some( new TransparentMaterialProvider )
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    materialPopupMenu.add( newSingleColorMaterialMenuItem )
    materialPopupMenu.add( newLambertMaterialMenuItem )
    materialPopupMenu.add( newPhongMaterialMenuItem )
    materialPopupMenu.add( newReflectiveMaterialMenuItem )
    materialPopupMenu.add( newTransparentMaterialMenuItem )

    val newTexturePopupMenu = new JPopupMenu
    val newSingleColorTextureMenuItem = new JMenuItem( "Single color texture" )
    newSingleColorTextureMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( lp : LambertMaterialProvider ) =>
            lp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
          case Some( pp : PhongMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                pp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Specular Texture>" ) =>
                pp.specularTextureProvider = Some( new SingleColorTextureProvider )
              case Some( tp : TextureProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == tp ) pp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == tp ) pp.specularTextureProvider = Some( new SingleColorTextureProvider )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new SingleColorTextureProvider )
              case Some( tp : TextureProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == tp ) rp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == tp ) rp.specularTextureProvider = Some( new SingleColorTextureProvider )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == tp ) rp.reflectionTextureProvider = Some( new SingleColorTextureProvider )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( sbp : SkyboxProvider ) =>
            selection match {
              case Some( "<Front Texture>" ) =>
                sbp.frontTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Back Texture>" ) =>
                sbp.backTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Left Texture>" ) =>
                sbp.leftTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Right Texture>" ) =>
                sbp.rightTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Top Texture>" ) =>
                sbp.topTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Bottom Texture>" ) =>
                sbp.bottomTextureProvider = Some( new SingleColorTextureProvider )
              case Some( tp : TextureProvider ) =>
                if( sbp.frontTextureProvider.isDefined && sbp.frontTextureProvider.get == tp ) sbp.frontTextureProvider = Some( new SingleColorTextureProvider )
                if( sbp.backTextureProvider.isDefined && sbp.backTextureProvider.get == tp ) sbp.backTextureProvider = Some( new SingleColorTextureProvider )
                if( sbp.leftTextureProvider.isDefined && sbp.leftTextureProvider.get == tp ) sbp.leftTextureProvider = Some( new SingleColorTextureProvider )
                if( sbp.rightTextureProvider.isDefined && sbp.rightTextureProvider.get == tp ) sbp.rightTextureProvider = Some( new SingleColorTextureProvider )
                if( sbp.topTextureProvider.isDefined && sbp.topTextureProvider.get == tp ) sbp.topTextureProvider = Some( new SingleColorTextureProvider )
                if( sbp.bottomTextureProvider.isDefined && sbp.bottomTextureProvider.get == tp ) sbp.bottomTextureProvider = Some( new SingleColorTextureProvider )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newChessboardTextureMenuItem = new JMenuItem( "Chessboard texture" )
    newChessboardTextureMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( lp : LambertMaterialProvider ) =>
            lp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
          case Some( pp : PhongMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                pp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Specular Texture>" ) =>
                pp.specularTextureProvider = Some( new ChessboardTextureProvider )
              case Some( tp : TextureProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == tp ) pp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == tp ) pp.specularTextureProvider = Some( new ChessboardTextureProvider )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new ChessboardTextureProvider )
              case Some( tp : TextureProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == tp ) rp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == tp ) rp.specularTextureProvider = Some( new ChessboardTextureProvider )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == tp ) rp.reflectionTextureProvider = Some( new ChessboardTextureProvider )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( sbp : SkyboxProvider ) =>
            selection match {
              case Some( "<Front Texture>" ) =>
                sbp.frontTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Back Texture>" ) =>
                sbp.backTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Left Texture>" ) =>
                sbp.leftTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Right Texture>" ) =>
                sbp.rightTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Top Texture>" ) =>
                sbp.topTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Bottom Texture>" ) =>
                sbp.bottomTextureProvider = Some( new ChessboardTextureProvider )
              case Some( tp : TextureProvider ) =>
                if( sbp.frontTextureProvider.isDefined && sbp.frontTextureProvider.get == tp ) sbp.frontTextureProvider = Some( new ChessboardTextureProvider )
                if( sbp.backTextureProvider.isDefined && sbp.backTextureProvider.get == tp ) sbp.backTextureProvider = Some( new ChessboardTextureProvider )
                if( sbp.leftTextureProvider.isDefined && sbp.leftTextureProvider.get == tp ) sbp.leftTextureProvider = Some( new ChessboardTextureProvider )
                if( sbp.rightTextureProvider.isDefined && sbp.rightTextureProvider.get == tp ) sbp.rightTextureProvider = Some( new ChessboardTextureProvider )
                if( sbp.topTextureProvider.isDefined && sbp.topTextureProvider.get == tp ) sbp.topTextureProvider = Some( new ChessboardTextureProvider )
                if( sbp.bottomTextureProvider.isDefined && sbp.bottomTextureProvider.get == tp ) sbp.bottomTextureProvider = Some( new ChessboardTextureProvider )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newImageTextureMenuItem = new JMenuItem( "Image texture" )
    newImageTextureMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( lp : LambertMaterialProvider ) =>
            lp.diffuseTextureProvider = Some( new ImageTextureProvider( ui ) )
          case Some( pp : PhongMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                pp.diffuseTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Specular Texture>" ) =>
                pp.specularTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( tp : TextureProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == tp ) pp.diffuseTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == tp ) pp.specularTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( tp : TextureProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == tp ) rp.diffuseTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == tp ) rp.specularTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == tp ) rp.reflectionTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( sbp : SkyboxProvider ) =>
            selection match {
              case Some( "<Front Texture>" ) =>
                sbp.frontTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Back Texture>" ) =>
                sbp.backTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Left Texture>" ) =>
                sbp.leftTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Right Texture>" ) =>
                sbp.rightTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Top Texture>" ) =>
                sbp.topTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( "<Bottom Texture>" ) =>
                sbp.bottomTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some( tp : TextureProvider ) =>
                if( sbp.frontTextureProvider.isDefined && sbp.frontTextureProvider.get == tp ) sbp.frontTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( sbp.backTextureProvider.isDefined && sbp.backTextureProvider.get == tp ) sbp.backTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( sbp.leftTextureProvider.isDefined && sbp.leftTextureProvider.get == tp ) sbp.leftTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( sbp.rightTextureProvider.isDefined && sbp.rightTextureProvider.get == tp ) sbp.rightTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( sbp.topTextureProvider.isDefined && sbp.topTextureProvider.get == tp ) sbp.topTextureProvider = Some( new ImageTextureProvider( ui ) )
                if( sbp.bottomTextureProvider.isDefined && sbp.bottomTextureProvider.get == tp ) sbp.bottomTextureProvider = Some( new ImageTextureProvider( ui ) )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    val newInterpolatedImageTextureMenuItem = new JMenuItem( "Interpolated image texture" )
    newInterpolatedImageTextureMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( lp : LambertMaterialProvider ) =>
            lp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
          case Some( pp : PhongMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                pp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Specular Texture>" ) =>
                pp.specularTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( tp : TextureProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == tp ) pp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == tp ) pp.specularTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( tp : TextureProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == tp ) rp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == tp ) rp.specularTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == tp ) rp.reflectionTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some( sbp : SkyboxProvider ) =>
            selection match {
              case Some( "<Front Texture>" ) =>
                sbp.frontTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Back Texture>" ) =>
                sbp.backTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Left Texture>" ) =>
                sbp.leftTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Right Texture>" ) =>
                sbp.rightTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Top Texture>" ) =>
                sbp.topTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( "<Bottom Texture>" ) =>
                sbp.backTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some( tp : TextureProvider ) =>
                if( sbp.frontTextureProvider.isDefined && sbp.frontTextureProvider.get == tp ) sbp.frontTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( sbp.backTextureProvider.isDefined && sbp.backTextureProvider.get == tp ) sbp.backTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( sbp.leftTextureProvider.isDefined && sbp.leftTextureProvider.get == tp ) sbp.leftTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( sbp.rightTextureProvider.isDefined && sbp.rightTextureProvider.get == tp ) sbp.rightTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( sbp.topTextureProvider.isDefined && sbp.topTextureProvider.get == tp ) sbp.topTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
                if( sbp.bottomTextureProvider.isDefined && sbp.bottomTextureProvider.get == tp ) sbp.bottomTextureProvider = Some( new InterpolatedImageTextureProvider( ui ) )
              case Some(_) => assert( false, "This should not happen!")
              case None =>
            }
          case Some(_) => assert( false, "This should not happen!")
          case None =>
        }
        updateUI()
      }
    })

    newTexturePopupMenu.add( newSingleColorTextureMenuItem )
    newTexturePopupMenu.add( newChessboardTextureMenuItem )
    newTexturePopupMenu.add( newImageTextureMenuItem )
    newTexturePopupMenu.add( newInterpolatedImageTextureMenuItem )


    val sceneGraphTree = new JTree
    sceneGraphTree.setModel( new SceneGraphTreeModel( worldProvider ) )
    sceneGraphTree.addKeyListener( new KeyListener {
      def keyTyped(e: KeyEvent) {
        e.getKeyChar match {
          case KeyEvent.VK_DELETE =>
            val node = sceneGraphTree.getLastSelectedPathComponent
            if( node != null ) {
              worldProvider.remove( node )
              updateUI()
              detailsTable.model = DummyTableModel
            }
          case _ =>
        }
      }

      def keyPressed(e: KeyEvent) {}

      def keyReleased(e: KeyEvent) {}
    } )

    sceneGraphTree.addMouseListener( new MouseAdapter {
      override def mousePressed(e: MouseEvent) {
        val path = sceneGraphTree.getPathForLocation( e.getX, e.getY )

        if( path != null && SwingUtilities.isRightMouseButton( e ) ) {
          path.getLastPathComponent match {
            case "<Background>" =>
              backgroundPopupMenu.show( e.getComponent, e.getX, e.getY )

            case bp : BackgroundProvider =>
              backgroundPopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Camera>" =>
              cameraPopupMenu.show( e.getComponent, e.getX, e.getY )

            case cp : CameraProvider =>
              cameraPopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Front Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Back Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Left Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Right Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Top Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Bottom Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Anti-Aliasing Sampling Pattern>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              samplingPatternPopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Lens Sampling Pattern>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              samplingPatternPopupMenu.show( e.getComponent, e.getX, e.getY )

            case spp : SamplingPatternProvider =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              samplingPatternPopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Material>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              materialPopupMenu.show( e.getComponent, e.getX, e.getY )

            case mp : MaterialProvider =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              materialPopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Diffuse Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Specular Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )

            case "<Reflection Texture>" =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )


            case tp : TextureProvider =>
              selection = Some( path.getLastPathComponent )
              selectionParent = Some( path.getPathComponent( path.getPathCount - 2 ) )
              newTexturePopupMenu.show( e.getComponent, e.getX, e.getY )


            case _ =>
          }

        }
      }

    })

    sceneGraphTree.addTreeSelectionListener( new TreeSelectionListener {
      def valueChanged(e: TreeSelectionEvent) {
        val node = sceneGraphTree.getLastSelectedPathComponent
        if( node != null ) {
          node match {
            case o : TableModel =>
              detailsTable.model = o
            case _ =>
              detailsTable.model = DummyTableModel
          }
        } else {
          detailsTable.model = DummyTableModel
        }
      }
    })
    sceneGraphTree.getSelectionModel.setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION )
    val sceneGraphTreeScrollPane = new ScrollPane {
      contents = new Component {
        override lazy val peer = sceneGraphTree
      }
    }

    c.fill = Fill.Both
    c.ipady = 300
    c.weightx = 0.0
    c.weighty = 0.5
    c.gridwidth = 6
    c.gridx = 0
    c.gridy = 2
    c.anchor = Anchor.North
    layout( sceneGraphTreeScrollPane ) = c


    val detailsTable = new Table()
    val detailsTableScrollPane = new ScrollPane {
      contents = detailsTable
    }

    detailsTable.model = DummyTableModel
    c.fill = Fill.Both
    c.ipady = 100
    c.weighty = 0.5
    c.anchor = Anchor.PageEnd
    c.gridwidth = 6
    c.gridx = 0
    c.gridy = 3
    layout( detailsTableScrollPane ) = c

    val renderButton = new Button {
      text = "Render"
      reactions += {
        case ButtonClicked(_) =>
          val (c,w) = worldProvider.createWorld
          val window = new NiceRenderingWindow( w, c, renderingWindowsSize, Runtime.getRuntime.availableProcessors(), recursionDepth, clusterNodes.toList )
          window.a ! StartRendering()

      }
    }
    c.fill = Fill.Horizontal
    c.ipady = 0
    c.weighty = 0
    c.weightx = 0.5
    c.gridx = 0
    c.gridy = 4
    layout( renderButton ) = c

    private def updateUI() {
      sceneGraphTree.updateUI()
      renderButton.enabled = worldProvider.isReady
    }

  }

  def top = new MainFrame {
    title = "ScalaDelRay"
    contents = ui
    size = new Dimension( 500, 700 )
    resizable = false
    menuBar = new MenuBar
    val fileMenu = new Menu( "FILE" )
    fileMenu.contents += new MenuItem( "New")
    fileMenu.contents += new MenuItem( "Load...")
    val renderingSettingsMenu = new Menu( "RENDERING" )
    renderingSettingsMenu.contents += new MenuItem( Action("Settings...") {

      val frame = new Frame {

        lazy val imagePage = new GridBagPanel {

          val c = new Constraints



          val widthLabel = new Label( "Width:")

          c.fill = Fill.Horizontal

          c.weightx = 0.3
          c.gridx = 0
          c.gridy = 0

          layout( widthLabel ) = c

          val widthTextField = new TextField( "" + renderingWindowsSize.getWidth.toInt )

          c.weightx = 0.7
          c.gridx = 1
          layout( widthTextField ) = c

          val heightLabel = new Label( "Height:" )

          c.weightx = 0.3
          c.gridy = 1
          c.gridx = 0
          layout( heightLabel ) = c

          val heightTextField = new TextField( "" + renderingWindowsSize.getHeight.toInt )
          c.weightx = 0.7
          c.gridx = 1
          layout( heightTextField ) = c

          // Just to correct the layout
          val dummyLabel = new Label( "" )
          c.fill = Fill.Horizontal
          c.ipady = 0
          c.weighty = 1.0
          c.anchor = Anchor.PageEnd
          c.gridx = 1
          c.gridwidth = 1
          c.gridy = 2
          layout(dummyLabel) = c

        }

        lazy val algorithmPage =  new GridBagPanel {

          val c = new Constraints

          val algorithmLabel = new Label( "Algorithm: " )

          c.fill = Fill.Horizontal

          c.weightx = 0.3
          c.gridx = 0
          c.gridy = 0

          layout( algorithmLabel ) = c

          val algorithmComboBox = new ComboBox[String]( List( "Ray Casting", "Recursive Ray Tracing", "Stochastic Ray Tracing", "Path Tracing", "Photon Mapping" ) )
          algorithmComboBox.enabled = false
          algorithmComboBox.selection.item = "Stochastic Ray Tracing"
          c.weightx = 0.7
          c.gridx = 1

          layout( algorithmComboBox ) = c

          val recursionsLabel = new Label( "Recursions: " )

          c.fill = Fill.Horizontal

          c.weightx = 0.3
          c.gridx = 0
          c.gridy = 1

          layout( recursionsLabel ) = c

          val recursionsTextField = new TextField( "" + recursionDepth )

          c.weightx = 0.7
          c.gridx = 1

          layout( recursionsTextField ) = c

          val epsilonLabel = new Label( "Epsilon: " )

          c.fill = Fill.Horizontal

          c.weightx = 0.3
          c.gridx = 0
          c.gridy = 2

          layout( epsilonLabel ) = c

          val epsilonTextField = new TextField( "" + Constants.EPSILON )
          epsilonTextField.editable = false
          c.weightx = 0.7
          c.gridx = 1

          layout( epsilonTextField ) = c

          // Just to correct the layout
          val dummyLabel = new Label( "" )
          c.fill = Fill.Horizontal
          c.ipady = 0
          c.weighty = 1.0
          c.anchor = Anchor.PageEnd
          c.gridx = 1
          c.gridwidth = 1
          c.gridy = 3
          layout(dummyLabel) = c

        }

        lazy val clusterPage =  new GridBagPanel {

          var tempClusterNodes = clusterNodes.clone()

          val c = new Constraints

          val addButton = new Button("+" )
          addButton.action = Action( "+" ) {
            tempClusterNodes += (("",4444,1))
            nodesTable.revalidate()
          }

          c.fill = Fill.Horizontal

          c.weightx = 0.5
          c.gridx = 0
          c.gridy = 0

          layout( addButton ) = c

          val deleteButton = new Button("-" )
          deleteButton.action = Action( "-" ) {
            val x = nodesTable.selection.rows.toList.reverse
            for( i <- x ) if( i < tempClusterNodes.size ) tempClusterNodes.remove( i )
            nodesTable.revalidate()
          }

          c.fill = Fill.Horizontal

          c.weightx = 0.5
          c.gridx = 1
          c.gridy = 0

          layout( deleteButton ) = c

          val nodesTable = new Table()
          val nodesTableScrollPane = new ScrollPane {
            contents = nodesTable
          }

          nodesTable.model = new TableModel {
            def getRowCount: Int = tempClusterNodes.size

            def getColumnCount: Int = 3

            def getColumnName(columnIndex: Int): String =
              columnIndex match {
                case 0 =>
                  "Address"
                case 1 =>
                  "Port"
                case 2 =>
                  "Cores"
              }

            def getColumnClass(columnIndex: Int): Class[_] =
              columnIndex match {
                case 0 =>
                  classOf[String]
                case 1 =>
                  classOf[java.lang.Integer]
                case 2 =>
                  classOf[java.lang.Integer]
              }


            def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true

            def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = {
              val (address,port,cores) = tempClusterNodes( rowIndex )
              columnIndex match {
                case 0 =>
                  address
                case 1 =>
                  port.asInstanceOf[java.lang.Integer]
                case 2 =>
                  cores.asInstanceOf[java.lang.Integer]
              }
            }

            def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int) {
              val (address,port,cores) = tempClusterNodes( rowIndex )
              columnIndex match {
                case 0 =>
                  tempClusterNodes.update( rowIndex, (aValue.asInstanceOf[String],port,cores) )
                case 1 =>
                  tempClusterNodes.update( rowIndex, (address,aValue.asInstanceOf[Int],cores) )
                case 2 =>
                  tempClusterNodes.update( rowIndex, (address,port,aValue.asInstanceOf[Int]) )

              }
            }

            def addTableModelListener(l: TableModelListener) {}

            def removeTableModelListener(l: TableModelListener) {}
          }
          c.fill = Fill.Both
          c.ipady = 0
          c.weighty = 0.5
          c.anchor = Anchor.PageEnd
          c.gridwidth = 2
          c.gridx = 0
          c.gridy = 1
          layout( nodesTableScrollPane ) = c



          val discoverButton = new Button("Discover" )
          discoverButton.action = Action( "Discover" ) {
            discoverButton.enabled = false
            okButton.enabled = false
            cancelButton.enabled = false
            addButton.enabled = false
            deleteButton.enabled = false

            val socket = new DatagramSocket()
            val buf = new Array[Byte]( 256 )
            val address = InetAddress.getByName("228.5.6.7")
            var packet = new DatagramPacket(buf, buf.length, address, 12345 )
            packet.setData( "ScalaDelRay:1.0".getBytes )
            socket.send( packet )

            val discoveryActor = actorSystem.actorOf( Props( new Actor {

              def receive = {
                case m : StartDiscovery =>
                  try {
                    while( true ) {
                      packet = new DatagramPacket(buf, buf.length, address, 12345 )
                      socket.receive( packet )
                      val nodeAddress = new String( packet.getData, 8, packet.getLength - 8 )
                      val nodePort = (packet.getData()(0) << 24) | (packet.getData()(1) << 16) |  (packet.getData()(2) << 8) |  packet.getData()(3)
                      val nodeCores = (packet.getData()(4) << 24) | (packet.getData()(5) << 16) |  (packet.getData()(6) << 8) |  packet.getData()(7)

                      if( !tempClusterNodes.contains( (nodeAddress,nodePort,nodeCores) ) ) tempClusterNodes += ((nodeAddress,nodePort,nodeCores))

                      nodesTable.revalidate()
                    }
                  } catch {
                    case ex : SocketException =>

                  }
                  self ! PoisonPill

              }

            }))

            discoveryActor ! StartDiscovery()

            actorSystem.actorOf( Props( new Actor {

              discoverProgressBar.value = 0
              this.context.setReceiveTimeout( 0.0625 second )

              def receive = {
                case m : ReceiveTimeout =>
                discoverProgressBar.value = discoverProgressBar.value + 1

                if( discoverProgressBar.value == 60 ) {
                  self ! PoisonPill
                  discoverButton.enabled = true
                  okButton.enabled = true
                  cancelButton.enabled = true
                  addButton.enabled = true
                  deleteButton.enabled = true
                  socket.close()

                }

              }

            }))



          }

          c.ipady = 0
          c.weightx = 0.5
          c.weighty = 0
          c.gridx = 0
          c.gridy = 2
          c.gridwidth = 2


          layout( discoverButton ) = c

          val discoverProgressBar = new ProgressBar() {
            min = 0
            max = 60
          }


          c.gridy = 3


          layout( discoverProgressBar ) = c



        }

        title = "Rendering settings"

        val cancelButton = new Button("Cancel" )
        val okButton = new Button("Ok" )

        contents = new GridBagPanel {
          val c = new Constraints

          c.fill = Fill.Both
          c.weightx = 0.5
          c.weighty = 0.5
          c.gridx = 0
          c.gridy = 0
          c.gridwidth = 2


          c.anchor = Anchor.PageEnd

          val tabbedPane = new TabbedPane {
            pages += new Page( "Image", imagePage )
            pages += new Page( "Algorithm", algorithmPage )
            pages += new Page( "Cluster", clusterPage )
          }

          layout( tabbedPane ) = c
          c.weighty = 0.5
          c.weighty = 0
          c.fill = Fill.Horizontal
          c.gridwidth = 1


          cancelButton.action = Action( "Cancel" ) {
            close()
          }
          c.gridy = 1
          c.gridx = 0
          layout( cancelButton ) = c


          okButton.action = Action( "Ok" ) {
            renderingWindowsSize = new Dimension( imagePage.widthTextField.text.toInt, imagePage.heightTextField.text.toInt )
            recursionDepth = algorithmPage.recursionsTextField.text.toInt
            clusterNodes = clusterPage.tempClusterNodes
            close()
          }
          c.gridx = 1
          layout( okButton ) = c


        }
        visible = true


      }
    })
    val helpMenu = new Menu( "HELP" )
    helpMenu.contents += new MenuItem( "About" )
    //menuBar.contents += fileMenu
    menuBar.contents += renderingSettingsMenu
    menuBar.contents += helpMenu



  }



  private def createStandardScene : WorldProvider = {
    val worldProvider = new WorldProvider
    worldProvider.ambientLight = Color( 0.1, 0.1, 0.1 )
    val bp = new SingleBackgroundColorProvider
    bp.color = Color( 0.1, 0.1, 0.1 )
    worldProvider.backgroundProvider = Some( bp )
    val cameraProvider = new PerspectiveCameraProvider
    cameraProvider.position = Point3( 0, -0.5, 4.0 )
    cameraProvider.gazeDirection = Vector3( 0, 0, -1 )
    cameraProvider.samplingPatternProvider.get.asInstanceOf[RegularSamplingPatternProvider].x = 5
    cameraProvider.samplingPatternProvider.get.asInstanceOf[RegularSamplingPatternProvider].y = 5

    worldProvider.cameraProvider = Some( cameraProvider )

    val sphere1Provider = new SphereProvider
    sphere1Provider.translate = Point3( -1, -1, 0 )

    val sphere1ReflectiveMaterial = new ReflectiveMaterialProvider
    sphere1ReflectiveMaterial.phongExponent = 64
    val sphere1DiffuseColorTexture = new SingleColorTextureProvider
    sphere1DiffuseColorTexture.color = Color( 0.25, 0.01, 0.01 )
    val sphere1SpecularColorTexture = new SingleColorTextureProvider
    sphere1SpecularColorTexture.color = Color( 0.5, 0.5, 0.5 )
    val sphere1MirrorColorTexture = new SingleColorTextureProvider
    sphere1MirrorColorTexture.color = Color( 0.75, 0.75, 0.75 )
    sphere1ReflectiveMaterial.diffuseTextureProvider = Some( sphere1DiffuseColorTexture )
    sphere1ReflectiveMaterial.specularTextureProvider = Some( sphere1SpecularColorTexture )
    sphere1ReflectiveMaterial.reflectionTextureProvider = Some( sphere1MirrorColorTexture )
    sphere1Provider.materialProvider = Some( sphere1ReflectiveMaterial )
    worldProvider.renderableProvider += sphere1Provider

    val sphere2Provider = new SphereProvider
    sphere2Provider.translate = Point3( 1, -1, 0 )

    val sphere2ReflectiveMaterial = new ReflectiveMaterialProvider
    sphere2ReflectiveMaterial.phongExponent = 64
    val sphere2DiffuseColorTexture = new SingleColorTextureProvider
    sphere2DiffuseColorTexture.color = Color( 0.01, 0.25, 0.01 )
    val sphere2SpecularColorTexture = new SingleColorTextureProvider
    sphere2SpecularColorTexture.color = Color( 0.5, 0.5, 0.5 )
    val sphere2MirrorColorTexture = new SingleColorTextureProvider
    sphere2MirrorColorTexture.color = Color( 0.75, 0.75, 0.75 )
    sphere2ReflectiveMaterial.diffuseTextureProvider = Some( sphere2DiffuseColorTexture )
    sphere2ReflectiveMaterial.specularTextureProvider = Some( sphere2SpecularColorTexture )
    sphere2ReflectiveMaterial.reflectionTextureProvider = Some( sphere2MirrorColorTexture )
    sphere2Provider.materialProvider = Some( sphere2ReflectiveMaterial )
    worldProvider.renderableProvider += sphere2Provider

    val sphere3Provider = new SphereProvider
    sphere3Provider.translate = Point3( 0, 0.707, 0 )

    val sphere3ReflectiveMaterial = new ReflectiveMaterialProvider
    sphere3ReflectiveMaterial.phongExponent = 64
    val sphere3DiffuseColorTexture = new SingleColorTextureProvider
    sphere3DiffuseColorTexture.color = Color( 0.01, 0.01, 0.25 )
    val sphere3SpecularColorTexture = new SingleColorTextureProvider
    sphere3SpecularColorTexture.color = Color( 0.5, 0.5, 0.5 )
    val sphere3MirrorColorTexture = new SingleColorTextureProvider
    sphere3MirrorColorTexture.color = Color( 0.75, 0.75, 0.75 )
    sphere3ReflectiveMaterial.diffuseTextureProvider = Some( sphere3DiffuseColorTexture )
    sphere3ReflectiveMaterial.specularTextureProvider = Some( sphere3SpecularColorTexture )
    sphere3ReflectiveMaterial.reflectionTextureProvider = Some( sphere3MirrorColorTexture )
    sphere3Provider.materialProvider = Some( sphere3ReflectiveMaterial )
    worldProvider.renderableProvider += sphere3Provider

    val sphere4Provider = new SphereProvider
    sphere4Provider.translate = Point3( 0, 0.0, -0.707 )

    val sphere4ReflectiveMaterial = new ReflectiveMaterialProvider
    sphere4ReflectiveMaterial.phongExponent = 64
    val sphere4DiffuseColorTexture = new SingleColorTextureProvider
    sphere4DiffuseColorTexture.color = Color( 0.25, 0.25, 0.01 )
    val sphere4SpecularColorTexture = new SingleColorTextureProvider
    sphere4SpecularColorTexture.color = Color( 0.5, 0.5, 0.5 )
    val sphere4MirrorColorTexture = new SingleColorTextureProvider
    sphere4MirrorColorTexture.color = Color( 0.75, 0.75, 0.75 )
    sphere4ReflectiveMaterial.diffuseTextureProvider = Some( sphere4DiffuseColorTexture )
    sphere4ReflectiveMaterial.specularTextureProvider = Some( sphere4SpecularColorTexture )
    sphere4ReflectiveMaterial.reflectionTextureProvider = Some( sphere4MirrorColorTexture )
    sphere4Provider.materialProvider = Some( sphere4ReflectiveMaterial )
    worldProvider.renderableProvider += sphere4Provider


    val pointLightProvider = new PointLightProvider
    pointLightProvider.position = Point3( 0, -0.5, 1.5 )
    pointLightProvider.color = Color( 1.5, 1.5, 1.5 )

    worldProvider.lightDescriptionProvider += pointLightProvider


    worldProvider
  }

}


object DummyTableModel extends TableModel {

  def getRowCount: Int = 0

  def getColumnCount: Int = 2

  def getColumnName( column : Int): String = column match {
    case 0 => "Property"
    case 1 => "Value"
  }

  def getColumnClass(row: Int): Class[_] = classOf[String]

  def isCellEditable(row: Int, column: Int): Boolean = false

  def getValueAt(row: Int, column: Int): AnyRef = ""

  def setValueAt(obj: Any, row: Int, column: Int) {}

  def addTableModelListener(p1: TableModelListener) {}

  def removeTableModelListener(p1: TableModelListener) {}


}


