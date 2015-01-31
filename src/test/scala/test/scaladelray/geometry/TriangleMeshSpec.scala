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

package test.scaladelray.geometry

import org.scalatest.FunSpec

class TriangleMeshSpec extends FunSpec  {

  describe( "A TriangleMesh" ) {
    it( "should return a hit with original normal and texture coordinate when hit on a vertex." ) (pending)
    it( "should return interpolated normals and texture coordinates when hit an edge." ) (pending)
    it( "should return interpolated normals and texture coordinates when hit on the face." ) (pending)
    it( "should return nothing when missed by the ray." ) (pending)
    it( "should create the octree correctly" ) (pending)
    it( "should hit return the correct hit when it contains a triangle" ) (pending)
  }

}
