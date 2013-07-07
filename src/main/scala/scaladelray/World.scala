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

package scaladelray

import geometry.{Hit, Geometry}
import scaladelray.light.{LightDescription, Light}
import math.Ray


case class World( backgroundColor : Color = Color( 0, 0, 0 ), objects : Set[Geometry], ambientLight : Color = Color( 0, 0, 0 ), lightDescriptions : Set[LightDescription] = Set(), indexOfRefraction : Double = 1.0 ) {

  def <--( r : Ray ) : Set[Hit] = {
    var hits = Set[Hit]()
    for( e <- objects ) {
      hits = hits | (r --> e)
    }
    hits
  }

}
