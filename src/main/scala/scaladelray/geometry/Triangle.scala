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

import scaladelray.material.Material
import scaladelray.math.{Mat3x3, Ray, Normal3, Point3}
import scaladelray.texture.TexCoord2D
import scaladelray.Constants


class Triangle  ( material : Material, a: Point3, b : Point3, c : Point3,
                an: Normal3, bn : Normal3, cn : Normal3,
                at: TexCoord2D, bt : TexCoord2D, ct : TexCoord2D ) extends Geometry( material ) with Serializable {

  def <--(r: Ray) = {
    val base = Mat3x3( a.x - b.x, a.x - c.x, r.d.x,
                       a.y - b.y, a.y - c.y, r.d.y,
                       a.z - b.z, a.z - c.z, r.d.z)

    val vec = a - r.o

    val beta = base.replaceCol1( vec ).determinant / base.determinant
    val gamma = base.replaceCol2( vec ).determinant / base.determinant
    val t = base.replaceCol3( vec ).determinant / base.determinant

    if( beta < 0.0 || gamma < 0.0 || beta + gamma > 1.0 || t < Constants.EPSILON ) {
      Set()
    } else {
      val alpha = 1 - beta - gamma
      Set() + Hit( r, this, t, an * alpha + bn * beta + cn * gamma, at * alpha + bt * beta + ct * gamma )
    }


  }
}
