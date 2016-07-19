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

package scaladelray

import scaladelray.math.i.Size2

/**
 * An instance of this class represents an image with high dynamic range. It uses the [[scaladelray.Color]] to represent
 * to color for each pixel.
 *
 * @param size The size of the image.
 */
case class HDRImage( size : Size2 ) {

  /**
   * The array that contains the color of all pixels.
   */
  private val d = Array.ofDim[Color]( size.width, size.height )

  // Set all pixels to black
  for{ x <- 0 until size.width
       y <- 0 until size.height
  } d( x )( y ) = Color( 0, 0, 0 )

  /**
   * This function returns the color of the request pixel.
   *
   * @param x The x coordinate of the pixel. Must be at least 0 an smaller than width.
   * @param y The y coordinate of the pixel. Must be at least 0 and smaller than height.
   * @return The color of the pixel
   */
  def apply( x : Int, y : Int ) : Color = {
    require( x >= 0, "The x coordinate of a pixel must be at least 0!" )
    require( x < size.width, "The x coordinate must be smaller than the width of the image!" )
    require( y >= 0, "The y coordinate of a pixel must be at least 0!" )
    require( y < size.height, "The y coordinate must be smaller than the height of the image!" )
    d( x )( y )
  }

  /**
   * This function sets the color of a pixel.
   * @param x The x coordinate of the pixel. Must be at least 0 an smaller than width.
   * @param y The y coordinate of the pixel. Must be at least 0 and smaller than height.
   * @param c The new color for the pixel.
   */
  def set( x : Int, y : Int, c : Color ) {
    require( x >= 0, "The x coordinate of a pixel must be at least 0!" )
    require( x < size.width, "The x coordinate must be smaller than the width of the image!" )
    require( y >= 0, "The y coordinate of a pixel must be at least 0!" )
    require( y < size.height, "The y coordinate must be smaller than the height of the image!" )
    d( x )( y ) = c
  }

  /**
   * Returns the minimum value of r, g, or b.
   *
   * @return The minimum of r, g, or b.
   */
  def min : Double = {
    var v = Double.MaxValue
    for{
      x <- 0 until size.width
      y <- 0 until size.height
    } {
      val c = this( x,y )
      v = scala.math.min( c.r, v )
      v = scala.math.min( c.g, v )
      v = scala.math.min( c.b, v )
    }
    v
  }

  /**
   * Returns the maximum of r, g, or b.
   *
   * @return The maximum of r, g, or b.
   */
  def max : Double = {
    var v = Double.MinValue
    for{
      x <- 0 until size.width
      y <- 0 until size.height
    } {
      val c = this( x,y )
      v = scala.math.max( c.r, v )
      v = scala.math.max( c.g, v )
      v = scala.math.max( c.b, v )
    }
    v
  }
}
