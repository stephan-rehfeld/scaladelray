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
import scaladelray.HDRImage
import scala.collection.mutable
import scaladelray.rendering.{Renderable, Algorithm}

class RayCasting extends Algorithm {

  override def render( w: World, c: Camera, width: Int, height: Int, l: Option[(HDRImage) => Unit] ): HDRImage = {

    val lightEmitting = mutable.Set[Renderable]()

    for( g <- w.objects ) {
      if( g.material.isEmissive ) lightEmitting += g
    }

    for( g <- lightEmitting ) {

    }

    HDRImage( width, height )
    // For all light emitting objects
      // Translate them to light source
    // For all pixels
      // Find intersection with smallest positive t.
      // Determine lighting
      // For all BSDFs
    // Return image


  }

}
