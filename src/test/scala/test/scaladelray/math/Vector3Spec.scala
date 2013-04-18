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
import scaladelray.math.{Vector3, Normal3}

class Vector3Spec extends FunSpec {

  describe( "A Vector3" ) {
    it( "should set the parameters for x, y, and z correctly" ) {
      val x = 3
      val y = 4
      val z = 5

      val vector = Vector3( x, y, z )

      assert( vector.x == x )
      assert( vector.y == y )
      assert( vector.z == z )
    }

    it( "should be comparable" ) {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 2, 3, 5 )

      assert( vector1 == vector2 )
    }

    it( "should provide the correct magnitude" ) {
      val vector = Vector3( 2, 3, 5 )

      assert( vector.magnitude == math.sqrt( 2 * 2 + 3 * 3 + 5 * 5 ) )
    }

    it( "should calculate to correct sum of two vectors" ) {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      assert( vector1 + vector2 == Vector3( 2 + 7, 3 + 11, 5 + 13 ) )
    }

    it( "should calculate to correct sum of a vector and a normal" ) {
      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      assert( vector + normal == Vector3( 2 + 7, 3 + 11, 5 + 13 ) )
    }

    it( "should calculate to correct difference of a vector and a normal" ) {
      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      assert( vector - normal == Vector3( 2 - 7, 3 - 11, 5 - 13 ) )
    }

    it( "should calculate to correct product of a vector and a scalar" ) {
      val vector = Vector3( 2, 3, 5 )

      assert( vector * 10 == Vector3( 2 *10, 3 * 10, 5 * 10 ) )
    }

    it( "should calculate to correct fraction of a vector and a scalar" )(pending)

    it( "should calculate to correct cross product of two vectors" ) {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      assert( (vector1 x vector2) == Vector3( 3 * 13 - 5 * 11, 5 * 7 - 2 * 13, 2 * 11 - 3 * 7 )  )
    }

    it( "should compute the dot product with another vector correctly" ) {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      assert( (vector1 dot vector2) == 2 * 7 + 3 * 11 + 5 * 13 )
    }

    it( "should compute the dot product with a normal correctly" ) {
      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      assert( (vector dot normal) == 2 * 7 + 3 * 11 + 5 * 13 )
    }

    it( "should compute the correct normalized vector" ) {
      val vector = Vector3( 2, 3, 5 )

      assert( vector.normalized == Vector3( 2 / vector.magnitude, 3 / vector.magnitude, 5 / vector.magnitude ) )
    }

    it( "should be convertible to a Normal3 with with normalized values for x, z, and y" ) {
      val vector = Vector3( 2, 3, 5 )
      val normal = vector.asNormal

      assert( vector.normalized.x == normal.x )
      assert( vector.normalized.y == normal.y )
      assert( vector.normalized.z == normal.z )
    }

    it( "should be reflectable on a normal" ) {
      val vector1 = Vector3( -1, 1, 0 ).normalized
      val normal1 = Normal3( 0, 1, 0 )

      assert( vector1.reflectOn( normal1 ) == Vector3( 0.7071067811865475, 0.7071067811865475,0.0 ) )

      val vector2 = Vector3( 0, 1, -1 ).normalized
      val normal2 = Normal3( 0, 0, -1 )

      assert( vector2.reflectOn( normal2 ) == Vector3( 0.0, -0.7071067811865475, -0.7071067811865475 ) )
    }

    it( "should have a working unary - operator" ) {
      val vector = Vector3( 2, 3, 5 )

      assert( -vector == Vector3( -2, -3, -5 ) )
    }

    it( "should not be altered after addition with another vector") {

      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      vector1 + vector2

      assert( vector1.x == 2 )
      assert( vector1.y == 3 )
      assert( vector1.z == 5 )
    }

    it( "should not be altered after addition with a normal") {

      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      vector + normal

      assert( vector.x == 2 )
      assert( vector.y == 3 )
      assert( vector.z == 5 )
    }

    it( "should not be altered after subtraction with a normal") {

      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      vector - normal

      assert( vector.x == 2 )
      assert( vector.y == 3 )
      assert( vector.z == 5 )
    }

    it( "should not be altered after multiplication with a scalar") {
      val vector = Vector3( 2, 3, 5 )

      vector * 10

      assert( vector.x == 2 )
      assert( vector.y == 3 )
      assert( vector.z == 5 )
    }

    it( "should not be altered after divided with a scalar")(pending)

    it( "should not be altered after calculating the cross product with another vector") {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      vector1 x vector2

      assert( vector1.x == 2 )
      assert( vector1.y == 3 )
      assert( vector1.z == 5 )
    }

    it( "should not be altered after calculating the dot product with another vector") {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      vector1 dot vector2

      assert( vector1.x == 2 )
      assert( vector1.y == 3 )
      assert( vector1.z == 5 )
    }

    it( "should not be altered after calculating the dot product with a normal") {
      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      vector dot normal

      assert( vector.x == 2 )
      assert( vector.y == 3 )
      assert( vector.z == 5 )
    }

    it( "should not be altered after calculating the normalized vector") {
      val vector = Vector3( 2, 3, 5 )

      vector.normalized

      assert( vector.x == 2 )
      assert( vector.y == 3 )
      assert( vector.z == 5 )
    }

    it( "should not be altered after converted to a normal") {
      val vector = Vector3( 2, 3, 5 )

      vector.asNormal

      assert( vector.x == 2 )
      assert( vector.y == 3 )
      assert( vector.z == 5 )
    }

    it( "should be not be altered after reflected on a normal" ) {
      val vector = Vector3( -1, 1, 0 ).normalized
      val normal = Normal3( 0, 1, 0 )

      vector.reflectOn( normal )

      assert( vector.x == -0.7071067811865475 )
      assert( vector.y == 0.7071067811865475 )
      assert( vector.z == 0 )
    }

    it( "should not the other vector while calculating the sum" ) {

      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      vector1 + vector2

      assert( vector2.x == 7 )
      assert( vector2.y == 11 )
      assert( vector2.z == 13 )
    }

    it( "should not the other normal while calculating the sum" ) {

      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      vector + normal

      assert( normal.x == 7 )
      assert( normal.y == 11 )
      assert( normal.z == 13 )
    }

    it( "should not the other normal while calculating the difference" ) {

      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      vector - normal

      assert( normal.x == 7 )
      assert( normal.y == 11 )
      assert( normal.z == 13 )
    }

    it( "should not alter the other vector while calculating the cross product" ) {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      vector1 x vector2

      assert( vector2.x == 7 )
      assert( vector2.y == 11 )
      assert( vector2.z == 13 )
    }

    it( "should not alter the other vector while calculating the dot product" ) {
      val vector1 = Vector3( 2, 3, 5 )
      val vector2 = Vector3( 7, 11, 13 )

      vector1 dot vector2

      assert( vector2.x == 7 )
      assert( vector2.y == 11 )
      assert( vector2.z == 13 )
    }

    it( "should not alter the other normal while calculating the dot product" ) {
      val vector = Vector3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      vector dot normal

      assert( normal.x == 7 )
      assert( normal.y == 11 )
      assert( normal.z == 13 )
    }


    it( "should be alter the normal where the vector is reflected on" ) {
      val vector = Vector3( -1, 1, 0 ).normalized
      val normal = Normal3( 0, 1, 0 )

      vector.reflectOn( normal )

      assert( normal.x == 0 )
      assert( normal.y == 1 )
      assert( normal.z == 0 )
    }

  }

}
