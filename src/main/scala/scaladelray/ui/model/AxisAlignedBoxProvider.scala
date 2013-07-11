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

import scaladelray.math.{Transform, Point3, Vector3}
import scaladelray.geometry.{Node, AxisAlignedBox, Geometry}

class AxisAlignedBoxProvider extends GeometryProvider {

  var materialProvider : Option[MaterialProvider] = None
  var translate = Point3( 0, 0, 0 )
  var scale = Vector3( 1, 1, 1 )
  var rotate = Vector3( 0, 0, 0 )

  def createGeometry: Geometry = {
    val aab = new AxisAlignedBox( materialProvider.get.createMaterial )
    val t = Transform.scale( scale.x, scale.y, scale.z ).rotateZ( rotate.z ).rotateY(rotate.y ).rotateX( rotate.x ).translate( translate )
    new Node( t, aab )
  }
}
