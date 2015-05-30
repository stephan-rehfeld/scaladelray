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
import scaladelray.math.{Vector3, Ray, Point3, Normal3}
import scaladelray.texture.TexCoord2D
import scaladelray.geometry.TriangleMesh

class TriangleMeshSpec extends FunSpec  {

  val n1 = Normal3( 1, 0, 0 )
  val n2 = Normal3( 0, 1, 0 )
  val n3 = Normal3( 0, 0, 1 )

  val t1 = TexCoord2D( 0, 0 )
  val t2 = TexCoord2D( 1, 0 )
  val t3 = TexCoord2D( 1, 1 )

  val a = Point3( 0, 0, 0 )
  val b = Point3( 1, 0, 0 )
  val c = Point3( 1, 1, 0 )

  val triangleMesh = TriangleMesh( Array( a, b, c ), Array( n1, n2, n3 ), Array( t1, t2, t3 ), Array( List( (0, Some(0), Some(0) ), (1, Some(1), Some(1) ), (2, Some(2), Some(2) ) ) ), (_,_) => false, None )

  describe( "A TriangleMesh" ) {
    it( "should return a hit with original normal and texture coordinate when hit on a vertex." ) {
      val r1 = Ray( Point3( 0, 0, 1 ), Vector3( 0, 0, -1 ) )
      val r2 = Ray( Point3( 1, 0, 1 ), Vector3( 0, 0, -1 ) )
      val r3 = Ray( Point3( 1, 1, 1 ), Vector3( 0, 0, -1 ) )

      val h1 = r1 --> triangleMesh
      val h2 = r2 --> triangleMesh
      val h3 = r3 --> triangleMesh

      assert( h1.nonEmpty )
      assert( h2.nonEmpty )
      assert( h3.nonEmpty )


      for( h <- h1 ) {
        assert( h.sp.n == n1 )
        assert( h.sp.t == t1 )
      }

      for( h <- h2 ) {
        assert( h.sp.n == n2 )
        assert( h.sp.t == t2 )
      }

      for( h <- h3 ) {
        assert( h.sp.n == n3 )
        assert( h.sp.t == t3 )
      }
    }

    it( "should return interpolated normals and texture coordinates when hit an edge." ) {
      val r1 = Ray( Point3( 0.5, 0, 1 ), Vector3( 0, 0, -1 ) )
      val r2 = Ray( Point3( 1, 0.5, 1 ), Vector3( 0, 0, -1 ) )
      val r3 = Ray( Point3( 0.5, 0.5, 1 ), Vector3( 0, 0, -1 ) )

      val h1 = r1 --> triangleMesh
      val h2 = r2 --> triangleMesh
      val h3 = r3 --> triangleMesh

      assert( h1.nonEmpty )
      assert( h2.nonEmpty )
      assert( h3.nonEmpty )

      for( h <- h1 ) {
        assert( h.sp.n == (n1 + n2) * 0.5 )
        assert( h.sp.t == (t1 + t2) * 0.5 )
      }

      for( h <- h2 ) {
        assert( h.sp.n == (n2 + n3 ) * 0.5  )
        assert( h.sp.t == (t2 + t3) * 0.5 )
      }

      for( h <- h3 ) {
        assert( h.sp.n == (n3 + n1) * 0.5 )
        assert( h.sp.t == (t3 + t1) * 0.5 )
      }
    }

    it( "should return interpolated normals and texture coordinates when hit on the face." ) {
      val o = (((a.asVector + b.asVector + c.asVector) * (1.0/3.0)) - Vector3( 0, 0, 1 ) ).asPoint
      val r = Ray( o, Vector3( 0, 0, 1 ) )

      val h = r --> triangleMesh

      assert( h.nonEmpty )

      for( hit <- h ) {
        assert( hit.sp.n =~= (n1 + n2 + n3 ) * (1.0/3.0)  )
        assert( hit.sp.t == (t1 + t2 + t3) * (1.0/3.0) )
      }
    }

    it( "should return nothing when missed by the ray." ) {
      val r = Ray( Point3( 0, 1, 1 ), Vector3( 0, 0, -1 ) )
      assert( (r --> triangleMesh).isEmpty )
    }

    it( "should create the octree correctly" ) (pending)
  }

}
