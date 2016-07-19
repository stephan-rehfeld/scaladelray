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

package test.scaladelray.rendering.raycasting.light

import org.scalatest.FunSpec

import scaladelray.Color
import scaladelray.math.d.Point3
import scaladelray.rendering.raycasting.light.PointLight

class PointLightSpec extends FunSpec {

  describe( "A PointLight" ) {
    it( "should radiate in all directions." ) {

      val l = PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val points = Point3( 1, 0, 0 ) :: Point3( 0, 1, 0 ) :: Point3( 0, 0, 1 ) :: Point3( -1, 0, 0 ) :: Point3( 0, -1, 0 ) :: Point3( 0, 0, -1 ) :: Nil

      for( p <- points )
        assert( l.illuminates( p ) )

    }

    it( "should calculate the constant attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 1, 0, 0 )

      val l = PointLight( Color( 1, 1, 1 ), pl )

      assert( l.intensity( p ) == 1 )
    }


    it( "should calculate the linear attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = PointLight( Color( 1, 1, 1 ), pl, 0, 0.5 )

      assert( l.intensity( p ) == 1 / (2*0.5) )
    }

    it( "should calculate the quadratic attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = PointLight( Color( 1, 1, 1 ), pl, 0, 0, 0.5 )

      assert( l.intensity( p ) == 1/(2*2*0.5) )

    }

    it( "should calculate the direction correctly") {

      val pl = Point3( 0, 0, 0 )
      val p = Point3( 3, -2, -4 )

      val d = (pl - p).normalized

      val l = PointLight( Color( 1, 1, 1 ), pl )

      assert( l.directionFrom( p ) == d )
    }
  }
}

