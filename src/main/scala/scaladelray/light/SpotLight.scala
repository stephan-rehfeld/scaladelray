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

import scaladelray.math.d.{Direction3, Point3}
import scaladelray.{Color, Constants}
import scaladelray.math.Ray
import scaladelray.world.World

/**
 * A point lights illuminates the scene similar to a point light, but only within a specified angle.
 * Because a spot light is a simple light, it implements [[scaladelray.light.LightDescription]] and also is
 * a [[scaladelray.light.Light]]. It returns itself when createLight is called.
 *
 * @param color The color of the light.
 * @param position The position of the light.
 * @param direction The main direction of the light.
 * @param halfAngle The half angle of the light.
 * @param constantAttenuation The constant attenuation.
 * @param linearAttenuation The linear attenuation.
 * @param quadraticAttenuation The quadratic attenuation.
 */
class SpotLight(color : Color, position : Point3, direction : Direction3, halfAngle : Double, constantAttenuation : Double = 1.0, linearAttenuation : Double = 0.0, quadraticAttenuation : Double = 0.0 ) extends LightDescription( color ) with Light with Serializable {
  override def illuminates( point: Point3, world : World ) = {
    val w = math.acos( (point - position).normalized dot direction) <= halfAngle
    if( w ) {
      val ray = Ray( point, (position - point).normalized )
      val hits = (ray --> world).filter( _.t > Constants.EPSILON ).toList
      ( hits.isEmpty || (hits.sortWith(  _.t < _.t ).head.t > ray( position ) ) ) :: Nil
    } else {
      w :: Nil
    }
  }
  override def directionFrom( point : Point3 ) = {
    (position - point).normalized :: Nil
  }

  override def intensity( point: Point3 ) = {
    val distance = (point - position).magnitude
    (1 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance)) :: Nil
  }

  override def createLight: Light = this

  override def samplingPoints: Int = 1
}
