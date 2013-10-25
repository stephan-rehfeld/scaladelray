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
import scaladelray.math.{Normal3, Ray}
import scaladelray.texture.TexCoord2D


/**
 * An infinite large plane with the normal 0 1 0.
 *
 * @author Stephan Rehfeld
 * @param material The material of the geometry.
 */
class Plane( material : Material ) extends Geometry( material ) with Serializable {

   override def <-- ( r : Ray ) = {
     val h = r.d dot Plane.n
     if( h != 0.0 ) {
       val t = ((-r.o.asVector) dot Plane.n) / h
       val p = r( t )
       Set( Hit( r, this, t, Plane.n, TexCoord2D( p.x, -p.z ) ) )
     } else
       Set()
   }

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
