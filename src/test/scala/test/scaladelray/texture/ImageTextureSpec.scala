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
import scaladelray.texture.{TexCoord2D, ImageTexture}
import scaladelray.Color

class ImageTextureSpec extends FunSpec {

  describe( "A ImageTexture" ) {
    it( "should return the color of the pixel when the texture coordinate corresponds exactly to the middle of the pixel" ) {
      val t = ImageTexture( "texture-example.bmp" )

      val c00 = t( TexCoord2D( 0.25, 0.25 ) )
      val c10 = t( TexCoord2D( 0.75, 0.25 ) )
      val c01 = t( TexCoord2D( 0.25, 0.75 ) )
      val c11 = t( TexCoord2D( 0.75, 0.75 ) )
      assert( c00 == Color( 0, 0, 1 ) )
      assert( c10 == Color( 1, 1, 0 ) )
      assert( c01 == Color( 1, 0, 0 ) )
      assert( c11 == Color( 0, 1, 0 ) )
    }

    it( "should not interpolate the color of the pixel" ) {
      val t = ImageTexture( "texture-example.bmp" )

      val c00 = t( TexCoord2D( 0.49, 0.49 ) )
      val c10 = t( TexCoord2D( 0.51, 0.49 ) )
      val c01 = t( TexCoord2D( 0.49, 0.51 ) )
      val c11 = t( TexCoord2D( 0.51, 0.51 ) )
      assert( c00 == Color( 0, 0, 1 ) )
      assert( c10 == Color( 1, 1, 0 ) )
      assert( c01 == Color( 1, 0, 0 ) )
      assert( c11 == Color( 0, 1, 0 ) )
    }
  }

}
