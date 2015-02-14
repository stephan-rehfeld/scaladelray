/*
 * Copyright 2015 Stephan Rehfeld
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

package test.scaladelray.material

import scaladelray.texture.{TexCoord2D, Texture}
import scaladelray.{World, Color}
import scaladelray.math.{Point3, Vector3}
import scaladelray.light.{Light, LightDescription}

class TextureTestAdapter extends Texture {

  var coordinates : Option[TexCoord2D] = None

  def apply(texCoord: TexCoord2D): Color = {
    coordinates = Some( texCoord )
    Color( 0, 0, 0 )
  }

}

class LightTestAdapter( illuminatesData : List[Boolean], directionFromData : List[Vector3], intensityData : List[Double] ) extends LightDescription( Color( 0, 0, 0 )) with Light {

  assert( illuminatesData.size == directionFromData.size )
  assert( illuminatesData.size == intensityData.size )
  assert( directionFromData.size == intensityData.size )

  var illuminatesPoint : Option[Point3] = None
  var illuminatesWorld : Option[World] = None
  var directionPoint : Option[Point3] = None
  var intensityPoint : Option[Point3] = None
  var createLightCalled = false

  def samplingPoints: Int = illuminatesData.size

  def illuminates( point: Point3, world: World ): List[Boolean] = {
    illuminatesPoint = Some( point )
    illuminatesWorld = Some( world )
    illuminatesData
  }

  def directionFrom( point: Point3 ): List[Vector3] = {
    directionPoint = Some( point )
    directionFromData
  }

  def intensity( point: Point3 ): List[Double] = {
    intensityPoint = Some( point )
    intensityData
  }

  def createLight: Light = {
    createLightCalled = true
    this
  }
}


class MaterialSpec {

}
