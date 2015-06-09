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
import scaladelray.light.{Light, LightDescription}
import scaladelray.Color

class LightDescriptionSpec extends FunSpec  {
  describe( "A LightDescription" ) {
    it( "should have a convenience function to create a set out of two light descriptions" ) {

      val l1 = new LightDescription( Color( 0, 0, 0 ) ) {
        def createLight : Light = {
          null
        }
      }

      val l2 = new LightDescription( Color( 0, 0, 0 ) ){

        def createLight : Light = {
          null
        }
      }

      val s : Set[LightDescription] = l1 + l2

      assert( s.contains( l1 ))
      assert( s.contains( l2 ))

    }

  }

}
