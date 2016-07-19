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

package scaladelray.rendering.raycasting.light

import scaladelray.Color
import scaladelray.math.d.{Direction3, Point3}

/**
 * A directional light illuminates the whole scene from the same direction.
 *
 * @param d The direction of the point light.
 */
case class DirectionalLight( c : Color ,d : Direction3 ) extends Light {

  require( d.magnitude != 0, "The length of the direction vector must not be 0!")

  private val l = -d.normalized

  override def illuminates(p: Point3): Boolean = true

  override def directionFrom(p: Point3): Direction3 = l

  override def intensity( point: Point3 ): Double = 1.0
}
