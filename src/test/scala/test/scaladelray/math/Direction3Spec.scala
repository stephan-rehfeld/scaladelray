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
import scaladelray.math.{Direction3, Normal3}
import scaladelray.Constants

class Direction3Spec extends FunSpec {

  describe( "A Direction3" ) {
    it( "should set the parameters for x, y, and z correctly" ) {
      val x = 3
      val y = 4
      val z = 5

      val direction = Direction3( x, y, z )

      assert( direction.x == x )
      assert( direction.y == y )
      assert( direction.z == z )
    }

    it( "should be comparable" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 2, 3, 5 )

      assert( direction1 == direction2 )
    }

    it( "should provide the correct magnitude" ) {
      val direction = Direction3( 2, 3, 5 )

      assert( direction.magnitude == math.sqrt( 2 * 2 + 3 * 3 + 5 * 5 ) )
    }

    it( "should calculate to correct sum of two directions" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      assert( direction1 + direction2 == Direction3( 2 + 7, 3 + 11, 5 + 13 ) )
    }

    it( "should calculate to correct sum of a direction and a normal" ) {
      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      assert( direction + normal == Direction3( 2 + 7, 3 + 11, 5 + 13 ) )
    }

    it( "should calculate to correct difference of two direction" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      assert( direction1 - direction2 == Direction3( 2 - 7, 3 - 11, 5 - 13 ) )
    }

    it( "should calculate to correct difference of a direction and a normal" ) {
      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      assert( direction - normal == Direction3( 2 - 7, 3 - 11, 5 - 13 ) )
    }

    it( "should calculate to correct product of a direction and a scalar" ) {
      val direction = Direction3( 2, 3, 5 )

      assert( direction * 10 == Direction3( 2 *10, 3 * 10, 5 * 10 ) )
    }

    it( "should calculate to correct fraction of a direction and a scalar" ) {
      val direction = Direction3( 2, 3, 5 )

      assert( direction / 10 == Direction3( 2.0 / 10.0, 3.0 / 10.0, 5.0 / 10.0 ) )
    }

    it( "should calculate to correct cross product of two directions" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      assert( (direction1 x direction2) == Direction3( 3 * 13 - 5 * 11, 5 * 7 - 2 * 13, 2 * 11 - 3 * 7 )  )
    }

    it( "should compute the dot product with another direction correctly" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      assert( (direction1 dot direction2) == 2 * 7 + 3 * 11 + 5 * 13 )
    }

    it( "should compute the dot product with a normal correctly" ) {
      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      assert( (direction dot normal) == 2 * 7 + 3 * 11 + 5 * 13 )
    }

    it( "should compute the correct normalized direction" ) {
      val direction = Direction3( 2, 3, 5 )

      assert( direction.normalized == Direction3( 2 / direction.magnitude, 3 / direction.magnitude, 5 / direction.magnitude ) )
    }

    it( "should be convertible to a Normal3 with normalized values for x, z, and y" ) {
      val direction = Direction3( 2, 3, 5 )
      val normal = direction.asNormal

      assert( direction.normalized.x == normal.x )
      assert( direction.normalized.y == normal.y )
      assert( direction.normalized.z == normal.z )
    }

    it( "should be convertible to a Point with the same values for x, z, and y" ) {
      val direction = Direction3( 2, 3, 5 )
      val point = direction.asPoint

      assert( direction.x == point.x )
      assert( direction.y == point.y )
      assert( direction.z == point.z )
    }

    it( "should be reflectable on a normal" ) {
      val direction1 = Direction3( -1, 1, 0 ).normalized
      val normal1 = Normal3( 0, 1, 0 )

      assert( direction1.reflectOn( normal1 ) == Direction3( 0.7071067811865475, 0.7071067811865475,0.0 ) )

      val direction2 = Direction3( 0, 1, -1 ).normalized
      val normal2 = Normal3( 0, 0, -1 )

      assert( direction2.reflectOn( normal2 ) == Direction3( 0.0, -0.7071067811865475, -0.7071067811865475 ) )
    }

    it( "should have a working unary - operator" ) {
      val direction = Direction3( 2, 3, 5 )

      assert( -direction == Direction3( -2, -3, -5 ) )
    }

    it( "should not be altered after addition with another direction") {

      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      direction1 + direction2

      assert( direction1.x == 2 )
      assert( direction1.y == 3 )
      assert( direction1.z == 5 )
    }

    it( "should not be altered after addition with a normal") {

      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      direction + normal

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should not be altered after subtraction with a normal") {

      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      direction - normal

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should not be altered after multiplication with a scalar") {
      val direction = Direction3( 2, 3, 5 )

      direction * 10

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should not be altered after divided with a scalar") {
      val direction = Direction3( 2, 3, 5 )

      direction / 10

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should not be altered after calculating the cross product with another direction") {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      direction1 x direction2

      assert( direction1.x == 2 )
      assert( direction1.y == 3 )
      assert( direction1.z == 5 )
    }

    it( "should not be altered after calculating the dot product with another direction") {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      direction1 dot direction2

      assert( direction1.x == 2 )
      assert( direction1.y == 3 )
      assert( direction1.z == 5 )
    }

    it( "should not be altered after calculating the dot product with a normal") {
      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      direction dot normal

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should not be altered after calculating the normalized direction") {
      val direction = Direction3( 2, 3, 5 )

      direction.normalized

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should not be altered after converted to a normal") {
      val direction = Direction3( 2, 3, 5 )

      direction.asNormal

      assert( direction.x == 2 )
      assert( direction.y == 3 )
      assert( direction.z == 5 )
    }

    it( "should be not be altered after reflected on a normal" ) {
      val direction = Direction3( -1, 1, 0 ).normalized
      val normal = Normal3( 0, 1, 0 )

      direction.reflectOn( normal )

      assert( direction.x == -0.7071067811865475 )
      assert( direction.y == 0.7071067811865475 )
      assert( direction.z == 0 )
    }

    it( "should not the other direction while calculating the sum" ) {

      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      direction1 + direction2

      assert( direction2.x == 7 )
      assert( direction2.y == 11 )
      assert( direction2.z == 13 )
    }

    it( "should not the other normal while calculating the sum" ) {

      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      direction + normal

      assert( normal.x == 7 )
      assert( normal.y == 11 )
      assert( normal.z == 13 )
    }

    it( "should not the other normal while calculating the difference" ) {

      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      direction - normal

      assert( normal.x == 7 )
      assert( normal.y == 11 )
      assert( normal.z == 13 )
    }

    it( "should not alter the other direction while calculating the cross product" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      direction1 x direction2

      assert( direction2.x == 7 )
      assert( direction2.y == 11 )
      assert( direction2.z == 13 )
    }

    it( "should not alter the other direction while calculating the dot product" ) {
      val direction1 = Direction3( 2, 3, 5 )
      val direction2 = Direction3( 7, 11, 13 )

      direction1 dot direction2

      assert( direction2.x == 7 )
      assert( direction2.y == 11 )
      assert( direction2.z == 13 )
    }

    it( "should not alter the other normal while calculating the dot product" ) {
      val direction = Direction3( 2, 3, 5 )
      val normal = Normal3( 7, 11, 13 )

      direction dot normal

      assert( normal.x == 7 )
      assert( normal.y == 11 )
      assert( normal.z == 13 )
    }


    it( "should be alter the normal where the direction is reflected on" ) {
      val direction = Direction3( -1, 1, 0 ).normalized
      val normal = Normal3( 0, 1, 0 )

      direction.reflectOn( normal )

      assert( normal.x == 0 )
      assert( normal.y == 1 )
      assert( normal.z == 0 )
    }

    it( "should have a roughly-equals (=~=) operator that compares two directions math within the tolerance defined by Constants.EPSILON") {
      val v = Direction3( 0, 0, 0 )

      assert( v =~= v )
      assert( v =~= Direction3( v.x + Constants.EPSILON, v.y, v.z ) )
      assert( v =~= Direction3( v.x - Constants.EPSILON, v.y, v.z ) )
      assert( v =~= Direction3( v.x, v.y + Constants.EPSILON, v.z ) )
      assert( v =~= Direction3( v.x, v.y - Constants.EPSILON, v.z ) )
      assert( v =~= Direction3( v.x, v.y, v.z + Constants.EPSILON ) )
      assert( v =~= Direction3( v.x, v.y, v.z - Constants.EPSILON ) )
    }

  }

}
