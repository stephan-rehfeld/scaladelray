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

package scaladelray.rendering.raycasting

import scaladelray.world.World
import scaladelray.camera.Camera
import scaladelray.{Color, Constants, HDRImage}
import scala.collection.mutable
import scaladelray.rendering.{Renderable, Algorithm}
import scaladelray.geometry.Sphere
import scaladelray.light.Light

class RayCasting( ambient : Color ) extends Algorithm {

  override def render( w: World, c: Camera, width: Int, height: Int, l: Option[(HDRImage) => Unit] ): HDRImage = {

    val lightEmitting = mutable.Set[Renderable]()

    for( r <- w.objects ) {
      if( r.material.isEmissive ) lightEmitting += r
    }

    val lights = List[Light]()

    // For all light emitting objects
    // Translate them to light source
    for( r <- lightEmitting ) {
      r.geometry match {
        case s : Sphere =>

      }
    }

    val img = HDRImage( width, height )

    for { x <- 0 until width
          y <- 0 until height
    } {
      val ray = c( x, y ).head
      for( r <- w.objects ) {
        val hits = (ray --> w).toList.filter( _.t > Constants.EPSILON ).sortWith( _.t < _.t )
        if( hits.isEmpty ) {
          w.background( ray )
        } else {
          val hit = hits.head
          var c = Color( 0, 0, 0 )
          if( c != ambient ) {
            for( (_, texture, _ ) <- hit.renderable.material.bsdfs ) {
              c = c + texture( hit.sp.t ) * ambient
            }
          }
          for( light <- lights ) {
            for( (w, texture, bsdf ) <- hit.renderable.material.bsdfs ) {
              c = c + light.color * texture( hit.sp.t ) * bsdf( hit.sp, light.directionFrom( hit.sp.p ).head, 1.0, hit.sp, ray.d ) * w
            }
          }
        }
      }
    }
    // For all pixels
      // Find intersection with smallest positive t.
      // Determine lighting
      // For all BSDFs
    // Return image

    img

  }

}
