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
 * The spot emission emits light only within a given angle.
 *
 * @param c The color of the light source.
 * @param halfAngle The half angle within the light is emitted.
 */
case class SpotEmission( c : Color, halfAngle : Double ) extends Emission {

  require( halfAngle > 0, "The halfAngle must be larger than 0." )
  require( halfAngle <= math.Pi / 2.0, "The halfAngle must not be larger than 90 degrees.")

  override def apply( sp: SurfacePoint, d : Direction3 ) = if( math.acos(  sp.n dot d ) <= halfAngle ) c else Color( 0, 0, 0 )

}
