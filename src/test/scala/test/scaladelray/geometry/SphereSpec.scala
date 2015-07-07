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
import scaladelray.geometry.Sphere
import scaladelray.math.{Normal3, Point3, Ray, Vector3}

class SphereSpec extends FunSpec {
  describe( "A Sphere" ) {
    it( "should return two hit points for a ray that comes from the front of the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the back of the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, -3 ), Vector3( 0, 0, 1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the left of the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( -3, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }
    it( "should return two hit points for a ray that comes from the right of the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 3, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the top of the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 3, 0 ), Vector3( 0, -1, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from the bottom of the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, -3, 0 ), Vector3( 0, 1, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 2  ) )
      assert( hits.exists( _.t == 4  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the back" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the front" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, 1 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the left" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the right" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the top" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return two hit points for a ray that comes from inside the sphere and directs to the bottom" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )
      val hits = r --> s
      assert( hits.size == 2 )
      assert( hits.exists( _.t == 1  ) )
      assert( hits.exists( _.t == -1  ) )
    }

    it( "should return one hit point for a ray that intersects the sphere at the border" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 1, 1 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.size == 1 )
      assert( hits.exists( _.t == 1  ) )
    }

    it( "should return no hit point for a ray that does not hit the sphere" ) {
      val s = Sphere( None )
      val r = Ray( Point3( 0, 2, 0 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.isEmpty )
    }

    it( "should request the color from the texture that is used as normal map" ) {
      val t = new TextureTestAdapter( Color( 0, 0, 1 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      r --> s
      assert( t.coordinates.isDefined )
    }

    it( "should interpret a blue color of 1 as +z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 1 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, 1 ) ) )
    }

    it( "should interpret a blue color of 0 as -z Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0.5, 0 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 0, -1 ) ) )
    }

    it( "should interpret a red color of 1 as +x Axis" ) {
      val t = new TextureTestAdapter( Color( 1, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 1, 0, 0 ) ) )
    }

    it( "should interpret a red color of 0 as -x Axis" ) {
      val t = new TextureTestAdapter( Color( 0, 0.5, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.exists( (h) => h.sp.n =~= Normal3( -1, 0, 0 ) ) )
    }

    it( "should interpret a green color of 1 as +y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 1, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, 1, 0 ) ) )
    }

    it( "should interpret a green color of 0 as -y Axis" ) {
      val t = new TextureTestAdapter( Color( 0.5, 0, 0.5 ) )
      assert( t.coordinates.isEmpty )
      val s = Sphere( Some( t ) )
      val r = Ray( Point3( 0, 0, 3 ), Vector3( 0, 0, -1 ) )
      val hits = r --> s
      assert( hits.exists( (h) => h.sp.n =~= Normal3( 0, -1, 0 ) ) )
    }

    it( "should calculate the tangent and bitangent correctly" ) {
      val s = Sphere( None )
      val data = (Point3(0,0,2),Vector3(0,0,-1),Vector3(1,0,0),Vector3(0,1,0) ) ::
                 (Point3(0,0,-2),Vector3(0,0,1),Vector3(-1,0,0),Vector3(0,1,0) ) ::
                 (Point3(2,0,0),Vector3(-1,0,0),Vector3(0,0,-1),Vector3(0,1,0) ) ::
                 (Point3(-2,0,0),Vector3(-1,0,0),Vector3(0,0,1),Vector3(0,1,0) ) ::
                 (Point3(2,0,2),Vector3(-1,0,-1).normalized,Vector3(1,0,-1).normalized,Vector3(0,1,0) ) ::
                 (Point3(2,0,-2),Vector3(-1,0,1).normalized,Vector3(-1,0,-1).normalized,Vector3(0,1,0) ) ::
                 (Point3(-2,0,-2),Vector3(1,0,1).normalized,Vector3(-1,0,1).normalized,Vector3(0,1,0) ) ::
                 (Point3(-2,0,2),Vector3(1,0,-1).normalized,Vector3(1,0,1).normalized,Vector3(0,1,0) ) ::
                 (Point3(0,2,2),Vector3(0,-1,-1).normalized,Vector3(1,0,0).normalized,Vector3(0,1,-1).normalized ) ::
                 (Point3(0,-2,2),Vector3(0,1,-1).normalized,Vector3(1,0,0).normalized,Vector3(0,1,1).normalized ) ::
                 (Point3(2,2,0),Vector3(-1,-1,0).normalized,Vector3(0,0,-1).normalized,Vector3(-1,1,0).normalized ) ::
                 (Point3(2,-2,0),Vector3(-1,1,0).normalized,Vector3(0,0,-1).normalized,Vector3(1,1,0).normalized ) ::
                 (Point3(0,2,-2),Vector3(0,-1,1).normalized,Vector3(-1,0,0).normalized,Vector3(0,1,1).normalized ) ::
                 (Point3(0,-2,-2),Vector3(0,1,1).normalized,Vector3(-1,0,0).normalized,Vector3(0,1,-1).normalized ) ::
                 (Point3(-2,2,0),Vector3(1,-1,0).normalized,Vector3(0,0,1).normalized,Vector3(1,1,0).normalized ) ::
                 (Point3(-2,-2,0),Vector3(1,1,0).normalized,Vector3(0,0,1).normalized,Vector3(-1,1,0).normalized ) :: Nil
      for( (o,d,tan,biTan) <- data ) {
        val r = Ray( o, d )
        val hits = r --> s
        assert( hits.exists( (h) => h.sp.tan =~= tan && h.sp.biTan =~= biTan ) )
        assert( hits.exists( (h) => h.sp.tan =~= -tan && h.sp.biTan =~= biTan ) )
      }
    }

    it( "should have a center point of 0/0/0" ) {
      val s = Sphere( None )
      assert( s.center == Point3( 0, 0, 0 ) )
    }

    it( "should have a lbf point of -1/-1/-1" ) {
      val s = Sphere( None )
      assert( s.lbf == Point3( -1, -1, -1 ))
    }

    it( "should have a run point of 1/1/1" ) {
      val s = Sphere( None )
      assert( s.run == Point3( 1, 1, 1 ) )
    }

    it( "should have a main axis of 0/1/0" ) {
      val s = Sphere( None )
      assert( s.axis == Vector3( 0, 1, 0 ) )
    }

  }

}
