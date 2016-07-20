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

import scaladelray.camera.OrthographicCamera
import scaladelray.math.d.{Direction3, Point3}

class OrthographicCameraSpec extends FunSpec {

  describe("An OrthographicCamera") {

    it("should throw an exception if one or both numbers of the image sensor tuple is not larger than zero") {
      val e = Point3(2, 3, 5)
      val g = Direction3(7, 11, 13)
      val t = Direction3(17, 19, 23)

      val lensRadius = 0.0
      val depthOfField = Double.PositiveInfinity

      intercept[IllegalArgumentException] {
        val imagePlaneFormat = (0.0, 0.1)
        OrthographicCamera(e, g, t, imagePlaneFormat, lensRadius, depthOfField )
      }

      intercept[IllegalArgumentException] {
        val imagePlaneFormat = (0.1, 0.0)
        OrthographicCamera(e, g, t, imagePlaneFormat, lensRadius, depthOfField )
      }

      intercept[IllegalArgumentException] {
        val imagePlaneFormat = (0.0, 0.0)
        OrthographicCamera(e, g, t, imagePlaneFormat, lensRadius, depthOfField )
      }
    }
    it("should throw an exception if the lens radius is not at least zero") {
      intercept[IllegalArgumentException] {
        val e = Point3(2, 3, 5)
        val g = Direction3(7, 11, 13)
        val t = Direction3(17, 19, 23)

        val imagePlaneFormat = (0.1, 0.1)

        val lensRadius = -0.1
        val depthOfField = Double.PositiveInfinity

        OrthographicCamera(e, g, t, imagePlaneFormat, lensRadius, depthOfField )
      }
    }

    it("should throw an exception if the depth of field is not larger than zero") {
      intercept[IllegalArgumentException] {
        val e = Point3(2, 3, 5)
        val g = Direction3(7, 11, 13)
        val t = Direction3(17, 19, 23)

        val imagePlaneFormat = (0.1, 0.1)

        val lensRadius = 0.0
        val depthOfField = 0.0

        OrthographicCamera(e, g, t, imagePlaneFormat, lensRadius, depthOfField )
      }
    }
  }
}