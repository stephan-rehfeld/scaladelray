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

import scaladelray.Constants
import scaladelray.math.{Mat3x3, Normal3, Point3, Ray, _}
import scaladelray.texture.{TexCoord2D, Texture}

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

  val (tan, biTan) = calcTanAndBiTan()

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
      var n = an * alpha + bn * beta + cn * gamma
      val texCoord = at * alpha + bt * beta + ct * gamma

      n = normalMap match {
        case Some( texture ) =>
          val c = texture( texCoord )
          (tan * (c.r-0.5) + biTan * (c.g-0.5) + n * (c.b-0.5)).normalized.asNormal
        case None =>
          n
      }
      Set() + GeometryHit( r, this, t, SurfacePoint( r( t ), n, tan, biTan, texCoord ) )
    }

  }

  override val center = ((a.asVector + b.asVector + c.asVector) / 3.0).asPoint

  override val lbf = Point3( math.min( a.x, math.min( b.x, c.x ) ), math.min( a.y, math.min( b.y, c.y ) ), math.min( a.z, math.min( b.z, c.z ) ) )

  override val run = Point3( math.max( a.x, math.max( b.x, c.x ) ), math.max( a.y, math.max( b.y, c.y ) ), math.max( a.z, math.max( b.z, c.z ) ) )

  override val axis = ((an + bn + cn).asVector / 3.0).normalized

  private def calcTanAndBiTan() : (Vector3,Vector3) = {
    val ab = b - a
    val ac = c - a

    val abt = bt - at
    val act = ct - at

    val r = 1.0 / (abt.u * act.v - act.u * abt.v)

    val tan = Vector3( (act.v*ab.x-abt.v*ac.x) * r,
                       (act.v*ab.y-abt.v*ac.y) * r,
                       (act.v*ab.z-abt.v*ac.z) * r )
    val biTan = Vector3( (abt.u*ac.x-act.u*ac.x) * r,
                         (abt.u*ac.y-act.u*ac.y) * r,
                         (abt.u*ac.z-act.u*ac.z) * r
    )
    (tan,biTan)
  }
}
