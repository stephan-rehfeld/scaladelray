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

import scaladelray.{Color, Constants}
import scaladelray.math.Ray
import scaladelray.math.d.Point3
import scaladelray.world.World


/**
 * A point lights illuminates the scene from a single point in all directions.
 * Because a point light is a simple light, it implements [[scaladelray.light.LightDescription]] and also is
 * a [[scaladelray.light.Light]]. It returns itself when createLight is called.
 *
 * @param color The color of the light.
 * @param position The position of the point light.
 * @param castsShadows A flag if the lights casts a shadow.
 * @param constantAttenuation The constant attenuation.
 * @param linearAttenuation The linear attenuation.
 * @param quadraticAttenuation The quadratic attenuation.
 */
class PointLight( color : Color, position : Point3, castsShadows : Boolean = true, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends LightDescription( color ) with Light with Serializable {

  override def illuminates(point: Point3, world : World) = {
    if( castsShadows ) {
      val ray = Ray( point, (position - point).normalized )
      val hits = (ray --> world).filter( _.t > Constants.EPSILON ).toList
      (hits.isEmpty || (hits.sortWith(  _.t < _.t ).head.t > ray( position ) ) ) :: Nil
    } else {
      true :: Nil
    }

  }

  override def directionFrom( point : Point3 ) = (position - point).normalized :: Nil

  override def intensity(point: Point3)  = {
    val distance = (point - position).magnitude
    (1 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)) :: Nil
  }

  override def createLight: Light = this

  override def samplingPoints: Int = 1
}
