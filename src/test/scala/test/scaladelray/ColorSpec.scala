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

import org.scalatest.FunSpec
import scaladelray.Color

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

    it( "should convert the color to an ARGB integer if each channel is between 0 and 1" )( pending )

    it( "should convert the color to an ARGB integer if one or more channels are larger than 1" )( pending )

    it( "should convert throw an exception if r is smaller than 0" )( pending )

    it( "should convert throw an exception if g is smaller than 0" )( pending )

    it( "should convert throw an exception if b is smaller than 0" )( pending )

    it( "should have a multiply operator for a scalar" )( pending )

    it( "should have a multiply operator for another color" )( pending )

    it( "should have a add operator for another color" )( pending )

    it( "should not be altered while calculating an ARGB integer" )( pending )

    it( "should not be altered while multiplied with a scalar" )( pending )

    it( "should not be altered while multiplied with another color" )( pending )

    it( "should not be altered while added with another color" )( pending )

    it( "should not alter the other color while being multiplied with it")( pending)

    it( "should not alter the other color while being added with it")( pending)



  }

}
