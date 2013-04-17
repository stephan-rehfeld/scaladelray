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

package scaladelray.examples

import scaladelray.math.{Normal3, Vector3}

object Main {
  def main( args : Array[String] ) {
    val v = Vector3( -1, 1, 0 ).normalized
    val r = refracted( v, Normal3( 0, 1, 0 ), 1.0, 2.417 ).get
    val n = Normal3( 0, 1, 0 )
    println( v )
    println( r )

    println( (n * -1) dot r )


  }

  def refracted( e : Vector3, n : Normal3, etaOut : Double, etaIn : Double ) : Option[Vector3] = {
    val inside = (n dot e) < 0
    val eta = if( inside )  etaIn/etaOut else etaOut/etaIn
    val cn = if( inside ) n * -1 else n

    val costhetai = cn dot e
    val h = 1 - (eta*eta) * (1-costhetai*costhetai)
    if( h < 0 ) {
      None
    } else {
      val costhetat = scala.math.sqrt( h )
      //Some( (e * -1 * (1/eta)) - (cn*(costhetat - (1/eta) * costhetai )) )
      Some( (e * -1 * eta) - (cn*(costhetat - eta * costhetai )) )
    }
  }
}
