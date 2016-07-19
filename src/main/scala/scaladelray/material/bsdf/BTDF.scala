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
import scaladelray.math.Direction3

/**
 * A Bidirectional transmittance distribution function describes how the light is transmitted from one
 * media to another and reflected. It is assumed that the in and output are the same.
 */
abstract class BTDF extends BSDF {

  override def apply(pIn: SurfacePoint, dIn: Direction3, eta : Double, pOut: SurfacePoint, dOut: Direction3 ) : Double = {
    if( pIn != pOut ) 0.0 else apply( pIn, dIn, eta, dOut )

  }

  /**
   * A simplified form of apply, with only one point.
   *
   * @param p The point on the surface.
   * @param dIn The incident direction.
   * @param eta The index of refraction of the media, where the light comes from.
   * @param dOut The direction of the transmission or the reflection

   * @return The amount of light that is transmitted or reflected between both directions at this point.
   */
  def apply(p : SurfacePoint, dIn : Direction3, eta : Double, dOut : Direction3 ) : Double

}
