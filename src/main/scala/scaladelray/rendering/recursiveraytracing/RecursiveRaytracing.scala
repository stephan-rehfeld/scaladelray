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

package scaladelray.rendering.recursiveraytracing

import scala.collection.mutable
import scaladelray.camera.{Camera, OldCamera}
import scaladelray.material.bsdf.{PerfectReflectiveBRDF, PerfectTransparentBTDF}
import scaladelray.material.emission.{DirectionalEmission, SimpleEmission, SpotEmission}
import scaladelray.math.Ray
import scaladelray.rendering.recursiveraytracing.light.{DirectionalLight, Light, PointLight, SpotLight}
import scaladelray.rendering.{Algorithm, Renderable}
import scaladelray.world.World
import scaladelray.{Color, Constants, HDRImage}

class RecursiveRaytracing( ambient : Color, world : World, recursionDepth : Int  ) extends Algorithm {

  require( recursionDepth > 0, "The parameter 'recursionDepth' must be larger than 0!" )

  var lights = mutable.HashMap[Light, Renderable]()

  for( r <- world.objects ) {
    if( r.material.isEmissive ) r.material.e.get match {
      case simple : SimpleEmission =>
        lights += (PointLight( r, simple.c, r.center ) -> r )
      case spot : SpotEmission =>
        lights += (SpotLight( r, spot.c, r.center, r.axis, spot.halfAngle ) -> r)
      case directional : DirectionalEmission =>
        lights += (DirectionalLight( r, directional.c, r.axis) -> r)
    }
  }

  private def trace( ray : Ray, recursionsLeft : Int ) : Color =
    if (recursionsLeft > 0) {
      var c = Color(0, 0, 0)
      val hits = (ray --> world).toList.filter(_.t > Constants.EPSILON).sortWith(_.t < _.t)
      if (hits.isEmpty) {
        c = world.background(ray)
      } else {
        val hit = hits.head
        if (hit.renderable.material.isEmissive) {
          c = hit.renderable.material.e.get(hit.sp, -ray.d)
        } else {
          if (c != ambient) {
            for ((_, texture, _) <- hit.renderable.material.bsdfs) {
              c = c + texture(hit.sp.t) * ambient
            }
          }
          for ((weight, texture, bsdf) <- hit.renderable.material.bsdfs) {
            val cr = texture(hit.sp.t)
            bsdf match {
              case perfectReflectiveBRDF: PerfectReflectiveBRDF =>
                val outGoingDirection = (-ray.d).reflectOn(hit.sp.n)
                val bsdfItensity = bsdf(hit.sp, outGoingDirection, 1.0, hit.sp, -ray.d)
                val incomingReflectionColor = trace(Ray(hit.sp.p, outGoingDirection), recursionsLeft - 1)
                c = c + cr * bsdfItensity * weight * incomingReflectionColor
              case perfectTransparentBTDF : PerfectTransparentBTDF =>
                val reflected = perfectTransparentBTDF.reflectedRay( -ray.d, hit.sp.n )
                val bsdfItensity = bsdf(hit.sp, reflected, 1.0, hit.sp, -ray.d)
                val incomingReflectionColor = trace(Ray(hit.sp.p, reflected), recursionsLeft - 1)
                c = c + cr * bsdfItensity * weight * incomingReflectionColor
                perfectTransparentBTDF.refractedRay( -ray.d, hit.sp.n, world.indexOfRefraction ) match {
                  case Some( refracted ) =>
                    val bsdfItensity = bsdf(hit.sp, refracted, 1.0, hit.sp, -ray.d)
                    println( "Refracted intensity: " + bsdfItensity )
                    val incomingRefractionColor = trace(Ray(hit.sp.p, refracted), recursionsLeft - 1)
                    c = c + cr * bsdfItensity * weight * incomingRefractionColor
                  case _ =>
                }
              case _ =>
                for ((light, renderable) <- lights) {
                  if (light.illuminates(hit.sp.p, world )) {
                    val bsdfItensity = bsdf(hit.sp, light.directionFrom(hit.sp.p), 1.0, hit.sp, -ray.d)
                    val lightIntensity = light.intensity(hit.sp.p)
                    val cos = hit.sp.n dot light.directionFrom(hit.sp.p)
                    c = c + light.c * cr * bsdfItensity * weight * lightIntensity * math.max( 0, cos )
                  }
                }
            }
          }
        }
      }
      c
    } else {
      Color( 0, 0, 0 )
    }

  override def render( cam : Camera, c : OldCamera, rect : HDRImage.Rectangle ) : HDRImage = {

    val img = HDRImage( rect.width, rect.height )

    for { x <- rect.x until rect.x + rect.width
          y <- rect.y until rect.y + rect.height
    } {
      val ray = c( x, y ).head
      img.set( x-rect.x, y-rect.y, trace( ray, recursionDepth ) )
    }
    img
  }

}
