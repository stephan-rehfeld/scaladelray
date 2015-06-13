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
import scaladelray.geometry.{SurfacePoint, Geometry, GeometryHit}
import scaladelray.math.Vector3
import scaladelray.math.Point3
import scaladelray.math.Ray
import scaladelray.rendering.Renderable
import scaladelray.Color
import scaladelray.texture.{SingleColorTexture, TexCoord2D}
import scaladelray.material.{Material, LambertOldMaterial}

class RenderableSpec extends FunSpec {

  describe( "A Renderable" ) {
    it( "should transform the ray call the \"shoot-the-ray\" function of the geometry with the transformed ray" ) {
      var called = false
      val t = Transform.translate( 1, 3, 4 ).scale( 2, 1.5, 4 ).rotateX( math.Pi ).rotateY( math.Pi / 8 ).rotateZ( -math.Pi )
      val ray = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val g = new Geometry {
        override val normalMap = None
        override def <--(r: Ray): Set[GeometryHit] = {
          called = true
          assert( r == Ray( t.i * ray.o, t.i * ray.d ) )
          Set()
        }
        override val center = Point3( 0, 0, 0 )
        override val lbf = Point3( 0, 0, 0 )
        override val run = Point3( 0, 0, 0 )
        override val axis = Vector3( 0, 0, 0 )
      }
      val renderable = Renderable( t, g, LambertOldMaterial( SingleColorTexture( Color( 0, 0, 0 ) ) ), Material( None ) )
      ray --> renderable
      assert( called )
    }

    it( "should convert the Geometry hit to a hit and adjust the normal") {
      val t = Transform.translate( 1, 3, 4 ).scale( 2, 1.5, 4 ).rotateX( math.Pi ).rotateY( math.Pi / 8 ).rotateZ( -math.Pi )
      val ray = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val g = new Geometry {
        override val normalMap = None
        override def <--(r: Ray): Set[GeometryHit] = {
          Set() + GeometryHit( r, this, 1, SurfacePoint( r( 1 ), Normal3( 0, 1, 0), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), TexCoord2D( 0, 0 ) ) )
        }
        override val center = Point3( 0, 0, 0 )
        override val lbf = Point3( 0, 0, 0 )
        override val run = Point3( 0, 0, 0 )
        override val axis = Vector3( 0, 0, 0 )
      }
      val renderable = Renderable( t, g, LambertOldMaterial( SingleColorTexture( Color( 0, 0, 0 ) ) ), Material( None ) )
      val hits = ray --> renderable
      assert( hits.size == 1 )
      assert( hits.head.t == 1 )
      assert( hits.head.ray == ray )
      assert( hits.head.renderable == renderable )
      assert( hits.head.sp.n == (t.i.transposed * Normal3( 0, 1, 0 )).normalized )
    }
  }

}
