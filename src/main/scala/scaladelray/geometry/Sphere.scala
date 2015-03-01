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

import scaladelray.math.{Vector3, Point3, Ray}
import scaladelray.texture.{Texture, TexCoord2D}


/**
 * A sphere with the center at 0 0 0 and a radius of 1.
 *
 * @param normalMap An optional normal map for the sphere.
 */
case class Sphere( normalMap : Option[Texture] ) extends Geometry with Serializable {

  override def <-- ( r : Ray ) = {
    val o = r.o.asVector
    val one = r.d dot r.d
    val two = r.d dot (o * 2)
    val three = (o dot o) - 1
    val four = two * two - 4 * one * three

    four match {
      case x if x < 0.0 => Set()
      case 0.0 =>
        val t = -two / (2 * one)
        val p = r( t )
        val tangent = Vector3( -p.z, 0.0, p.x ).normalized
        val bitangent = tangent x p.asVector
        val texCoord = texCoordFor( p )
        val n = normalMap match {
          case Some( texture ) =>
            val c = texture( texCoord )
            (tangent * c.r + bitangent * c.g + p.asVector * c.b).normalized.asNormal
          case None =>
            p.asNormal
        }
        Set( GeometryHit( r, this, t, n, texCoord ) )

      case _ =>
        val t1 = (-two + scala.math.sqrt( four ))/ (2 * one)
        val p1 = r( t1 )
        val tangent1 = Vector3( -p1.z, 0.0, p1.x ).normalized
        val bitangent1 = tangent1 x p1.asVector
        val texCoord1 = texCoordFor( p1 )
        val n1 = normalMap match {
          case Some( texture ) =>
            val c = texture( texCoord1 )
            (tangent1 * c.r + bitangent1 * c.g + p1.asVector * c.b).normalized.asNormal
          case None =>
            p1.asNormal
        }


        val t2 = (-two - scala.math.sqrt( four ))/ (2 * one)
        val p2 = r( t2 )
        val tangent2 = Vector3( -p2.z, 0.0, p2.x ).normalized
        val bitangent2 = tangent2 x p2.asVector
        val texCoord2 = texCoordFor( p2 )
        val n2 = normalMap match {
          case Some( texture ) =>
            val c = texture( texCoord2 )
            (tangent2 * c.r + bitangent2 * c.g + p2.asVector * c.b).normalized.asNormal
          case None =>
            p2.asNormal
        }

        Set() + GeometryHit( r, this, t1, n1, texCoord1 ) + GeometryHit( r, this, t2, n2, texCoord2 )
    }
  }

  /**
   * This function calculates the texture coordinates of a point.
   *
   * @param p The point.
   * @return The texture coordinate.
   */
  private def texCoordFor( p : Point3 ) : TexCoord2D = {
    val d = p.asVector
    val theta = math.acos( d.y )
    val phi = math.atan2( d.x, d.z )
    TexCoord2D( phi / (math.Pi * 2), -(theta/math.Pi) )
  }

}
