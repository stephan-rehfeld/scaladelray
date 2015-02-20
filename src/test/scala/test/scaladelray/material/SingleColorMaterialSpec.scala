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

package test.scaladelray.material

import org.scalatest.FunSpec
import scaladelray.math._
import scaladelray.rendering.{Hit, Renderable}
import test.scaladelray.geometry.GeometryTestAdapter
import scaladelray.material.SingleColorMaterial
import scaladelray.math.Vector3
import scaladelray.math.Point3
import scaladelray.math.Ray
import scaladelray.Color
import scaladelray.texture.TexCoord2D
import scaladelray.math.Normal3
import scaladelray.world.{SingleBackgroundColor, World}

class SingleColorMaterialSpec extends FunSpec {

  describe( "A SingleColorMaterial" ) {
    it( "should return the color that has been passed to the constructor" ) {
      val m = SingleColorMaterial( Color( 1, 1, 1 ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1, Normal3( 0, 1, 0 ), tc )

      var called = false

      val tracer = ( r: Ray, w : World) => {
        called = true
        Color( 0, 0, 0 )
      }

      assert( m.colorFor( h, w, tracer ) == Color( 1, 1, 1 ) )
    }

    it( "should not call the tracer" ) {
      val m = SingleColorMaterial( Color( 1, 1, 1 ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1, Normal3( 0, 1, 0 ), tc )

      var called = false

      val tracer = ( r: Ray, w : World) => {
        called = true
        Color( 0, 0, 0 )
      }

      m.colorFor( h, w, tracer )

      assert( !called )
    }


    it( "should not request the lights from the world" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 0 ) :: Nil
      val intensityData = 1.0 :: Nil

      val m = SingleColorMaterial( Color( 1, 1, 1 ) )

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1, Normal3( 0, 1, 0 ), tc )

      m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( !l.createLightCalled )
      assert( l.directionPoint.isEmpty )
      assert( l.illuminatesPoint.isEmpty )
      assert( l.illuminatesWorld.isEmpty )
      assert( l.intensityPoint.isEmpty )
    }

  }

}
