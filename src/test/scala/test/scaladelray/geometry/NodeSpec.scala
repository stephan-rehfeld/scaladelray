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

import scaladelray.geometry.{Geometry, GeometryHit, Node, SurfacePoint}
import scaladelray.math._
import scaladelray.math.Ray
import scaladelray.math.d.{Direction3, Normal3, Point3}

class NodeTestGeometry( t : Transform, r : Ray, hits : GeometryHit*  ) extends Geometry {

  var called = false

  override def <--(r: Ray): Set[GeometryHit] = {
    assert( r == Ray(t.i * this.r.o, t.i * this.r.d) )
    called = true
    hits.toSet
  }

  override val normalMap = None

  override val center = Point3( 0, 0, 0 )
  override val lbf = Point3( -1, -1, -1 )
  override val run = Point3( 1, 1, 1 )
  override val axis = Direction3( 0, 1, 0 )

}

class NodeSpec extends FunSpec {

  describe( "A node" ) {
    it( "should apply the transform to the ray, collect the hits from all object and transform back the normal" ) {
      val t = Transform.translate( 2, 3, 5 ).rotateX( 7 )
      val r = Ray( Point3( 5, 3, 2 ), Direction3( 7, 11, 13 ) )

      val g = new NodeTestGeometry( t, r, GeometryHit( r, null, 1, SurfacePoint( r( 1 ), Normal3( 1, 0, 0 ), Direction3( 0, 1, 0 ), Direction3( 0, 0, -1) , null ) ) , GeometryHit( r, null, 1,SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Direction3( 1, 0, 0 ), Direction3( 0, 0, -1) , null ) ), GeometryHit( r, null, 1, SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Direction3( 1, 0, 0 ), Direction3( 0, 1, 0) , null ) ) )
      val n = Node( t, g )

      val hits = n <-- r

      assert( hits.size == 3 )
      assert( g.called )
      assert( hits.exists( _.sp.n == (t.i.transposed * Normal3( 1, 0, 0 )).normalized ) )
      assert( hits.exists( _.sp.n == (t.i.transposed * Normal3( 0, 1, 0 )).normalized ) )
      assert( hits.exists( _.sp.n == (t.i.transposed * Normal3( 0, 0, 1 )).normalized ) )
    }
  }


}
