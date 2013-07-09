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

import scaladelray.material.Material
import scaladelray.math.{Point3, Ray, Transform}

/**
 * This class represents an axis aligned box. The box has a width, height and depth of 1.
 *
 * @param material The material for the box.
 */
class AxisAlignedBox( material : Material ) extends Geometry( material ) {

  /**
   * The right plane of the box.
   */
  private val right = new Node(
    Transform.translate( AxisAlignedBox.run ).rotateZ( -math.Pi/2.0 ),
    new Plane( material )
  )

  /**
   * The top plane of the box.
   */
  val top = new Node(
    Transform.translate( AxisAlignedBox.run ),
    new Plane( material )
  )

  /**
   * The front plane of the box.
   */
  val front = new Node(
    Transform.translate( AxisAlignedBox.run ).rotateZ( math.Pi ).rotateX( math.Pi/2.0 ),
    new Plane( material )
  )

  /**
   * The left plane of the box.
   */
  val left =  new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateZ( math.Pi/2.0 ),
    new Plane( material )
  )

  /**
   * The bottom plane of the box.
   */
  val bottom = new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateX( math.Pi ),
    new Plane( material )
  )

  /**
   * The far plane of the box.
   */
  val far = new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateZ( math.Pi ).rotateX( -math.Pi/2.0 ),
    new Plane( material )
  )

  override def <--(r: Ray) = {

    var hits = Set[Hit]()

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

    for( hit <- hits ) yield Hit( hit.ray, this, hit.t, hit.n, hit.texCoord2D )

  }

}

/**
 * The companion object of the axis aligned box. It contains nothing public ;-)
 */
object AxisAlignedBox {

  /**
   * The default right-upper-near point of the axis aligned box.
   */
  private var run = Point3( 0.5, 0.5, 0.5 )

  /**
   * The default lower-bottom-far point of the axis aligned box.
   */
  private var lbf = Point3( -0.5, -0.5, -0.5 )
}