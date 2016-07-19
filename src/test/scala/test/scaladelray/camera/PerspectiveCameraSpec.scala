/*
 * Copyright 2016 Stephan Rehfeld
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

import scaladelray.camera.PerspectiveCamera
import scaladelray.math.{Point3, Direction3}

class PerspectiveCameraSpec extends FunSpec {

  describe( "A PerspectiveCamera") {

    it( "should throw an exception if one or both numbers of the image sensor tuple is not larger than zero" ) {
      val e = Point3( 2, 3, 5 )
      val g = Direction3( 7, 11, 13 )
      val t = Direction3( 17, 19, 23 )

      val focalLength = 0.01
      val fNumber = 4
      val depthOfField = 2

      intercept[IllegalArgumentException] {
        val imagePlaneFormat = (0.0,0.1)
        PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      }

      intercept[IllegalArgumentException] {
        val imagePlaneFormat = (0.1,0.0)
        PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      }

      intercept[IllegalArgumentException] {
        val imagePlaneFormat = (0.0,0.0)
        PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      }
    }
    it( "should throw an exception if the focal length not larger than zero" ) {
      intercept[IllegalArgumentException] {
        val e = Point3( 2, 3, 5 )
        val g = Direction3( 7, 11, 13 )
        val t = Direction3( 17, 19, 23 )

        val imagePlaneFormat = (0.1,0.1)
        val focalLength = 0.0
        val fNumber = 4
        val depthOfField = 2

        PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      }
    }
    it( "should throw an exception if the f# is not larger than zero" ) {
      intercept[IllegalArgumentException] {
        val e = Point3( 2, 3, 5 )
        val g = Direction3( 7, 11, 13 )
        val t = Direction3( 17, 19, 23 )

        val imagePlaneFormat = (0.1,0.1)
        val focalLength = 0.01
        val fNumber = 0.0
        val depthOfField = 2

        PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      }
    }
    it( "should throw an exception if the depth of field is not larger than zero" ) {
      intercept[IllegalArgumentException] {
        val e = Point3( 2, 3, 5 )
        val g = Direction3( 7, 11, 13 )
        val t = Direction3( 17, 19, 23 )

        val imagePlaneFormat = (0.1,0.1)
        val focalLength = 0.01
        val fNumber = 4
        val depthOfField = 0.0

        PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      }
    }
  }

}
