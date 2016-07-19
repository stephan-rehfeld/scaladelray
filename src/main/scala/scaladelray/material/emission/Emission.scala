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

package scaladelray.material.emission

import scaladelray.Color
import scaladelray.geometry.SurfacePoint
import scaladelray.math.d.Direction3

/**
 * An emission describes how the material emits light to the scene.
 */
abstract class Emission {

  /**
   * Returns the light that is emitted at the surface point in the direction.
   *
   * @param sp The point on the surface.
   * @param d The direction.
   *
   * @return The light that is emitted from the point in the direction.
   */
  def apply( sp: SurfacePoint, d : Direction3 ) : Color

}
