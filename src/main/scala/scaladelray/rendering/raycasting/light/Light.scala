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

import scaladelray.math.{Direction3, Point3}
import scaladelray.Color

/**
 * Basic trait for all lights in the ray casting algorithm.
 *
 */
trait Light {

  /**
   * The color of the light
   */
  val c : Color

  /**
   * An implementation of this method checks if a point in space is illuminated by this light.
   *
   * @param p The point in space.
   * @return Return true if the point is illuminated by the light.
   */
  def illuminates( p : Point3 ) : Boolean

  /**
   * An implementation of this method returns the direction to the light source from a point in space.
   *
   * @param p The point in space.
   * @return The direction to the light source.
   */
  def directionFrom( p : Point3 ) : Direction3

  /**
   * An implementation of this function returns the intensity of the light that reaches the illuminated point.
   * Usually, attenuation is used to calculate the intensity.
   *
   * @param point The illuminated point.
   * @return The intensity.
   */
  def intensity( point : Point3 ) : Double

}
