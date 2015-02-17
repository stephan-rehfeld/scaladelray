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

import scaladelray.math.{Point3, Ray}
import scaladelray.texture.TexCoord2D


/**
 * A sphere with the center at 0 0 0 and a radius of 1.
 *
 */
case class Sphere() extends Geometry with Serializable {

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
        Set( GeometryHit( r, this, t, r( t ).asNormal, texCoordFor( r( t ) )) )

      case _ =>
        val t1 = (-two + scala.math.sqrt( four ))/ (2 * one)
        val t2 = (-two - scala.math.sqrt( four ))/ (2 * one)
        Set() + GeometryHit( r, this, t1, r( t1 ).asNormal, texCoordFor( r( t1 ) ) ) + GeometryHit( r, this, t2, r( t2 ).asNormal, texCoordFor( r( t2 ) ) )
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
