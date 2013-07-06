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

import scaladelray.math.{Ray, Vector3, Point3}
import scaladelray.camera.PerspectiveCamera
import org.scalatest.FunSpec
import scaladelray.sampling.SamplingPattern

class PerspectiveCameraSpec extends FunSpec {
  describe( "A PerspectiveCamera") {
    it( "should calculate the correct center for an image with the size of 1024x768 and half angle of view of PI/4") {
      val cam = new PerspectiveCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 1024, 768, math.Pi/4.0 )
      assert( cam( 511, 383 ) == Set( Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) ) ) )
    }

    it( "should calculate the correct amount of rays for the given sampling pattern" ) {
      val cam1 = new PerspectiveCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 400, 400, math.Pi / 4 )
      assert( cam1( 0, 0 ).size == 1 )


      val cam2 = new PerspectiveCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 400, 400,  math.Pi / 4, SamplingPattern.regularPattern( 3, 2 ) )
      assert( cam2( 0, 0 ).size == 3 * 2 )
    }

    it( "should use the points of the sampling pattern to generate correct altered ray of the primary ray of the pixel" ) (pending)
  }
}
