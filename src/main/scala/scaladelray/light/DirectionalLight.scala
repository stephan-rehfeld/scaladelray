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

package scaladelray.light

import scaladelray.{Constants, World, Color}
import scaladelray.math.{Ray, Point3, Vector3}


class DirectionalLight( color : Color, direction : Vector3 ) extends Light( color ) {
  val l = (direction * -1).normalized

  def illuminates(point: Point3, world : World) = (Ray( point, direction * -1 ) --> world).filter( _.t > Constants.EPSILON ).isEmpty
  def directionFrom( point : Point3 ) = l
  def intensity(point: Point3): Double = 1.0
}
