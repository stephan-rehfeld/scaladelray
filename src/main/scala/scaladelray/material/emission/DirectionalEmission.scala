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
import scaladelray.math.Direction3

/**
 * A directional emission only emits within a single direction
 *
 * @param c The color of the emitted light.
 */
case class DirectionalEmission( c : Color ) extends Emission {

  override def apply( sp: SurfacePoint, d : Direction3 ) = if( sp.n.asDirection.normalized =~= d.normalized ) c else Color( 0, 0, 0 )

}
