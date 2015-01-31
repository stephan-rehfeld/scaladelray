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
import scaladelray.math.{Ray, Point3, Vector3}

/**
 * The directional light represents the light of the sun. It behaves like a point light with an infinite distance.
 *
 * Because a directional light is a simple light, it implements [[scaladelray.light.LightDescription]] and also is
 * a [[scaladelray.light.Light]]. It returns itself when createLight is called.
 *
 * @param color The color of the light.
 * @param direction The direction of the light.
 */
class DirectionalLight( color : Color, direction : Vector3 ) extends LightDescription( color ) with Light with Serializable {
  val l = (direction * -1).normalized

  override def illuminates(point: Point3, world : World) = (Ray( point, direction * -1 ) --> world).filter( _.t > Constants.EPSILON ).isEmpty :: Nil
  override def directionFrom( point : Point3 ) = l :: Nil
  override def intensity(point: Point3) = 1.0 :: Nil

  override def createLight: Light = this

  override def samplingPoints: Int = 1
}
