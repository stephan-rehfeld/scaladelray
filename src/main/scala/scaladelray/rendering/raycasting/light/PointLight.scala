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

import scaladelray.math.{Vector3, Point3}
import scaladelray.Color

/**
 * A point lights illuminates the scene from a single point in all directions.
 *
 * @param c The color of the light.
 * @param p The position of the light.
 * @param constantAttenuation The constant attenuation.
 * @param linearAttenuation The linear attenuation.
 * @param quadraticAttenuation The quadratic attenuation.
 */
case class PointLight( c : Color, p : Point3, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends Light {

  override def illuminates( p: Point3 ): Boolean = true

  override def directionFrom( p: Point3 ): Vector3 = (this.p - p).normalized

  override def intensity(point: Point3): Double = {
    val distance = (point - p).magnitude
    1.0 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)
  }
}
