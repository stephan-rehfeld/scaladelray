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

package scaladelray

import math.Ray
import scaladelray.world.World


object Tracer {
  def standardTracer( ray : Ray, world : World, recursions : Int ) : Color = {
    if( recursions < 0 ) {
      Color( 1, 1, 0 )
    } else {
      val hits = (ray --> world).toList.filter( _.t > Constants.EPSILON ).sortWith( _.t < _.t )
      if( hits.isEmpty ) {
        world.background( ray )
      } else {
        val hit = hits.head
        hit.renderable.oldMaterial.colorFor( hit, world, Tracer.standardTracer( _, _, recursions - 1 ) )
      }
    }
  }


}
