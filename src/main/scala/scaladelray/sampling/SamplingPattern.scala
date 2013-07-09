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

package scaladelray.sampling

import scaladelray.math.Point2
import scala.util.Random


/**
 * A sampling pattern contains several sampling points that describes the sampling pattern.
 *
 * @param samplingPoints The points of the sampling pattern. Each element of each point must be between -0.5 and 0.5.
 */
case class SamplingPattern( samplingPoints : Set[Point2] ) {

  for( p <- samplingPoints ) require( p.x >= -0.5 && p.x <= 0.5 && p.y >= -0.5 && p.y <= 0.5, "The values for x and y of each point must be between -0.5 and 0.5!" )

  /**
   * This method maps the sampling points to a unit disc.
   *
   * @return The sampling points mapped to a unit disc.
   */
  def asDisc = {
    for( p <- samplingPoints ) yield {
      val x = 2.0 * p.x
      val y = 2.0 * p.y

      val (r,a) = if( x > -y ) {
        if( x > y ) (x,y/x) else (y,2.0-x/y)
      } else {
        if( x < y ) (-x,4+y/x) else (-y,if(y!=0) 6-x/y else 0 )
      }

      val phi = a * math.Pi / 4.0
      Point2( r*math.cos(phi), r*math.sin(phi) )
    }
  }
}

/**
 * The companion object of sampling pattern provides several methods to generate different types of sampling patterns.
 */
object SamplingPattern {
  /**
   * This function creates a sampling pattern where all points are arranged in a regular grid. The amount of points
   * in x and y direction are passed as argument.
   *
   * @param x The amount of sampling points in x direction.
   * @param y The amount of sampling points in y direction.
   * @return A SamplingPattern that contains the points.
   */
  def regularPattern( x : Int, y : Int ) : SamplingPattern = {
    val xStep = if( x > 1 ) 1.0 / (x-1.0) else 0.0
    val yStep = if( y > 1 ) 1.0 / (y-1.0) else 0.0

    val xStart = if( x > 1 ) -0.5 else 0.0
    val yStart = if( y > 1 ) -0.5 else 0.0

    var points = Set[Point2]()

    for( i <- 0 until x ) {
      for( j <- 0 until y ) {
        points = points + Point2( xStart + i * xStep, yStart + j * yStep )
      }
    }
    SamplingPattern( points )
  }

  private val random = new Random( System.currentTimeMillis() )

  /**
   * This function creates a sampling pattern with the given amount of random points.
   *
   * @param points The amount of points.
   * @return A sampling pattern with the given amount of random sampling points.
   */
  def randomPattern( points : Int ) : SamplingPattern = {
    val ps = for( i <- 0 until points ) yield Point2( random.nextDouble() - 0.5, random.nextDouble() - 0.5  )
    require( ps.size == points )
    new SamplingPattern( ps.toSet )
  }


}
