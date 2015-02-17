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
import scaladelray.Constants

/**
 * A behavior test of [[scaladelray.math.Normal3]].
 *
 * @author Stephan Rehfeld
 */
class Normal3Spec extends FunSpec {

  describe( "A Normal3") {

    it( "should set the parameters for x, y, and z correctly" ) {
      val x = 3
      val y = 4
      val z = 5

      val normal = Normal3( x, y, z )

      assert( normal.x == x )
      assert( normal.y == y )
      assert( normal.z == z )
    }

    it( "should be comparable" ) {
      val normal1 = Normal3( 2, 3, 5 )
      val normal2 = Normal3( 2, 3, 5 )

      assert( normal1 == normal2 )
    }

    it( "should provide the correct magnitude" ) {
      val normal = Normal3( 2, 3, 5 )

      assert( normal.magnitude == math.sqrt( 2 * 2 + 3 * 3 + 5 * 5 ) )
    }

    it( "should compute the product with a scalar correctly" ) {
      val normal = Normal3( 2, 3, 5 )
      val v = 10

      assert( normal * v == Normal3( 2 * v, 3 * v, 5 * v ) )
    }

    it( "should compute the sum with another normal correctly" ) {
      val normal1 = Normal3( 2, 3, 5 )
      val normal2 = Normal3( 7, 11, 13 )

      assert( normal1 + normal2 == Normal3( 2+7, 3+11, 5+13 ) )
    }

    it( "should compute the dot product with Vector3 correctly" ) {
      val normal = Normal3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      assert( (normal dot vector) == 2 * 7 + 3 * 11 + 5 * 13 )
    }

    it( "should be convertible to a Vector3 with the same values for x, z, and y" ) {
      val normal = Normal3( 2, 3, 5 )
      val vector = normal.asVector

      assert( normal.x == vector.x )
      assert( normal.y == vector.y )
      assert( normal.z == vector.z )
    }

    it( "should not be altered after the multiplication with a scalar") {
      val normal = Normal3( 2, 3, 5 )
      val v = 10

      normal * v

      assert( normal.x == 2 )
      assert( normal.y == 3 )
      assert( normal.z == 5 )
    }

    it( "should not be altered after the addition with another Normal3") {
      val normal1 = Normal3( 2, 3, 5 )
      val normal2 = Normal3( 7, 11, 13 )

      normal1 + normal2

      assert( normal1.x == 2 )
      assert( normal1.y == 3 )
      assert( normal1.z == 5 )
    }

    it( "should not be altered after being added to another Normal3") {
      val normal1 = Normal3( 2, 3, 5 )
      val normal2 = Normal3( 7, 11, 13 )

      normal1 + normal2

      assert( normal2.x == 7 )
      assert( normal2.y == 11 )
      assert( normal2.z == 13 )
    }

    it( "should not be altered after calculating the dot product with a Vector3") {

      val normal = Normal3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      normal dot vector

      assert( normal.x == 2 )
      assert( normal.y == 3 )
      assert( normal.z == 5 )
    }

    it( "should not alter the Vector3 while calculating the dot product") {
      val normal = Normal3( 2, 3, 5 )
      val vector = Vector3( 7, 11, 13 )

      normal dot vector

      assert( vector.x == 7 )
      assert( vector.y == 11 )
      assert( vector.z == 13 )
    }

    it( "should not be altered while converted to a Vector3") {
      val normal = Normal3( 2, 3, 5 )

      normal.asVector

      assert( normal.x == 2 )
      assert( normal.y == 3 )
      assert( normal.z == 5 )
    }

    it( "should respect the operator orders (* before +)") {
      val normal1 = Normal3( 2, 3, 5 )
      val normal2 = Normal3( 7, 11, 13 )
      val v = 10

      assert( normal1 + normal2 * v == normal2 * v + normal1 )
    }

    it( "should have a working unary - operator" ) {
      val normal = Normal3( 2, 3, 5 )

      assert( -normal == Normal3( -2, -3, -5 ) )
    }

    it( "should have a roughly-equals (=~=) operator that compares two normals math within the tolerance defined by Constants.EPSILON") {
      val n = Normal3( 0, 0, 0 )

      assert( n =~= n )
      assert( n =~= Normal3( n.x + Constants.EPSILON, n.y, n.z ) )
      assert( n =~= Normal3( n.x - Constants.EPSILON, n.y, n.z ) )
      assert( n =~= Normal3( n.x, n.y + Constants.EPSILON, n.z ) )
      assert( n =~= Normal3( n.x, n.y - Constants.EPSILON, n.z ) )
      assert( n =~= Normal3( n.x, n.y, n.z + Constants.EPSILON ) )
      assert( n =~= Normal3( n.x, n.y, n.z - Constants.EPSILON ) )
    }

    it( "should compute the correct normalized normal" ) {
      val normal = Normal3( 2, 3, 5 )

      assert( normal.normalized == Normal3( 2 / normal.magnitude, 3 / normal.magnitude, 5 / normal.magnitude ) )
    }

  }

}
