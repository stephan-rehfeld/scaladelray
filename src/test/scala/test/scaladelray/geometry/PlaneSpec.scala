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
import test.scaladelray.material.TextureTestAdapter

import scaladelray.Color
import scaladelray.geometry.Plane
import scaladelray.math.{Normal3, Point3, Ray, Direction3}

class PlaneSpec extends FunSpec {

  describe( "A Plane" ) {
    it( "should return one hit for a ray that comes from above the plane and directs downward" ) {
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, -1, 0 ) )
      val p = Plane( None )

      val hits = r --> p
      assert( hits.size == 1 )
      assert( hits.head.t == 1.0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the plane but has the origin outside the plane" ) {
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, 0, 1 ) )
      val p = Plane( None )

      val hits = r --> p
      assert( hits.size == 0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the plane and has the origin on the plane" ) {
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, 1 ) )
      val p = Plane( None )

      val hits = r --> p
      assert( hits.size == 0 )
    }

    it( "should request the color from the texture that is used as normal map" ) {
      val t = new TextureTestAdapter( Color( 0, 0, 1 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      r --> p
      assert( t.coordinates.isDefined )
    }

    it( "should interpret a blue color of 1 as +z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 1 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> p
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 1, 0 ) ) )
    }

    it( "should interpret a blue color of 0 as -z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 0 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> p
      assert( hits.exists( (h) => h.sp.n == Normal3( 0, -1, 0 ) ) )
    }

    it( "should interpret a red color of 1 as +x Axis" ) {
      val t = new TextureTestAdapter( Color( 1, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> p
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 1, 0, 0 ) ) )
    }

    it( "should interpret a red color of 0 as -x Axis" ) {
      val t = new TextureTestAdapter( Color( 0, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> p
      assert( hits.exists( (h) => h.sp.n =~= Normal3( -1, 0, 0 ) ) )
    }

    it( "should interpret a green color of 1 as +y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 1, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> p
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, -1 ) ) )
    }

    it( "should interpret a green color of 0 as -y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val p = Plane( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> p
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, 1 ) ) )
    }

    it( "should return the same tangent and bitangent for any ray that hits the plane" ) {
      val p = Plane( None )
      for{
        ox <- 0 to 20
        oy <- 0 to 20
        oz <- 0 to 20
        dx <- 0 to 20
        dy <- 0 to 20
        dz <- 0 to 20
      } {
        val r = Ray( Point3( -1.0 + ox * 0.1, -1.0 + oy * 0.1, -1.0 + oz * 0.1 ), Direction3( -1.0 + dx * 0.1, -1.0 + dy * 0.1, -1.0 + dz * 0.1 ) )
        val hits = r --> p
        assert( hits.forall( (h) => h.sp.tan == Direction3( 1, 0, 0 ) && h.sp.biTan == Direction3( 0, 0, -1 ) ) )
      }

    }

    it( "should have a center point of 0/0/0" ) {
      val p = Plane( None )
      assert( p.center == Point3( 0, 0, 0 ) )
    }

    it( "should have a lbf point of -Inf/0/-Inf" ) {
      val p = Plane( None )
      assert( p.lbf == Point3(Double.NegativeInfinity, 0, Double.NegativeInfinity))
    }

    it( "should have a run point of Inf/0/Inf" ) {
      val p = Plane( None )
      assert( p.run == Point3( Double.PositiveInfinity, 0, Double.PositiveInfinity ) )
    }

    it( "should have a main axis of 0/1/0" ) {
      val p = Plane( None )
      assert( p.axis == Direction3( 0, 1, 0 ) )
    }
  }

}
