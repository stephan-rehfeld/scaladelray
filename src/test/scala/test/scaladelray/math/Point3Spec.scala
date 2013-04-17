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
import scaladelray.math.{Vector3, Point3}

/**
 * A behavior test of [[scaladelray.math.Normal3]].
 *
 * @author Stephan Rehfeld
 */
class Point3Spec extends FunSpec {
  describe( "A Point3 ") {

    it( "should set the parameters for x, y, and z correctly" ) {
      val x = 3
      val y = 4
      val z = 5

      val point = Point3( x, y, z )

      assert( point.x == x )
      assert( point.y == y )
      assert( point.z == z )
    }

    it( "should be comparable" ) {
      val point1 = Point3( 2, 3, 5 )
      val point2 = Point3( 2, 3, 5 )

      assert( point1 == point2 )
    }

    it( "should calculate the correct vector between two points (- operator)") {
      val point1 = Point3( 2, 3, 5 )
      val point2 = Point3( 7, 11, 13 )

      assert( point1 - point2 == Vector3( 2-7, 3-11, 5-13) )
    }

    it( "should calculate the correct point if a direction is subtracted (- operator)") {
      val point = Point3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      assert( point - vector == Point3( 2-7, 3-11, 5-13) )
    }

    it( "should calculate the correct point if a direction is added (+ operator)") {
      val point = Point3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      assert( point + vector == Point3( 2+7, 3+11, 5+13) )
    }

    it( "should be convertible to a Vector3 with the same values for x, z, and y" ) {
      val point = Point3( 2, 3, 5 )
      val vector = point.asVector

      assert( point.x == vector.x )
      assert( point.y == vector.y )
      assert( point.z == vector.z )
    }

    it( "should be convertible to a Normal3 with the same values for x, z, and y" ) {
      val point = Point3( 2, 3, 5 )
      val normal = point.asNormal

      assert( point.x == normal.x )
      assert( point.y == normal.y )
      assert( point.z == normal.z )
    }

    it( "should not be altered after subtraction with another point") {

      val point1 = Point3( 2, 3, 5 )
      val point2 = Point3( 7, 11, 13 )

      point1 - point2

      assert( point1.x == 2 )
      assert( point1.y == 3 )
      assert( point1.z == 5 )
    }

    it( "should not be altered after subtraction with a Vector3") {

      val point = Point3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      point - vector

      assert( point.x == 2 )
      assert( point.y == 3 )
      assert( point.z == 5 )
    }

    it( "should not be altered after addition with a Vector3") {

      val point = Point3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      point + vector

      assert( point.x == 2 )
      assert( point.y == 3 )
      assert( point.z == 5 )
    }

    it( "should not be altered while converted to a Vector3") {
      val point = Point3( 2, 3, 5 )

      point.asVector

      assert( point.x == 2 )
      assert( point.y == 3 )
      assert( point.z == 5 )
    }

    it( "should not be altered while converted to a Normal3") {
      val point = Point3( 2, 3, 5 )

      point.asNormal

      assert( point.x == 2 )
      assert( point.y == 3 )
      assert( point.z == 5 )
    }

    it( "should not alter the other point while subtracting") {
      val point1 = Point3( 2, 3, 5 )
      val point2 = Point3( 7, 11, 13 )

      point1 - point2

      assert( point2.x == 7 )
      assert( point2.y == 11 )
      assert( point2.z == 13 )
    }

    it( "should not alter the Vector while subtracting") {
      val point = Point3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      point - vector

      assert( vector.x == 7 )
      assert( vector.y == 11 )
      assert( vector.z == 13 )
    }

    it( "should not alter the Vector while adding") {
      val point = Point3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      point + vector

      assert( vector.x == 7 )
      assert( vector.y == 11 )
      assert( vector.z == 13 )
    }


  }
}
