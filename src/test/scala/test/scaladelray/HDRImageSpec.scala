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

package test.scaladelray

import org.scalatest.FunSpec

import scala.util.Random
import scaladelray.{Color, HDRImage}

class HDRImageSpec extends FunSpec {

  describe( "A HDRImage" ) {
    it( "should have the correct size after initialized" ) {
      val width = 640
      val height = 480

      val i = HDRImage( width , height )
      assert( i.width == width )
      assert( i.height == height )
    }

    it( "should throw an exception if the width is 0 or smaller" ) {
      intercept[RuntimeException] {
        HDRImage( -1 , 480 )
      }
    }
    it( "should throw an exception if the height is 0 or smaller" ) {
      intercept[RuntimeException] {
        HDRImage( 640, -1 )
      }
    }
    it( "should be initialized by a black color" ) {
      val i = HDRImage( 640, 480 )
      for{
        x <- 0 until i.width
        y <- 0 until i.height
      } assert( i( x, y ) == Color( 0, 0, 0 ) )
    }

    it( "should throw an exception when a pixel with an x coordinate smaller than 0 is read" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i( -1, 0 )
      }
    }

    it( "should throw an exception when a pixel with an x coordinate larger than width-1  is read" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i( 640, 0 )
      }
    }

    it( "should throw an exception when a pixel with an y coordinate smaller than 0 is read" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i( 0, -1 )
      }
    }

    it( "should throw an exception when a pixel with an y coordinate larger than height-1  is read" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i( 0, 480 )
      }
    }

    it( "should throw an exception when a pixel width a x coordinate smaller than 0 is set" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i.set( -1, 0, Color( 0, 0, 0 ) )
      }
    }

    it( "should throw an exception when a pixel width a x coordinate larger than width-1  is set" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i.set( 640, 0, Color( 0, 0, 0 ) )
      }
    }

    it( "should throw an exception when a pixel width a y coordinate smaller than 0 is set" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i.set( 0, -1, Color( 0, 0, 0 ) )
      }
    }

    it( "should throw an exception when a pixel width a y coordinate larger than height-1  is set" ) {
      val i = HDRImage( 640, 480 )
      intercept[RuntimeException] {
        i.set( 0, 480, Color( 0, 0, 0 ) )
      }
    }

    it( "should save a color when the pixel is set and return the same color when this pixel is read" ) {
      val i = HDRImage( 640, 480 )
      val r = new Random()
      for{
        x <- 0 until i.width
        y <- 0 until i.height
      } {
        val c = Color( r.nextDouble(), r.nextDouble(), r.nextDouble() )
        i.set( x, y, c )
        assert( i( x, y ) == c )
      }
    }

    it( "should calculate the correct min value of the image" ) {
      val i = HDRImage( 640, 480 )
      val r = new Random()
      for{
        x <- 0 until i.width
        y <- 0 until i.height
      } {
        val c = Color( r.nextDouble(), r.nextDouble(), r.nextDouble() )
        i.set( x, y, c )
        assert( i( x, y ) == c )
      }

      var min = Double.MaxValue
      for{
        x <- 0 until i.width
        y <- 0 until i.height
      } {
        min = scala.math.min( min, i( x, y  ).r )
        min = scala.math.min( min, i( x, y  ).g )
        min = scala.math.min( min, i( x, y  ).b )
      }

      assert( min == i.min )
    }

    it( "should calculate the correct max value of the image" ) {
      val i = HDRImage( 640, 480 )
      val r = new Random()
      for{
        x <- 0 until i.width
        y <- 0 until i.height
      } {
        val c = Color( r.nextDouble(), r.nextDouble(), r.nextDouble() )
        i.set( x, y, c )
        assert( i( x, y ) == c )
      }

      var max = Double.MinValue
      for{
        x <- 0 until i.width
        y <- 0 until i.height
      } {
        max = scala.math.max( max, i( x, y  ).r )
        max = scala.math.max( max, i( x, y  ).g )
        max = scala.math.max( max, i( x, y  ).b )
      }

      assert( max == i.max )
    }

  }

}
