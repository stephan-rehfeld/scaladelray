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

import org.scalatest.FunSpec
import scaladelray.math.{Point2, Ray, Vector3, Point3}
import scaladelray.camera.OrthographicCamera
import scaladelray.sampling.SamplingPattern


class OrthographicCameraSpec extends FunSpec {

  describe( "An OrthographicCamera") {
    it( "should calculate the correct center for an image with the size of 1024x768 and a size of 1") {
      val cam = new OrthographicCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 1024, 768, 1 )
      assert( cam( 511, 383 ) == Set( Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) ) ) )
    }

    it( "should calculate the correct amount of rays for the given sampling pattern" ) {
      val cam1 = new OrthographicCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 400, 400, 400 )
      assert( cam1( 0, 0 ).size == 1 )


      val cam2 = new OrthographicCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 400, 400, 400, SamplingPattern.regularPattern( 3, 2 ) )
      assert( cam2( 0, 0 ).size == 3 * 2 )
    }

    it( "should use the points of the sampling pattern to generate correct altered ray of the primary ray of the pixel" ) {
      val sp = SamplingPattern( Set() + Point2( 0, 0 ) + Point2( -0.5, -0.5 ) + Point2( 0.5, -0.5 ) + Point2( 0.5, 0.5 ) + Point2( -0.5, 0.5 ) )
      val cam = new OrthographicCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 400, 400, 400, sp )
      val rays = cam( 199, 199 )
      assert( rays.contains( Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) ) ) )
      assert( rays.contains( Ray( Point3( 0.5, 0.5, 0 ), Vector3( 0, 0, -1 ) ) ) )
      assert( rays.contains( Ray( Point3( -0.5, 0.5, 0 ), Vector3( 0, 0, -1 ) ) ) )
      assert( rays.contains( Ray( Point3( -0.5, -0.5, 0 ), Vector3( 0, 0, -1 ) ) ) )
      assert( rays.contains( Ray( Point3( 0.5, -0.5, 0 ), Vector3( 0, 0, -1 ) ) ) )
    }
  }

}
