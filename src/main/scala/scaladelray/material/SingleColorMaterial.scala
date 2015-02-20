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

package scaladelray.material

import scaladelray.Color
import scaladelray.math.Ray
import scaladelray.rendering.Hit
import scaladelray.world.World

/**
 * A single color material always returns the same color, regardless of lighting conditions.
 * It is primarily used for testing and not in real scene.
 *
 * @param color The color of the material
 */
case class SingleColorMaterial( color : Color ) extends Material with Serializable {

  override def colorFor( hit: Hit, world : World, tracer : ((Ray,World) => Color) ) = color

}
