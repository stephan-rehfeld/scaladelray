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

package scaladelray.optimization

import scaladelray.geometry.{GeometryHit, Plane, Node}
import scaladelray.math.{Point3, Ray, Transform}
import scaladelray.Color
import scaladelray.material.SingleColorOldMaterial

/**
 * This class represents an axis aligned bounding box. It is used by the [[scaladelray.geometry.TriangleMesh]] to speed
 * up the rendering. It takes the right upper near and the left bottom far point as parameter. The "shoot-the-ray"
 * method returns if the ray intersects the bounding box.
 *
 * @param run The right upper near point of the bounding box.
 * @param lbf The left bottom far point of the bounding box.
 */
class AxisAlignedBoundingBox( run : Point3, lbf : Point3 ) {

  /**
   * A dummy material for the planes of the AABB.
   */
  private val material = SingleColorOldMaterial( Color( 0, 0, 0 ) )

  /**
   * The right plane of the AABB.
   */
  private val right = new Node(
    Transform.translate( run ).rotateZ( -math.Pi/2.0 ),
    Plane( None )
  )

  /**
   * The top plane of the AABB.
   */
  private val top = new Node(
    Transform.translate( run ),
    Plane( None )
  )

  /**
   * The front plane of the AABB.
   */
  private val front = new Node(
    Transform.translate( run ).rotateZ( math.Pi ).rotateX( math.Pi/2.0 ),
    Plane( None )
  )

  /**
   * The left plane of the AABB.
   */
  private val left =  new Node(
    Transform.translate( lbf ).rotateZ( math.Pi/2.0 ),
    Plane( None )
  )

  /**
   * The bottom plane of the AABB.
   */
  private val bottom = new Node(
    Transform.translate( lbf ).rotateX( math.Pi ),
    Plane( None )
  )

  /**
   * The far plane of the AABB.
   */
  private val far = new Node(
    Transform.translate( lbf ).rotateZ( math.Pi ).rotateX( -math.Pi/2.0 ),
    Plane( None )
  )

  /**
   * This method tests if the ray intersects the AABB.
   *
   * @param r The ray.
   * @return Returns true, if the ray intersects the AABB.
   */
  def <--(r: Ray) = {

    var hits = Set[GeometryHit]()

    var planeHits = (r --> right) | (r --> left)
    for( hit <- planeHits ) {
      val p = r( hit.t )
      if( p.y >= lbf.y && p.y <= run.y && p.z >= lbf.z && p.z <= run.z ) hits = hits + hit
    }

    planeHits = (r --> top) | (r --> bottom)
    for( hit <- planeHits ) {
      val p = r( hit.t )
      if( p.x >= lbf.x && p.x <= run.x && p.z >= lbf.z && p.z <= run.z ) hits = hits + hit
    }

    planeHits = (r --> front) | (r --> far)
    for( hit <- planeHits ) {
      val p = r( hit.t )
      if( p.x >= lbf.x && p.x <= run.x && p.y >= lbf.y && p.y <= run.y ) hits = hits + hit
    }
    //hits = hits.filter( (h) => h.t > Constants.EPSILON)
    !hits.isEmpty
  }

}
