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

package scaladelray

case class Color( r : Double, g : Double, b : Double ) {
  require( r >= 0.0, "The parameter 'r' must be at least 0" )
  require( g >= 0.0, "The parameter 'g' must be at least 0" )
  require( b >= 0.0, "The parameter 'b' must be at least 0" )

  val rgbInteger = 0xff << 24 | ((scala.math.min(r,1)*255).asInstanceOf[Int] & 0xff) << 16 | ((scala.math.min(g,1)*255).asInstanceOf[Int] & 0xff) << 8 | ((scala.math.min(b,1)*255).asInstanceOf[Int] & 0xff)
  def *( f : Double ) = Color( r*f, g*f, b*f )
  def *( c : Color ) = Color( r*c.r, g*c.g, b*c.b )
  def +( c : Color ) = Color( r+c.r, g+c.g, b+c.b )
}
