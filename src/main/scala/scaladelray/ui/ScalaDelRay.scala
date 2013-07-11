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

    val newSphereButton = new Button( "Sphere" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 1
    c.gridy = 0
    layout( newSphereButton ) = c

    val newBoxButton = new Button( "Box" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 2
    c.gridy = 0
    layout( newBoxButton ) = c

    val newTriangleButton = new Button( "Triangle" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 3
    c.gridy = 0
    layout( newTriangleButton ) = c

    val newNodeButton = new Button( "Node" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 4
    c.gridy = 0
    layout( newNodeButton ) = c

    val newModelFromFile = new Button( "Model" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 5
    c.gridy = 0
    layout( newModelFromFile ) = c



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
            }
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
    c.gridy = 1
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
    c.gridy = 2
    layout( detailsTableScrollPane ) = c

    val renderButton = new Button( "Render" )
    c.fill = Fill.Horizontal
    c.ipady = 0
    c.weighty = 0
    c.weightx = 0.5
    c.gridx = 0
    c.gridy = 3
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