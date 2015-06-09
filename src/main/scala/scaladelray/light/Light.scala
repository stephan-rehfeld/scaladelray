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

import scaladelray.Color
import scaladelray.math.{Vector3, Point3}
import scaladelray.world.World

/**
 * This trait defines attributes and functions of a light. All lights in the ray tracer need to implement this trait.
 * A light can have multiple sampling points and therefore can provide information how much a point is illuminated
 * from each of this point.
 */
trait Light extends Serializable {

  /**
   * The color of the light.
   */
  val color : Color

  /**
   * Return the number of sampling points of the light.
   *
   * @return The number of the sampling points of the light.
   */
  def samplingPoints : Int

  /**
   * An implementation of this method checks if a point is illuminated from the light. If the light uses
   * multiple sampling points the returned list contains information for each sampling point.
   *
   * @param point The point that should be checked.
   * @param world The world.
   * @return A list that contains information for each sampling point if the light from this point hits the point.
   */
  def illuminates( point : Point3, world : World ) : List[Boolean]

  /**
   * An implementation of this function returns the direction from a point to the light source. If the light uses
   * multiple sampling points the list contains the direction to each sampling point.
   *
   * @param point The illuminated point.
   * @return A list that contains the direction to each sampling point.
   */
  def directionFrom( point : Point3 ) : List[Vector3]

  /**
   * An implementation of this function returns the intensity of the light that reaches the illuminated point.
   * Usually, attenuation is used to calculate the intensity.
   *
   * @param point The illuminated point.
   * @return The intensity.
   */
  def intensity( point : Point3 ) : List[Double]


}
