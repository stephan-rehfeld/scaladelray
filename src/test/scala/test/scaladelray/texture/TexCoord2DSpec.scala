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
import scaladelray.texture.TexCoord2D

class TexCoord2DSpec extends FunSpec {

  describe( "A TexCoord2D" ) {
    it( "should have a method to add two texture coordinates" ) {
      val t1 = TexCoord2D( 2, 3 )
      val t2 = TexCoord2D( 5, 7 )

      assert( (t1+t2) == TexCoord2D( 2+5, 3+7) )
    }
    it( "should have a method to multiply the texture coordinate with a scalar value" ) {
      val t1 = TexCoord2D( 2, 3 )

      assert( (t1 * 5) == TexCoord2D( 10, 15 ) )
    }
  }

}
