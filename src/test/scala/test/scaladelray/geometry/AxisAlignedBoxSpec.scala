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
import scaladelray.geometry.AxisAlignedBox
import scaladelray.math.{Normal3, Point3, Ray, Direction3}


class AxisAlignedBoxSpec extends FunSpec {

  private def roughlyEquals( v : Double, r : Double ) = v >= r - 0.00001 && v <= r + 0.00001

  describe( "An AxisAlignedBox" ) {
    it( "should return two hit points for a ray that comes from the front of the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the back of the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, -3 ), Direction3( 0, 0, 1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the left of the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( -3, 0, 0 ), Direction3( 1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }
    it( "should return two hit points for a ray that comes from the right of the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 3, 0, 0 ), Direction3( -1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the top of the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the bottom of the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, -3, 0 ), Direction3( 0, 1, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }



    it( "should return two hit points for a ray that comes from inside the box and directs to the back" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the front" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, 1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the left" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( -1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the right" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the top" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the bottom" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( -1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return no hit point for a ray that does not hit the box" ) {
      val aab = AxisAlignedBox( None )
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.isEmpty )
    }

    it( "should request the color from the texture that is used as normal map" ) {
      val t = new TextureTestAdapter( Color( 0, 0, 1 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      r --> aab
      assert( t.coordinates.isDefined )
    }

    it( "should interpret a blue color of 1 as +z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 1 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, 1 ) ) )
    }

    it( "should interpret a blue color of 0 as -z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 0 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, -1 ) ) )
    }

    it( "should interpret a red color of 1 as +x Axis" ) {
      val t = new TextureTestAdapter( Color( 1, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.exists( (h) => h.sp.n == Normal3( 1, 0, 0 ) ) )
    }

    it( "should interpret a red color of 0 as -x Axis" ) {
      val t = new TextureTestAdapter( Color( 0, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.exists( (h) => h.sp.n == Normal3( -1, 0, 0 ) ) )
    }

    it( "should interpret a green color of 1 as +y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 1, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 1, 0 ) ) )
    }

    it( "should interpret a green color of 0 as -y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val aab = AxisAlignedBox( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, -1, 0 ) ) )
    }

    it( "should calculate the tangent and bitangent correcly" ) {
      val data = (Point3( 0, 0, 2 ), Direction3( 0, 0, -1 ), Direction3( 1, 0, 0 ), Direction3( 0, 1, 0 ) ) :: //front
                 (Point3( 2, 0, 0 ), Direction3( -1, 0, 0 ), Direction3( 0, -1, 0 ), Direction3( 0, 0, -1 ) ) :: //right
                 (Point3( 0, 0, -2 ), Direction3( 0, 0, 1 ), Direction3( 1, 0, 0 ), Direction3( 0, -1, 0 ) ) :: // far
                 (Point3( -2, 0, 0 ), Direction3( 1, 0, 0 ), Direction3( 0, 1, 0 ), Direction3( 0, 0, -1 ) ) :: // left
                 (Point3( 0, 2, 0 ), Direction3( 0, -1, 0 ), Direction3( 1, 0, 0 ), Direction3( 0, 0, -1 ) ) :: // top
                 (Point3( 0, -2, 0 ), Direction3( 0, 1, 0 ), Direction3( 1, 0, 0 ), Direction3( 0, 0, 1 ) ) :: Nil // bottom

      val aab = AxisAlignedBox( None )

      for( (o,d,tan,biTan) <- data ) {
        val hits = Ray( o, d ) --> aab
        assert( hits.exists( (h) => h.sp.tan =~= tan && h.sp.biTan =~= biTan ) )
      }

    }

    it( "should have a center point of 0/0/0" ) {
      val aab = AxisAlignedBox( None )
      assert( aab.center == Point3( 0, 0, 0 ) )
    }

    it( "should have a  point of -0.5/-0.5/-0.5" ) {
      val aab = AxisAlignedBox(None)
      assert(aab.lbf == Point3(-0.5, -0.5, -0.5))
    }

    it( "should have a lbf run point of 0.5/0.5/0.5" ) {
      val aab = AxisAlignedBox( None )
      assert( aab.run == Point3( 0.5, 0.5, 0.5 ) )
    }

    it( "should have a main axis of 0/1/0" ) {
      val aab = AxisAlignedBox( None )
      assert( aab.axis == Direction3( 0, 1, 0 ) )
    }

  }

}
