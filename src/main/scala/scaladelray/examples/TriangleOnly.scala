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
import scaladelray.geometry.Triangle
import scaladelray.material.SingleColorMaterial
import scaladelray.math.{Vector3, Normal3, Point3}
import scaladelray.texture.TexCoord2D
import scaladelray.camera.PerspectiveCamera
import scaladelray.ui.WindowedRayTracer


object TriangleOnly extends WindowedRayTracer {

  def world = World(
    backgroundColor = Color( 0, 0, 0 ),
    objects = Set() + new Triangle (
      material = SingleColorMaterial( Color( 1, 0, 1 ) ),
      a = Point3( -0.5, 0.5, -3 ),
      b = Point3( 0.5, 0.5, -3 ),
      c = Point3( 0.5, -0.5, -3 ),
      an = Normal3( 0, 1, 0 ),
      bn = Normal3( 0, 1, 0 ),
      cn = Normal3( 0, 1, 0 ),
      at = TexCoord2D( 0, 0 ),
      bt = TexCoord2D( 0, 1 ),
      ct = TexCoord2D( 1, 1 )
    )
  )

  def camera = PerspectiveCamera(
    Point3( 0, 0, 0 ),
    Vector3( 0, 0, -1 ),
    Vector3( 0, 1, 0 ),
    _,
    _,
    math.Pi * 0.25
  )


}