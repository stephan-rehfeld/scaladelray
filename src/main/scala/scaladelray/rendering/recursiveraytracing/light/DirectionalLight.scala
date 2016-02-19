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

package scaladelray.rendering.recursiveraytracing.light

import scaladelray.math.{Point3, Ray, Vector3}
import scaladelray.rendering.Renderable
import scaladelray.world.World
import scaladelray.{Color, Constants}

/**
 * A directional light illuminates the whole scene from the same direction.
 *
 * @param renderable The rendereable of this light
 * @param d The direction of the point light.
 */
case class DirectionalLight( renderable : Renderable, c : Color ,d : Vector3 ) extends Light {

  require( d.magnitude != 0, "The length of the direction vector must not be 0!")

  private val l = -d.normalized

  override def illuminates(point: Point3, world : World) = (Ray( point, d * -1 ) --> world).exists( (h) => h.t > Constants.EPSILON && h.renderable != renderable )

  override def directionFrom(p: Point3): Vector3 = l

  override def intensity( point: Point3 ): Double = 1.0
}