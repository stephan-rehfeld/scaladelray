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

package scaladelray.material

import scaladelray.texture.Texture
import scaladelray.geometry.Hit
import scaladelray.{Color, World}
import scaladelray.math.Ray


case class LightBlendingMaterial( bright : Texture, dark : Texture ) extends Material {

  override def colorFor( hit: Hit, world : World, tracer : ((Ray,World) => Color) ) : Color = {
    val colorBright = bright( hit.texCoord2D )
    val colorDark = dark( hit.texCoord2D )
    val normal = hit.n
    val p =  hit.ray( hit.t )
    val lights = for( light <- world.lights ) yield light
    if ( lights.size == 0 )
      colorDark
    else {
      var c = Color( 0, 0, 0 )
      for ( light <- lights ) {
        if( light.illuminates( p, world ) ) {
          val l = light.directionFrom( p )
          val f = normal dot l
          c = c + ( (colorBright * f) + (colorDark * (1.0-f)) )
        } else {
          c = c + colorDark
        }
      }
      c
    }
  }
}