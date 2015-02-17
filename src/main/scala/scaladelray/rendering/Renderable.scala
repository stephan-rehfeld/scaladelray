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

import scaladelray.math.{Normal3, Ray, Transform, Transformable}
import scaladelray.geometry.Geometry
import scaladelray.material.Material
import scaladelray.texture.TexCoord2D

case class Hit( t : Double, ray : Ray, renderable : Renderable, n : Normal3, texCoord2D : TexCoord2D )

/**
 * A renderable combines a transformation with a geometry and a material.
 *
 * @param t The transformation of the renderable.
 * @param g The geometry of the renderable.
 * @param m The material of the renderable.
 */
case class Renderable( override val t : Transform, g : Geometry, m : Material ) extends Transformable( t ) {

  def <-- ( r : Ray ) : Set[Hit] = {
    val ri = down( r )
    val geoHits = ri --> g
    for( h <- geoHits ) yield Hit( h.t, r, this, up( h.n ), h.texCoord2D )
  }

}
