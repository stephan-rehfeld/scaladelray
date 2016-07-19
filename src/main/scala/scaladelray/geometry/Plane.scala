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

import scaladelray.math.d.{Direction3, Normal3, Point3}
import scaladelray.math.Ray
import scaladelray.texture.{TexCoord2D, Texture}


/**
 * An infinite large plane with the normal 0 1 0.
 *
 * @param normalMap An optional normal map for the plane.
 */
case class Plane( normalMap : Option[Texture] ) extends Geometry with Serializable {

   override def <-- ( r : Ray ) = {
     val h = r.d dot Plane.n
     if( h != 0.0 ) {
       val t = ((-r.o.asDirection) dot Plane.n) / h
       val p = r( t )
       val tangent = Direction3( 1, 0, 0 )
       val bitangent = Direction3( 0, 0, -1 )
       val texCoord = TexCoord2D( p.x, -p.z )

       val n = normalMap match {
         case Some( texture ) =>
           val c = texture( texCoord )
           (tangent * (c.r-0.5) + bitangent * (c.g-0.5) + Plane.n * (c.b-0.5)).normalized.asNormal
         case None =>
           Plane.n
       }

       Set( GeometryHit( r, this, t,  SurfacePoint( r(t), n, tangent, bitangent, texCoord ) ) )
     } else
       Set()
   }

  override val center = Point3( 0, 0, 0 )

  override val lbf = Point3( Double.NegativeInfinity, 0.0, Double.NegativeInfinity )

  override val run = Point3( Double.PositiveInfinity, 0.0, Double.PositiveInfinity )

  override val axis: Direction3 = Plane.n.asDirection
}

/**
 * The companion object of the plane. It contains the standard normal.
 */
object Plane {

  /**
   * The standard normal of all planes.
   */
  val n = Normal3( 0.0, 1.0, 0.0 )
}
