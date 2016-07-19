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
import scaladelray.geometry.Rectangle
import scaladelray.math.{Normal3, Point3, Ray, Direction3}

class RectangleSpec extends FunSpec {

  describe( "A Rectangle" ) {
    it( "should return one hit for a ray that comes from above the disc and directs downward" ) {
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, -1, 0 ) )
      val d = Rectangle( None )

      val hits = r --> d
      assert( hits.size == 1 )
      assert( hits.head.t == 1.0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the disc but has the origin outside the rectangle" ) {
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, 0, 1 ) )
      val d = Rectangle( None )

      val hits = r --> d
      assert( hits.size == 0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the rectangle and has the origin on the rectangle" ) {
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, 1 ) )
      val d = Rectangle( None )

      val hits = r --> d
      assert( hits.size == 0 )
    }

    it( "should return no hit for a ray that misses the rectangle (is outside of the area" ) {
      val points = Point3( 1, 0, 1 ) :: Point3( 1, 0, -1 ) :: Point3( -1, 0, -1 ) :: Point3( 1, 0, 1 ) :: Nil
      for( p <- points ) {
        val r = Ray( p, Direction3( 0, -1, 0 ) )
        val d = Rectangle( None )

        val hits = r --> d
        assert( hits.size == 0 )
      }
    }

    it( "should request the color from the texture that is used as normal map" ) {
      val t = new TextureTestAdapter( Color( 0, 0, 1 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      r --> d
      assert( t.coordinates.isDefined )
    }

    it( "should interpret a blue color of 1 as +z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 1 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 1, 0 ) ) )
    }

    it( "should interpret a blue color of 0 as -z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 0 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n == Normal3( 0, -1, 0 ) ) )
    }

    it( "should interpret a red color of 1 as +x Axis" ) {
      val t = new TextureTestAdapter( Color( 1, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 1, 0, 0 ) ) )
    }

    it( "should interpret a red color of 0 as -x Axis" ) {
      val t = new TextureTestAdapter( Color( 0, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( -1, 0, 0 ) ) )
    }

    it( "should interpret a green color of 1 as +y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 1, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, -1 ) ) )
    }

    it( "should interpret a green color of 0 as -y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val d = Rectangle( Some( t ) )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> d
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, 1 ) ) )
    }

    it( "should return the same tangent and bitangent for any ray that hits the rectangle" ) {
      val rec = Rectangle( None )
      val res = 20
      val step = Rectangle.e / res
      for{
        ox <- 0 to res
        oy <- 0 to res
        oz <- 0 to res
        dx <- 0 to res
        dz <- 0 to res
      } {
        val o = Point3( -Rectangle.e + ox * step, -Rectangle.e + oy * step, -Rectangle.e + oz * step )
        val d = (Point3( -Rectangle.e + dx * step, 0, -Rectangle.e + dz * step ) - o).normalized
        val r = Ray( o, d )
        val hits = r --> rec
        assert( hits.forall( (h) => h.sp.tan == Direction3( 1, 0, 0 ) && h.sp.biTan == Direction3( 0, 0, -1 ) ) )
      }

    }

    it( "should have a center point of 0/0/0" ) {
      val rec = Rectangle( None )
      assert( rec.center == Point3( 0, 0, 0 ) )
    }

    it( "should have a lbf point of -0.5/0/-0.5" ) {
      val rec = Rectangle( None )
      assert( rec.lbf == Point3(-0.5, 0, -0.5))
    }

    it( "should have a run point of 0.5/0/0.5" ) {
      val rec = Rectangle( None )
      assert( rec.run == Point3( 0.5, 0, 0.5 ) )
    }

    it( "should have a main axis of 0/1/0" ) {
      val rec = Rectangle( None )
      assert( rec.axis == Direction3( 0, 1, 0 ) )
    }
  }

}

