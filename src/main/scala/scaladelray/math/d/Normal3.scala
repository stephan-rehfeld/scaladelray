/*
 * Copyright 2016 Stephan Rehfeld
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

package scaladelray.math.d

import scaladelray.Constants

/**
 * This class represents a normal on the surface of an object. It it used for light calculation. An instance of this
 * class is immutable. Every method creates a new object that contains the result.
 *
 * @author Stephan Rehfeld
 *
 * @param x The x-direction of the normal.
 * @param y The y-direction of the normal.
 * @param z The z-direction of the normal.
 */
case class Normal3( x : Double, y : Double, z : Double ) extends Serializable {

  /**
   * The magnitude of the normal.
   */
  val magnitude = scala.math.sqrt( x*x + y*y + z*z )

  /**
   * This method multiplies the normal with a scalar and returns the result as a new [[Normal3]]
   * object.
   *
   * @param v The factor for the multiplication.
   * @return A new normal that contains the result of the multiplication.
   */
  def *( v : Double ) = Normal3( x * v, y * v, z * v )

  /**
   * This method multiplies two normals and returns the result as a new [[Normal3]]
   * object.
   *
   * @param n The normal that should be added to this normal.
   * @return The result of the addition.
   */
  def +( n : Normal3 ) = Normal3( x + n.x, y + n.y, z + n.z )

  /**
   * This method calculated the dot product of this normal and a direction.
   *
   * @param v The direction for the dot product.
   * @return The dot product of this normal and the direction.
   */

  def dot( v : Direction3 ) : Double = x * v.x + y * v.y + z * v.z

  /**
   * This method converts the normal to a [[Direction3]]. The direction has the same values for x, y, and
   * z.
   *
   * @return A direction with the same x, y, and z values as this normal.
   */
  def asDirection = Direction3( x, y, z )

  /**
    * Inverts the normal.
    *
    * @return An inverted normal.
    */
  def unary_- = Normal3( -x, -y, -z )

  /**
   * This method returns a normalized normal that points in the same direction like this normal.
   *
   * @return A normalized normal that points in the same direction like this normal.
   */
  def normalized = Normal3( x / this.magnitude, y / this.magnitude, z / this.magnitude )

  /**
   * A roughly equals between two normals. It is used to get rid of problems from rounding errors of floating points.
   *
   * @param n The other normals.
   * @return Returns true if both normals equal roughly.
   */
  def =~=( n : Normal3 ) = x >= n.x - Constants.EPSILON && x <= n.x + Constants.EPSILON &&
                           y >= n.y - Constants.EPSILON && y <= n.y + Constants.EPSILON &&
                           z >= n.z - Constants.EPSILON && z <= n.z + Constants.EPSILON
}
