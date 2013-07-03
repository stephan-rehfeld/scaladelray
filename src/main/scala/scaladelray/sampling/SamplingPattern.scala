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


/**
 * A sampling pattern contains several sampling points that describes the sampling pattern.
 *
 * @param samplingPoints The points of the sampling pattern. Each element of each point must be between -0.5 and 0.5.
 */
case class SamplingPattern( samplingPoints : Set[Point2] ) {

  for( p <- samplingPoints ) require( p.x >= -0.5 && p.x <= 0.5 && p.y >= -0.5 && p.y <= 0.5, "The values for x and y of each point must be between -0.5 and 0.5!" )

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


}
