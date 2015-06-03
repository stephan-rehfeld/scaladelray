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

package scaladelray.material

import scaladelray.texture.Texture

/**
 * A material aggregates several BSDFs and optionally contains a emission.
 *
 * @param e An optional light emission of the material.
 * @param bsdfs BSDFs, the texture for the bsdfs and how much they contribute to the overall reflection of this material.
 */
case class Material( e : Option[Emission], bsdfs : (Double,Texture,BSDF)* ) {

  for( e <- bsdfs ) require( e._1 >= 0.0 && e._1 <= 1.0, "The contribution of each BSD must be between 0 and 1" )
  require( bsdfs.foldLeft( 0.0 )( (s, e) => s + e._1 ) <= 1.0, "The total contribution of all BSDFs muss be <= 1.0!" )

  val isEmissive = e.isDefined
}
