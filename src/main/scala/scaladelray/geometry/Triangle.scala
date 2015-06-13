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

import scaladelray.math._
import scaladelray.texture.{Texture, TexCoord2D}
import scaladelray.Constants
import scaladelray.math.Point3
import scaladelray.math.Ray
import scaladelray.math.Normal3
import scaladelray.math.Mat3x3

/**
 * This class represents a triangle. The vertices can freely be set. Furthermore, the normal and texture coordinate of
 * each vertex can be set.
 *
 * @param a Point for the first vertex.
 * @param b Point for the second vertex.
 * @param c Point for the third vertex.
 * @param an Normal on the first vertex.
 * @param bn Normal on the second vertex.
 * @param cn Normal on the third vertex.
 * @param at Texture coordinate on the first vertex.
 * @param bt Texture coordinate on the second vertex.
 * @param ct Texture coordinate on the third vertex.
 * @param normalMap An optional normal map for the triangle.
 */
case class Triangle  ( a: Point3, b : Point3, c : Point3,
                an: Normal3, bn : Normal3, cn : Normal3,
                at: TexCoord2D, bt : TexCoord2D, ct : TexCoord2D, normalMap : Option[Texture] ) extends Geometry with Serializable {

  override def <--(r: Ray) = {
    val base = Mat3x3( a.x - b.x, a.x - c.x, r.d.x,
                       a.y - b.y, a.y - c.y, r.d.y,
                       a.z - b.z, a.z - c.z, r.d.z)

    val vec = a - r.o

    val beta = base.replaceCol1( vec ).determinant / base.determinant
    val gamma = base.replaceCol2( vec ).determinant / base.determinant
    val t = base.replaceCol3( vec ).determinant / base.determinant

    if( beta < 0.0 || gamma < 0.0 || beta + gamma > 1.0 || t < Constants.EPSILON ) {
      Set()
    } else {
      val alpha = 1 - beta - gamma
      Set() + GeometryHit( r, this, t, SurfacePoint( r( t ), an * alpha + bn * beta + cn * gamma, Vector3( 0, 0, 0 ), Vector3( 0, 0, 0 ), at * alpha + bt * beta + ct * gamma ) )
    }

  }

  override val center = ((a.asVector + b.asVector + c.asVector) / 3.0).asPoint

  override val lbf = Point3( math.min( a.x, math.min( b.x, c.x ) ), math.min( a.y, math.min( b.y, c.y ) ), math.min( a.z, math.min( b.z, c.z ) ) )

  override val run = Point3( math.max( a.x, math.max( b.x, c.x ) ), math.max( a.y, math.max( b.y, c.y ) ), math.max( a.z, math.max( b.z, c.z ) ) )

  override val axis = (an + bn + cn).asVector / 3.0
}
