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

package scaladelray

/**
 * This class represents a color in RGB color space. The channels normally have values between 0 and 1. Values smaller
 * than 0 are not allowed. Values larger than 0 are allowed to support HDRR effect in future. An object of this class
 * is immutable.
 *
 * @author Stephan Rehfeld
 *
 * @param r The red color channel. Must be at least 0.
 * @param g The green color channel. Must be at least 0.
 * @param b The blue color channel. Must be at least 0.
 */
case class Color( r : Double, g : Double, b : Double ) extends Serializable {
  require( r >= 0.0, "The parameter 'r' must be at least 0" )
  require( g >= 0.0, "The parameter 'g' must be at least 0" )
  require( b >= 0.0, "The parameter 'b' must be at least 0" )

  /**
   * The color converted to an ARGB integer. Values larger than 0 are just capped.
   *
   */
  val rgbInteger = 0xff << 24 | ((scala.math.min(r,1)*255).asInstanceOf[Int] & 0xff) << 16 | ((scala.math.min(g,1)*255).asInstanceOf[Int] & 0xff) << 8 | ((scala.math.min(b,1)*255).asInstanceOf[Int] & 0xff)

  /**
   * This method multiplies each channel of this color with a scalar and returns the result as new color.
   *
   * @param f The factor for the multiplication.
   * @return The result of the multiplication.
   */
  def *( f : Double ) = Color( r*f, g*f, b*f )

  /**
   * This method multiplies each channel of this color with the corresponding channel of another color.
   *
   * @param c The other color.
   * @return The result of the multiplication.
   */
  def *( c : Color ) = Color( r*c.r, g*c.g, b*c.b )

  /**
   * This method divides each channel of this color by a scalar and returns the result as new color.
   *
   * @param f The divisor for the division.
   * @return The result of the division.
   */
  def /( f : Double ) = Color( r/f, g/f, b/f )

  /**
   * This method adds each channel of this color with the corresponding channel of another color.
   *
   * @param c The other color.
   * @return The result of the addition.
   */
  def +( c : Color ) = Color( r+c.r, g+c.g, b+c.b )

  /**
   * A roughly equals between two colors. It is used to get rid of problems from rounding errors of floating points.
   *
   * @param c The other color.
   * @return Returns true if both colors equal roughly.
   */
  def =~=( c : Color ) = r >= c.r - Constants.EPSILON && r <= c.r + Constants.EPSILON &&
                         g >= c.g - Constants.EPSILON && g <= c.g + Constants.EPSILON &&
                         b >= c.b - Constants.EPSILON && b <= c.b + Constants.EPSILON
}
