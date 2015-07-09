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

package scaladelray.material.bsdf

import scaladelray.geometry.SurfacePoint
import scaladelray.math.Vector3

/**
 * A BSDF is a Bidirectional scattering distribution function. It is the super class of:
 *   - Bidirectional scattering-surface reflectance distribution functions (BSSRDFs)
 *   - Bidirectional reflectance distribution function (BRDFs)
 *   - Bidirectional transmittance distribution function (BTDFs)
 */
abstract class BSDF {

  /**
   * This function returns how much light is transported between both points and directions.
   *
   * @param pIn The point on the surface where the incident ray intersects the geometry.
   * @param dIn The direction of the incident ray.
   * @param eta The index of refraction of the media, where the light comes from.
   * @param pOut The point where the ray leaves the surface.
   * @param dOut The direction in which the ray leaved the surface.
   * @return The amount of light that is transported between both points and directions.
   */
  def apply( pIn : SurfacePoint, dIn : Vector3, eta : Double, pOut : SurfacePoint, dOut : Vector3 ) : Double


  /**
   * A function to determine if a value is positive.
   * @param v Returns true if v is positive.
   * @return True if v is positive.
   */
  protected def isPositive( v : Double ) = v > 0.0
}
