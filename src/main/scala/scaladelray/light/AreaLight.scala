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
import scaladelray.{Constants, Color}
import scaladelray.sampling.SamplingPattern
import scaladelray.world.World

/**
 * A rectangular area light with random sampling points. As a complex light, this only is a light description. A new
 * light instance is created on every call of createLight. All newly created lights a different random sampling pattern.
 *
 * @param color The color of the light.
 * @param position The position of the light
 * @param direction The direction of the light
 * @param upVector The op vector of the light.
 * @param size The size of the area light.
 * @param samplingPoints The number of sampling points on the light.
 * @param constantAttenuation The constant attenuation.
 * @param linearAttenuation The linear attenuation.
 * @param quadraticAttenuation The quadratic attenuation.
 */
class AreaLight( color : Color, position : Point3, direction : Vector3, upVector: Vector3, size : Double, samplingPoints : Int, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends LightDescription( color ) with Serializable {

  /**
   * The w-axis of the local coordinate system of the light.
   */
  private val w = direction.normalized * -1

  /**
   * The e-axis of the local coordinate system of the light.
   */
  private val u = (upVector x w).normalized

  /**
   * The v-axis of the local coordinate system of the light.
   */
  private val v = w x u

  /**
   * The color of the light.
   */
  private val _color = color

  /**
   * The number of the sampling points of the light.
   */
  private val _samplingPoints = samplingPoints

  override def createLight: Light = new Light {
    val color : Color = _color

    private val sp = SamplingPattern.randomPattern( _samplingPoints )

    override def illuminates(point: Point3, world: World): List[Boolean] = {
      for( p <- sp.samplingPoints.toList ) yield {
        val pos = position + u * p.x * size + v * p.y * size
        val ray = Ray( point, (pos - point).normalized )
        val hits = (ray --> world).filter( _.t > Constants.EPSILON ).toList
        hits.isEmpty || (hits.sortWith(  _.t < _.t ).head.t > ray( pos ) )
      }
    }

    override def directionFrom(point: Point3): List[Vector3] = {
      for( p <- sp.samplingPoints.toList ) yield {
        val pos = position + u * p.x * size + v * p.y * size
        (pos - point).normalized
      }
    }

    override def intensity(point: Point3): List[Double] = {
      for( p <- sp.samplingPoints.toList ) yield {
        val pos = position + u * p.x * size + v * p.y * size
        val distance = (point - pos).magnitude
        1 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)
      }
    }

    override def samplingPoints: Int = _samplingPoints
  }


}
