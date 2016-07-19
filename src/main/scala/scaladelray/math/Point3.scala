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
 * This class represents a point. It it used for light calculation. An instance of this class is immutable. Every
 * method creates a new object that contains the result.
 *
 * @author Stephan Rehfeld
 *
 * @param x The x-value of the point.
 * @param y The y-value of the point.
 * @param z The z-value of the point.
 */
case class Point3( x : Double, y : Double, z : Double ) extends Serializable {

  /**
   * This method calculates the direction between two points and returns the result as [[scaladelray.math.Direction3]].
   *
   * @param p The other point.
   * @return The direction between both points as [[scaladelray.math.Direction3]].
   */
  def -( p : Point3 ) = Direction3( x - p.x, y - p.y, z - p.z )

  /**
   * This method calculates a new point by subtracting a direction from this point.
   *
   * @param v The direction.
   * @return The new point in the opposite direction of v.
   */
  def -( v : Direction3 ) = Point3( x - v.x, y - v.y, z - v.z )

  /**
   * This method calculates a new point by adding a direction to this point.
   *
   * @param v The direction.
   * @return The new point in the direction of v.
   */
  def +( v : Direction3 ) = Point3( x + v.x, y + v.y, z + v.z )

  /**
   * This method converts the point to a [[scaladelray.math.Direction3]]. The direction has the same values for x, y, an
   * z.
   *
   * @return A direction with the same x, y, and z values as this point.
   */
  def asDirection = Direction3( x, y, z )

  /**
   * This method converts the point to a [[scaladelray.math.Normal3]]. The normal has the same values for x, y, and
   * z.
   *
   * @return A normal with the same x, y, and z values as this point.
   */
  def asNormal = Normal3( x, y, z )

}
