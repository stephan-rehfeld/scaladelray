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
 * This class represents a vector. It it used to represent directions. An instance of this class is immutable. Every
 * method creates a new object that contains the result.
 *
 * @author Stephan Rehfeld
 *
 * @param x The x-direction of the vector.
 * @param y The y-direction of the vector.
 * @param z The z-direction of the vector.
 */
case class Vector3( x : Double, y : Double, z : Double ) extends Serializable {

  /**
   * The magnitude of the vector.
   */
  val magnitude = scala.math.sqrt( x*x + y*y + z*z )

  /**
   * This method calculates the sum of two vector and returns the result.
   *
   * @param v The other vector.
   * @return The sum of both vectors.
   */
  def +( v : Vector3 ) = Vector3( x + v.x, y + v.y, z + v.z )

  /**
   * This method calculates the sum between this vector and a normal and returns the result as vector.
   *
   * @param v The normal.
   * @return The sum of the vector and the normal.
   */
  def +( v : Normal3 ) = Vector3( x + v.x, y + v.y, z + v.z )

  /**
   * This method calculates the difference between this vector and a normal and returns the result as vector.
   *
   * @param v The normal.
   * @return The difference of the vector and the normal.
   */
  def -( v : Normal3 ) = Vector3( x - v.x, y - v.y, z - v.z )

  /**
   * This method calculates the product between this vector and a scalar.
   *
   * @param v The scalar.
   * @return The product between this vector and the scalar.
   */
  def *( v : Double ) = Vector3( x * v, y * v, z * v )

  /**
   * This method divides all elements of the vector by a scalar.
   *
   * @param v The scalar.
   * @return The vector divided by the scalar.
   */
  def /( v : Double ) = Vector3( x / v, y / v, z / v )

  /**
   * This method calculates the cross product between two vectors and returns the result.
   *
   * @param v The other vector.
   * @return The cross product between both vectors.
   */
  def x( v : Vector3 ) : Vector3 = Vector3( y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x )

  /**
   * This method calculates the dot product between two vectors and returns the result.
   *
   * @param v The other vector.
   * @return The dot product between both vectors.
   */
  def dot( v : Vector3 ) : Double = x * v.x + y * v.y + z * v.z

  /**
   * This method calculates the dot product between this vector and a normal.
   *
   * @param n The normal
   * @return The dot product between this vector and the normal.
   */
  def dot( n : Normal3 ) : Double = x * n.x + y * n.y + z * n.z

  /**
   * This method returns a normalized vector that points in the same direction like this vector.
   *
   * @return A normalized vector that points in the same direction like this vector.
   */
  def normalized = Vector3( x / this.magnitude, y / this.magnitude, z / this.magnitude )

  /**
   * This method created a normalized vector of this vector and converts it to a normal.
   *
   * @return A normal with a magnitude of one, that points in the same direction like this vector.
   */
  def asNormal = {
    val n = normalized
    Normal3( n.x, n.y, n.z )
  }

  /**
   * This method reflects this vector on a normal. The normal and the vector point away from the point of the surface.
   *
   * @param n The normal.
   * @return The reflected vector.
   */
  def reflectOn( n : Normal3 ) = -this + (n * ((this dot n) * 2.0 ))

  /**                                                  g
   * Inverts the vector.
   *
   * @return An inverted vector.
   */
  def unary_- = Vector3( -x, -y, -z )

}
