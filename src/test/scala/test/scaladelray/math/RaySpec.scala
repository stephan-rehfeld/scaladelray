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

package test.scaladelray.math

import org.scalatest.FunSpec
import scaladelray.math.{Ray, Vector3, Point3}
import scaladelray.geometry.Geometry
import scaladelray.Color
import scaladelray.world.{SingleBackgroundColor, World}

class RaySpec extends FunSpec {

  describe( "A Ray" ) {

    it( "should take the origin and the direction as parameter and provide them as attributes" ) {

      val o = Point3( 0, 0, 0 )
      val d = Vector3( 0, 0, -1 )

      val r = Ray( o, d )

      assert( r.o == o )
      assert( r.d == d )
    }

    it( "should throw an exception if null is passed as origin" ) {
      val d = Vector3( 0, 0, -1 )
      intercept[RuntimeException] {
        Ray( null, d )
      }
    }

    it( "should throw an exception if null is passed as direction" ) {
      val o  = Point3( 0, 0, 0 )
      intercept[RuntimeException] {
        Ray( o, null )
      }
    }

    it( "should have an apply method that takes a double value as parameter and calculates a point" ) {

      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      assert( r( 10 ) == Point3( 2 + 10 * 7, 3 + 10 * 11, 5 + 10 * 13 ) )

    }

    it( "should have an apply method that takes a point value as parameter and calculates the t of the point" )  {

      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      assert( r( Point3( 2 + 10 * 7, 3 + 10 * 11, 5 + 10 * 13 ) ) == 10 )

    }

    it( "should have a \"shoot-the-ray\" operator that take a geometry as parameter and calls the operator on that geometry" ) {

      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      var called = false

      val geo = new Geometry {
        override val normalMap = None
        override def <--(r: Ray) = {
          called = true
          Set()
        }
      }

      r --> geo

      assert( called )

    }

    it( "should have a \"shoot-the-ray\" operator that take a world as parameter and calls the operator on that world" ) {
      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      var called = false

      val world = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set(), 1.0 ) {
        override def <--(r: Ray) = {
          called = true
          Set()
        }
      }

      r --> world

      assert( called )
    }

    it( "should not be altered by the apply method to calculate a point" ) {

      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      r( 10 )

      assert( r.o == o )
      assert( r.d == d )

    }

    it( "should not be altered by the apply method to calculate a t" ) {
      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      r( Point3( 2 + 10 * 7, 3 + 10 * 11, 5 + 10 * 13 ) )

      assert( r.o == o )
      assert( r.d == d )
    }

    it( "should not be altered by \"shoot-the-ray\" operator for a geometry" ) {

      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      var called = false

      val geo = new Geometry {
        override val normalMap = None
        override def <--(r: Ray) = {
          called = true
          Set()
        }
      }

      r --> geo

      assert( r.o == o )
      assert( r.d == d )

    }

    it( "should not be altered by \"shoot-the-ray\" operator for a world" ) {
      val o = Point3( 2, 3, 5 )
      val d = Vector3( 7, 11, 13 )

      val r = Ray( o, d )

      var called = false

      val world = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set(), 1.0 ) {
        override def <--(r: Ray) = {
          called = true
          Set()
        }
      }

      r --> world

      assert( r.o == o )
      assert( r.d == d )
    }


  }

}
