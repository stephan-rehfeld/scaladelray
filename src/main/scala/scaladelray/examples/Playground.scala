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

package scaladelray.examples

import scaladelray.{Color, World}
import scaladelray.geometry.{AxisAlignedBox, Node, Plane}
import scaladelray.material.{PhongMaterial, LambertMaterial}
import scaladelray.texture.SingleColorTexture
import scaladelray.math.{Vector3, Point3, Transform}
import scaladelray.light.PointLight
import scaladelray.camera.PerspectiveCamera
import scaladelray.ui.WindowedRayTracer


object Playground extends WindowedRayTracer {

  def world = World(
    backgroundColor = Color( 0, 0, 0 ),
    objects = new Plane(
      material = LambertMaterial( SingleColorTexture( Color( 0.8, 0.8, 0.8 )) )
    ) + new Node(
      Transform.translate( 0, 1, 0  ).scale( 2, 2, 2 ),
      new AxisAlignedBox(
        material = PhongMaterial( SingleColorTexture( Color( 1, 0, 0 )), SingleColorTexture( Color( 1, 1, 1 )), 64 )
      )
    ),
    ambientLight = Color( 0.25, 0.25, 0.25 ),
    indexOfRefraction = 1.0,
    lightDescriptions = Set() + new PointLight(
      position = Point3( 8, 8, 0 ),
      color = Color ( 1, 1, 1 )
    )
  )

  def camera = PerspectiveCamera(
    Point3( 8, 8, 8 ),
    Vector3( -1, -1, -1 ),
    Vector3( 0, 1, 0 ),
    _,
    _,
    math.Pi * 0.25
  )


}
