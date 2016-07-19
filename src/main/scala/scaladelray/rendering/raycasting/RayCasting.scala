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

import scala.collection.mutable
import scaladelray.camera.{Camera, OldCamera, PerspectiveCamera}
import scaladelray.material.emission.{DirectionalEmission, SimpleEmission, SpotEmission}
import scaladelray.math.Ray
import scaladelray.rendering.raycasting.light.{DirectionalLight, Light, PointLight, SpotLight}
import scaladelray.rendering.{Algorithm, Renderable}
import scaladelray.world.World
import scaladelray.{Color, Constants, HDRImage}

class RayCasting( ambient : Color, world : World  ) extends Algorithm {

  var lights = mutable.HashMap[Light, Renderable]()

  for( r <- world.objects ) {
    if( r.material.isEmissive ) r.material.e.get match {
      case simple : SimpleEmission =>
        lights += (PointLight( simple.c, r.center ) -> r )
      case spot : SpotEmission =>
        lights += (SpotLight( spot.c, r.center, r.axis, spot.halfAngle ) -> r)
      case directional : DirectionalEmission =>
        lights += (DirectionalLight( directional.c, r.axis) -> r)
    }
  }

  override def render( cam : Camera, c : OldCamera, rect : HDRImage.Rectangle ) : HDRImage = {

    val img = HDRImage( rect.width, rect.height )

    for { x <- rect.x until rect.x + rect.width
          y <- rect.y until rect.y + rect.height
    } {
      val ray = c( x, y ).head
      val hits = (ray --> world).toList.filter( _.t > Constants.EPSILON ).sortWith( _.t < _.t )
      if( hits.isEmpty ) {
        world.background( ray )
      } else {
        val hit = hits.head
        if( hit.renderable.material.isEmissive ) {
          img.set( x -rect.x, y - rect.y, hit.renderable.material.e.get( hit.sp, -ray.d ) )
        } else {
          var c = Color( 0, 0, 0 )
          if( c != ambient ) {
            for( (_, texture, _ ) <- hit.renderable.material.bsdfs ) {
              c = c + texture( hit.sp.t ) * ambient
            }
          }
          for( (light,renderable) <- lights ) {
            if( light.illuminates( hit.sp.p ) ) for( (weight, texture, bsdf ) <- hit.renderable.material.bsdfs ) {
              val cr = texture( hit.sp.t )
              val bsdfItensity = bsdf( hit.sp, light.directionFrom( hit.sp.p ), 1.0, hit.sp, -ray.d )
              val lightIntensity = light.intensity( hit.sp.p )
              val cos = hit.sp.n dot light.directionFrom( hit.sp.p )

              c = c + light.c * cr * bsdfItensity * weight * lightIntensity * cos
            }
          }
          img.set( x-rect.x, y-rect.y, c )
        }
      }
    }
    img
  }

}
