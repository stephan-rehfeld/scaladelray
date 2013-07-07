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

import scaladelray.math.{Ray, Vector3, Point3}
import scaladelray.{Constants, World, Color}
import scaladelray.sampling.SamplingPattern

class AreaLight( color : Color, position : Point3, direction : Vector3, upVector: Vector3, size : Double, samplingPoints : Int, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends LightDescription( color ) {

  private val w = direction.normalized * -1
  private val u = (upVector x w).normalized
  private val v = w x u
  private val _color = color
  private val _samplingPoints = samplingPoints

  def createLight: Light = new Light {
    val color : Color = _color

    val sp = SamplingPattern.randomPattern( _samplingPoints )

    def illuminates(point: Point3, world: World): List[Boolean] = {
      for( p <- sp.samplingPoints.toList ) yield {
        val pos = position + u * p.x * size + v * p.y * size
        val ray = Ray( point, (pos - point).normalized )
        val hits = (ray --> world).filter( _.t > Constants.EPSILON ).toList
        hits.isEmpty || (hits.sortWith(  _.t < _.t ).head.t > ray( pos ) )
      }
    }

    def directionFrom(point: Point3): List[Vector3] = {
      for( p <- sp.samplingPoints.toList ) yield {
        val pos = position + u * p.x * size + v * p.y * size
        (pos - point).normalized
      }
    }

    def intensity(point: Point3): List[Double] = {
      for( p <- sp.samplingPoints.toList ) yield {
        val pos = position + u * p.x * size + v * p.y * size
        val distance = (point - pos).magnitude
        1 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)
      }
    }

    def samplingPoints: Int = _samplingPoints
  }


}
