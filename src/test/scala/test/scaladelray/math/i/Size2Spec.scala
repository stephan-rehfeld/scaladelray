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

package test.scaladelray.math.i

import org.scalatest.FunSpec

import scaladelray.math.i.Size2

class Size2Spec extends FunSpec {

  describe( "A Size2" ) {

    it( "should throw an Exception if the width is smaller than 0" ) {
      intercept[IllegalArgumentException] {
        Size2( -1, 255 )
      }
    }

    it( "should throw an Exception if the height is smaller than 0" ) {
      intercept[IllegalArgumentException] {
        Size2( 255, -1 )
      }
    }

  }

}
