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


class AxisAlignedBox( material : Material ) extends Geometry( material ) {

  val right = new Node(
    Transform.translate( AxisAlignedBox.run ).rotateZ( -math.Pi/2.0 ),
    new Plane( material )
  )
  val top = new Node(
    Transform.translate( AxisAlignedBox.run ),
    new Plane( material )
  )
  val front = new Node(
    Transform.translate( AxisAlignedBox.run ).rotateZ( math.Pi ).rotateX( math.Pi/2.0 ),
    new Plane( material )
  )
  val left =  new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateZ( math.Pi/2.0 ),
    new Plane( material )
  )
  val bottom = new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateX( math.Pi ),
    new Plane( material )
  )
  val far = new Node(
    Transform.translate( AxisAlignedBox.lbf ).rotateZ( math.Pi ).rotateX( -math.Pi/2.0 ),
    new Plane( material )
  )

  def <--(r: Ray) = {

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

object AxisAlignedBox {
  private var run = Point3( 0.5, 0.5, 0.5 )
  private var lbf = Point3( -0.5, -0.5, -0.5 )
}