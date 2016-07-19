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

import scaladelray.Color
import scaladelray.math.{Normal3, Direction3, Ray}
import scaladelray.rendering.Hit
import scaladelray.world.World


case class TransparentOldMaterial( indexOfRefraction : Double ) extends OldMaterial with Serializable {
  override def colorFor( hit: Hit, world : World, tracer : ((Ray,World) => Color) ) : Color = {
    val normal = hit.sp.n
    val p =  hit.ray( hit.t )
    val e = -hit.ray.d
    val r = e reflectOn normal
    val ot = refracted( e, normal, world.indexOfRefraction, indexOfRefraction )
    ot match {
      case Some( t ) =>
        val (a,b) = schlick( e, normal, world.indexOfRefraction, indexOfRefraction )
        ( tracer( Ray( p, r ), world ) * a) + (tracer( Ray( p, t ), world ) * b)
      case None =>
        tracer( Ray( p, r ), world )
    }
  }

  def refracted(e : Direction3, n : Normal3, etaOut : Double, etaIn : Double ) : Option[Direction3] = {
    val inside = (n dot e) < 0
    val eta = if( inside )  etaIn/etaOut else etaOut/etaIn
    val cn = if( inside ) -n else n

    val costhetai = cn dot e
    val h = 1 - (eta*eta) * (1-costhetai*costhetai)
    if( h < 0 ) {
      None
    } else {
      val costhetat = scala.math.sqrt( h )
      //Some( (e * -1 * (1/eta)) - (cn*(costhetat - (1/eta) * costhetai )) )
      Some( (-e * eta) - (cn*(costhetat - eta * costhetai )) )
    }
  }

  /*def refracted( e : Vector3, n : Normal3, etaOut : Double, etaIn : Double ) : Option[Vector3] = {
    val d = e * -1
    val costhetai = n dot e
    val h = 1 - math.pow( (etaOut/etaIn), 2 ) * (1-costhetai*costhetai)
    if( h < 0 ) {
      None
    } else {
      val costhetat = scala.math.sqrt( h )
      if( costhetai >= 0 ) {
        Some( d * (etaIn/etaOut) + (n * ((etaIn/etaOut) * costhetai - costhetat ))  )
      } else {
        Some( d * (etaIn/etaOut) + (n * ((etaIn/etaOut) * costhetai + costhetat ))  )
      }

    }
  }*/


  def schlick(e : Direction3, n : Normal3, etaOut : Double, etaIn : Double ) : (Double,Double) = {
    val inside = (n dot e) < 0
    val cn = if( inside ) n * -1 else n
    val cosThetaI = cn dot e
    //TODO: Is it correct? I think it must be changed in order.
    val r0 = scala.math.pow( (etaOut - etaIn)/(etaOut+etaIn), 2 )
    val r = r0 + (1-r0)*scala.math.pow(1-cosThetaI,5)
    val t = 1-r
    (r,t)
  }

}