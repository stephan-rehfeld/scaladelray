package scaladelray.optimization

import scaladelray.math.Point3


/**
 * This class represents a node of an octree. It can also represent the root node. It's derived from
 * [[scaladelray.optimization.AxisAlignedBoundingBox]].
 *
 * @param run The right upper near point of the bounding box.
 * @param lbf The left bottom far point of the bounding box.
 * @param nodes A set of none or eight octants.
 * @param data The data of the node. Usually faces.
 * @tparam T The type of the data of the node.
 */
class Octree[T](run : Point3, lbf : Point3, val nodes : Set[Octree[T]], val data : T ) extends AxisAlignedBoundingBox( run, lbf ) with Serializable {
  require( nodes.size == 0 || nodes.size == 8, "Each node must contain 0 or 8 octants." )
}

/**
 * The companion object of Octree. If contains an enum value for each octant and a function to determine the octant
 * of a vertex.
 */
object Octree extends Enumeration {

  type Octants =  Value
  val LeftUpperNear, LeftUpperFar, LeftLowerNear, LeftLowerFar, RightUpperNear, RightUpperFar, RightLowerNear, RightLowerFar = Value

  /**
   * This method determines the octant of a vertex relative to the center point of the octree node.
   *
   * @param center The center point of the octree node.
   * @param vertex The vertex.
   * @return The octant of the vertex.
   */
  def getOctantOfVertex( center : Point3, vertex : Point3 ) : Octree.Octants = {
    if( vertex.x > center.x ) { // right
      if( vertex.y > center.y ) { // upper
        if( vertex.z > center.z ) { // near
          RightUpperNear
        } else { // far
          RightUpperFar
        }
      } else { // lower
        if( vertex.z > center.z ) { // near
          RightLowerNear
        } else { // far
          RightLowerFar
        }
      }
    } else { // left
      if( vertex.y > center.y ) { // upper
        if( vertex.z > center.z ) { // near
          LeftUpperNear
        } else { // far
          LeftUpperFar
        }
      } else { // lower
        if( vertex.z > center.z ) { // near
          LeftLowerNear
        } else { // far
          LeftLowerFar
        }
      }
    }
  }
}
