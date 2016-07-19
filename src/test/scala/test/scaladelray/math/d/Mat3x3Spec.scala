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

package test.scaladelray.math.d

import org.scalatest.FunSpec

import scaladelray.math.d.{Direction3, Mat3x3, Point3}

class Mat3x3Spec  extends FunSpec {

  describe( "A Mat3x3" ) {
    it( "should take all nine elements as constructor parameter and provide them as attributes" ) {
      val m = Mat3x3(  2,  3,  5,
                       7, 11, 13,
                      17, 19, 23 )

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )

      assert( m.m21 == 7 )
      assert( m.m22 == 11 )
      assert( m.m23 == 13 )

      assert( m.m31 == 17 )
      assert( m.m32 == 19 )
      assert( m.m33 == 23 )
    }

    it( "should be comparable" ) {
      val m1 = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val m2 = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      assert( m1 == m2 )
    }

    it( "should calculate the correct determinant" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      assert( m.determinant == -78 )
    }

    it( "should be multiply-able with vectors" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      val r = m * v

      assert( r == Direction3( 2 * 29 + 3 * 31 + 5 * 37, 7 * 29 + 11 * 31 + 13 * 37, 17 * 29 + 19 * 31 + 23 * 37 ))
    }

    it( "should multiply-able with points" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val p = Point3( 29, 31, 37 )

      val r = m * p

      assert( r == Point3( 2 * 29 + 3 * 31 + 5 * 37, 7 * 29 + 11 * 31 + 13 * 37, 17 * 29 + 19 * 31 + 23 * 37 ))
    }

    it( "should have a function to replace the first column") {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      val r = m.replaceCol1( v )

      assert( r.m11 == 29 )
      assert( r.m21 == 31 )
      assert( r.m31 == 37 )
    }

    it( "should have a function to replace the second column") {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      val r = m.replaceCol2( v )

      assert( r.m12 == 29 )
      assert( r.m22 == 31 )
      assert( r.m32 == 37 )
    }

    it( "should have a function to replace the third column") {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      val r = m.replaceCol3( v )

      assert( r.m13 == 29 )
      assert( r.m23 == 31 )
      assert( r.m33 == 37 )
    }

    it( "should not be altered after multiplying with a vector" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m * v

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m21 == 7 )
      assert( m.m22 == 11 )
      assert( m.m23 == 13 )
      assert( m.m31 == 17 )
      assert( m.m32 == 19 )
      assert( m.m33 == 23 )
    }

    it( "should not be altered after multiplied with a point" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val p = Point3( 29, 31, 37 )

      m * p

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m21 == 7 )
      assert( m.m22 == 11 )
      assert( m.m23 == 13 )
      assert( m.m31 == 17 )
      assert( m.m32 == 19 )
      assert( m.m33 == 23 )
    }

    it( "should not be altered after changing the first column" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m.replaceCol1( v )

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m21 == 7 )
      assert( m.m22 == 11 )
      assert( m.m23 == 13 )
      assert( m.m31 == 17 )
      assert( m.m32 == 19 )
      assert( m.m33 == 23 )
    }

    it( "should not be altered after changing the second column" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m.replaceCol2( v )

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m21 == 7 )
      assert( m.m22 == 11 )
      assert( m.m23 == 13 )
      assert( m.m31 == 17 )
      assert( m.m32 == 19 )
      assert( m.m33 == 23 )
    }

    it( "should not be altered after changing the third column" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m.replaceCol3( v )

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m21 == 7 )
      assert( m.m22 == 11 )
      assert( m.m23 == 13 )
      assert( m.m31 == 17 )
      assert( m.m32 == 19 )
      assert( m.m33 == 23 )
    }

    it( "should not alter the other vector while multiplying" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m * v

      assert( v.x == 29 )
      assert( v.y == 31 )
      assert( v.z == 37 )
    }

    it( "should not alter the other point while multiplying" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val p = Point3( 29, 31, 37 )

      m * p

      assert( p.x == 29 )
      assert( p.y == 31 )
      assert( p.z == 37 )
    }

    it( "should not alter the other vector while changing the first column" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m.replaceCol1( v )

      assert( v.x == 29 )
      assert( v.y == 31 )
      assert( v.z == 37 )
    }

    it( "should not alter the other vector while changing the second column" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m.replaceCol2( v )

      assert( v.x == 29 )
      assert( v.y == 31 )
      assert( v.z == 37 )
    }

    it( "should not alter the other vector while changing the third column" ) {
      val m = Mat3x3(  2,  3,  5,
        7, 11, 13,
        17, 19, 23 )

      val v = Direction3( 29, 31, 37 )

      m.replaceCol3( v )

      assert( v.x == 29 )
      assert( v.y == 31 )
      assert( v.z == 37 )
    }


  }

}
