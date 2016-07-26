/*
 * Copyright 2016 Stephan Rehfeld
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

package test.scaladelray.math.d

import org.scalatest.FunSpec

import scaladelray.math.d.Point2

class Point2Spec extends FunSpec {

  describe( "A Point2" ) {
    it( "has a constructor that takes the x and y value as parameter and provides them as attributes" ) {
      val p = Point2( 2, 3 )
      assert( p.x == 2 )
      assert( p.y == 3 )
    }

    it( "should be comparable" ) {
      val point1 = Point2( 2, 3 )
      val point2 = Point2( 2, 3 )

      assert( point1 == point2 )
    }

    it( "should have an overloaded *-operator that multiplies the x and y element with a scalar." ) {
      val point = Point2( 2, 3 )

      assert( (point*5) == Point2( 2 * 5, 3 * 5 ) )
    }

    it( "should have not be altered when its multiplied by a scalar." ) {
      val point = Point2( 2, 3 )
      point * 5
      assert( point == Point2( 2, 3 ) )
    }
  }

}
