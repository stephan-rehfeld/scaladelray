/*
 * Copyright 2016 Stephan Rehfeld
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

package test.scaladelray.rendering.recursiveraytracing.light

import org.scalatest.FunSpec

import scaladelray.Color
import scaladelray.geometry.Sphere
import scaladelray.material.Material
import scaladelray.math.d.{Direction3, Point3}
import scaladelray.math.{Ray, Transform}
import scaladelray.rendering.recursiveraytracing.light.DirectionalLight
import scaladelray.rendering.{Hit, Renderable}
import scaladelray.world.{SingleBackgroundColor, World}

/**
  * Created by Stephan Rehfeld on 09.03.2016.
  */
class DirectionalLightSpec extends FunSpec {

  describe( "A DirectionalLight" ) {
    it( "should radiate all points" ) {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() )
      val l = DirectionalLight( r, Color( 1, 1, 1 ), Direction3( 0, -1, 0 ) )

      val points = Point3( 2, 0, 0 ) :: Point3( 0, 2, 0 ) :: Point3( 0, 0, 2 ) :: Point3( -2, 0, 0 ) :: Point3( 0, -2, 0 ) :: Point3( 0, 0, -2 ) :: Nil

      for( p <- points )
        assert( l.illuminates( p, w ) )
    }

    it( "should check the world if an object is between the point and the point light" ) {
      var called = false

      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() ) {
        override def <--( r : Ray ) : Set[Hit] = {
          called = true
          Set[Hit]()
        }
      }
      val l = DirectionalLight( r, Color( 1, 1, 1 ), Direction3( 0, -1, 0 ) )
      l.illuminates( Point3( 3, 3, 3 ), w )
      assert( called )
    }

    it( "should return false if an object is between the point and the light" ) {
      val r = Renderable( Transform(), Sphere( None ),  Material( None ) )

      val directions = Direction3( 1, 0, 0 ) :: Direction3( 0, 1, 0 ) :: Direction3( 0, 0, 1 ) :: Direction3( -1, 0, 0 ) :: Direction3( 0, -1, 0 ) :: Direction3( 0, 0, -1 ) :: Nil
      val s = Sphere( None )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() + Renderable( Transform(), s, Material( None ) ) )
      for( d <- directions ) {
        val l = DirectionalLight( r, Color( 1, 1, 1 ), d )
        val p = (d * 2).asPoint
        assert( !l.illuminates( p, w ) )
      }
    }

    it( "should return true if an object is between the point and the light, but if this object is the renderable of the light" ) {
      val r = Renderable( Transform(), Sphere( None ),  Material( None ) )

      val directions = Direction3( 1, 0, 0 ) :: Direction3( 0, 1, 0 ) :: Direction3( 0, 0, 1 ) :: Direction3( -1, 0, 0 ) :: Direction3( 0, -1, 0 ) :: Direction3( 0, 0, -1 ) :: Nil
      val s = Sphere( None )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() + r )
      for( d <- directions ) {
        val l = DirectionalLight( r, Color( 1, 1, 1 ), d )
        val p = (d * 2).asPoint
        assert( l.illuminates( p, w ) )
      }
    }

    it( "should always return the same direction") {

      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      val directions = Direction3( 1, 0, 0 ) :: Direction3( 0, 1, 0 ) :: Direction3( 0, 0, 1 ) :: Direction3( -1, 0, 0 ) :: Direction3( 0, -1, 0 ) :: Direction3( 0, 0, -1 ) :: Nil
      val points = for( d <- directions ) yield d.asPoint

      for( d <- directions )
        for( p <- points ) {
          val l = DirectionalLight( r, Color( 1, 1, 1 ), d )
          assert( l.directionFrom( p ) == -d )
        }

    }
  }

}
