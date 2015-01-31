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

/**
 * Created by Stephan Rehfeld on 31.01.15.
 */
class AreaLightSpec extends FunSpec {

  describe( "An AreaLight" ) {
    it( "should radiate all points" ) (pending)
    it( "should return a new light for each call of createLight" ) (pending)
    it( "should check the world if an object is between the point and the area light" ) (pending)
    it( "should return false if an object is between the point and the light" ) (pending)
    it( "should calculate the constant attenuation correctly") (pending)
    it( "should calculate the linear attenuation correctly") (pending)
    it( "should calculate the quadratic attenuation correctly") (pending)
    it( "should have the specified number of sampling points") (pending)
    it( "should always calculate the direction to each sampling point") (pending)
  }

}
