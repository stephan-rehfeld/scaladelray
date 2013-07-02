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

case class OrthographicCamera( e : Point3, g : Vector3, t : Vector3, width : Int, height : Int, s : Double ) extends Camera( e, g, t ) {

  val a = width.asInstanceOf[Double] / height.asInstanceOf[Double]
  val d = g.normalized
  val w1 = width-1
  val h1 = height-1
  val w12 = w1/2
  val h12 = h1/2
  val as = a * s

  override def apply( x : Int, y : Int ) = {
    val o = e +  u *  as * (x-w12)/w1  +  v * s * (y-h12)/h1
    Ray( o, d )
  }

}
