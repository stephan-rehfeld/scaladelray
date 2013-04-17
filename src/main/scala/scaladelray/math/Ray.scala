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

package scaladelray.math

import scaladelray.geometry.Geometry
import scaladelray.World

case class Ray( o : Point3, d : Vector3 ) {

  require( o != null )
  require( d != null )

  def apply( t : Double ) = o + (d*t);
  def apply( p : Point3 ) = (p - o).magnitude / d.magnitude
  def --> ( g : Geometry ) = g <-- this
  def --> ( w : World ) = w <-- this
}
