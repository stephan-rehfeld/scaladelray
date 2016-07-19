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

package test.scaladelray.rendering

import org.scalatest.FunSpec
import scaladelray.math._
import scaladelray.geometry.SurfacePoint
import scaladelray.math.Ray
import test.scaladelray.geometry.GeometryTestAdapter
import scaladelray.rendering.Renderable
import scaladelray.Color
import scaladelray.texture.{SingleColorTexture, TexCoord2D}
import scaladelray.math.Direction3
import scaladelray.math.Normal3
import scaladelray.math.Point3
import scaladelray.rendering.Hit
import scaladelray.material.{LambertOldMaterial, Material}

class HitSpec extends FunSpec {

  describe( "A Hit" ) {
    it( "should consume a ray, geometry, t, and surface point as constructor parameter and provider them as value") {
      val r = Ray( Point3( 3, 5, 7 ), Direction3( 11, 13, 17 ) )
      val t = 8.15
      val ren = Renderable( Transform.scale( 1, 1, 1 ), GeometryTestAdapter(), LambertOldMaterial( SingleColorTexture( Color( 0, 0, 0 ) ) ), Material( None ) )
      val sp = SurfacePoint( r( t ), Normal3( 1, 0, 0 ), Direction3( 1, 0, 0 ), Direction3( 0, 0, -1 ), TexCoord2D( 2, 3 ) )

      val hit = Hit( r, ren, t, sp )

      assert( hit.ray == r )
      assert( hit.renderable == ren )
      assert( hit.t == t )
      assert( hit.sp == sp)
    }
  }

}
