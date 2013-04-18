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

package scala.del.ray.geometry

import scaladelray.geometry.{Geometry, Hit}
import scaladelray.material.Material
import scaladelray.math.{Ray, Normal3, Point3}
import scaladelray.texture.TexCoord2D

class TriangleMesh( material : Material, val vertices : Array[Point3], val normals : Array[Normal3], val texCoords : Array[TexCoord2D], val faces : Array[List[(Int,Option[Int],Option[Int])]] ) extends Geometry( material ) {

  def <--(r: Ray) : Set[Hit] = {
    Set()
  }
}
