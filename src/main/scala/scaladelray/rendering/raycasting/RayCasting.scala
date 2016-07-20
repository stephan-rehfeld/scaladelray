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
import scaladelray.camera.{Camera, OrthographicCamera, PerspectiveCamera}
import scaladelray.material.emission.{DirectionalEmission, SimpleEmission, SpotEmission}
import scaladelray.math.Ray
import scaladelray.math.i.{Point2, Rectangle, Size2}
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

  override def render( cam: Camera, imageSize: Size2, rect : Rectangle  ) : HDRImage = {

    val img = HDRImage( rect.size )

    for { x <- rect.corner.x until rect.corner.x + rect.size.width
          y <- rect.corner.y until rect.corner.y + rect.size.height
    } {
      val ray = cam match {
        case pCam : PerspectiveCamera => rayFor( pCam, imageSize, Point2( x, y ) )
        case oCam : OrthographicCamera => rayFor( oCam, imageSize, Point2( x, y ) )
      }
      val hits = (ray --> world).toList.filter( _.t > Constants.EPSILON ).sortWith( _.t < _.t )
      if( hits.isEmpty ) {
        world.background( ray )
      } else {
        val hit = hits.head
        if( hit.renderable.material.isEmissive ) {
          img.set( x -rect.corner.x, y - rect.corner.y, hit.renderable.material.e.get( hit.sp, -ray.d ) )
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
              val bsdIntensity = bsdf( hit.sp, light.directionFrom( hit.sp.p ), 1.0, hit.sp, -ray.d )
              val lightIntensity = light.intensity( hit.sp.p )
              val cos = hit.sp.n dot light.directionFrom( hit.sp.p )

              c = c + light.c * cr * bsdIntensity * weight * lightIntensity * cos
            }
          }
          img.set( x-rect.corner.x, y-rect.corner.y, c )
        }
      }
    }
    img
  }

  def rayFor( cam : PerspectiveCamera, imageSize : Size2, pixel : Point2 ): Ray = {
    val lensCenter = cam.e + -cam.w * cam.focalLength
    val upperRightOfImagePlane = cam.e + cam.u * cam.imagePlaneFormat._1 / 2 + cam.v * cam.imagePlaneFormat._2 / 2
    val xStepWidth = cam.imagePlaneFormat._1 / imageSize.width
    val yStepHeight = cam.imagePlaneFormat._2 / imageSize.height

    val o = upperRightOfImagePlane - cam.u * xStepWidth * pixel.x - cam.v * yStepHeight * pixel.y
    val d = lensCenter - o

    Ray( o, d )
  }

  def rayFor( cam : OrthographicCamera, imageSize : Size2, pixel : Point2 ): Ray = {
    val lowerLeftOfImagePlane = cam.e - cam.u * cam.imagePlaneFormat._1 / 2 - cam.v * cam.imagePlaneFormat._2 / 2
    val xStepWidth = cam.imagePlaneFormat._1 / imageSize.width
    val yStepHeight = cam.imagePlaneFormat._2 / imageSize.height

    val o = lowerLeftOfImagePlane + cam.u * xStepWidth * pixel.x + cam.v * yStepHeight * pixel.y
    val d = -cam.w

    Ray( o, d )
  }

}
