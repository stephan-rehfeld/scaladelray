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
import scaladelray.math.{Vector3, Point3, Ray}
import scaladelray.geometry.Sphere

class SphereSpec extends FunSpec {
  describe( "A Sphere" ) {
    it( "should return two hit points for a ray that comes from the front of the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the back of the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, -3 ), Vector3( 0, 0, 1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the left of the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( -3, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }
    it( "should return two hit points for a ray that comes from the right of the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 3, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the top of the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 3, 0 ), Vector3( 0, -1, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the bottom of the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, -3, 0 ), Vector3( 0, 1, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }



    it( "should return two hit points for a ray that comes from inside the sphere and directs to the back" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the front" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, 1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the left" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the right" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the top" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the bottom" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return one hit point for a ray that intersects the sphere at the border" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 1, 1 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.size == 1 )
      assert( hits.exists( _.t == 1  ) )
    }

    it( "should return no hit point for a ray that does not hit the sphere" ) {
      val s = new Sphere( MaterialTestAdapter() )
      val r = Ray( Point3( 0, 2, 0 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.isEmpty )
    }

  }

}
