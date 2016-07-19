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

package test.scaladelray.geometry

import org.scalatest.FunSpec
import scaladelray.math.{Normal3, Direction3, Point3}
import scaladelray.geometry.SurfacePoint
import scaladelray.texture.TexCoord2D


class SurfacePointSpec extends FunSpec {

  describe( "A SurfacePoint" ) {
    it( "should consume a point, a normal, a tangent, a bitangent, and a texture coordinate as constructor parameter and provider them as value") {
      val p = Point3( 0, 0, 0 )
      val n = Normal3( 1, 0, 0 )
      val tan = Direction3( 1, 0, 0 )
      val biTan =  Direction3( 0, 0, -1 )
      val t = TexCoord2D( 2, 3 )

      val sp = SurfacePoint( p, n, tan, biTan, t )

      assert( sp.p == p )
      assert( sp.n == n )
      assert( sp.tan == tan )
      assert( sp.biTan == biTan )
      assert( sp.t == t )
    }
  }

}
