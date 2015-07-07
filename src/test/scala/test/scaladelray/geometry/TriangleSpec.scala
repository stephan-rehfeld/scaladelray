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
import scaladelray.math.{Normal3, Point3, Ray, Vector3}
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

      val h1 = r1 --> triangle
      val h2 = r2 --> triangle
      val h3 = r3 --> triangle

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

      val h = r --> triangle

      assert( h.nonEmpty )

      for( hit <- h ) {
        assert( hit.sp.n =~= (n1 + n2 + n3 ) * (1.0/3.0)  )
        assert( hit.sp.t == (t1 + t2 + t3) * (1.0/3.0) )
      }

    }

    it( "should return nothing when missed by the ray." ) {
      val r = Ray( Point3( 0, 1, 1 ), Vector3( 0, 0, -1 ) )
      assert( (r --> triangle).isEmpty )
    }

    it( "should calculate the center point correctly" ) {

      val steps = 6
      val halfRange = 0.5
      val stepSize = halfRange*4.0/steps

      for{
        ax <- 0 to steps
        ay <- 0 to steps
        az <- 0 to steps
        bx <- 0 to steps
        by <- 0 to steps
        bz <- 0 to steps
        cx <- 0 to steps
        cy <- 0 to steps
        cz <- 0 to steps
      } {
        val a = Point3( ax * stepSize - halfRange, ay * stepSize - halfRange, az * stepSize - halfRange )
        val b = Point3( bx * stepSize - halfRange, by * stepSize - halfRange, bz * stepSize - halfRange )
        val c = Point3( cx * stepSize - halfRange, cy * stepSize - halfRange, cz * stepSize - halfRange )
        val n = ((b-a) x (c-a)).normalized.asNormal
        val t = Triangle( a, b, c, n, n, n, t1, t2, t3, None )
        assert( t.center == ((a.asVector + b.asNormal + c.asVector) / 3.0).asPoint )
      }

    }

    it( "should calculate the lbf point correctly" ) {
      val steps = 6
      val halfRange = 0.5
      val stepSize = halfRange*4.0/steps

      for{
        ax <- 0 to steps
        ay <- 0 to steps
        az <- 0 to steps
        bx <- 0 to steps
        by <- 0 to steps
        bz <- 0 to steps
        cx <- 0 to steps
        cy <- 0 to steps
        cz <- 0 to steps
      } {
        val a = Point3( ax * stepSize - halfRange, ay * stepSize - halfRange, az * stepSize - halfRange )
        val b = Point3( bx * stepSize - halfRange, by * stepSize - halfRange, bz * stepSize - halfRange )
        val c = Point3( cx * stepSize - halfRange, cy * stepSize - halfRange, cz * stepSize - halfRange )
        val n = ((b-a) x (c-a)).normalized.asNormal
        val t = Triangle( a, b, c, n, n, n, t1, t2, t3, None )
        assert( t.lbf == Point3( math.min( a.x, math.min( b.x, c.x ) ), math.min( a.y, math.min( b.y, c.y ) ), math.min( a.z, math.min( b.z, c.z ) ) ) )
      }
    }

    it( "should calculate the run point correctly" ) {
      val steps = 6
      val halfRange = 0.5
      val stepSize = halfRange*4.0/steps

      for{
        ax <- 0 to steps
        ay <- 0 to steps
        az <- 0 to steps
        bx <- 0 to steps
        by <- 0 to steps
        bz <- 0 to steps
        cx <- 0 to steps
        cy <- 0 to steps
        cz <- 0 to steps
      } {
        val a = Point3( ax * stepSize - halfRange, ay * stepSize - halfRange, az * stepSize - halfRange )
        val b = Point3( bx * stepSize - halfRange, by * stepSize - halfRange, bz * stepSize - halfRange )
        val c = Point3( cx * stepSize - halfRange, cy * stepSize - halfRange, cz * stepSize - halfRange )
        val n = ((b-a) x (c-a)).normalized.asNormal
        val t = Triangle( a, b, c, n, n, n, t1, t2, t3, None )
        assert( t.run == Point3( math.max( a.x, math.max( b.x, c.x ) ), math.max( a.y, math.max( b.y, c.y ) ), math.max( a.z, math.max( b.z, c.z ) ) ) )
      }
    }

    it( "should calculate a main axis that is the average of the normals" ) {
      val steps = 6
      val halfRange = 0.5
      val stepSize = halfRange*4.0/steps

      for{
        ax <- 0 to steps
        ay <- 0 to steps
        az <- 0 to steps
        bx <- 0 to steps
        by <- 0 to steps
        bz <- 0 to steps
        cx <- 0 to steps
        cy <- 0 to steps
        cz <- 0 to steps
      } {
        val na = Vector3( ax * stepSize - halfRange, ay * stepSize - halfRange, az * stepSize - halfRange ).normalized.asNormal
        val nb = Vector3( bx * stepSize - halfRange, by * stepSize - halfRange, bz * stepSize - halfRange ).normalized.asNormal
        val nc = Vector3( cx * stepSize - halfRange, cy * stepSize - halfRange, cz * stepSize - halfRange ).normalized.asNormal
        val t = Triangle( a, b, c, na, nb, nc, t1, t2, t3, None )

        assert( t.axis =~= ((na.asVector + nb.asVector + nc.asVector) / 3.0).normalized )
      }
    }
  }

}
