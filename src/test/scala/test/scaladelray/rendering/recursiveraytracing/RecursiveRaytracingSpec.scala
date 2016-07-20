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

package test.scaladelray.rendering.recursiveraytracing

import org.scalatest.FunSpec

import scaladelray.Color
import scaladelray.camera.{OrthographicCamera, PerspectiveCamera}
import scaladelray.math.Ray
import scaladelray.math.d.{Direction3, Point3}
import scaladelray.math.i.{Point2, Size2}
import scaladelray.rendering.recursiveraytracing.RecursiveRaytracing
import scaladelray.world.{SingleBackgroundColor, World}

class RecursiveRaytracingSpec extends FunSpec {

  describe( "The RecursiveRaytracing algorithm" ) {

    it( "should have a function that calculates correct rays for a each pixel of an image out of a perspective camera" ) {
      val e = Point3( 0, 0, 0 )
      val g = Direction3( 0, 0, -1 )
      val t = Direction3( 0, 1, 0 )
      val imagePlaneFormat = (0.036, 0.024)
      val focalLength = 0.055
      val fNumber = 4.0
      val depthOfField = Double.PositiveInfinity

      val width = 1024
      val height = 768

      val cam = PerspectiveCamera( e, g, t, imagePlaneFormat, focalLength, fNumber, depthOfField )
      val renderingAlgorithm = new RecursiveRaytracing( Color( 0, 0, 0 ), World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() ), 5 )

      for { x <- 0 until width
            y <- 0 until height
      } {
        val lensCenter = cam.e - cam.w * focalLength
        val upperRightCorner = cam.e + cam.u * imagePlaneFormat._1 / 2.0 + cam.v * imagePlaneFormat._2 / 2.0
        val xStepWidth = imagePlaneFormat._1 / width
        val yStepHeight = imagePlaneFormat._2 / height

        val o = upperRightCorner - cam.u * xStepWidth * x - cam.v * yStepHeight * y
        val d = (lensCenter - o).normalized
        assert( renderingAlgorithm.rayFor( cam, Size2( width, height ), Point2( x, y ) ) == Ray( o, d ) )
      }

    }
    it( "should have a function that calculates correct rays for a each pixel of an image out of a orthographic camera" ) {
      val e = Point3( 0, 0, 0 )
      val g = Direction3( 0, 0, -1 )
      val t = Direction3( 0, 1, 0 )
      val imagePlaneFormat = (16.0, 9.0)
      val lensRadius = 0.0
      val depthOfField = Double.PositiveInfinity

      val width = 1024
      val height = 768

      val cam = OrthographicCamera( e, g, t, imagePlaneFormat, lensRadius, depthOfField )
      val renderingAlgorithm = new RecursiveRaytracing( Color( 0, 0, 0 ), World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() ), 5 )
      for { x <- 0 until width
            y <- 0 until height
      } {
        val lowerLeftCorner = cam.e - cam.u * imagePlaneFormat._1 / 2.0 - cam.v * imagePlaneFormat._2 / 2.0
        val xStepWidth = imagePlaneFormat._1 / width
        val yStepHeight = imagePlaneFormat._2 / height

        val o = lowerLeftCorner + cam.u * xStepWidth * x + cam.v * yStepHeight * y
        val d = -cam.w
        assert( renderingAlgorithm.rayFor( cam, Size2( width, height ), Point2( x, y ) ) == Ray( o, d ) )
      }

    }

  }

}
