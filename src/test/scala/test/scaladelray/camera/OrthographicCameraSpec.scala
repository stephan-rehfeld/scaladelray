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

package test.scaladelray.camera

import org.scalatest.FunSpec
import scaladelray.math.{Ray, Vector3, Point3}
import scaladelray.camera.OrthographicCamera


class OrthographicCameraSpec extends FunSpec {

  describe( "An OrthographicCamera") {
    it( "should calculate the correct center for an image with the size of 1024x768 and a size of 1") {
      val cam = new OrthographicCamera( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ), Vector3( 0, 1, 0 ), 1024, 768, 1 )
      assert( cam( 511, 383 ) == Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) ) )
    }
  }

}
