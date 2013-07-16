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
import javax.swing.event.{TableModelListener, TreeSelectionEvent, TreeSelectionListener}
import java.awt.event._
import scala.swing.event.ButtonClicked

object ScalaDelRay extends SimpleSwingApplication {

  var worldProvider = new WorldProvider

  lazy val ui = new GridBagPanel {
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
          sceneGraphTree.updateUI()
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
          sceneGraphTree.updateUI()
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
          sceneGraphTree.updateUI()
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
          sceneGraphTree.updateUI()
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
          sceneGraphTree.updateUI()
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 4
    c.gridy = 0
    layout( newNodeButton ) = c

    val newModelButton = new Button {
      text = "Model"
      reactions += {
        case ButtonClicked(_) =>
          val sp = new ModelProvider
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
          sceneGraphTree.updateUI()
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
            sceneGraphTree.updateUI()
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
            sceneGraphTree.updateUI()
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
            sceneGraphTree.updateUI()
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
            sceneGraphTree.updateUI()
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
        sceneGraphTree.updateUI()
      }
    })
    val createPerspectiveCameraMenuItem = new JMenuItem( "Perspective Camera" )
    createPerspectiveCameraMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.cameraProvider = Some( new PerspectiveCameraProvider )
        sceneGraphTree.updateUI()
      }
    })
    val createDOFCameraMenuItem = new JMenuItem( "DOF Camera" )
    createDOFCameraMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        worldProvider.cameraProvider = Some( new DOFCameraProvider )
        sceneGraphTree.updateUI()
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
            }
          case None =>
        }
        sceneGraphTree.updateUI()
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
          case None =>
        }
        sceneGraphTree.updateUI()
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
          case None =>
        }
        sceneGraphTree.updateUI()
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
          case None =>
        }
        sceneGraphTree.updateUI()
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
          case None =>
        }
        sceneGraphTree.updateUI()
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
          case None =>
        }
        sceneGraphTree.updateUI()
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
              case Some( spp : SamplingPatternProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == spp ) pp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == spp ) pp.specularTextureProvider = Some( new SingleColorTextureProvider )
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new SingleColorTextureProvider )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new SingleColorTextureProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == spp ) rp.diffuseTextureProvider = Some( new SingleColorTextureProvider )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == spp ) rp.specularTextureProvider = Some( new SingleColorTextureProvider )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == spp ) rp.reflectionTextureProvider = Some( new SingleColorTextureProvider )
            }
          case None =>
        }
        sceneGraphTree.updateUI()
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
              case Some( spp : SamplingPatternProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == spp ) pp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == spp ) pp.specularTextureProvider = Some( new ChessboardTextureProvider )
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new ChessboardTextureProvider )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new ChessboardTextureProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == spp ) rp.diffuseTextureProvider = Some( new ChessboardTextureProvider )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == spp ) rp.specularTextureProvider = Some( new ChessboardTextureProvider )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == spp ) rp.reflectionTextureProvider = Some( new ChessboardTextureProvider )
            }
          case None =>
        }
        sceneGraphTree.updateUI()
      }
    })

    val newImageTextureMenuItem = new JMenuItem( "Image texture" )
    newImageTextureMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( lp : LambertMaterialProvider ) =>
            lp.diffuseTextureProvider = Some( new ImageTextureProvider )
          case Some( pp : PhongMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                pp.diffuseTextureProvider = Some( new ImageTextureProvider )
              case Some( "<Specular Texture>" ) =>
                pp.specularTextureProvider = Some( new ImageTextureProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == spp ) pp.diffuseTextureProvider = Some( new ImageTextureProvider )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == spp ) pp.specularTextureProvider = Some( new ImageTextureProvider )
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new ImageTextureProvider )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new ImageTextureProvider )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new ImageTextureProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == spp ) rp.diffuseTextureProvider = Some( new ImageTextureProvider )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == spp ) rp.specularTextureProvider = Some( new ImageTextureProvider )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == spp ) rp.reflectionTextureProvider = Some( new ImageTextureProvider )
            }
          case None =>
        }
        sceneGraphTree.updateUI()
      }
    })

    val newInterpolatedImageTextureMenuItem = new JMenuItem( "Interpolated image texture" )
    newInterpolatedImageTextureMenuItem.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) {
        selectionParent match {
          case Some( lp : LambertMaterialProvider ) =>
            lp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider )
          case Some( pp : PhongMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                pp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider )
              case Some( "<Specular Texture>" ) =>
                pp.specularTextureProvider = Some( new InterpolatedImageTextureProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( pp.diffuseTextureProvider.isDefined && pp.diffuseTextureProvider.get == spp ) pp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider )
                if( pp.specularTextureProvider.isDefined && pp.specularTextureProvider.get == spp ) pp.specularTextureProvider = Some( new InterpolatedImageTextureProvider )
            }
          case Some( rp : ReflectiveMaterialProvider ) =>
            selection match {
              case Some( "<Diffuse Texture>" ) =>
                rp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider )
              case Some( "<Specular Texture>" ) =>
                rp.specularTextureProvider = Some( new InterpolatedImageTextureProvider )
              case Some( "<Reflection Texture>" ) =>
                rp.reflectionTextureProvider = Some( new InterpolatedImageTextureProvider )
              case Some( spp : SamplingPatternProvider ) =>
                if( rp.diffuseTextureProvider.isDefined && rp.diffuseTextureProvider.get == spp ) rp.diffuseTextureProvider = Some( new InterpolatedImageTextureProvider )
                if( rp.specularTextureProvider.isDefined && rp.specularTextureProvider.get == spp ) rp.specularTextureProvider = Some( new InterpolatedImageTextureProvider )
                if( rp.reflectionTextureProvider.isDefined && rp.reflectionTextureProvider.get == spp ) rp.reflectionTextureProvider = Some( new InterpolatedImageTextureProvider )
            }
          case None =>
        }
        sceneGraphTree.updateUI()
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
              sceneGraphTree.updateUI()
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
          val window = new NiceRenderingWindow( w, c, new Dimension( 640, 480 ), Runtime.getRuntime.availableProcessors() )
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

  }

  def top = new MainFrame {
    title = "ScalaDelRay"
    contents = ui
    size = new Dimension( 500, 700 )
    resizable = false

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