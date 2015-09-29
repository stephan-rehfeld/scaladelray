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

import scaladelray.math.{Point3, Ray, Vector3}
import scaladelray.sampling.SamplingPattern

/**
 * This class represents a perspective camera. All rays of the camera are coming from the same origin but have a
 * different direction. The half angle of view adjusts how much of the scene is visible. It's calculated by the half
 * height of the image. The width is adjusted by the aspect ratio.
 *
 * @author Stephan Rehfeld
 *
 * @param e The position of the camera.
 * @param g The gaze direction of the camera.
 * @param t The up vector of the camera.
 * @param width The width of the image.
 * @param height The height of the image.
 * @param a The half angle of view.
 * @param samplingPattern The sampling pattern that should be used by generating the rays. The default is a regular sampling pattern with only one point.
 */
case class PerspectiveOldCamera( e : Point3, g : Vector3, t : Vector3, width : Int, height : Int, a : Double, samplingPattern : SamplingPattern = SamplingPattern.regularPattern( 1, 1 ) ) extends OldCamera( e, g, t ) with Serializable {

  // Some computations that are valid for the whole lifetime of the camera.
  private val mw = w * -1
  private val one = height/2/scala.math.tan( a / 2 )
  private val two = (width-1)/2
  private val three = (height-1)/2

  override def apply( x : Int, y : Int ) = {
    val rays = for( p <- samplingPattern.samplingPoints ) yield {
      val r = mw * one + u * (x-two)  + v * (y-three) + u * p.x + v * p.y
      Ray( e, r.normalized )
    }
    rays.toSet
  }

}
