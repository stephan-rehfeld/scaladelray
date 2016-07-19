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

package scaladelray.camera

import scaladelray.math.{Point3, Ray, Direction3}
import scaladelray.sampling.SamplingPattern

/**
 *
 * @param e The position of the camera.
 * @param g The gaze direction of the camera.
 * @param t The up vector of the camera.
 * @param width The width of the image.
 * @param height The height of the image.
 * @param a The half angle of view.
 * @param focalLength The distance to the focus plane.
 * @param lensRadius The radius of the simulated lense
 * @param aaSamplingPattern The sampling pattern that should be used by generating the rays. The default is a regular sampling pattern with only one point.
 * @param lensSamplingPoints The sampling pattern for the lens. The points are used to vary the origin of the rays.
 */
case class DOFOldCamera (e : Point3, g : Direction3, t : Direction3, width : Int, height : Int, a : Double, focalLength : Double, lensRadius : Double, aaSamplingPattern : SamplingPattern = SamplingPattern.regularPattern( 1, 1 ), lensSamplingPoints : SamplingPattern = SamplingPattern.regularPattern( 1, 1 ) ) extends OldCamera( e, g, t ) with Serializable {

  // Some computations that are valid for the whole lifetime of the camera.
  private val mw = w * -1
  private val one = height/2/scala.math.tan( a / 2 )
  private val two = (width-1)/2
  private val three = (height-1)/2

  override def apply( x : Int, y : Int ) = {
    val rays = collection.mutable.Set[Ray]()
    for( p <- aaSamplingPattern.samplingPoints ) yield {
      val px = x-two + p.x
      val py = y-three + p.y

      val fx = px * focalLength/one
      val fy = py * focalLength/one

      val fp = e + mw * focalLength + u * fx + v * fy

      for( lp <- lensSamplingPoints.asDisc ) {
        val lo = e + u * lp.x * lensRadius + v * lp.y * lensRadius
        val d = (fp - lo).normalized
        rays += Ray( lo, d )
      }
    }
    rays.toSet
  }

}
