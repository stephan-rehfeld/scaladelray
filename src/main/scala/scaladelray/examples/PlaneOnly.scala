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
import scaladelray.geometry.Plane
import scaladelray.material.SingleColorMaterial
import scaladelray.camera.PerspectiveCamera
import scaladelray.math.{Vector3, Point3}
import scaladelray.ui.WindowedRayTracer


object PlaneOnly extends WindowedRayTracer {

  def world = World(
    backgroundColor = Color( 0, 0, 0 ),
    objects = Set() + new Plane(
      material = SingleColorMaterial( Color( 0, 1, 0 ) )
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