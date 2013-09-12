package scaladelray.optimization

import scaladelray.math.Point3


class Octree[T](run : Point3, lbf : Point3, val nodes : Set[Octree[T]], val data : T ) extends AxisAlignedBoundingBox( run, lbf ) {
  require( nodes.size == 0 || nodes.size == 8, "Each node must contain 0 or 8 octants." )
}

object Octree extends Enumeration {
  type Octants =  Value
  val LeftUpperNear, LeftUpperFar, LeftLowerNear, LeftLowerFar, RightUpperNear, RightUpperFar, RightLowerNear, RightLowerFar = Value

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
