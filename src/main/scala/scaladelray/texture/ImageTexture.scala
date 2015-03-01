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

import javax.imageio.ImageIO
import java.io.File
import scaladelray.Color

/**
 * An image texture reads an image from a file returns a color from the image for a texture coordinate. The point 0/0
 * is the lower left corner of the image, while 1/1 is the upper right corner. The implementation has the capability
 * to flip the image horizontally and vertically.
 *
 * @param file The name of the file that contains the image.
 * @param flipHorizontally Flag if the image should be flipped horizontally.
 * @param flipVertically Flag if the image should be flipped vertically.
 */
case class ImageTexture( file : String, flipHorizontally : Boolean = false, flipVertically : Boolean = false ) extends Texture with Serializable {

  /**
   * The image.
   */
  private val image = ImageIO.read( new File( file ) )

  override def apply(texCoord: TexCoord2D) = {
    var t = ImageTexture.normalize( texCoord )
    if( flipHorizontally ) t = ImageTexture.flipHorizontally( t )
    if( flipVertically ) t = ImageTexture.flipVertically( t )
    val x = (image.getWidth-1) * t.u
    val y = (image.getHeight-1) - ((image.getHeight-1) * t.v)
    val argb = image.getRGB(math.round( x ).asInstanceOf[Int], math.round( y ).asInstanceOf[Int])
    val r = (argb & 0xff0000) >> 16
    val g = (argb & 0xff00) >> 8
    val b = argb & 0xff
    Color( r/255.0, g/255.0, b/255.0 )
  }

}

/**
 * The companion object of the ImageTexture. It provides several methods to manipulate texture coordinates.
 */
object ImageTexture {

  /**
   * This method normalizes a texture coordinate, making negative values positive and reducing values larger than 1 to a
   * value between 0 and 1. This results in a repeating texture.
   *
   * @param t The original texture coordinate.
   * @return The normalized texture coordinate.
   */
  def normalize( t : TexCoord2D ) : TexCoord2D = {
    var u = if(  t.u == 1.0 )  t.u else t.u % 1.0
    var v = if(  t.v == 1.0 )  t.v else t.v % 1.0
    if( u < 0.0 ) u = u + 1.0
    if( v < 0.0 ) v = v + 1.0
    TexCoord2D( u, v )
  }

  /**
   * This method flips a normalized texture coordinate horizontally by subtracting the u value from 1.
   *
   * @param t The original texture coordinate.
   * @return The flipped texture coordinate.
   */
  def flipHorizontally( t : TexCoord2D ) = TexCoord2D( 1.0 - t.u, t.v )

  /**
   * This method flips a normalized texture coordinate vertically by subtracting the v value from 1.
   *
   * @param t The original texture coordinate.
   * @return The flipped texture coordinate.
   */
  def flipVertically( t : TexCoord2D ) = TexCoord2D( t.u, 1.0 - t.v )

}