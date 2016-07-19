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

package scaladelray.world

import scaladelray.texture.{TexCoord2D, Texture}
import scaladelray.math.{Direction3, Ray}

/**
 * A skybox is a background that consists of six textures, one for each side. Usually a set of special images is used
 * to have seamless background of the scene.
 *
 * Technically, this class determines the dominant direction of the ray. Afterward, it calculates texture coordinate
 * from the other two elements of the direction.
 *
 * @param front The texture for the front side of the skybox.
 * @param back The texture for the back side of the skybox.
 * @param left The texture for the left side of the skybox.
 * @param right The texture for the right side of the skybox.
 * @param top The texture for the top side of the skybox.
 * @param bottom The texture for the bottom side of the skybox.
 */
case class Skybox( front : Texture, back : Texture, left : Texture, right : Texture, top: Texture, bottom : Texture ) extends Background {

  override def apply( r : Ray ) = {
    val d = r.d.normalized
    val m = mainDirection( d )
    m match {
      case "front" =>
        val dd = d /  math.abs(d.z)
        val u = 0.5 + (dd.x/2.0)
        val v = 0.5 + (dd.y/2.0)
        front( TexCoord2D( u, v ))
      case "back" =>
        val dd = d / d.z
        val u = 0.5 - (dd.x/2.0)
        val v = 0.5 + (dd.y/2.0)
        back( TexCoord2D( u, v ))
      case "left" =>
        val dd = d / math.abs(d.x)
        val u = 0.5 - (dd.z/2.0)
        val v = 0.5 + (dd.y/2.0)
        left( TexCoord2D( u, v ))
      case "right" =>
        val dd = d / d.x
        val u = 0.5 + (dd.z/2.0)
        val v = 0.5 + (dd.y/2.0)
        right( TexCoord2D( u, v ))
      case "top" =>
        val dd = d / d.y
        val u = 0.5 + (dd.x/2.0)
        val v = 0.5 + (dd.z/2.0)
        top( TexCoord2D( u, v ))
      case "bottom" =>
        val dd = d / math.abs(d.y)
        val u = 0.5 + (dd.x/2.0)
        val v = 0.5 - (dd.z/2.0)
        bottom( TexCoord2D( u, v ))
    }
  }

  /**
   * This method finds out which side of the sky box should be used to determine the color.
   * @param d The direction.
   * @return A string that contains the direction.
   */
  private def mainDirection( d : Direction3 ) : String = {
    val dAbs = Direction3( math.abs( d.x ), math.abs( d.y ), math.abs( d.z ) )
    val e = largest( dAbs.x, dAbs.y, dAbs.z )
    e match {
      case 0 =>
        if( d.x > 0 ) "right" else "left"
      case 1 =>
        if( d.y > 0 ) "top" else "bottom"
      case 2 =>
        if( d.z > 0 )  "back" else "front"
    }
  }

  /**
   * This functions returns the index of the largest passed value.
   *
   * @param v The values.
   * @return The index of the largest value.
   */
  private def largest( v : Double* ) : Int = {
    var r = 0
    for( i <- 1 until v.length ) {
      if( v( r ) < v ( i ) ) r = i
    }
    r
  }

}
