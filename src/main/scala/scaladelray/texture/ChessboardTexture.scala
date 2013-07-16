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

case class ChessboardTexture( x : Int, y : Int ) extends Texture {
  def apply( texCoord : TexCoord2D ) = {
    val xStep = 0.5 / x.asInstanceOf[Double]
    val yStep = 0.5 / y.asInstanceOf[Double]

    var black = isBlack( math.abs( texCoord.u/xStep ).asInstanceOf[Int] ) == isBlack( math.abs( texCoord.v/yStep ).asInstanceOf[Int]  )
    if( texCoord.u < 0 ) black = !black
    if( texCoord.v < 0 ) black = !black
    if( black ) Color( 0, 0, 0) else Color( 1, 1, 1 )
  }

  private def isBlack( v : Int ) = (v % 2) == 0
}
