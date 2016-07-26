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

package scaladelray.rendering

import scaladelray.geometry.{Geometry, SurfacePoint}
import scaladelray.material.Material
import scaladelray.math.{Ray, Transform, Transformable}

case class Hit( ray : Ray, renderable : Renderable, t : Double, sp : SurfacePoint )

/**
 * A renderable combines a transformation with a geometry and a material.
 *
 * @param t The transformation of the renderable.
 * @param geometry The geometry of the renderable.
 * @param material The material of the renderable.
 */
case class Renderable( override val t : Transform, geometry : Geometry, material : Material ) extends Transformable( t ) {

  def <-- ( r : Ray ) : Set[Hit] = {
    val ri = down( r )
    val geoHits = ri --> geometry
    for( h <- geoHits ) yield Hit( r, this, h.t, SurfacePoint( up( h.sp.p ), up( h.sp.n ), up( h.sp.tan.asNormal ).asDirection, up( h.sp.biTan.asNormal ).asDirection, h.sp.t ) )
  }

  /**
   * The center point of the geometry.
   */
  val center = up( geometry.center )

  /**
   * The lower bottom far point of the geometry.
   */
  val lbf = up( geometry.lbf )

  /**
   * The right upper near point of the geometry.
   */
  val run = up( geometry.run )

  /**
   * The axis of the geometry
   */
  val axis = up( geometry.axis )

}
