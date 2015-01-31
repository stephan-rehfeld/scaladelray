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

import scaladelray.Color

/**
 * A light description creates a instance of a light whenever createLight is called. This is necessary if a random
 * sampling pattern is used for an area light and needs to be recreated for each point.
 *
 * Simple lights, like spot or point lights usually derived directly and just return this.
 *
 * @param color The color of the light.
 */
abstract class LightDescription( val color : Color ) extends Serializable {

  /**
   * Convenience function to create a set of light descriptions.
   *
   * @param l Another light.
   * @return A set that contains this description and another light description.
   */
  def +( l : LightDescription ) = Set() + this + l

  /**
   * An implementation of this function should create a light that matches the description.
   *
   * @return A light.
   */
  def createLight : Light
}
