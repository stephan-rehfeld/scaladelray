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
import scaladelray.material.Material
import scaladelray.geometry.{Hit, Geometry}
import scaladelray.math.Ray
import scaladelray.{Color, World}

class GeometryTestAdapter( material : Material ) extends Geometry( material ) {

  override def <--(r: Ray): Set[Hit] = throw new UnsupportedOperationException( "Just a test adapter! Don't call this method!" )

}

// TODO: Move this class the materials as soon as you implemented the tests for the Material class.
case class MaterialTestAdapter() extends Material {
  def colorFor(hit: Hit, world: World, tracer: (Ray, World) => Color): Color = throw new UnsupportedOperationException( "Just a test adapter! Don't call this method!" )
}

class GeometrySpec extends FunSpec {

  describe( "A geometry" ) {
    it( "should save the material of the geometry" ) {
      val m = MaterialTestAdapter()
      val g = new GeometryTestAdapter( m )

      assert( g.material == m )
    }
    it( "should have a +-operator to construct a set out of two geometries") {
      val m1 = MaterialTestAdapter()
      val g1 = new GeometryTestAdapter( m1 )

      val m2 = MaterialTestAdapter()
      val g2 = new GeometryTestAdapter( m2 )

      val s = g1 + g2

      assert( s.size == 2 )
    }
  }

}
