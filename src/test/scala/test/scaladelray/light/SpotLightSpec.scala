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

package test.scaladelray.light

import org.scalatest.FunSpec
import scaladelray.Color
import scaladelray.geometry.Sphere
import scaladelray.light.SpotLight
import scaladelray.math.{Transform, Ray, Vector3, Point3}
import scaladelray.rendering.{Hit, Renderable}
import scaladelray.world.{SingleBackgroundColor, World}

class SpotLightSpec extends FunSpec {

  describe( "A SpotLight" ) {
    it( "should radiate a point within the angle" ) {
      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() )
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )

      val p1 = Point3( 0, 0, -1 )

      for( b <- l.illuminates( p1, w ) )
        assert( b )
    }

    it( "should not radiate a point outside the angle" ) {
      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() )
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )

      val p1 = Point3( 0, 0, 1 )

      for( b <- l.illuminates( p1, w ) )
        assert( !b )
    }

    it( "should return itself when createLight is called." ) {
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )
      assert( l.createLight == l )
    }

    it( "should check the world if an object is between the point and the point light" )  {
      var called = false

      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() ) {
        override def <--( r : Ray ) : Set[Hit] = {
          called = true
          Set[Hit]()
        }
      }
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )
      l.illuminates( Point3( 0, 0, -1 ), w )
      assert( called )
    }

    it( "should return false if an object is between the point and the light" ) {
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 2 ), Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )
      val s = Sphere( None )
      val p = Point3( 0, 0, -2 )
      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() + Renderable( Transform(), s, null ) )

      for( b <- l.illuminates( p, w ) )
        assert( !b )
    }

    it( "should calculate the constant attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 1, 0, 0 )

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Vector3( 1, 0, 0 ), math.toRadians( 22.5 ) )

      for( i <- l.intensity( p ) ) {
        assert( i == 1 )
      }
    }


    it( "should calculate the linear attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Vector3( 1, 0, 0 ), math.toRadians( 22.5 ), 0, 0.5  )

      for( i <- l.intensity( p ) ) {
        assert( i == 1 / (2*0.5) )
      }
    }

    it( "should calculate the quadratic attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Vector3( 1, 0, 0 ), math.toRadians( 22.5 ), 0, 0, 0.5 )

      for( i <- l.intensity( p ) ) {
        assert( i == 1/(2*2*0.5) )
      }
    }

    it( "should only have one sampling point") {
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 2 ), Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )
      assert( l.samplingPoints == 1 )
    }

    it( "should calculate the direction correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 3, -2, -4 )

      val d = (pl - p).normalized

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Vector3( 0, 0, -1 ), math.toRadians( 22.5 ) )

      for( dd <- l.directionFrom( p ) ) assert( dd == d )
    }
  }

}