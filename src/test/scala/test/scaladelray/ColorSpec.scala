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

package test.scaladelray

import scaladelray.{Constants, Color}
import org.scalatest.FunSpec

class ColorSpec extends FunSpec {

  describe( "A Color" ) {
    it( "should set the parameters for r, g, and b correctly" ) {
      val r = 0.3
      val g = 0.4
      val b = 0.5

      val color = Color( r, g, b )

      assert( color.r == r )
      assert( color.g == g )
      assert( color.b == b )
    }

    it( "should convert the color to an ARGB integer if each channel is between 0 and 1" ) {
      assert( Color( 1, 1, 1 ).rgbInteger == 0xffffffff )
      assert( Color( 0, 0, 0 ).rgbInteger == 0xff000000 )
      assert( Color( 0.5, 0.5, 0.5 ).rgbInteger == 0xff7f7f7f )
      assert( Color( 1, 0.5, 0 ).rgbInteger == 0xffff7f00 )

    }

    it( "should convert the color to an ARGB integer if one or more channels are larger than 1" ) {
      assert( Color( 1, 0, 0 ).rgbInteger == 0xffff0000 )
      assert( Color( 0, 1, 0 ).rgbInteger == 0xff00ff00 )
      assert( Color( 0, 0, 1 ).rgbInteger == 0xff0000ff )
    }

    it( "should convert throw an exception if r is smaller than 0" ) {
      intercept[RuntimeException] {
        Color( -10, 0, 0 )
      }
    }


    it( "should convert throw an exception if g is smaller than 0" ) {
      intercept[RuntimeException] {
        Color( 0, -10, 0 )
      }
    }

    it( "should convert throw an exception if b is smaller than 0" ) {
      intercept[RuntimeException] {
        Color( 0, 0, -10 )
      }
    }

    it( "should have a multiply operator for a scalar" ) {
      val c = Color( 1, 0.5, 0.3 )
      val r = c * 0.3

      assert( r.r == 1 * 0.3 )
      assert( r.g == 0.5 * 0.3 )
      assert( r.b == 0.3 * 0.3 )
    }

    it( "should have a divide operator for a scalar" ) {
      val c = Color( 1, 0.5, 0.3 )
      val r = c / 0.3

      assert( r.r == 1 / 0.3 )
      assert( r.g == 0.5 / 0.3 )
      assert( r.b == 0.3 / 0.3 )
    }

    it( "should have a multiply operator for another color" ) {
      val c1 = Color( 1, 0.5, 0.3 )
      val c2 = Color( 0.1, 0.2, 0.3 )

      val r = c1 * c2

      assert( r.r == 1 * 0.1 )
      assert( r.g == 0.5 * 0.2 )
      assert( r.b == 0.3 * 0.3 )
    }

    it( "should have an add operator for another color" ) {
      val c1 = Color( 1, 0.5, 0.3 )
      val c2 = Color( 0.1, 0.2, 0.3 )
      val r = c1 + c2

      assert( r.r == 1 + 0.1 )
      assert( r.g == 0.5 + 0.2 )
      assert( r.b == 0.3 + 0.3 )
    }

    it( "should not be altered while multiplied with a scalar" ) {
      val c = Color( 1, 0.5, 0.3 )
      val r = c * 0.3

      assert( c.r == 1 )
      assert( c.g == 0.5 )
      assert( c.b == 0.3 )
    }

    it( "should not be altered while divided with a scalar" ) {
      val c = Color( 1, 0.5, 0.3 )
      val r = c / 0.3

      assert( c.r == 1 )
      assert( c.g == 0.5 )
      assert( c.b == 0.3 )
    }

    it( "should not be altered while multiplied with another color" ) {
      val c1 = Color( 1, 0.5, 0.3 )
      val c2 = Color( 0.1, 0.2, 0.3 )

      c1 * c2

      assert( c1.r == 1 )
      assert( c1.g == 0.5 )
      assert( c1.b == 0.3 )
    }

    it( "should not be altered while added with another color" ) {
      val c1 = Color( 1, 0.5, 0.3 )
      val c2 = Color( 0.1, 0.2, 0.3 )

      c1 + c2

      assert( c1.r == 1 )
      assert( c1.g == 0.5 )
      assert( c1.b == 0.3 )
    }

    it( "should not alter the other color while being multiplied with it") {
      val c1 = Color( 1, 0.5, 0.3 )
      val c2 = Color( 0.1, 0.2, 0.3 )

      c1 * c2

      assert( c2.r == 0.1 )
      assert( c2.g == 0.2 )
      assert( c2.b == 0.3 )
    }

    it( "should not alter the other color while being added with it" ) {
      val c1 = Color( 1, 0.5, 0.3 )
      val c2 = Color( 0.1, 0.2, 0.3 )

      c1 * c2

      assert( c2.r == 0.1 )
      assert( c2.g == 0.2 )
      assert( c2.b == 0.3 )
    }

    it( "should have a roughly-equals (=~=) operator that compares two colors math within the tolerance defined by Constants.EPSILON") {
      val c = Color( 0.5, 0.5, 0.5 )

      assert( c =~= c )
      assert( c =~= Color( c.r + Constants.EPSILON, c.g, c.b ) )
      assert( c =~= Color( c.r - Constants.EPSILON, c.g, c.b ) )
      assert( c =~= Color( c.r, c.g + Constants.EPSILON, c.b ) )
      assert( c =~= Color( c.r, c.g - Constants.EPSILON, c.b ) )
      assert( c =~= Color( c.r, c.g, c.b + Constants.EPSILON ) )
    }
  }
}
