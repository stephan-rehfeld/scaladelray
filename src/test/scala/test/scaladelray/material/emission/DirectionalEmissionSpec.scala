/*
 * Copyright 2015 Stephan Rehfeld
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

package test.scaladelray.material.emission

import org.scalatest.FunSpec

import scaladelray.Color
import scaladelray.geometry.SurfacePoint
import scaladelray.material.emission.DirectionalEmission
import scaladelray.math.d.{Direction3, Normal3, Point3}
import scaladelray.texture.TexCoord2D

class DirectionalEmissionSpec extends FunSpec {
  describe( "A DirectionalEmission" ) {
    it( "should radiate only in single direction" ) {
      val e = DirectionalEmission( Color( 1, 1, 1 ) )
      val sp = SurfacePoint( Point3( 0, 0, 0 ), Normal3( 0, 0, 1 ), Direction3( 1, 0, 0 ), Direction3( 0, 1, 0 ), TexCoord2D( 0, 0 )  )

      val steps = 10

      for{
        polar <-  0 to steps
        azimuthal  <- 0 to steps
      } {
        val theta = polar.toDouble * math.Pi / steps.toDouble
        val phi = azimuthal.toDouble * math.Pi * 2.0 / steps.toDouble

        val d = Direction3( math.sin( theta ) * math.cos( phi ), math.sin( theta ) * math.sin( phi ), math.cos( theta ) )

        if( sp.n.asDirection =~= d )
          assert( e(sp, d ) == e.c )
        else
          assert( e(sp, d ) == Color( 0, 0, 0 ) )
      }
    }
  }
}
