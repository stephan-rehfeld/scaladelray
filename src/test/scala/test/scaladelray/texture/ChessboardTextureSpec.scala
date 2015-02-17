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

package test.scaladelray.texture

import org.scalatest.FunSpec
import scaladelray.texture.{TexCoord2D, ChessboardTexture}
import scaladelray.Color

class ChessboardTextureSpec extends FunSpec {
  describe( "A ChessboardTexture" ) {
    it( "should return the colors black and white according to the number of configured changes.") {
      for( x <- 1 to 10; y <- 1 to 10 ) {
        val t = ChessboardTexture( x, y )
        val stepSizeU = 0.5 / x
        val stepSizeV = 0.5 / y
        val startU = stepSizeU / 2.0
        val startV = stepSizeV / 2.0
        for( u <- 0 to x*2; v <- 0 to y*2 ) {
          val tc = TexCoord2D( startU + (u*stepSizeU), startV + (v*stepSizeV) )
          val c = t( tc )
          val cr = if( (u+v) % 2 == 0 ) Color( 0, 0, 0 ) else Color( 1, 1, 1 )
          assert( c == cr )
        }

      }
    }
  }

}
