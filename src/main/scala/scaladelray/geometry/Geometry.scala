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

import scaladelray.math.{Normal3, Ray}
import scaladelray.texture.TexCoord2D
import scaladelray.material.Material


/**
 * This class represents a hit of a ray with a geometry.
 *
 * @author Stephan Rehfeld
 *
 * @param ray The ray that hits the geometry.
 * @param geometry The geometry hit by the ray.
 * @param t The t value of the hit.
 * @param n The normal of the hit.
 * @param texCoord2D The texture coordinate of the hit.
 */
case class Hit( ray : Ray, geometry : Geometry, t : Double, n : Normal3, texCoord2D : TexCoord2D )

abstract class Geometry( val material : Material ) {

  def <-- ( r : Ray ) : Set[Hit]
  def +( g : Geometry ) = Set() + this + g

}
