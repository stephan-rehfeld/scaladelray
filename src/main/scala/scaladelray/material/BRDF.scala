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

package scaladelray.material

import scaladelray.geometry.SurfacePoint
import scaladelray.math.Vector3

/**
 * A Bidirectional reflectance distribution function describes how light is reflected between two directions
 * at one point on a surface, while both directions are on the same side of the surface.
 */
abstract class BRDF extends BSDF {

  override def apply( pIn: SurfacePoint, dIn: Vector3, eta : Double, pOut: SurfacePoint, dOut: Vector3 ) : Double = {
    if( pIn != pOut || isPositive(pIn.n dot dIn) != isPositive(pIn.n dot dOut) ) 0.0 else apply( pIn, dIn, dOut )
  }

  /**
   * A simplified form of apply, with only one point.
   *
   * @param p The point on the surface.
   * @param dIn The incident direction.
   * @param dOut The direction of the reflection.
   * @return The amount of light that is reflected between both directions at this point.
   */
  def apply( p : SurfacePoint, dIn : Vector3, dOut : Vector3 ) : Double

}
