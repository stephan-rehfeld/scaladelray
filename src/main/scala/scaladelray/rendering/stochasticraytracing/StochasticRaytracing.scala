/*
 * Copyright 2016 Stephan Rehfeld
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

package scaladelray.rendering.stochasticraytracing

import scala.collection.mutable
import scaladelray.camera.{Camera, OrthographicCamera, PerspectiveCamera}
import scaladelray.material.bsdf.{PerfectReflectiveBRDF, PerfectTransparentBTDF}
import scaladelray.math.Ray
import scaladelray.math.d.Mat3x3
import scaladelray.math.i.{Point2, Rectangle, Size2}
import scaladelray.rendering.{Algorithm, Renderable}
import scaladelray.sampling.SamplingPattern
import scaladelray.world.World
import scaladelray.{Color, Constants, HDRImage}

class StochasticRaytracing( ambient : Color, world : World, recursionDepth : Int, aaSamplingPattern : SamplingPattern, lensSamplingPattern : SamplingPattern  ) extends Algorithm {

  require( recursionDepth > 0, "The parameter 'recursionDepth' must be larger than 0!" )

  var lights = mutable.HashSet[Renderable]()

  for( r <- world.objects ) {
    if( r.material.isEmissive ) {
      lights += r
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
                    val incomingRefractionColor = trace(Ray(hit.sp.p, refracted), recursionsLeft - 1)
                    c = c + cr * bsdfItensity * weight * incomingRefractionColor
                  case _ =>
                }
              case _ =>
                for (light <- lights) {
                  // Generate sampling points on surface
                  // For each point on surface
                    // Generate show ray
                    // Test shadow ray
                    //
                }
            }
          }
        }
      }
      c
    } else {
      Color( 0, 0, 0 )
    }

  override def render( cam: Camera, imageSize: Size2, rect : Rectangle  ) : HDRImage = {

    val img = HDRImage( rect.size )

    for { x <- rect.corner.x until rect.corner.x + rect.size.width
          y <- rect.corner.y until rect.corner.y + rect.size.height
    } {
      val rays = cam match {
        case pCam : PerspectiveCamera => rayFor( pCam, imageSize, Point2( x, y ), aaSamplingPattern, lensSamplingPattern )
        case oCam : OrthographicCamera => rayFor( oCam, imageSize, Point2( x, y ), aaSamplingPattern, lensSamplingPattern )
      }
      var color = Color( 0, 0, 0 )
      for( ray <- rays ) color = color + trace( ray, recursionDepth )
      color = color / rays.size
      img.set( x-rect.corner.x, y-rect.corner.y, color )
    }
    img
  }

  def rayFor( cam : PerspectiveCamera, imageSize : Size2, pixel : Point2, aaSamplingPattern : SamplingPattern, lensSamplingPattern : SamplingPattern ): Set[Ray] = {
    val lensCenter = cam.e + -cam.w * cam.focalLength
    val upperRightOfImagePlane = cam.e + cam.u * cam.imagePlaneFormat._1 / 2 + cam.v * cam.imagePlaneFormat._2 / 2
    val xStepWidth = cam.imagePlaneFormat._1 / imageSize.width
    val yStepHeight = cam.imagePlaneFormat._2 / imageSize.height
    val lensRadius = cam.focalLength / cam.fNumber / 2.0
    val aaScaleMatrix = Mat3x3( xStepWidth, 0.0,         0.0,
                                0.0,        yStepHeight, 0.0,
                                0.0,        0.0,         1.0 )

    val rays = mutable.HashSet[Ray]()

    for {
      aaSamplingPoint <- aaSamplingPattern.samplingPoints
      lensSamplingPoint <- lensSamplingPattern.asDisc
    } {
      val adjustedAASamplingPoint = aaScaleMatrix * aaSamplingPoint
      val adjustedLensSamplingPoint = lensSamplingPoint * lensRadius
      val o = upperRightOfImagePlane - cam.u * (xStepWidth * pixel.x - adjustedAASamplingPoint.x) - cam.v * (yStepHeight * pixel.y - adjustedAASamplingPoint.y )

      val adjustedLensCenter = lensCenter + cam.u * adjustedLensSamplingPoint.x + cam.v * adjustedLensSamplingPoint.y
      val d = (adjustedLensCenter - o).normalized

      // TODO: Now calculate the direction after this was refracted by the lens, to hit the DOF.
      Ray( o, d )
    }

    rays.toSet

  }

  def rayFor( cam : OrthographicCamera, imageSize : Size2, pixel : Point2, aaSamplingPattern : SamplingPattern, lensSamplingPattern : SamplingPattern ): Set[Ray] = {
    val lowerLeftOfImagePlane = cam.e - cam.u * cam.imagePlaneFormat._1 / 2 - cam.v * cam.imagePlaneFormat._2 / 2
    val xStepWidth = cam.imagePlaneFormat._1 / imageSize.width
    val yStepHeight = cam.imagePlaneFormat._2 / imageSize.height

    val rays = mutable.HashSet[Ray]()

    val o = lowerLeftOfImagePlane + cam.u * xStepWidth * pixel.x + cam.v * yStepHeight * pixel.y
    val d = -cam.w

    Ray( o, d )

    rays.toSet
  }

}
