/*
 * Copyright 2015 Stephan Rehfeld
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

import scaladelray.math.{Point3, Normal3, Vector3, Ray}
import scaladelray.texture.{Texture, TexCoord2D}

/**
 * A rectangle.
 *
 * @param normalMap An optional normal map for the rectangle.
 */
case class Rectangle( normalMap : Option[Texture] ) extends Geometry with Serializable {

  override def <-- ( r : Ray ) = {
    val h = r.d dot Rectangle.n
    if( h != 0.0 ) {
      val t = ((-r.o.asVector) dot Rectangle.n) / h
      val p = r( t )
      if( p.x >= -Rectangle.e && p.x < Rectangle.e && p.z >= -Rectangle.e && p.z < Rectangle.e ) {
        val tangent = Vector3( 1, 0, 0 )
        val bitangent = Vector3( 0, 0, -1 )
        val texCoord = TexCoord2D( p.x + 0.5, -p.z + 0.5 )

        val n = normalMap match {
          case Some( texture ) =>
            val c = texture( texCoord )
            (tangent * (c.r-0.5) + bitangent * (c.g-0.5) + Rectangle.n * (c.b-0.5)).normalized.asNormal
          case None =>
            Disc.n
        }

        Set( GeometryHit( r, this, t,  SurfacePoint( r(t), n, tangent, bitangent, texCoord ) ) )
      } else Set()
    } else
      Set()
  }

  override val center = Point3( 0, 0, 0 )

  override val lbf = Point3( -0.5, 0, -0.5 )

  override val run = Point3( 0.5, 0, 0.5 )

  override val axis = Rectangle.n.asVector
}

/**
 * The companion object of the rectangle. It contains the standard normal and length of edges.
 */
object Rectangle {

  /**
   * The standard normal of all discs.
   */
  val n = Normal3( 0.0, 1.0, 0.0 )

  /**
   * The standard half length of the edges.
   */
  val e = 0.5

}