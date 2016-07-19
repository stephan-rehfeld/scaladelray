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

package test.scaladelray.world

import org.scalatest.FunSpec
import scaladelray.Color
import scaladelray.world.SingleBackgroundColor
import scaladelray.math.{Ray, Point3, Direction3}

class SingleBackgroundColorSpec extends FunSpec {

  describe(" A SingleBackgroundColor" ) {
    it( "should return the same color for any ray" ) {
      val c = Color( 0, 1.0, 0.77 )
      val b = SingleBackgroundColor( c )
      for( ox <- -10 to 10; oy <- -10 to 10; oz <- -10 to 10; dx <- -10 to 10; dy <- -10 to 10; dz <- -10 to 10 )
        assert( b( Ray( Point3( ox, oy, oz ), Direction3( dx, dy, dz ).normalized ) ) == c )
    }
  }

}
