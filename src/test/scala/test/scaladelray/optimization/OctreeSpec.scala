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

package test.scaladelray.optimization

import org.scalatest.FunSpec

import scaladelray.math.d.Point3
import scaladelray.optimization.Octree

class OctreeSpec extends FunSpec {

  describe( "An Octree" ) {
    it( "accept a set that contains 0 octants" ) {
      val run = Point3( 1, 1, 1 )
      val lbf = Point3( -1, -1, -1 )
      val root = new Octree[Int]( run, lbf, Set(), 0 )
    }

    it( "should accept a set that contains 8 octants" )  {
      val run = Point3( 1, 1, 1 )
      val lbf = Point3( -1, -1, -1 )
      val center = lbf + ((run - lbf) / 2.0)

      val octants = Set() + new Octree[Int]( run, center, Set(), 0 ) +
        new Octree[Int]( Point3( center.x, run.y, run.z ), Point3( lbf.x, center.y, center.z ), Set(), 0 ) +
        new Octree[Int]( Point3( run.x, run.y, center.z ), Point3( center.x, center.y, lbf.z ), Set(), 0 ) +
        new Octree[Int]( Point3( center.x, run.y, center.z ), Point3( lbf.x, center.y, lbf.z ), Set(), 0 ) +
        new Octree[Int]( Point3( run.x, center.y, run.z ), Point3( center.x, lbf.y, center.z ), Set(), 0 ) +
        new Octree[Int]( Point3( center.x, center.y, run.z ), Point3( lbf.x, lbf.y, center.z ), Set(), 0 ) +
        new Octree[Int]( Point3( run.x, center.y, center.z ), Point3( center.x, lbf.y, lbf.z ), Set(), 0 ) +
        new Octree[Int]( center, lbf, Set(), 0 )

      val root = new Octree[Int]( run, lbf, octants, 0 )
    }

    it( "should throw an exception if a different amount than 0 or 8 octants are passed" ) {
      val run = Point3( 1, 1, 1 )
      val lbf = Point3( -1, -1, -1 )
      val center = lbf + ((run - lbf) / 2.0)

      val octants = Set() + new Octree[Int]( run, center, Set(), 0 ) +
        new Octree[Int]( Point3( center.x, run.y, run.z ), Point3( lbf.x, center.y, center.z ), Set(), 0 ) +
        new Octree[Int]( Point3( run.x, run.y, center.z ), Point3( center.x, center.y, lbf.z ), Set(), 0 ) +
        new Octree[Int]( Point3( center.x, run.y, center.z ), Point3( lbf.x, center.y, lbf.z ), Set(), 0 ) +
        new Octree[Int]( Point3( run.x, center.y, run.z ), Point3( center.x, lbf.y, center.z ), Set(), 0 ) +
        new Octree[Int]( Point3( center.x, center.y, run.z ), Point3( lbf.x, lbf.y, center.z ), Set(), 0 ) +
        new Octree[Int]( Point3( run.x, center.y, center.z ), Point3( center.x, lbf.y, lbf.z ), Set(), 0 ) /*+
        new Octree[Int]( center, lbf, Set(), 0 )*/

      intercept[IllegalArgumentException] {
        val root = new Octree[Int]( run, lbf, octants, 0 )
      }
    }
    it( "should provide the passed data" ) {
      val run = Point3( 1, 1, 1 )
      val lbf = Point3( -1, -1, -1 )

      val numbers = 4 :: 8 :: 15 :: 16 :: 23 :: 42 :: Nil

      val root = new Octree[List[Int]]( run, lbf, Set(), numbers )

      assert( root.data.head == 4 )
      assert( root.data( 1 ) == 8 )
      assert( root.data( 2 ) == 15 )
      assert( root.data( 3 ) == 16 )
      assert( root.data( 4 ) == 23 )
      assert( root.data( 5 ) == 42 )
    }


  }

}
