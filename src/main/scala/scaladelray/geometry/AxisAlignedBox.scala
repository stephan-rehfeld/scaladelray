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

package scaladelray.geometry

import scaladelray.math.{Vector3, Point3, Ray, Transform}
import scaladelray.texture.Texture

/**
 * This class represents an axis aligned box. The box has a width, height and depth of 1.
 *
 * @param normalMap An optional normal map for the box.
 */
case class AxisAlignedBox( normalMap : Option[Texture] ) extends Geometry with Serializable {

  /**
   * The right plane of the box.
   */
  private val right = new Node(
    Transform.translate( AxisAlignedBox.run ).rotateZ( -math.Pi/2.0 ),
    Plane( normalMap )
  )

  /**
   * The top plane of the box.
   */
  private val top = new Node(
    Transform.translate( AxisAlignedBox.run ),
    Plane( normalMap )
  )

  /**
   * The front plane of the box.
   */
  private val front = new Node(
    Transform.translate( AxisAlignedBox.run ).rotateX( math.Pi/2.0 ),
    Plane( normalMap )
  )

  /**
   * The left plane of the box.
   */
  private val left =  new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateZ( math.Pi/2.0 ),
    Plane( normalMap )
  )

  /**
   * The bottom plane of the box.
   */
  private val bottom = new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateX( math.Pi ),
    Plane( normalMap )
  )

  /**
   * The far plane of the box.
   */
  private val far = new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateX( -math.Pi/2.0 ),
    Plane( normalMap )
  )

  override def <--(r: Ray) = {

    var hits = Set[GeometryHit]()

    var planeHits = (r --> right) | (r --> left)
    for( hit <- planeHits ) {
      val p = r( hit.t )
      if( p.y >= AxisAlignedBox.lbf.y && p.y <= AxisAlignedBox.run.y && p.z >= AxisAlignedBox.lbf.z && p.z <= AxisAlignedBox.run.z ) hits = hits + hit
    }

    planeHits = (r --> top) | (r --> bottom)
    for( hit <- planeHits ) {
      val p = r( hit.t )
      if( p.x >= AxisAlignedBox.lbf.x && p.x <= AxisAlignedBox.run.x && p.z >= AxisAlignedBox.lbf.z && p.z <= AxisAlignedBox.run.z ) hits = hits + hit
    }

    planeHits = (r --> front) | (r --> far)
    for( hit <- planeHits ) {
      val p = r( hit.t )
      if( p.x >= AxisAlignedBox.lbf.x && p.x <= AxisAlignedBox.run.x && p.y >= AxisAlignedBox.lbf.y && p.y <= AxisAlignedBox.run.y ) hits = hits + hit
    }

    for( hit <- hits ) yield GeometryHit( hit.ray, this, hit.t, hit.sp )

  }

  override val center = Point3( 0, 0, 0 )

  override val lbf = AxisAlignedBox.lbf

  override val run = AxisAlignedBox.run

  override val axis = Vector3( 0, 1, 0 )
}

/**
 * The companion object of the axis aligned box. It contains nothing public ;-)
 */
object AxisAlignedBox {

  /**
   * The default right-upper-near point of the axis aligned box.
   */
  val run = Point3( 0.5, 0.5, 0.5 )

  /**
   * The default lower-bottom-far point of the axis aligned box.
   */
  val lbf = Point3( -0.5, -0.5, -0.5 )
}