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
import scaladelray.geometry.Triangle
import scaladelray.math.{Ray, Vector3, Normal3, Point3}
import scaladelray.texture.TexCoord2D

class TriangleSpec extends FunSpec {

  val n1 = Normal3( 1, 0, 0 )
  val n2 = Normal3( 0, 1, 0 )
  val n3 = Normal3( 0, 0, 1 )

  val t1 = TexCoord2D( 0, 0 )
  val t2 = TexCoord2D( 1, 0 )
  val t3 = TexCoord2D( 1, 1 )

  val a = Point3( 0, 0, 0 )
  val b = Point3( 1, 0, 0 )
  val c = Point3( 1, 1, 0 )

  val triangle = new Triangle( a, b, c, n1, n2, n3, t1, t2, t3, None )

  describe( "A Triangle" ) {
    it( "should return a hit with original normal and texture coordinate when hit on a vertex." ) {

      val r1 = Ray( Point3( 0, 0, 1 ), Vector3( 0, 0, -1 ) )
      val r2 = Ray( Point3( 1, 0, 1 ), Vector3( 0, 0, -1 ) )
      val r3 = Ray( Point3( 1, 1, 1 ), Vector3( 0, 0, -1 ) )

      val h1 = r1 --> triangle
      val h2 = r2 --> triangle
      val h3 = r3 --> triangle

      assert( h1.nonEmpty )
      assert( h2.nonEmpty )
      assert( h3.nonEmpty )


      for( h <- h1 ) {
        assert( h.n == n1 )
        assert( h.texCoord2D == t1 )
      }

      for( h <- h2 ) {
        assert( h.n == n2 )
        assert( h.texCoord2D == t2 )
      }

      for( h <- h3 ) {
        assert( h.n == n3 )
        assert( h.texCoord2D == t3 )
      }

    }

    it( "should return interpolated normals and texture coordinates when hit an edge." ) {
      val r1 = Ray( Point3( 0.5, 0, 1 ), Vector3( 0, 0, -1 ) )
      val r2 = Ray( Point3( 1, 0.5, 1 ), Vector3( 0, 0, -1 ) )
      val r3 = Ray( Point3( 0.5, 0.5, 1 ), Vector3( 0, 0, -1 ) )

      val h1 = r1 --> triangle
      val h2 = r2 --> triangle
      val h3 = r3 --> triangle

      assert( h1.nonEmpty )
      assert( h2.nonEmpty )
      assert( h3.nonEmpty )

      for( h <- h1 ) {
        assert( h.n == (n1 + n2) * 0.5 )
        assert( h.texCoord2D == (t1 + t2) * 0.5 )
      }

      for( h <- h2 ) {
        assert( h.n == (n2 + n3 ) * 0.5  )
        assert( h.texCoord2D == (t2 + t3) * 0.5 )
      }

      for( h <- h3 ) {
        assert( h.n == (n3 + n1) * 0.5 )
        assert( h.texCoord2D == (t3 + t1) * 0.5 )
      }
    }

    it( "should return interpolated normals and texture coordinates when hit on the face." ) {
      val o = (((a.asVector + b.asVector + c.asVector) * (1.0/3.0)) - Vector3( 0, 0, 1 ) ).asPoint
      val r = Ray( o, Vector3( 0, 0, 1 ) )

      val h = r --> triangle

      assert( h.nonEmpty )

      for( hit <- h ) {
        assert( hit.n =~= (n1 + n2 + n3 ) * (1.0/3.0)  )
        assert( hit.texCoord2D == (t1 + t2 + t3) * (1.0/3.0) )
      }

    }

    it( "should return nothing when missed by the ray." ) {
      val r = Ray( Point3( 0, 1, 1 ), Vector3( 0, 0, -1 ) )
      assert( (r --> triangle).isEmpty )
    }
  }

}
