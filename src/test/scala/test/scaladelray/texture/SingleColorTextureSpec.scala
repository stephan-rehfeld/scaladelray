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
import scaladelray.texture.{TexCoord2D, SingleColorTexture}

class SingleColorTextureSpec extends FunSpec {

  describe( "A SingleColorTexture" ) {
    it( "should always return the same color" ) {
      val c = Color( 0.1, 0.4, 0.753 )
      val t = SingleColorTexture( c )

      for( x <- 0 to 100; y <- 0 to 100 ) {
        assert( t( TexCoord2D( x * 0.01, y * 0.01 ) ) == c )
      }
    }
  }

}
