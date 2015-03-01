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

      val c00 = t( TexCoord2D( 0, 0 ) )
      val c10 = t( TexCoord2D( 1, 0 ) )
      val c01 = t( TexCoord2D( 0, 1 ) )
      val c11 = t( TexCoord2D( 1, 1 ) )
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

    it( "should be flippable in horizontal direction" ) {
      val t = ImageTexture( "texture-example.bmp", true )

      val c00 = t( TexCoord2D( 0, 0 ) )
      val c10 = t( TexCoord2D( 1, 0 ) )
      val c01 = t( TexCoord2D( 0, 1 ) )
      val c11 = t( TexCoord2D( 1, 1 ) )

      assert( c00 == Color( 1, 1, 0 ) )
      assert( c10 == Color( 0, 0, 1 ) )
      assert( c01 == Color( 0, 1, 0 ) )
      assert( c11 == Color( 1, 0, 0 ) )
    }

    it( "should be flippable in vertical direction" ) {
      val t = ImageTexture( "texture-example.bmp", false, true )

      val c00 = t( TexCoord2D( 0, 0 ) )
      val c10 = t( TexCoord2D( 1, 0 ) )
      val c01 = t( TexCoord2D( 0, 1 ) )
      val c11 = t( TexCoord2D( 1, 1 ) )

      assert( c00 == Color( 1, 0, 0 ) )
      assert( c10 == Color( 0, 1, 0 ) )
      assert( c01 == Color( 0, 0, 1 ) )
      assert( c11 == Color( 1, 1, 0 ) )
    }

    it( "should be flippable in horizontal and vertical direction" ) {
      val t = ImageTexture( "texture-example.bmp", true, true )

      val c00 = t( TexCoord2D( 0, 0 ) )
      val c10 = t( TexCoord2D( 1, 0 ) )
      val c01 = t( TexCoord2D( 0, 1 ) )
      val c11 = t( TexCoord2D( 1, 1 ) )

      assert( c00 == Color( 0, 1, 0 ) )
      assert( c10 == Color( 1, 0, 0 ) )
      assert( c01 == Color( 1, 1, 0 ) )
      assert( c11 == Color( 0, 0, 1 ) )
    }
  }

  describe( "The ImageTexture companion object" ) {
    it( "should have a function to normalize a texture coordinate" ) {

      val t1 = TexCoord2D( 0, 0 )
      val t2 = TexCoord2D( 0.5, 0.5 )
      val t3 = TexCoord2D( 1, 1 )

      assert( ImageTexture.normalize( t1 ) == t1 )
      assert( ImageTexture.normalize( t2 ) == t2 )
      assert( ImageTexture.normalize( t3 ) == t3 )

      val t4 = TexCoord2D( 1.0, 1.5 )
      val t5 = TexCoord2D( 1.5, 1.0 )

      assert( ImageTexture.normalize( t4 ) == TexCoord2D( 1.0, 0.5 ) )
      assert( ImageTexture.normalize( t5 ) == TexCoord2D( 0.5, 1.0 ) )

      val t6 = TexCoord2D( -0.5, 0 )
      val t7 = TexCoord2D( 0, -0.5 )

      assert( ImageTexture.normalize( t6 ) == TexCoord2D( 0.5, 0.0 ) )
      assert( ImageTexture.normalize( t7 ) == TexCoord2D( 0.0, 0.5 ) )
    }

    it( "should have a function to flip a texture coordinate horizontally" ) {
      for( i <- 0 to 100; j <- 0 to 100 ) {
        val t = TexCoord2D( i * 0.01, j * 0.01 )
        assert( ImageTexture.flipHorizontally( t ) == TexCoord2D( 1.0 - t.u, t.v ))
      }
    }

    it( "should have a function to flip a texture coordinate vertically" ) {
      for( i <- 0 to 100; j <- 0 to 100 ) {
        val t = TexCoord2D( i * 0.01, j * 0.01 )
        assert( ImageTexture.flipVertically( t ) == TexCoord2D( t.u, 1.0 - t.v ))
      }
    }
  }

}
