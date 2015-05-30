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
 * A BSDF is a Bidirectional scattering distribution function. It is the super class of:
 *   - Bidirectional scattering-surface reflectance distribution functions (BSSRDFs)
 *   - Bidirectional reflectance distribution function (BRDFs)
 *   - Bidirectional transmittance distribution function (BTDFs)
 */
abstract class BSDF {

  def apply( pIn : SurfacePoint, dIn : Vector3, pOut : SurfacePoint, dOut : Vector3 ) : Double


  protected def isPositive( v : Double ) = v > 0.0
}
