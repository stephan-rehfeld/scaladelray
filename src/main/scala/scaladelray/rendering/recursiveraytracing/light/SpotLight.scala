/*
 * Copyright 2015 Stephan Rehfeld
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

package scaladelray.rendering.recursiveraytracing.light

import scaladelray.math.d.{Direction3, Point3}
import scaladelray.math.Ray
import scaladelray.rendering.Renderable
import scaladelray.world.World
import scaladelray.{Color, Constants}

/**
 * A point lights illuminates the scene similar to a point light, but only within a specified angle.
 *
 * @param renderable The rendereable of this light
 * @param c The color of the light.
 * @param p The position of the light.
 * @param d The main direction of the light.
 * @param halfAngle The half angle of the light.
 * @param constantAttenuation The constant attenuation.
 * @param linearAttenuation The linear attenuation.
 * @param quadraticAttenuation The quadratic attenuation.
 */
case class SpotLight(renderable : Renderable, c : Color, p : Point3, d : Direction3, halfAngle : Double, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends Light {
  require( d.magnitude != 0, "The length of the direction must not be 0!" )
  require( halfAngle > 0, "The halfAngle must be larger than 0." )
  require( halfAngle <= math.Pi / 2.0, "The halfAngle must not be larger than 90 degrees.")

  override def illuminates( point: Point3, world : World ) = {
    val w = math.acos( (point - p).normalized dot d) <= halfAngle
    if( w ) {
      val ray = Ray( point, (p - point).normalized )
      val hits = (ray --> world).filter( (h) => h.t > Constants.EPSILON && h.renderable != renderable).toList
      hits.isEmpty || (hits.sortWith(  _.t < _.t ).head.t > ray( p ) )
    } else {
      false
    }
  }

  override def directionFrom( p: Point3 ): Direction3 = (this.p - p).normalized

  override def intensity(point: Point3): Double = {
    val distance = (point - p).magnitude
    1.0 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)
  }

}
