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
import scaladelray.math.{Point3, Ray, Vector3}
import scaladelray.geometry.AxisAlignedBox


class AxisAlignedBoxSpec extends FunSpec {

  private def roughlyEquals( v : Double, r : Double ) = v >= r - 0.00001 && v <= r + 0.00001

  describe( "An AxisAlignedBox" ) {
    it( "should return two hit points for a ray that comes from the front of the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the back of the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, -3 ), Vector3( 0, 0, 1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the left of the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( -3, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }
    it( "should return two hit points for a ray that comes from the right of the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 3, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the top of the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 3, 0 ), Vector3( 0, -1, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }

    it( "should return two hit points for a ray that comes from the bottom of the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, -3, 0 ), Vector3( 0, 1, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2.5  ) )
      assert( hits.exists( _.t == 3.5  ) )
    }



    it( "should return two hit points for a ray that comes from inside the box and directs to the back" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the front" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, 1 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the left" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the right" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the top" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return two hit points for a ray that comes from inside the box and directs to the bottom" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> aab
      assert( hits.size == 2 )
      assert( hits.exists( (h) => roughlyEquals( h.t, 0.5 ) ) )
      assert( hits.exists( (h) => roughlyEquals( h.t, -0.5 ) ) )
    }

    it( "should return no hit point for a ray that does not hit the box" ) {
      val aab = new AxisAlignedBox( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 1, 0 ), Vector3( 0, 0, -1 ) )
      val hits = r --> aab
      assert( hits.isEmpty )
    }

  }

}
