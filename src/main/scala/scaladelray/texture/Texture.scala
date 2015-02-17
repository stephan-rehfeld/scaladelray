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

package scaladelray.texture

import scaladelray.Color

/**
 * A texture coordinate represents a 2D coordinate for a texture.
 *
 * @param u The u element of the coordinate.
 * @param v The v element of the coordinate.
 */
case class TexCoord2D( u : Double, v : Double ) {

  /**
   * This function adds two texture coordinates and returns the result.
   *
   * @param t The other texture coordinate.
   * @return The sum of both texture coordinates.
   */
  def +( t : TexCoord2D ) = TexCoord2D( u + t.u, v + t.v )

  /**
   * This method multiplies the texture coordinate with a value and returns the product.
   *
   * @param n The other value.
   * @return The product.
   */
  def *( n : Double ) = TexCoord2D( u * n, v * n )
}

/**
 * An abstract base class for all textures.
 */
abstract class Texture {

  /**
   * An implementation of this method returns a color for a texture coordinate.
   *
   * @param texCoord The texture coordinate of the point.
   * @return The color of the point.
   */
  def apply( texCoord : TexCoord2D ) : Color
}
