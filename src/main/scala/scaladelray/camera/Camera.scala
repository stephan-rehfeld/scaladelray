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

import scaladelray.math.d.{Direction3, Point3}

/**
  * An instance of this class presents a camera.
  *
  * @param e The eye position of the camera.
  * @param g The gaze direction of the camera. The passed direction must not have a magnitude of 0.
  * @param t The up-vector of the camera. The passed direction must not have a magnitude of 0. Must not point in the same direction as g.
  */
class Camera(e : Point3, g : Direction3, t : Direction3 ) {

  require( this.g.magnitude != 0.0 )
  require( this.t.magnitude != 0.0 )
  require( this.g.normalized != this.t.normalized )

  /**
    * The w axis of the camera coordinate system.
    */
  val w = -g.normalized

  /**
    * The u axis of the camera coordinate system.
    */
  val u = (t x w).normalized

  /**
    * The v axis of the camera coordinate system.
    */
  val v = w x u

}
