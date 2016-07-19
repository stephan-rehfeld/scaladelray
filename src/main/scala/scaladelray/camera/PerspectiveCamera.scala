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

package scaladelray.camera

import scaladelray.math.{Point3, Vector3}

/**
  * An instance of this class presents a perspective camera.
  *
  * @param e The eye position of the camera.
  * @param g The gaze direction of the camera. The passed vector must not have a magnitude of 0.
  * @param t The up-vector of the camera. The passed vector must not have a magnitude of 0. Must not point in the same direction as g.
  * @param imagePlaneFormat The format of the image plane. Must be two positive numbers.
  * @param focalLength The focal length between image plane and lens. Must be a positive number.
  * @param fNumber The f number of the camera. This is the ratio between lens's focal length to the diameter of the entrance pupil. Must be a positive number.
  * @param depthOfField The depth of field of the camera. Must be a positive number.
  */
case class PerspectiveCamera( e : Point3, g : Vector3, t : Vector3, imagePlaneFormat : (Double,Double), focalLength : Double, fNumber : Double, depthOfField : Double ) extends Camera( e, g, t ) {

  require( this.imagePlaneFormat._1 > 0.0 )
  require( this.imagePlaneFormat._2 > 0.0 )
  require( this.focalLength > 0.0 )
  require( this.fNumber > 0.0 )
  require( this.depthOfField > 0.0 )

}
