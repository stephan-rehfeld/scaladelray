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
import scala.swing.event.ButtonClicked
import scaladelray.Color
import scaladelray.math.{Vector3, Point3}
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

object ScalaDelRay extends SimpleSwingApplication {

  var worldProvider = createStandardScene
  var renderingWindowsSize = new Dimension( 640, 480 )
  var recursionDepth = 10
  var clusterNodes = mutable.ListBuffer[String]()
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
                worldProvider.geometryProvider += pp

            }
          } else {
            worldProvider.geometryProvider += pp

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
                worldProvider.geometryProvider += sp

            }
          } else {
            worldProvider.geometryProvider += sp

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
                worldProvider.geometryProvider += sp

            }
          } else {
            worldProvider.geometryProvider += sp

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
                worldProvider.geometryProvider += sp

            }
          } else {
            worldProvider.geometryProvider += sp

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
                worldProvider.geometryProvider += sp

            }
          } else {
            worldProvider.geometryProvider += sp

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
                worldProvider.geometryProvider += sp

            }
          } else {
            worldProvider.geometryProvider += sp

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
            case "<Camera>" =>
              cameraPopupMenu.show( e.getComponent, e.getX, e.getY )

            case cp : CameraProvider =>
              cameraPopupMenu.show( e.getComponent, e.getX, e.getY )

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
          val window = new NiceRenderingWindow( w, c, renderingWindowsSize, Runtime.getRuntime.availableProcessors(), recursionDepth )
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

          val recursionsLabel = new Label( "Recursions: " )

          c.fill = Fill.Horizontal

          c.weightx = 0.3
          c.gridx = 0
          c.gridy = 0

          layout( recursionsLabel ) = c

          val recursionsTextField = new TextField( "" + recursionDepth )

          c.weightx = 0.7
          c.gridx = 1

          layout( recursionsTextField ) = c

          val epsilonLabel = new Label( "Epsilon: " )

          c.fill = Fill.Horizontal

          c.weightx = 0.3
          c.gridx = 0
          c.gridy = 1

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
          c.gridy = 2
          layout(dummyLabel) = c

        }

        lazy val clusterPage =  new GridBagPanel {

          var tempClusterNodes = clusterNodes.clone()

          val c = new Constraints

          val addButton = new Button("+" )
          addButton.action = Action( "+" ) {
            tempClusterNodes += ""
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

            def getColumnCount: Int = 1

            def getColumnName(columnIndex: Int): String = "Address"

            def getColumnClass(columnIndex: Int): Class[_] = classOf[String]

            def isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true

            def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = tempClusterNodes( rowIndex )

            def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int) {
              tempClusterNodes.update( rowIndex, aValue.asInstanceOf[String] )
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
            actorSystem.actorOf( Props( new Actor {

              discoverProgressBar.value = 0
              this.context.setReceiveTimeout( 0.0625 second )

              def receive = {
                case m : ReceiveTimeout =>
                discoverProgressBar.value = discoverProgressBar.value + 1

                if( discoverProgressBar.value == 60 ) {
                  self ! PoisonPill
                  discoverButton.enabled = true
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

          val cancelButton = new Button("Cancel" )
          cancelButton.action = Action( "Cancel" ) {
            close()
          }
          c.gridy = 1
          c.gridx = 0
          layout( cancelButton ) = c

          val okButton = new Button("Ok" )
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
    val cameraProvider = new PerspectiveCameraProvider
    cameraProvider.position = Point3( 8, 8, 8 )
    cameraProvider.gazeDirection = Vector3( -1, -1, -1 )

    worldProvider.cameraProvider = Some( cameraProvider )


    val planeProvider = new PlaneProvider
    val planeLambertMaterial = new LambertMaterialProvider
    val greySingleColorTextureProvider = new SingleColorTextureProvider
    greySingleColorTextureProvider.color = Color( 0.8, 0.8, 0.8 )

    planeLambertMaterial.diffuseTextureProvider = Some( greySingleColorTextureProvider )
    planeProvider.materialProvider = Some( planeLambertMaterial )
    worldProvider.geometryProvider += planeProvider


    val sphereProvider = new SphereProvider
    sphereProvider.translate = Point3( 0, 1, -5 )

    val spherePhongMaterial = new PhongMaterialProvider
    spherePhongMaterial.phongExponent = 64
    val redSingleColorTexture = new SingleColorTextureProvider
    redSingleColorTexture.color = Color( 1, 0.2, 0.2 )
    val whiteSingleColorTextureProvider = new SingleColorTextureProvider
    whiteSingleColorTextureProvider.color = Color( 1, 1, 1 )
    spherePhongMaterial.diffuseTextureProvider = Some( redSingleColorTexture )
    spherePhongMaterial.specularTextureProvider = Some( whiteSingleColorTextureProvider )

    sphereProvider.materialProvider = Some( spherePhongMaterial )

    worldProvider.geometryProvider += sphereProvider


    val pointLightProvider = new PointLightProvider
    pointLightProvider.position = Point3( 0, 1, 0 )

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


