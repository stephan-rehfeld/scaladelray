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
import scaladelray.ui.model.{NodeProvider, PlaneProvider, WorldProvider}
import javax.swing.JTree
import javax.swing.tree.{TreeSelectionModel, DefaultMutableTreeNode}
import javax.swing.event.{TableModelListener, TreeSelectionEvent, TreeSelectionListener}
import scala.swing.event.ButtonClicked
import scaladelray.Color
import scala.swing.Color


object ScalaDelRay extends SimpleSwingApplication {

  var worldProvider = new WorldProvider
  val worldProviderTreeNode = new DefaultMutableTreeNode( worldProvider, true )
  worldProviderTreeNode.add( new DefaultMutableTreeNode( "<Camera>", false ) )

  lazy val ui = new GridBagPanel {
    val c = new Constraints
    val shouldFill = true

    if( shouldFill ) c.fill = Fill.Horizontal

    val newPlaneButton = new Button {
      text = "Plane"
      reactions += {
        case ButtonClicked(_) =>
          val pp = new PlaneProvider
          val node = sceneGraphTree.getLastSelectedPathComponent.asInstanceOf[DefaultMutableTreeNode]
          if( node != null ) {
            node.getUserObject match {
              case np : NodeProvider =>
                np.childNodes += pp
                val planeNode = new DefaultMutableTreeNode( pp, true )
                planeNode.add( new DefaultMutableTreeNode( "<Material>", false ) )
                node.add( planeNode )
                sceneGraphTree.updateUI()
              case _ =>
                worldProvider.geometryProvider += pp
                val planeNode = new DefaultMutableTreeNode( pp, true )
                planeNode.add( new DefaultMutableTreeNode( "<Material>", false ) )
                worldProviderTreeNode.add( planeNode )
                sceneGraphTree.updateUI()
            }
          } else {
            worldProvider.geometryProvider += pp
            val planeNode = new DefaultMutableTreeNode( pp, true )
            planeNode.add( new DefaultMutableTreeNode( "<Material>", false ) )
            worldProviderTreeNode.add( planeNode )
            sceneGraphTree.updateUI()
          }

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

    val newAxisAlignedBoxButton = new Button( "Axis aligned box" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 2
    c.gridy = 0
    layout( newAxisAlignedBoxButton ) = c

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

    val newModelFromFile = new Button( "Model from File" )
    c.fill = Fill.Horizontal
    c.weightx = 0.5
    c.gridx = 5
    c.gridy = 0
    layout( newModelFromFile ) = c



    val sceneGraphTree = new JTree( worldProviderTreeNode )
    sceneGraphTree.addTreeSelectionListener( new TreeSelectionListener {
      def valueChanged(e: TreeSelectionEvent) {
        val node = sceneGraphTree.getLastSelectedPathComponent.asInstanceOf[DefaultMutableTreeNode]
        if( node != null ) {
          node.getUserObject match {
            case o : TableModel =>
              detailsTable.model = o
            case _ =>
              detailsTable.model = DummyTableModel
          }
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


  }

  def top = new MainFrame {
    title = "ScalaDelRay"
    contents = ui
    size = new Dimension( 600, 700 )
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