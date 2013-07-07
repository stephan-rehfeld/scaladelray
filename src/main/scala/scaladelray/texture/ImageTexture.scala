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

case class ImageTexture( file : String ) extends Texture {

  private val image = ImageIO.read( new File( file ) )

  def apply(texCoord: TexCoord2D) = {
    var u = texCoord.u % 1.0
    var v = texCoord.v % 1.0
    if( u < 0.0 ) u = u + 1.0
    if( v < 0.0 ) v = v + 1.0
    val x = (image.getWidth-1) * u
    val y = (image.getHeight-1) - ((image.getHeight-1) * v)
    val argb = image.getRGB(math.round( x ).asInstanceOf[Int], math.round( y ).asInstanceOf[Int])
    val r = (argb & 0xff0000) >> 16
    val g = (argb & 0xff00) >> 8
    val b = argb & 0xff
    Color( r/255.0, g/255.0, b/255.0 )

  }
}
