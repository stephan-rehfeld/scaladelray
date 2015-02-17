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

case class InterpolatedImageTexture( file : String ) extends Texture with Serializable {

  private val image = ImageIO.read( new File( file ) )

  override def apply(texCoord: TexCoord2D) = {
    var u = texCoord.u % 1.0
    var v = texCoord.v % 1.0

    if( u < 0.0 ) u = u + 1.0
    if( v < 0.0 ) v = v + 1.0

    val x = (image.getWidth-1) * u
    val y = (image.getHeight-1) - ((image.getHeight-1) * v)

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

  private def extract( argb : Int ) = ((argb & 0xff0000) >> 16,(argb & 0xff00) >> 8, argb & 0xff)
}