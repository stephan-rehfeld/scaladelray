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

package scaladelray.texture

import scaladelray.Color

case class TexCoord2D( u : Double, v : Double ) {
  def +( t : TexCoord2D ) = TexCoord2D( u + t.u, v + t.v )
  def *( n : Double ) = TexCoord2D( u * n, v * n )
}

abstract class Texture {
  def apply( texCoord : TexCoord2D ) : Color
}
