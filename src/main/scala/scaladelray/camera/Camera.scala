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

abstract class Camera( e : Point3, g : Vector3, t : Vector3 ) {

  require( e != null )
  require( g != null )
  require( t != null )

  val w = g.normalized * -1
  val u = (t x w).normalized
  val v = w x u


  def apply( x : Int, y : Int ) : Ray
  def apply( p : (Int,Int) ) : Ray = this( p._1, p._2 )

}
