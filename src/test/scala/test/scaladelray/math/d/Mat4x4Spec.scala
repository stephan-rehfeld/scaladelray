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

import scaladelray.math.d.{Direction3, Mat4x4, Normal3, Point3}

class Mat4x4Spec extends FunSpec {
  describe( "A Mat4x4" ) {
    it( "should take all 16 elements as constructor parameter and provide them as attributes" ) {
      val m = Mat4x4( 2,   3,  5,  7,
                      11, 13, 17, 19,
                      23, 29, 31, 37,
                      41, 43, 47, 53 )

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m14 == 7 )

      assert( m.m21 == 11 )
      assert( m.m22 == 13 )
      assert( m.m23 == 17 )
      assert( m.m24 == 19 )

      assert( m.m31 == 23 )
      assert( m.m32 == 29 )
      assert( m.m33 == 31 )
      assert( m.m34 == 37 )

      assert( m.m41 == 41 )
      assert( m.m42 == 43 )
      assert( m.m43 == 47 )
      assert( m.m44 == 53 )
    }
    it( "should be comparable") {
      val m1 = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val m2 = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      assert( m1 == m2 )
    }

    it( "should have a multiply operator for a vector, assuming that a vector is a 4 element vector with value 0 for w" ) {
      val m = Mat4x4( 2,   3,  5,  7,
                      11, 13, 17, 19,
                      23, 29, 31, 37,
                      41, 43, 47, 53 )

      val v = Direction3( 59, 61, 67 )

      val r = m * v

      assert( r.x == ( 2*59 +  3*61 +  5*67 +  7*0))
      assert( r.y == (11*59 + 13*61 + 17*67 + 19*0))
      assert( r.z == (23*59 + 29*61 + 31*67 + 37*0))
    }

    it( "should have a multiply operator for a normal, assuming that a normal is a 4 element normal with value 0 for w" ) {
      val m = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val n = Normal3( 59, 61, 67 )

      val r = m * n

      assert( r.x == ( 2*59 +  3*61 +  5*67 +  7*0))
      assert( r.y == (11*59 + 13*61 + 17*67 + 19*0))
      assert( r.z == (23*59 + 29*61 + 31*67 + 37*0))
    }

    it( "should have a multiply operator for a point, assuming that a point is a 4 element vector with value 1 for w" ) {
      val m = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val p = Point3( 59, 61, 67 )

      val r = m * p

      assert( r.x == ( 2*59 +  3*61 +  5*67 +  7*1))
      assert( r.y == (11*59 + 13*61 + 17*67 + 19*1))
      assert( r.z == (23*59 + 29*61 + 31*67 + 37*1))
    }

    it( "should have a multiply operator for another Mat4x4" ) {
      val m1 = Mat4x4( 2,  3,  5,  7,
                      11, 13, 17, 19,
                      23, 29, 31, 37,
                      41, 43, 47, 53 )

      val m2 = Mat4x4(  59,  61,  67,  71,
                        73,  79,  83,  89,
                        97, 101, 103, 107,
                       109, 113, 127, 131 )

      val r = m1 * m2

      assert( r.m11 == 2*59 + 3 *73 + 5*97 + 7*109 )
      assert( r.m12 == 2*61 + 3 *79 + 5*101 + 7*113 )
      assert( r.m13 == 2*67 + 3 *83 + 5*103 + 7*127 )
      assert( r.m14 == 2*71 + 3 *89 + 5*107 + 7*131 )

      assert( r.m21 == 11*59 + 13*73 + 17*97  + 19*109 )
      assert( r.m22 == 11*61 + 13*79 + 17*101 + 19*113 )
      assert( r.m23 == 11*67 + 13*83 + 17*103 + 19*127 )
      assert( r.m24 == 11*71 + 13*89 + 17*107 + 19*131 )

      assert( r.m31 == 23*59 + 29*73 + 31*97  + 37*109 )
      assert( r.m32 == 23*61 + 29*79 + 31*101 + 37*113 )
      assert( r.m33 == 23*67 + 29*83 + 31*103 + 37*127 )
      assert( r.m34 == 23*71 + 29*89 + 31*107 + 37*131 )

      assert( r.m41 == 41*59 + 43*73 + 47*97  + 53*109 )
      assert( r.m42 == 41*61 + 43*79 + 47*101 + 53*113 )
      assert( r.m43 == 41*67 + 43*83 + 47*103 + 53*127 )
      assert( r.m44 == 41*71 + 43*89 + 47*107 + 53*131 )

    }

    it( "should create the correct transposed matrix") {
      val m1 = Mat4x4( 2,  3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val m2 = Mat4x4( 2, 11, 23, 41,
                       3, 13, 29, 43,
                       5, 17, 31, 47,
                       7, 19, 37, 53 )

      assert( m1.transposed == m2 )
    }

    it( "should not be altered after multiplied with a vector" ) {
      val m = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val v = Direction3( 59, 61, 67 )

      val r = m * v

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m14 == 7 )

      assert( m.m21 == 11 )
      assert( m.m22 == 13 )
      assert( m.m23 == 17 )
      assert( m.m24 == 19 )

      assert( m.m31 == 23 )
      assert( m.m32 == 29 )
      assert( m.m33 == 31 )
      assert( m.m34 == 37 )

      assert( m.m41 == 41 )
      assert( m.m42 == 43 )
      assert( m.m43 == 47 )
      assert( m.m44 == 53 )
    }


    it( "should not be altered after multiplied with a point" ) {
      val m = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val p = Point3( 59, 61, 67 )

      val r = m * p

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m14 == 7 )

      assert( m.m21 == 11 )
      assert( m.m22 == 13 )
      assert( m.m23 == 17 )
      assert( m.m24 == 19 )

      assert( m.m31 == 23 )
      assert( m.m32 == 29 )
      assert( m.m33 == 31 )
      assert( m.m34 == 37 )

      assert( m.m41 == 41 )
      assert( m.m42 == 43 )
      assert( m.m43 == 47 )
      assert( m.m44 == 53 )
    }

    it( "should not be altered after multiplied with a Mat4x4" ) {
      val m1 = Mat4x4( 2,  3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val m2 = Mat4x4(  59,  61,  67,  71,
        73,  79,  83,  89,
        97, 101, 103, 107,
        109, 113, 127, 131 )

      val r = m1 * m2

      assert( m1.m11 == 2 )
      assert( m1.m12 == 3 )
      assert( m1.m13 == 5 )
      assert( m1.m14 == 7 )

      assert( m1.m21 == 11 )
      assert( m1.m22 == 13 )
      assert( m1.m23 == 17 )
      assert( m1.m24 == 19 )

      assert( m1.m31 == 23 )
      assert( m1.m32 == 29 )
      assert( m1.m33 == 31 )
      assert( m1.m34 == 37 )

      assert( m1.m41 == 41 )
      assert( m1.m42 == 43 )
      assert( m1.m43 == 47 )
      assert( m1.m44 == 53 )

    }

    it( "should not be altered after creating the transposed matrix" ) {
      val m = Mat4x4( 2,  3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      m.transposed

      assert( m.m11 == 2 )
      assert( m.m12 == 3 )
      assert( m.m13 == 5 )
      assert( m.m14 == 7 )

      assert( m.m21 == 11 )
      assert( m.m22 == 13 )
      assert( m.m23 == 17 )
      assert( m.m24 == 19 )

      assert( m.m31 == 23 )
      assert( m.m32 == 29 )
      assert( m.m33 == 31 )
      assert( m.m34 == 37 )

      assert( m.m41 == 41 )
      assert( m.m42 == 43 )
      assert( m.m43 == 47 )
      assert( m.m44 == 53 )
    }

    it( "should not alter the other vector while multiplying" )  {
      val m = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val v = Direction3( 59, 61, 67 )

      val r = m * v

      assert( v.x == 59 )
      assert( v.y == 61 )
      assert( v.z == 67 )
    }

    it( "should not alter the other point while multiplying" )  {
      val m = Mat4x4( 2,   3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val p = Point3( 59, 61, 67 )

      val r = m * p

      assert( p.x == 59 )
      assert( p.y == 61 )
      assert( p.z == 67 )
    }

    it( "should not alter the other matrix while multiplying" )  {
      val m1 = Mat4x4( 2,  3,  5,  7,
        11, 13, 17, 19,
        23, 29, 31, 37,
        41, 43, 47, 53 )

      val m2 = Mat4x4(  59,  61,  67,  71,
        73,  79,  83,  89,
        97, 101, 103, 107,
        109, 113, 127, 131 )

      val r = m1 * m2

      assert( m2.m11 == 59 )
      assert( m2.m12 == 61 )
      assert( m2.m13 == 67 )
      assert( m2.m14 == 71 )

      assert( m2.m21 == 73 )
      assert( m2.m22 == 79 )
      assert( m2.m23 == 83 )
      assert( m2.m24 == 89 )

      assert( m2.m31 == 97 )
      assert( m2.m32 == 101 )
      assert( m2.m33 == 103 )
      assert( m2.m34 == 107 )

      assert( m2.m41 == 109 )
      assert( m2.m42 == 113 )
      assert( m2.m43 == 127 )
      assert( m2.m44 == 131 )

    }

  }
}
