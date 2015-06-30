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
 * An interpolated image texture reads the color from an image, but uses bi-linear interpolation to improve image
 * quality.
 *
 * @param file The name of the file that contains the image.
 * @param angle Rotates the texture by this angle.
 * @param flipHorizontally Flag if the image should be flipped horizontally.
 * @param flipVertically Flag if the image should be flipped vertically.
 */
case class InterpolatedImageTexture( file : String, angle : Double = 0.0, flipHorizontally : Boolean = false, flipVertically : Boolean = false ) extends Texture with Serializable {

  /**
   * The image.
   */
  private val image = ImageIO.read( new File( file ) )

  override def apply(texCoord: TexCoord2D) = {
    var t = TexCoord2D( math.cos( angle ) * texCoord.u + (-math.sin( angle )) * texCoord.v, math.sin( angle ) * texCoord.u + math.cos( angle ) * texCoord.v )
    t = ImageTexture.normalize( t )
    if( flipHorizontally ) t = ImageTexture.flipHorizontally( t )
    if( flipVertically ) t = ImageTexture.flipVertically( t )



    val x = (image.getWidth-1) * t.u
    val y = (image.getHeight-1) - ((image.getHeight-1) * t.v)

    val xa = x - math.floor( x )
    val ya = y - math.floor( y )

    val a = image.getRGB(math.floor( x ).asInstanceOf[Int], math.floor( y ).asInstanceOf[Int])
    val b = image.getRGB(math.ceil( x ).asInstanceOf[Int], math.floor( y ).asInstanceOf[Int])
    val c = image.getRGB(math.floor( x ).asInstanceOf[Int], math.ceil( y ).asInstanceOf[Int])
    val d = image.getRGB(math.ceil( x ).asInstanceOf[Int], math.ceil( y ).asInstanceOf[Int])

    val (redA,greenA,blueA) = extract( a )
    val (redB,greenB,blueB) = extract( b )
    val (redC,greenC,blueC) = extract( c )
    val (redD,greenD,blueD) = extract( d )

    val (redE,greenE,blueE) = (redA*(1.0-xa) + (redB * xa), greenA*(1.0-xa) + (greenB * xa), blueA*(1.0-xa) + (blueB * xa) )
    val (redF,greenF,blueF) = (redC*(1.0-xa) + (redD * xa), greenC*(1.0-xa) + (greenD * xa), blueC*(1.0-xa) + (blueD * xa) )

    val (red,green,blue) = (redE*(1.0-ya) + redF *ya , greenE* (1.0-ya) + greenF * ya, blueE*(1.0-ya) + blueF * ya )

    Color( red/255.0, green/255.0, blue/255.0 )
  }

  /**
   * This method extract single values between 0 and 255 from an integer that contains a color.
   *
   * @param argb The ARGB color integer.
   * @return A tuple that contains the red, green, and blue color channel. Values are between 0 and 255.
   */
  private def extract( argb : Int ) = ((argb & 0xff0000) >> 16,(argb & 0xff00) >> 8, argb & 0xff)
}