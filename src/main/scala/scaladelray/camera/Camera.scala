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

import scaladelray.math.{Vector3, Ray, Point3}

/**
 * This is the base class for all cameras of the ray tracer. I calculates to camera coordinate system (uvw vectors) out
 * of given e, g, and t vectors.
 *
 * One method that generates the actual ray for a given pixel coordinate must be implemented by a subclass. It's assumed
 * that the subclass knows the resolution of the target image.
 *
 * @param e The position of the camera.
 * @param g The gaze direction of the camera.
 * @param t The up vector of the camera.
 */
abstract class Camera( e : Point3, g : Vector3, t : Vector3 ) {

  require( e != null )
  require( g != null )
  require( t != null )

  /**
   * The w axis of the camera coordinate system.
   */
  val w = g.normalized * -1

  /**
   * The u axis of the camera coordinate system.
   */
  val u = (t x w).normalized

  /**
   * The v axis of the camera coordinate system.
   */
  val v = w x u


  /**
   * This method needs to be implemented by a camera implementation. An implementation generates a ray for the given
   * pixel coordinates. The coordinates start with 0 and ends with width-1 or height-1.
   *
   * @param x The x position of the pixel.
   * @param y The y position of the pixel.
   * @return The ray for the pixel.
   */
  def apply( x : Int, y : Int ) : Ray

  /**
   * This method calls the [[scaladelray.camera.Camera.apply()]] method but accepts a tuple of the pixel coordinates
   * as parameter.
   *
   * @param p The tuple of two integers, that contains the pixel coordinates.
   * @return The ray of the pixel coordinates.
   */
  def apply( p : (Int,Int) ) : Ray = this( p._1, p._2 )

}
