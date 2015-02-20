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

package scaladelray.world

import scaladelray.math.Ray
import scaladelray.Color

/**
 * A background generates a color for a ray that does not hit any renderable object in a world.
 */
trait Background {

  /**
   * An implementation of this method generates a color for the ray. The ray did not hit any object.
   *
   * @param r The ray that did not hit any renderable object.
   * @return A background color.
   */
  def apply( r : Ray ) : Color

}
