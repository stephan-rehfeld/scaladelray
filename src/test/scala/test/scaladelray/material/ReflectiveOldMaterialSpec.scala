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
import scaladelray.material._
import test.scaladelray.geometry.GeometryTestAdapter
import scaladelray.math.Vector3
import scaladelray.math.Point3
import scaladelray.math.Ray
import scaladelray.world.SingleBackgroundColor
import scaladelray.material.ReflectiveOldMaterial
import scaladelray.texture.TexCoord2D
import scaladelray.rendering.Hit
import scaladelray.geometry.SurfacePoint
import scaladelray.material.LambertBRDF
import scaladelray.world.World
import scaladelray.material.PhongSpecularBRDF
import scaladelray.material.Material
import scaladelray.rendering.Renderable
import scaladelray.texture.SingleColorTexture
import scaladelray.Color
import scaladelray.math.Normal3
import scaladelray.light.PointLight

class ReflectiveOldMaterialSpec extends FunSpec {

  describe( "A ReflectiveOldMaterial" ) {

    it( "should retrieve to color from the texture, using texture coordinate in the hit" ) {
      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t3 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )

      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      o.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( t1.coordinates.isDefined )
      assert( t1.coordinates.get == tc )

      assert( t2.coordinates.isDefined )
      assert( t2.coordinates.get == tc )

      assert( t3.coordinates.isDefined )
      assert( t3.coordinates.get == tc )
    }

    it( "should call the tracer with the reflected ray" ) {
      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t3 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )


      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Vector3( 0, 1, 1 ).normalized.asNormal, Vector3( 1, -1, 0 ).normalized, Vector3( 0, 0, -1 ), tc ) )

      var called = false

      val tracer = ( r: Ray, w : World) => {
        assert( r.o == Point3( 0, 0, -1 ) )
        assert( r.d =~= Vector3( 0, 1, 0 ) )
        called = true
        Color( 0, 0, 0 )
      }

      o.colorFor( h, w, tracer )

      assert( called )
    }

    it( "should call createLight of the light" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 0 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l1 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )
      val l2 = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t2 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val t3 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )


      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      o.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

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
      val t3 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )


      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      o.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

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
      val t3 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      o.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

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
      val t3 = new TextureTestAdapter( Color( 0, 0, 0 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), l1 + l2 )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 1, 0 ), Vector3( 1, 0, 0 ), Vector3( 0, 0, -1 ), tc ) )

      o.colorFor( h, w, (_,_) => Color( 0, 0, 0 ) )

      assert( l1.intensityPoint.isDefined )
      assert( l1.intensityPoint.get == r( h.t ) )

      assert( l2.intensityPoint.isDefined )
      assert( l2.intensityPoint.get == r( h.t ) )
    }

    it( "should use the color returned by the texture to calculate to color at the point on the surface" ) {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val t3 = new SingleColorTexture( Color( 0, 0, 1 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Vector3( 1, 0, 0 ).normalized, Vector3( 0, 1, 0 ), tc )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Vector3( 1, 0, 0 ).normalized, Vector3( 0, 1, 0 ), tc ) )

      assert( o.colorFor( h, w, (_,_) => Color( 1, 1, 1 ) ) == Color( 1, 1, 1 ) )
    }

    it( "should use the normal of the hit to calculate the color" ) {
      val l = new PointLight( Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val t3 = new SingleColorTexture( Color( 0, 0, 1 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Vector3( 0, 1, 1 ).normalized.asNormal, Vector3( 1, -1, 0 ).normalized, Vector3( 0, 0, -1 ), tc ) )

      assert( o.colorFor( h, w, (_,_) => Color( 1, 1, 1 ) ) == Color( 1 * Math.cos( Math.PI / 4 ) , Vector3( 0, 0, 1 ).reflectOn( h.sp.n ) dot -r.d , 1 ) )
    }

    it( "should use the information if the light illuminates the surface to calculate the color" ) {
      val illuminatesData = false :: Nil
      val directionFromData = Vector3( 0, 0, 1 ) :: Nil
      val intensityData = 1.0 :: Nil

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val t3 = new SingleColorTexture( Color( 0, 0, 1 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l  )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )

      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Vector3( 1, 0, 0 ).normalized, Vector3( 0, 1, 0 ), tc ) )

      assert( o.colorFor( h, w, (_,_) => Color( 1, 1, 1 ) ) == Color( 0, 0, 1 ) )
    }

    it( "should use the intensity returned by the light to calculate to color" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 0, 1 ) :: Nil
      val intensityData = 0.5 :: Nil

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val t3 = new SingleColorTexture( Color( 0, 0, 1 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l  )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Vector3( 1, 0, 0 ).normalized, Vector3( 0, 1, 0 ), tc ) )

      assert( o.colorFor( h, w, (_,_) => Color( 1, 1, 1 ) ) == Color( 0.5, 0.5, 1 ) )
    }

    it( "should use the direction returned by the light to calculate to color" ) {
      val illuminatesData = true :: Nil
      val directionFromData = Vector3( 0, 1, 1 ).normalized :: Nil
      val intensityData = 1.0 :: Nil

      val l = new LightTestAdapter( illuminatesData, directionFromData, intensityData )

      val t1 = new SingleColorTexture( Color( 1, 0, 0 ) )
      val t2 = new SingleColorTexture( Color( 0, 1, 0 ) )
      val t3 = new SingleColorTexture( Color( 0, 0, 1 ) )
      val o = ReflectiveOldMaterial( t1, t2, 1, t3 )
      val m = Material( None, (1.0/3.0, t1, LambertBRDF() ), (1.0/3.0, t2, PhongSpecularBRDF( 1 ) ), (1.0/3.0,t3, PerfectReflectiveBRDF() ) )

      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set(), Color( 0, 0, 0 ), Set() + l  )
      val r = Ray( Point3(0,0,0), Vector3( 0, 0, -1 ) )
      val g = new GeometryTestAdapter
      val tc = TexCoord2D( 1.0, 1.0 )
      val h = Hit( r, Renderable( Transform(), g, o, m ), 1, SurfacePoint( r( 1 ), Normal3( 0, 0, 1 ), Vector3( 1, 0, 0 ).normalized, Vector3( 0, 1, 0 ), tc ) )

      assert( o.colorFor( h, w, (_,_) => Color( 1, 1, 1 ) ) =~= Color( 1 * Math.cos( Math.PI / 4 ), 1 * Math.cos( Math.PI / 4 ), 1 )  )
    }

  }

}