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
import test.scaladelray.material.TextureTestAdapter

import scaladelray.Color
import scaladelray.geometry.Disc
import scaladelray.math.{Normal3, Point3, Ray, Direction3}

class DiscSpec extends FunSpec {

  describe( "A Disc" ) {
    it( "should return one hit for a ray that comes from above the disc and directs downward" ) {
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, -1, 0 ) )
      val d = Disc( None )

      val hits = r --> d
      assert( hits.size == 1 )
      assert( hits.head.t == 1.0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the disc but has the origin outside the disc" ) {
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, 0, 1 ) )
      val d = Disc( None )

      val hits = r --> d
      assert( hits.size == 0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the disc and has the origin on the disc" ) {
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, 1 ) )
      val d = Disc( None )

      val hits = r --> d
      assert( hits.size == 0 )
    }

    it( "should return no hit for a ray that misses the disc (is outside of the radius" ) {
      val points = Point3( 1, 0, 1 ) :: Point3( 1, 0, -1 ) :: Point3( -1, 0, -1 ) :: Point3( 1, 0, 1 ) :: Nil
      for( p <- points ) {
        val r = Ray( p, Direction3( 0, -1, 0 ) )
        val d = Disc( None )

        val hits = r --> d
        assert( hits.size == 0 )
      }
    }

    it( "should request the color from the texture that is used as normal map" ) {
      val t = new TextureTestAdapter( Color( 0, 0, 1 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      r --> d
      assert( t.coordinates.isDefined )
    }

    it( "should interpret a blue color of 1 as +z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 1 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 1, 0 ) ) )
    }

    it( "should interpret a blue color of 0 as -z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 0 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n == Normal3( 0, -1, 0 ) ) )
    }

    it( "should interpret a red color of 1 as +x Axis" ) {
      val t = new TextureTestAdapter( Color( 1, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 1, 0, 0 ) ) )
    }

    it( "should interpret a red color of 0 as -x Axis" ) {
      val t = new TextureTestAdapter( Color( 0, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( -1, 0, 0 ) ) )
    }

    it( "should interpret a green color of 1 as +y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 1, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, -1 ) ) )
    }

    it( "should interpret a green color of 0 as -y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Disc( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, 1 ) ) )
    }

    it( "should return the same tangent and bitangent for any ray that hits the disc" ) {
      val disc = Disc( None )
      for{
        ox <- 0 to 20
        oy <- 0 to 20
        oz <- 0 to 20
        rad <- 0 to 20
        theta <- 0 to 20
      } {
        val o = Point3( -1.0 + ox * 0.1, -1.0 + oy * 0.1, -1.0 + oz * 0.1 )
        val d = (Point3( math.sin( theta * math.Pi / 10.0  ) * rad * Disc.r / 20.0, 0,  math.cos( theta * math.Pi / 10.0  ) * rad * Disc.r / 20.0 ) - o).normalized
        val r = Ray( o, d )
        val hits = r --> disc
        assert( hits.forall( (h) => h.sp.tan == Direction3( 1, 0, 0 ) && h.sp.biTan == Direction3( 0, 0, -1 ) ) )
      }

    }

    it( "should have a center point of 0/0/0" ) {
      val disc = Disc( None )
      assert( disc.center == Point3( 0, 0, 0 ) )
    }

    it( "should have a lbf point of -0.5/0/-0.5" ) {
      val disc = Disc( None )
      assert( disc.lbf == Point3(-0.5, 0, -0.5))
    }

    it( "should have a run point of 0.5/0/0.5" ) {
      val disc = Disc( None )
      assert( disc.run == Point3( 0.5, 0, 0.5 ) )
    }

    it( "should have a main axis of 0/1/0" ) {
      val disc = Disc( None )
      assert( disc.axis == Direction3( 0, 1, 0 ) )
    }

  }

}

