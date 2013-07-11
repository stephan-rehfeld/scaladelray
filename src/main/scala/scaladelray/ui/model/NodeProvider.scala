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

package scaladelray.ui.model

import scaladelray.geometry.{Node, Geometry}
import scaladelray.math.{Transform, Vector3, Point3}
import scala.collection.mutable

class NodeProvider extends GeometryProvider {

  var childNodes = mutable.MutableList[GeometryProvider]()
  var translate = Point3( 0, 0, 0 )
  var scale = Vector3( 1, 1, 1 )
  var rotate = Vector3( 0, 0, 0 )

  def createGeometry: Geometry = {
    val cn = for( n <- childNodes ) yield n.createGeometry
    val t = Transform.scale( scale.x, scale.y, scale.z ).rotateZ( rotate.z ).rotateY(rotate.y ).rotateX( rotate.x ).translate( translate )
    new Node( t, cn:_*  )
  }
}
