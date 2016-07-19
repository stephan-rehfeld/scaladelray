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

package test.scaladelray.optimization

import org.scalatest.FunSpec
import scaladelray.math.{Direction3, Point3, Ray}
import scaladelray.optimization.AxisAlignedBoundingBox

class AxisAlignedBoundingBoxSpec extends FunSpec {

  describe( "An AxisAlignedBox" ) {
    it( "should return true for a ray that comes from the front of the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 3 ), Direction3( 0, 0, -1 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from the back of the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, -3 ), Direction3( 0, 0, 1 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from the left of the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( -3, 0, 0 ), Direction3( 1, 0, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }
    it( "should return true for a ray that comes from the right of the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 3, 0, 0 ), Direction3( -1, 0, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from the top of the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 3, 0 ), Direction3( 0, -1, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from the bottom of the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, -3, 0 ), Direction3( 0, 1, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }



    it( "should return true for a ray that comes from inside the box and directs to the back" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from inside the box and directs to the front" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 0, 0, 1 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from inside the box and directs to the left" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( -1, 0, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from inside the box and directs to the right" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 1, 0, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from inside the box and directs to the top" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( 1, 0, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return true for a ray that comes from inside the box and directs to the bottom" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 0, 0 ), Direction3( -1, 0, 0 ) )
      val hit = aabb <-- r
      assert( hit )
    }

    it( "should return false for a ray that does not hit the box" ) {
      val aabb = new AxisAlignedBoundingBox( Point3( 0.5, 0.5, 0.5 ), Point3( -0.5, -0.5, -0.5 )  )
      val r = Ray( Point3( 0, 1, 0 ), Direction3( 0, 0, -1 ) )
      val hit = aabb <-- r
      assert( !hit )
    }

  }

}
