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

import scaladelray.light.AreaLight
import scaladelray.math.{Ray, Transform}
import scaladelray.Color
import scaladelray.geometry.Sphere
import scaladelray.rendering.{Hit, Renderable}
import scaladelray.world.{SingleBackgroundColor, World}
import scaladelray.material.Material
import scaladelray.math.d.{Direction3, Point3}

class AreaLightSpec extends FunSpec {

  describe( "An AreaLight" ) {
    it( "should radiate all points" ) {
      val ld = new AreaLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ), Direction3( 0, 1, 0 ), 5, 1 )
      val l = ld.createLight
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() )
      val points = Point3( 1, 0, 0 ) :: Point3( 0, 1, 0 ) :: Point3( 0, 0, 1 ) :: Point3( -1, 0, 0 ) :: Point3( 0, -1, 0 ) :: Point3( 0, 0, -1 ) :: Nil

      for( p <- points )
        for( b <- l.illuminates( p, w ) )
          assert( b )
    }

    it( "should return a new light for each call of createLight" ) {
      val ld = new AreaLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ), Direction3( 0, 1, 0 ), 5, 1 )
      val lights = for( i <- 0 until 10 ) yield ld.createLight
      for( i <- 0 until 9 )
        for( j <- (i+1) until 10 )
           assert( lights(i) != lights(j) )

    }

    it( "should check the world if an object is between the point and the area light" ) {
      var called = false

      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() ) {
        override def <--( r : Ray ) : Set[Hit] = {
          called = true
          Set[Hit]()
        }
      }
      val ld = new AreaLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ), Direction3( 0, 1, 0 ), 5, 1 )
      val l = ld.createLight
      l.illuminates( Point3( 3, 3, 3 ), w )
      assert( called )
    }

    it( "should return false if an object is between the point and the light" ) {
      val ld = new AreaLight( Color( 1, 1, 1 ),Point3( 0, 0, -2 ), Direction3( 0, 0, -1 ), Direction3( 0, 1, 0 ), 0.01, 255 )
      val l = ld.createLight
      val s = Sphere( None )
      val p = Point3( 0, 0, 2 )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() + Renderable( Transform(), s, null, Material( None )  ) )

      for( b <- l.illuminates( p, w ) )
        assert( !b )
    }

    it( "should calculate the constant attenuation correctly") (pending)
    it( "should calculate the linear attenuation correctly") (pending)
    it( "should calculate the quadratic attenuation correctly") (pending)

    it( "should have the specified number of sampling points") {
      val sps = 255
      val ld = new AreaLight( Color( 1, 1, 1 ),Point3( 0, 0, -2 ), Direction3( 0, 0, -1 ), Direction3( 0, 1, 0 ), 0.01, sps )
      assert( ld.createLight.samplingPoints == sps )
    }

    it( "should always calculate the direction to each sampling point") {
      val ld = new AreaLight( Color( 1, 1, 1 ),Point3( 0, 0, -2 ), Direction3( 0, 0, -1 ), Direction3( 0, 1, 0 ), 10, 10 )
      val l = ld.createLight
      val directions = l.directionFrom( Point3( 0, 0, 0 ) )
      val s = directions.toSet
      assert( s.size == directions.size )
    }
  }

}
