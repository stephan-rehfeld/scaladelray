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

package scaladelray.light

import scaladelray.{Constants, World, Color}
import scaladelray.math.{Ray, Vector3, Point3}


class SpotLight( color : Color, position : Point3, direction : Vector3, halfAngle : Double ) extends Light( color ) {
  def illuminates( point: Point3, world : World ) = {
    val w = math.asin( ((point - position).normalized x direction).magnitude ) <= halfAngle
    if( w ) {
      val ray = Ray( point, (position - point).normalized )
      val hits = (ray --> world).filter( _.t > Constants.EPSILON ).toList
      hits.isEmpty || ( (hits.sortWith(  _.t < _.t ).head.t > ray( position ) ) )
    } else {
      w
    }
  }
  def directionFrom( point : Point3 ) = {
    (position - point).normalized
  }
}
