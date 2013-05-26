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

package test.scaladelray.math

import org.scalatest.FunSpec

class Mat4x4Spec extends FunSpec {
  describe( "A Mat4x4" ) {
    it( "should take all 16 elements as constructor parameter and provide them as attributes" )( pending )
    it( "should be comparable")(pending)
    it( "should have a multiply operator for a vector, assuming that a vector is a 4 element vector with value 0 for w" )(pending)
    it( "should have a multiply operator for a point, assuming that a point is a 4 element vector with value 1 for w" )(pending)
    it( "should have a multiply operator for another Mat4x4" )(pending)
    it( "should create the correct transposed matrix")(pending)

    it( "should not be altered after multiplied with a vector" )(pending)
    it( "should not be altered after multiplied with a point" )(pending)
    it( "should not be altered after multiplied with a Mat4x4" )(pending)
    it( "should not be altered after creating the transposed matrix" )(pending)

    it( "should not alter the other vector while multiplying" )(pending)
    it( "should not alter the other point while multiplying" )(pending)
    it( "should not alter the other matrix while multiplying" )(pending)

  }
}
