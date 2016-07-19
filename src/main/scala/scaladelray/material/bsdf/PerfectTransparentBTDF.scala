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

package scaladelray.material.bsdf

import scaladelray.geometry.SurfacePoint
import scaladelray.math.{Normal3, Direction3}

case class PerfectTransparentBTDF( indexOfRefraction : Double ) extends BTDF {

  override def apply(p : SurfacePoint, dIn : Direction3, eta : Double, dOut : Direction3 ) : Double = {
    val reflected = this.reflectedRay( dIn, p.n )
    val mayBeRefractedRay = this.refractedRay( dIn, p.n, eta )

    mayBeRefractedRay match {
      case Some( refracted ) =>
        val (r,t) = schlick( dIn, p.n, eta )
        if( dOut  =~= reflected ) r
        else if( dOut =~= refracted ) t
        else 0.0

      case None => if( dOut =~= reflected ) 1.0 else 0.0

    }
  }

  def reflectedRay(direction : Direction3, surfaceNormal : Normal3 ) : Direction3 = {
    if( surfaceNormal.dot( direction ) >= 0 )
      direction.reflectOn( surfaceNormal )
    else
      (-direction).reflectOn( surfaceNormal )
  }

  def refractedRay(direction : Direction3, n : Normal3, outsidesIndexOfRefraction : Double ) : Option[Direction3] = {
    val inside = (n dot direction) < 0
    val eta = if( inside )  indexOfRefraction/outsidesIndexOfRefraction else outsidesIndexOfRefraction/indexOfRefraction
    val cn = if( inside ) -n else n

    val costhetai = cn dot direction
    val h = 1 - (eta*eta) * (1-costhetai*costhetai)
    if( h < 0 ) {
      None
    } else {
      val costhetat = scala.math.sqrt( h )
      Some( (-direction * eta) - (cn*(costhetat - eta * costhetai )) )
    }
  }

  private def schlick(direction : Direction3, normal : Normal3, etaOut : Double ) : (Double,Double) = {
    val inside = (normal dot direction) < 0
    val cn = if( inside ) normal * -1 else normal
    val cosThetaI = cn dot direction
    val r0 = scala.math.pow( (etaOut - indexOfRefraction)/(etaOut+indexOfRefraction), 2 )
    val r = r0 + (1-r0)*scala.math.pow(1-cosThetaI,5)
    val t = 1-r
    (r,t)
  }

}
