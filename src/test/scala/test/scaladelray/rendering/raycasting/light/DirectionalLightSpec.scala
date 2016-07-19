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
import scaladelray.math.d.{Direction3, Point3}
import scaladelray.rendering.raycasting.light.DirectionalLight

class DirectionalLightSpec extends FunSpec {

  describe( "A DirectionalLight" ) {
    it( "should radiate all points" ) {
      val l = DirectionalLight( Color( 1, 1, 1 ), Direction3( 0, -1, 0 ) )

      val points = Point3( 1, 0, 0 ) :: Point3( 0, 1, 0 ) :: Point3( 0, 0, 1 ) :: Point3( -1, 0, 0 ) :: Point3( 0, -1, 0 ) :: Point3( 0, 0, -1 ) :: Nil

      for( p <- points )
        assert( l.illuminates( p ) )
    }

    it( "should always return the same direction") {

      val directions = Direction3( 1, 0, 0 ) :: Direction3( 0, 1, 0 ) :: Direction3( 0, 0, 1 ) :: Direction3( -1, 0, 0 ) :: Direction3( 0, -1, 0 ) :: Direction3( 0, 0, -1 ) :: Nil
      val points = for( d <- directions ) yield d.asPoint

      for( d <- directions )
        for( p <- points ) {
          val l = DirectionalLight( Color( 1, 1, 1 ), d )
          assert(  l.directionFrom( p ) == -d )
        }

    }
  }

}

