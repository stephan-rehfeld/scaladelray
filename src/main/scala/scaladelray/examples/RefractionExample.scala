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

import scaladelray.{Color, World, WindowedRayTracer}
import scaladelray.geometry.{AxisAlignedBox, Sphere, Node, Plane}
import scaladelray.material.{PhongMaterial, TransparentMaterial, ReflectiveMaterial}
import scaladelray.texture.SingleColorTexture
import scaladelray.math.{Vector3, Point3, Transform}
import scaladelray.camera.PerspectiveCamera
import scaladelray.light.PointLight


object RefractionExample extends WindowedRayTracer {

  def world = World(
    backgroundColor = Color( 0, 0, 0 ),
    objects = new Plane(
      material = ReflectiveMaterial( SingleColorTexture( Color( 0, 0.5, 0 ) ), SingleColorTexture( Color( 0, 0.5, 0 ) ), 64, SingleColorTexture( Color( 1, 1, 1 ) ) ) //LambertMaterial( SingleColorTexture( Color( 1, 0, 0 ) ) ),// SingleColorMaterial( Color( 1, 0, 0 ) ),
    ) + new Node(
        Transform.translate( -0.1, -0.1, -1 ).scale( 0.1, 0.1, 0.1 ),
        new Sphere(
          material = TransparentMaterial( 1.1 ) // PhongMaterial( SingleColorTexture( Color( 1, 0, 0 ) ), SingleColorTexture( Color( 1, 1, 1 ) ), 64 ) //LambertMaterial( SingleColorTexture( Color( 0, 1, 0 ) ) ),// SingleColorMaterial( Color( 0, 1, 0 ) ),
        )
    ) + new Node(
      Transform.translate(0.0, 0.0, -7.5 ).scale( 2.0, 3.0, 1.0 ).translate( 0.0, 0.5, 0.0 ),
      new AxisAlignedBox(
        material =   PhongMaterial( SingleColorTexture( Color( 1, 0, 0 ) ), SingleColorTexture( Color( 1, 1, 1 ) ), 64 )
      )
    ),
    ambientLight = Color( 0.1, 0.1, 0.1 ),
    lightDescriptions = Set() + new PointLight(
      position = Point3( 1, 1, 0 ),
      color = Color( 1, 1, 1 )

    )

  )

  def camera = PerspectiveCamera(
    Point3( 0, 1, 0 ),
    Vector3( 0, 0, -1 ),
    Vector3( 0, 1, 0 ),
    _,
    _,
    math.Pi * 0.25
  )

}
