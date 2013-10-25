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

/**
 * This class represents a ray. It has an origin and a direction. It implements a parametric line, so it can calculate
 * a point on a ray for a given t. It can also calculate a t of a point.
 *
 * @author Stephan Rehfeld
 *
 * @param o The origin of the ray. Must not be null.
 * @param d The direction of the ray. Must not be null.
 */
case class Ray( o : Point3, d : Vector3 ) extends Serializable {

  require( o != null )
  require( d != null )

  /**
   * This method calculates the point for a given t.
   *
   * @param t The t.
   * @return The point that belongs to the t.
   */
  def apply( t : Double ) = o + (d*t)

  /**
   * This method calculates the t of a point.
   *
   * @param p The point.
   * @return The t of the point.
   */
  def apply( p : Point3 ) = (p - o).magnitude / d.magnitude

  /**
   * This method shoots the ray on the geometry and returns the set with all hits on the geometry.
   *
   * @param g The geometry where this ray should be shot on.
   * @return A set with all hits.
   */
  def --> ( g : Geometry ) = g <-- this

  /**
   * This method shoots the ray on into a world and returns the set with all hits on the geometries in the world.
   *
   * @param w The world where this ray should be shot in.
   * @return A set with all hits.
   */
  def --> ( w : World ) = w <-- this
}
