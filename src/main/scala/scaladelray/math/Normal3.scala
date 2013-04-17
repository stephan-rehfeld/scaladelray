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
case class Normal3( x : Double, y : Double, z : Double ) {

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
   * This method calculated the dot product of this normal and a vector.
   *
   * @param v The vector for the dot product.
   * @return The dot product of this normal and the vector.
   */

  def dot( v : Vector3 ) : Double = x * v.x + y * v.y + z * v.z

  /**
   * This method converts the normal to a [[Vector3]]. The vector has the same values for x, y, and
   * z.
   *
   * @return A vector with the same x, y, and z values as this normal.
   */
  def asVector = Vector3( x, y, z )

  /**                                                  g
    * Inverts the normal.
    *
    * @return An inverted normal.
    */
  def unary_- = Normal3( -x, -y, -z )
}
