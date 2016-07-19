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

package test.scaladelray.material.bsdf

import org.scalatest.FunSpec

import scaladelray.geometry.SurfacePoint
import scaladelray.material.bsdf.PerfectReflectiveBRDF
import scaladelray.math.{Normal3, Point3, Direction3}
import scaladelray.texture.TexCoord2D

class PerfectReflectiveBRDFSpec extends FunSpec {

  describe("A PerfectReflectiveBRDF") {
    it("should 1.0 if out is the reflected in direction otherwise 0.0") {
      val perfectReflectiveBRDF = PerfectReflectiveBRDF()
      val surfacePoint = SurfacePoint(Point3(0, 0, 0), Normal3(0, 0, 0), Direction3(1, 0, 0), Direction3(0, 0, -1), TexCoord2D(0, 0))

      val steps = 6

      for {
        xIn <- 0 to steps
        yIn <- 1 to steps
        zIn <- 0 to steps
        xOut <- 0 to steps
        yOut <- 1 to steps
        zOut <- 0 to steps
      } {
        val in = Direction3(xIn.toDouble - steps.toDouble / 2.0, yIn.toDouble, zIn.toDouble - steps.toDouble / 2.0).normalized
        val out = Direction3(xIn.toDouble - steps.toDouble / 2.0, yIn.toDouble, zIn.toDouble - steps.toDouble / 2.0).normalized
        if (in.reflectOn(surfacePoint.n) =~= out)
          assert(perfectReflectiveBRDF(surfacePoint, in, 1.0, surfacePoint, out) == 1.0)
        else
          assert(perfectReflectiveBRDF(surfacePoint, in, 1.0, surfacePoint, out) == 0.0)
      }
    }
  }
}
