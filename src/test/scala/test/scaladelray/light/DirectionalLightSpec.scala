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
import scaladelray.{Color, World}
import scaladelray.geometry.{Sphere, Hit, Geometry}
import scaladelray.light.DirectionalLight
import scaladelray.math.{Ray, Vector3, Point3}


class DirectionalLightSpec extends FunSpec {

  describe( "A DirectionalLight" ) {
    it( "should radiate all points" ) {
      val w = new World( Color( 0, 0, 0 ), Set[Geometry]() )
      val l = new DirectionalLight( Color( 1, 1, 1 ), Vector3( 0, -1, 0 ) )

      val points = Point3( 1, 0, 0 ) :: Point3( 0, 1, 0 ) :: Point3( 0, 0, 1 ) :: Point3( -1, 0, 0 ) :: Point3( 0, -1, 0 ) :: Point3( 0, 0, -1 ) :: Nil

      for( p <- points )
        for( b <- l.illuminates( p, w ) )
          assert( b )
    }

    it( "should return itself when createLight is called." ) {
      val l = new DirectionalLight( Color( 1, 1, 1 ), Vector3( 0, -1, 0 ) )
      assert( l == l.createLight )
    }

    it( "should check the world if an object is between the point and the point light" ) {
      var called = false

      val w = new World( Color( 0, 0, 0 ), Set[Geometry]() ) {
        override def <--( r : Ray ) : Set[Hit] = {
          called = true
          Set[Hit]()
        }
      }
      val l = new DirectionalLight( Color( 1, 1, 1 ), Vector3( 0, -1, 0 ) )
      l.illuminates( Point3( 3, 3, 3 ), w )
      assert( called )
    }

    it( "should return false if an object is between the point and the light" ) {
      val directions = Vector3( 1, 0, 0 ) :: Vector3( 0, 1, 0 ) :: Vector3( 0, 0, 1 ) :: Vector3( -1, 0, 0 ) :: Vector3( 0, -1, 0 ) :: Vector3( 0, 0, -1 ) :: Nil
      val s = new Sphere( null )
      val w = new World( Color( 0, 0, 0 ), Set() + s )
      for( d <- directions ) {
        val l = new DirectionalLight( Color( 1, 1, 1 ), d )
        val p = (d * 2).asPoint
        for( b <- l.illuminates( p, w ) )
          assert( !b )
      }
    }


    it( "should only have one sampling point") {
      val l = new DirectionalLight( Color( 1, 1, 1 ), Vector3( 0, -1, 0 ) )
      assert( l.samplingPoints == 1 )
    }
    it( "should always return the same direction") {

      val directions = Vector3( 1, 0, 0 ) :: Vector3( 0, 1, 0 ) :: Vector3( 0, 0, 1 ) :: Vector3( -1, 0, 0 ) :: Vector3( 0, -1, 0 ) :: Vector3( 0, 0, -1 ) :: Nil
      val points = for( d <- directions ) yield d.asPoint

      for( d <- directions )
        for( p <- points ) {
          val l = new DirectionalLight( Color( 1, 1, 1 ), d )
          for( dd <- l.directionFrom( p ) ) assert( dd == -d )
        }

    }
  }

}
