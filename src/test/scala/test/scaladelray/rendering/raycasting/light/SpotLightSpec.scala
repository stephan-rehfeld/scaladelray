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
import scaladelray.world.{SingleBackgroundColor, World}
import scaladelray.Color
import scaladelray.rendering.Renderable
import scaladelray.math.{Direction3, Point3}
import scaladelray.rendering.raycasting.light.SpotLight

class SpotLightSpec extends FunSpec {

  describe( "A SpotLight" ) {
    it( "should radiate a point within the angle" ) {
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ), math.toRadians( 22.5 ) )

      val p1 = Point3( 0, 0, -1 )

      assert( l.illuminates( p1 ) )
    }

    it( "should not radiate a point outside the angle" ) {
      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() )
      val l = new SpotLight( Color( 1, 1, 1 ),  Point3( 0, 0, 0 ), Direction3( 0, 0, -1 ), math.toRadians( 22.5 ) )

      val p1 = Point3( 0, 0, 1 )

      assert( !l.illuminates( p1 ) )
    }

    it( "should calculate the constant attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 1, 0, 0 )

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Direction3( 1, 0, 0 ), math.toRadians( 22.5 ) )

      assert( l.intensity( p ) == 1 )
    }


    it( "should calculate the linear attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Direction3( 1, 0, 0 ), math.toRadians( 22.5 ), 0, 0.5  )

      assert( l.intensity( p ) == 1 / (2*0.5) )
    }

    it( "should calculate the quadratic attenuation correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Direction3( 1, 0, 0 ), math.toRadians( 22.5 ), 0, 0, 0.5 )

      assert( l.intensity( p ) == 1/(2*2*0.5) )
    }

    it( "should calculate the direction correctly") {
      val pl = Point3( 0, 0, 0 )
      val p = Point3( 3, -2, -4 )

      val d = (pl - p).normalized

      val l = new SpotLight( Color( 1, 1, 1 ),  pl, Direction3( 0, 0, -1 ), math.toRadians( 22.5 ) )

      assert( l.directionFrom( p ) == d )
    }
  }

}
