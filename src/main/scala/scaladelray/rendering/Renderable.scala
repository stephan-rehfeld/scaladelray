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

import scaladelray.math.{Ray, Transform, Transformable}
import scaladelray.geometry.{SurfacePoint, Geometry}
import scaladelray.material.{Material, OldMaterial}

case class Hit( ray : Ray, renderable : Renderable, t : Double, sp : SurfacePoint )

/**
 * A renderable combines a transformation with a geometry and a material.
 *
 * @param t The transformation of the renderable.
 * @param geometry The geometry of the renderable.
 * @param oldMaterial The material of the renderable.
 * @param material The material of the renderable.
 */
case class Renderable( override val t : Transform, geometry : Geometry, oldMaterial : OldMaterial, material : Material ) extends Transformable( t ) {

  def <-- ( r : Ray ) : Set[Hit] = {
    val ri = down( r )
    val geoHits = ri --> geometry
    for( h <- geoHits ) yield Hit( r, this, h.t, SurfacePoint( up( h.sp.p ), up( h.sp.n ), up( h.sp.tan.asNormal ).asVector, up( h.sp.biTan.asNormal ).asVector, h.sp.t ) )
  }

}
