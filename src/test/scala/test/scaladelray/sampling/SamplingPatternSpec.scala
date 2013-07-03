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

package test.scaladelray.sampling

import org.scalatest.FunSpec
import scaladelray.math.Point2
import scaladelray.sampling.SamplingPattern


class SamplingPatternSpec extends FunSpec {
  describe( "A SamplingPattern" ) {
    it( "should accept a set of 2D points and provide them as attribute" ) {
      val points = Set() + Point2( -0.5, 0.5 ) + Point2( 0.5, 0.5 ) + Point2( 0.5, -0.5 ) + Point2( -0.5, -0.5 ) + Point2( 0.0, 0.0 )
      val s = SamplingPattern( points )
      assert( s.samplingPoints == points )
    }

    it( "should accept point where the values of each axis is between -0.5 and 0.5")  {
      val points = Set() + Point2( -0.5, 0.5 ) + Point2( 0.5, 0.5 ) + Point2( 0.5, -0.5 ) + Point2( -0.5, -0.5 ) + Point2( 0.0, 0.0 )
      val s = SamplingPattern( points )
      assert( s.samplingPoints == points )
    }

    it( "should throw an Exception if the value of one axis is smaller than -0.5 or larger than 0.5" ) {
      val points = Set() + Point2( -0.5, 0.5 ) + Point2( 0.5, 0.5 ) + Point2( 0.5, -0.5 ) + Point2( -0.5, -0.5 ) + Point2( 0.0, 0.0 ) +
        Point2( -1.0, 0.0 ) + Point2( 1.0, 0.0 ) + Point2( 0.0, -1.0 ) + Point2( 0.0, 1.0 )

      intercept[IllegalArgumentException] {
        val s = SamplingPattern( points )
      }
    }
  }

  describe( "The SamplingPattern companion object ") {
    it( "should have a function to create a regular sampling pattern that contains only one point in the middle" ) {
      assert( SamplingPattern.regularPattern( 1, 1 ).samplingPoints == Set( Point2( 0, 0 ) ) )
    }

    it( "should have a function to create a regular sampling pattern that contains two columns and three rows of sampling points" ) {
      val points = Set() +
        Point2( -0.5, 0.5 ) + Point2( 0.5, 0.5 ) +
        Point2( -0.5, 0.0 ) + Point2( 0.5, 0.0 ) +
        Point2( -0.5, -0.5 ) + Point2( 0.5, -0.5 )
      assert( SamplingPattern.regularPattern( 2, 3 ).samplingPoints == points )
    }

    it( "should have a function to create a regular sampling pattern that contains three columns and two rows of sampling points" ) {
      val points = Set() +
        Point2( -0.5, 0.5 ) + Point2( 0.0, 0.5 ) + Point2( 0.5, 0.5 ) +
        Point2( -0.5, -0.5 ) + Point2( 0.0, -0.5 ) + Point2( 0.5, -0.5 )
      assert( SamplingPattern.regularPattern( 3, 2 ).samplingPoints == points )
    }
  }

}
