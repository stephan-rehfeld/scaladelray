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
import scaladelray.math.Vector3

/**
 * A phong specular BRDF.
 *
 * @param exponent The phong exponent.
 */
case class PhongSpecularBRDF( exponent : Double ) extends BRDF {

  override def apply( p : SurfacePoint, dIn : Vector3, dOut : Vector3 ) : Double = {
    val r = (-dIn).reflectOn(p.n)
    math.pow( r dot dOut, exponent ) / math.Pi
  }

}
