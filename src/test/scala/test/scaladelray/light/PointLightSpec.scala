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

package test.scaladelray.light

import org.scalatest.FunSpec

class PointLightSpec extends FunSpec {

  describe( "A PointLight" ) {
    it( "should radiate in all directions." ) (pending)
    it( "should return itself when createLight is called." ) (pending)
    it( "should check the world if an object is between the point and the point light" ) (pending)
    it( "should return false if an object is between the point and the light" ) (pending)
    it( "should calculate the constant attenuation correctly") (pending)
    it( "should calculate the linear attenuation correctly") (pending)
    it( "should calculate the quadratic attenuation correctly") (pending)
    it( "should only have one sampling point") (pending)
    it( "should calculate the direction correctly") (pending)
  }

}
