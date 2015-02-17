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
import scaladelray.math.{Ray, Point3}
import scaladelray.{World, Color}
import scaladelray.light.PointLight
import scaladelray.geometry.{GeometryHit, Sphere, Geometry}

class PointLightSpec extends FunSpec {

  describe( "A PointLight" ) {
    it( "should radiate in all directions." ) {

      val w = new World( Color( 0, 0, 0 ), Set[Geometry]() )
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val points = Point3( 1, 0, 0 ) :: Point3( 0, 1, 0 ) :: Point3( 0, 0, 1 ) :: Point3( -1, 0, 0 ) :: Point3( 0, -1, 0 ) :: Point3( 0, 0, -1 ) :: Nil

      for( p <- points )
        for( b <- l.illuminates( p, w ) )
          assert( b )

    }

    it( "should return itself when createLight is called." ) {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )
      assert( l == l.createLight )
    }

    it( "should check the world if an object is between the point and the point light" ) {
      var called = false

      val w = new World( Color( 0, 0, 0 ), Set[Geometry]() ) {
        override def <--( r : Ray ) : Set[GeometryHit] = {
          called = true
          Set[GeometryHit]()
        }
      }
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )
      l.illuminates( Point3( 3, 3, 3 ), w )
      assert( called )
    }

    it( "should return false if an object is between the point and the light" ) {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, -2 ) )
      val s = new Sphere( null )
      val p = Point3( 0, 0, 2 )
      val w = new World( Color( 0, 0, 0 ), Set() + s )

      for( b <- l.illuminates( p, w ) )
        assert( !b )
    }

    it( "should calculate the constant attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 1, 0, 0 )

      val l = new PointLight( Color( 1, 1, 1 ), pl )

      for( i <- l.intensity( p ) ) {
        assert( i == 1 )
      }
    }


    it( "should calculate the linear attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = new PointLight( Color( 1, 1, 1 ), pl, true, 0, 0.5 )

      for( i <- l.intensity( p ) ) {
        assert( i == 1 / (2*0.5) )
      }
    }

    it( "should calculate the quadratic attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = new PointLight( Color( 1, 1, 1 ), pl, true, 0, 0, 0.5 )

      for( i <- l.intensity( p ) ) {
        assert( i == 1/(2*2*0.5) )
      }
    }

    it( "should only have one sampling point") {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, -2 ) )
      assert( l.samplingPoints == 1 )
    }

    it( "should calculate the direction correctly") {

      val pl = Point3( 0, 0, 0 )
      val p = Point3( 3, -2, -4 )

      val d = (pl - p).normalized

      val l = new PointLight( Color( 1, 1, 1 ), pl )

      for( dd <- l.directionFrom( p ) ) assert( dd == d )
    }
  }

}
