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

package test.scaladelray.camera

import scaladelray.camera.Camera
import scaladelray.math.{Point3, Vector3, Ray}
import org.scalatest.FunSpec

class CameraProxy( e : Point3, g : Vector3, t : Vector3 ) extends Camera( e, g, t ) {
  var called = false

  def apply(x: Int, y: Int): Set[Ray] = {
    called = true
    Set() + Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
  }
}

class CameraSpec extends FunSpec {
  describe( "A Camera") {
    it( "should calculate to correct u, v, and w vectors out of e, g, and t" ) {

      val e = Point3( 2, 3, 5 )
      val g = Vector3( 7, 11, 13 )
      val t = Vector3( 17, 19, 23 )

      val w = -g.normalized
      val u = (t x w).normalized
      val v = w x u

      val cam = new CameraProxy( e, g, t )

      assert( cam.u == u )
      assert( cam.v == v )
      assert( cam.w == w )
    }

    it( "should have a function that takes a tuple of two Int's as parameter and calls the function to create a ray" ) {
      val e = Point3( 2, 3, 5 )
      val g = Vector3( 7, 11, 13 )
      val t = Vector3( 17, 19, 23 )

      val cam = new CameraProxy( e, g, t )

      assert( cam( (100,100) ) == Set( Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) ) ) )
      assert( cam.called )
    }
  }
}
