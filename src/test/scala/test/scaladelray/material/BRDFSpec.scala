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

package test.scaladelray.material

import org.scalatest.FunSpec
import scaladelray.material.BRDF
import scaladelray.geometry.SurfacePoint
import scaladelray.math.{Normal3, Point3, Vector3}
import scaladelray.texture.TexCoord2D

case class BRDFTestAdapter() extends BRDF {
  var called = false
  override def apply(p: SurfacePoint, dIn: Vector3, dOut: Vector3): Double = {
    called = true
    1.0
  }
}

class BRDFSpec extends FunSpec {

  describe( "A BRDF" ) {
    it( "should return 0.0 if the in and out points are different" ) {
      val brdf = BRDFTestAdapter()

      val sp1 = SurfacePoint( Point3( 0, 0, 0 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) )
      val sp2 = SurfacePoint( Point3( 1, 0, 0 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) )

      assert( brdf( sp1, Vector3( 0, 1, 0 ), sp2, Vector3( 0, 1, 0 ) ) == 0.0 )
      assert( !brdf.called )
    }

    it( "should return 0.0 if the in and out directions are on different sides of the surface" ) {
      val brdf = BRDFTestAdapter()

      val sp1 = SurfacePoint( Point3( 0, 0, 0 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) )
      val sp2 = SurfacePoint( Point3( 0, 0, 0 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) )

      assert( brdf( sp1, Vector3( 0, 1, 0 ), sp2, Vector3( 0, -1, 0 ) ) == 0.0 )
      assert( !brdf.called )
    }

    it( "should call the reduced apply function if in and out points are the same and in and out direction is one the same side of the surface" ) {
      val brdf = BRDFTestAdapter()

      val sp1 = SurfacePoint( Point3( 0, 0, 0 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) )
      val sp2 = SurfacePoint( Point3( 0, 0, 0 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) )

      assert( brdf( sp1, Vector3( 0, 1, 0 ), sp2, Vector3( 0, 1, 0 ) ) == 1.0 )
      assert( brdf.called )
    }
  }

}
