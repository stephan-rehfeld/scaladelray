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


class SpotLight( color : Color, position : Point3, direction : Vector3, halfAngle : Double, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends LightDescription( color ) with Light {
  override def illuminates( point: Point3, world : World ) = {
    val w = math.asin( ((point - position).normalized x direction).magnitude ) <= halfAngle
    if( w ) {
      val ray = Ray( point, (position - point).normalized )
      val hits = (ray --> world).filter( _.t > Constants.EPSILON ).toList
      hits.isEmpty || (hits.sortWith(  _.t < _.t ).head.t > ray( position ) )
    } else {
      w
    }
  }
  override def directionFrom( point : Point3 ) = {
    (position - point).normalized
  }

  override def intensity(point: Point3): Double = {
    val distance = (point - position).magnitude
    1 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)
  }

  override def createLight: Light = this
}
