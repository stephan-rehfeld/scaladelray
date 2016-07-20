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

package scaladelray.rendering

import scaladelray.HDRImage
import scaladelray.camera.Camera
import scaladelray.math.i.{Rectangle, Size2}

/**
 * Algorithm is the abstract base class for ray tracing based algorithms.
 */
abstract class Algorithm {

  def render( cam: Camera, imageSize: Size2, rect : Rectangle ) : HDRImage

}
