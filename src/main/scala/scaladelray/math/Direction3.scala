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

import scaladelray.Constants

/**
 * This class represents a direction. An instance of this class is immutable. Every
 * method creates a new object that contains the result.
 *
 * @author Stephan Rehfeld
 *
 * @param x The x-direction of the direction.
 * @param y The y-direction of the direction.
 * @param z The z-direction of the direction.
 */
case class Direction3(x : Double, y : Double, z : Double ) extends Serializable {

  /**
   * The magnitude of the direction.
   */
  val magnitude = scala.math.sqrt( x*x + y*y + z*z )

  /**
   * This method calculates the sum of two directions and returns the result.
   *
   * @param v The other direction.
   * @return The sum of both directions.
   */
  def +( v : Direction3 ) = Direction3( x + v.x, y + v.y, z + v.z )

  /**
   * This method calculates the sum between this ditecion and a normal and returns the result as ditecion.
   *
   * @param v The normal.
   * @return The sum of the direction and the normal.
   */
  def +( v : Normal3 ) = Direction3( x + v.x, y + v.y, z + v.z )

  /**
   * This method calculates the difference between two directions and returns the result as direction.
   *
   * @param v The direction.
   * @return The difference between the directions.
   */
  def -( v : Direction3 ) = Direction3( x - v.x, y - v.y, z - v.z )

  /**
   * This method calculates the difference between this direction and a normal and returns the result as direction.
   *
   * @param v The normal.
   * @return The difference of the direction and the normal.
   */
  def -( v : Normal3 ) = Direction3( x - v.x, y - v.y, z - v.z )

  /**
   * This method calculates the product between this direction and a scalar.
   *
   * @param v The scalar.
   * @return The product between this direction and the scalar.
   */
  def *( v : Double ) = Direction3( x * v, y * v, z * v )

  /**
   * This method divides all elements of the direction by a scalar.
   *
   * @param v The scalar.
   * @return The direction divided by the scalar.
   */
  def /( v : Double ) = Direction3( x / v, y / v, z / v )

  /**
   * This method calculates the cross product between two directions and returns the result.
   *
   * @param v The other direction.
   * @return The cross product between both directions.
   */
  def x( v : Direction3 ) : Direction3 = Direction3( y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x )

  /**
   * This method calculates the dot product between two directions and returns the result.
   *
   * @param v The other direction.
   * @return The dot product between both direction.
   */
  def dot( v : Direction3 ) : Double = x * v.x + y * v.y + z * v.z

  /**
   * This method calculates the dot product between this direction and a normal.
   *
   * @param n The normal
   * @return The dot product between this direction and the normal.
   */
  def dot( n : Normal3 ) : Double = x * n.x + y * n.y + z * n.z

  /**
   * This method returns a normalized direction that points in the same direction like this direction.
   *
   * @return A normalized direction that points in the same direction like this direction.
   */
  def normalized = Direction3( x / this.magnitude, y / this.magnitude, z / this.magnitude )

  /**
   * This method created a normalized direction of this direction and converts it to a normal.
   *
   * @return A normal with a magnitude of one, that points in the same direction like this direction.
   */
  def asNormal = {
    val n = normalized
    Normal3( n.x, n.y, n.z )
  }

  /**
   * This method creates a point out of the direction.
   *
   * @return A point at the point where the direction points to.
   */
  def asPoint = {
    Point3( x, y, z )
  }

  /**
   * This method reflects this direction on a normal. The normal and the direction point away from the point of the surface.
   *
   * @param n The normal.
   * @return The reflected direction.
   */
  def reflectOn( n : Normal3 ) = -this + (n * ((this dot n) * 2.0 ))

  /**
   * Inverts the direction.
   *
   * @return An inverted direction.
   */
  def unary_- = Direction3( -x, -y, -z )

  /**
   * A roughly equals between two direction. It is used to get rid of problems from rounding errors of floating points.
   *
   * @param v The other direction.
   * @return Returns true if both directions equal roughly.
   */
  def =~=( v : Direction3 ) = x >= v.x - Constants.EPSILON && x <= v.x + Constants.EPSILON &&
                           y >= v.y - Constants.EPSILON && y <= v.y + Constants.EPSILON &&
                           z >= v.z - Constants.EPSILON && z <= v.z + Constants.EPSILON

}
