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
import scaladelray.light.PointLight
import test.scaladelray.geometry.GeometryTestAdapter
import scaladelray.math.Vector3
import scaladelray.math.Point3
import scaladelray.math.Ray
import scaladelray.material.PhongOldMaterial
import scaladelray.texture.SingleColorTexture
import scaladelray.Color
import scaladelray.texture.TexCoord2D
import scaladelray.math.Normal3
import scaladelray.rendering.{Hit, Renderable}
import scaladelray.world.{SingleBackgroundColor, World}
import scaladelray.geometry.SurfacePoint

class PhongOldMaterialSpec extends FunSpec {

  describe( "A PhongOldMaterial" ) {
    it( "should retrieve to color from the texture, using texture coordinate in the hit" ) {
      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( t1.coordinates.isDefined )
      assert( t1.coordinates.get == tc )

      assert( t2.coordinates.isDefined )
      assert( t2.coordinates.get == tc )
    }

    it( "should not call the tracer" ) {
      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      var called = false

      val tracer = ( r: Ray, w : World) => {
        called = true
        Color( 0, 0, 0 )
      }

      m.colorFor( h, w, tracer )

      assert( !called )
    }

    it( "should call createLight of the light" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 0 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l1 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )
      val l2 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( l1.createLightCalled )
      assert( l2.createLightCalled )
    }

    it( "should request if a light hits a point" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 0 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l1 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )
      val l2 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( l1.illuminatesPoint.isDefined )
      assert( l1.illuminatesPoint.get == r( h.t ) )
      assert( l1.illuminatesWorld.isDefined )
      assert( l1.illuminatesWorld.get == w )

      assert( l2.illuminatesPoint.isDefined )
      assert( l2.illuminatesPoint.get == r( h.t ) )
      assert( l2.illuminatesWorld.isDefined )
      assert( l2.illuminatesWorld.get == w )
    }

    it( "should request the direction to the light" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 0 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l1 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )
      val l2 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )


      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( l1.directionPoint.isDefined )
      assert( l1.directionPoint.get == r( h.t ) )

      assert( l2.directionPoint.isDefined )
      assert( l2.directionPoint.get == r( h.t ) )
    }

    it( "should request the intensity of the light" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 0 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l1 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )
      val l2 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( l1.intensityPoint.isDefined )
      assert( l1.intensityPoint.get == r( h.t ) )

      assert( l2.intensityPoint.isDefined )
      assert( l2.intensityPoint.get == r( h.t ) )
    }

    it( "should use the color returned by the texture to calculate to color at the point on the surface" ) {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,  SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Vector3( 1, 0, 0 ), Vector3( 0, 1, 0 ), tc ) )

      assert( m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) ) == Color( 1, 1, 0 ) )
    }

    it( "should use the normal of the hit to calculate the color" ) {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )

      val h = Hit( r, Renderable( Transform(), g, m ), 1, SurfacePoint( r( 1 ), Vector3( 0, 1, 1 ).normalized.asNormal, Vector3( 1, -1, 0 ).normalized, Vector3( 0, 0, -1 ), tc ) )

      assert( m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) ) == Color( 1 * Math.cos( Math.PI / 4 ) , Vector3( 0, 0, 1 ).reflectOn( h.sp.n ) dot -r.d , 0 ) )
    }

    it( "should use the information if the light illuminates the surface to calculate the color" ) {
      val illuminatesData = false :: Nil
      val directionFromData = Vector3( 0, 0, 1 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l  )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )

      val h = Hit( r, Renderable( Transform(), g, m ), 1,       SurfacePoint( r( 1 ),Normal3( 0, 0, 1 ), Vector3( 0, -1, 0 ), Vector3( 0, 0, -1 ), tc ) )

      assert( m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) ) == Color( 0, 0, 0 ) )
    }

    it( "should use the intensity returned by the light to calculate to color" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 0, 1 ) :: Nil
      val intensityData = 0.5 :: Nil

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l  )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,       SurfacePoint( r( 1 ),Normal3( 0, 0, 1 ), Vector3( 0, -1, 0 ), Vector3( 0, 0, -1 ), tc ) )

      assert( m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) ) == Color( 0.5, 0.5, 0.0 ) )
    }

    it( "should use the direction returned by the light to calculate to color" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 1 ).normalized :: Nil
      val intensityData = 1.0 :: Nil

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val m = PhongOldMaterial( t1, t2, 1 )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l  )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, m ), 1,       SurfacePoint( r( 1 ),Normal3( 0, 0, 1 ), Vector3( 0, -1, 0 ), Vector3( 0, 0, -1 ), tc ) )

      assert( m.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) ) == Color( 1, 1, 0 ) * Math.cos( Math.PI / 4 ) )
    }

  }

}
