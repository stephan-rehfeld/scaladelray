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
import scaladelray.math._
import scaladelray.geometry.SurfacePoint
import test.scaladelray.geometry.GeometryTestAdapter
import scaladelray.world.World
import scaladelray.math.Vector3
import scaladelray.math.Point3
import scaladelray.material.TransparentOldMaterial
import scaladelray.math.Ray
import scaladelray.world.SingleBackgroundColor
import scaladelray.rendering.Renderable
import scaladelray.Color
import scaladelray.texture.TexCoord2D
import scaladelray.rendering.Hit

class TransparentOldMaterialSpec extends FunSpec {

  describe( "A TransparentOldMaterial" ) {

    it( "should call the tracer twice, once with the reflected and with the refracted ray" ) {
      val m = TransparentOldMaterial( 1.0 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )

      val h = Hit( r, Renderable( Transform(), g, m ), 1, SurfacePoint( r( 1 ), Vector3( 0, 1, 1 ).normalized.asNormal, Vector3( 1, -1, 0 ).normalized, Vector3( 0, 0, -1 ), tc ) )

      var called = 0

      val tracer = ( r: Ray, w : World) => {
        assert( r.o == Point3( 0, 0, -1 ) )
        assert( r.d =~= Vector3( 0, 1, 0 ) || r.d =~= Vector3( 0, 0, -1 ) )
        called += 1
        Color( 0, 0, 0 )
      }

      m.colorFor( h, w, tracer )

      assert( called == 2 )
    }

  }

}
