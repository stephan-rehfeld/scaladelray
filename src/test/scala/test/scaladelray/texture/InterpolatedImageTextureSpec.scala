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

import scaladelray.Color
import scaladelray.texture.{InterpolatedImageTexture, TexCoord2D}

class InterpolatedImageTextureSpec extends FunSpec {

  describe( "A ImageTexture" ) {
    it( "should return the color of the pixel when the texture coordinate corresponds exactly to the middle of the pixel" ) {
      val t = InterpolatedImageTexture( "texture-example.bmp" )

      val c00 = t( TexCoord2D( 0.0, 0.0 ) )
      val c10 = t( TexCoord2D( 1.0, 0.0 ) )
      val c01 = t( TexCoord2D( 0.0, 1.0 ) )
      val c11 = t( TexCoord2D( 1.0, 1.0 ) )
      assert( c00 == Color( 0, 0, 1 ) )
      assert( c10 == Color( 1, 1, 0 ) )
      assert( c01 == Color( 1, 0, 0 ) )
      assert( c11 == Color( 0, 1, 0 ) )
    }

    it( "should interpolate the color of the pixel" ) {
      val t = InterpolatedImageTexture( "texture-example.bmp" )

      val c00 = t( TexCoord2D( 0.5, 0.0 ) )
      val c10 = t( TexCoord2D( 1.0, 0.5 ) )
      val c01 = t( TexCoord2D( 0.5, 1.0 ) )
      val c11 = t( TexCoord2D( 0.0, 0.5 ) )

      assert( c00 == ((Color( 0, 0, 1 )+Color( 1, 1, 0 ))/2) )
      assert( c10 == ((Color( 1, 1, 0 )+Color( 0, 1, 0 ))/2) )
      assert( c01 == ((Color( 1, 0, 0 )+Color( 0, 1, 0 ))/2) )
      assert( c11 == ((Color( 1, 0, 0 )+Color( 0, 0, 1 ))/2) )
    }

    it( "should be flippable in horizontal direction" ) {
      val t = InterpolatedImageTexture( "texture-example.bmp", 0, true )

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
      val t = InterpolatedImageTexture( "texture-example.bmp", 0, false, true )

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
      val t = InterpolatedImageTexture( "texture-example.bmp", 0, true, true )

      val c00 = t( TexCoord2D( 0, 0 ) )
      val c10 = t( TexCoord2D( 1, 0 ) )
      val c01 = t( TexCoord2D( 0, 1 ) )
      val c11 = t( TexCoord2D( 1, 1 ) )

      assert( c00 == Color( 0, 1, 0 ) )
      assert( c10 == Color( 1, 0, 0 ) )
      assert( c01 == Color( 1, 1, 0 ) )
      assert( c11 == Color( 0, 0, 1 ) )
    }

    it( "should be rotatable" ) {

      val t = InterpolatedImageTexture( "texture-example.bmp", Math.PI / 2.0 )

      val c00 = t( TexCoord2D( 0.0, 0.0 ) )
      val c10 = t( TexCoord2D( 1.0, 0.0 ) )
      val c01 = t( TexCoord2D( 0.0, 1.0 ) )
      val c11 = t( TexCoord2D( 1.0, 1.0 ) )

      assert( c00 =~= Color( 0, 0, 1 ) )
      assert( c10 =~= Color( 1, 0, 0 ) )
      assert( c01 =~= Color( 0, 0, 1 ) )
      assert( c11 =~= Color( 1, 0, 0 ) )

    }

  }
}
