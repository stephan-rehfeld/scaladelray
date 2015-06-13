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

package test.scaladelray.geometry

import scaladelray.geometry.{GeometryHit, Geometry}
import scaladelray.math.{Point3, Vector3, Ray}

case class GeometryTestAdapter() extends Geometry {
  override def <--(r: Ray): Set[GeometryHit] = throw new UnsupportedOperationException( "Just a test adapter! Don't call this method!" )
  override val normalMap = None
  override val center = Point3( 0, 0, 0 )
  override val lbf = Point3( 0, 0, 0 )
  override val run = Point3( 0, 0, 0 )
  override val axis = Vector3( 0, 0, 0 )
}

class GeometrySpec {

}
