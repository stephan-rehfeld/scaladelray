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

package scaladelray.geometry

import scaladelray.math.{Ray, Transform}
import scaladelray.material.SingleColorMaterial
import scaladelray.Color


class Node( t : Transform, nodes : Geometry* ) extends Geometry( SingleColorMaterial( Color( 0, 0, 0 ) ) ) {
  def <--(r: Ray) : Set[Hit] = {
    var hits = Set[Hit]()
    val transformedRay = t * r
    for( node <- nodes ) {
      hits = hits | (transformedRay --> node )
    }
    for( hit <- hits ) yield Hit( r, hit.geometry, hit.t, t * hit.n, hit.texCoord2D )
  }
}