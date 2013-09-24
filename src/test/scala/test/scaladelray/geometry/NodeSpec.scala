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

package test.scaladelray.geometry

import org.scalatest.FunSpec
import scaladelray.geometry.{Node, Hit, Geometry}
import scaladelray.math._
import scaladelray.math.Ray
import scaladelray.math.Vector3
import scaladelray.math.Point3

class NodeTestGeometry( t : Transform, r : Ray, hits : Hit*  ) extends Geometry( MaterialTestAdapter() ) {

  var called = false

  override def <--(r: Ray): Set[Hit] = {
    assert( r == (t * this.r) )
    called = true
    hits.toSet
  }

}

class NodeSpec extends FunSpec {

  describe( "A node" ) {
    it( "should apply the transform to the ray, collect the hits from all object and transform back the normal" ) {
      val t = Transform.translate( 2, 3, 5 ).rotateX( 7 )
      val r = Ray( Point3( 5, 3, 2 ), Vector3( 7, 11, 13 ) )

      val g = new NodeTestGeometry( t, r, Hit( r, null, 1, Normal3( 1, 0, 0 ), null ), Hit( r, null, 1, Normal3( 0, 1, 0 ), null ), Hit( r, null, 1, Normal3( 0, 0, 1 ), null ) )
      val n = new Node( t, g )

      val hits = n <-- r

      assert( hits.size == 3 )
      assert( g.called )
      assert( hits.exists( _.n == t * Normal3( 1, 0, 0 ) ) )
      assert( hits.exists( _.n == t * Normal3( 0, 1, 0 ) ) )
      assert( hits.exists( _.n == t * Normal3( 0, 0, 1 ) ) )
    }
  }


}
