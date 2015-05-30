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

package test.scaladelray.geometry

import org.scalatest.FunSpec
import scaladelray.math.{Normal3, Ray, Point3, Vector3}
import scaladelray.geometry.{SurfacePoint, GeometryHit}
import scaladelray.texture.TexCoord2D

class GeometryHitSpec extends FunSpec {

  describe( "A GeometryHit" ) {
    it( "should consume a ray, geometry, t, and surface point as constructor parameter and provider them as value") {
      val r = Ray( Point3( 3, 5, 7 ), Vector3( 11, 13, 17 ) )
      val g = new GeometryTestAdapter()
      val t = 8.15
      val sp = SurfacePoint( r( t ), Normal3( 1, 0, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 2, 3 ) )

      val hit = GeometryHit( r, g, t, sp )

      assert( hit.ray == r )
      assert( hit.geometry == g )
      assert( hit.t == t )
      assert( hit.sp == sp)

    }
  }

}
