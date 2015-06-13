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

import scaladelray.Color
import scaladelray.geometry.SurfacePoint
import scaladelray.math.Vector3

/**
 * A simple emission emits light in all directions from a point.
 *
 * @param c The color of the light.
 */
case class SimpleEmission( c : Color ) extends Emission {

  override def apply( sp: SurfacePoint, d : Vector3 ) = c

}
