/*
 * Copyright 2016 Stephan Rehfeld
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

package test.scaladelray.rendering.recursiveraytracing.light

import org.scalatest.FunSpec

import scaladelray.Color
import scaladelray.geometry.Sphere
import scaladelray.material.Material
import scaladelray.math.d.Point3
import scaladelray.math.{Ray, Transform}
import scaladelray.rendering.recursiveraytracing.light.PointLight
import scaladelray.rendering.{Hit, Renderable}
import scaladelray.world.{SingleBackgroundColor, World}

/**
  * Created by Stephan Rehfeld on 09.03.2016.
  */
class PointLightSpec extends FunSpec {

  describe( "A PointLight" ) {
    it( "should radiate in all directions." ) {

      val r = Renderable( Transform(), Sphere( None ), Material( None ) )


      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() )
      val l = PointLight( r, Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )

      val points = Point3( 1, 0, 0 ) :: Point3( 0, 1, 0 ) :: Point3( 0, 0, 1 ) :: Point3( -1, 0, 0 ) :: Point3( 0, -1, 0 ) :: Point3( 0, 0, -1 ) :: Nil

      for( p <- points )
        assert( l.illuminates( p, w ) )

    }

    it( "should check the world if an object is between the point and the point light" ) {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      var called = false

      val w = new World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set[Renderable]() ) {
        override def <--( r : Ray ) : Set[Hit] = {
          called = true
          Set[Hit]()
        }
      }
      val l = PointLight( r, Color( 1, 1, 1 ), Point3( 0, 0, 0 ) )
      l.illuminates( Point3( 3, 3, 3 ), w )
      assert( called )
    }

    it( "should return false if an object is between the point and the light" ) {
      val r = Renderable( Transform().scale( 0.1, 0.1, 0.1 ), Sphere( None ), Material( None ) )

      val l = PointLight( r, Color( 1, 1, 1 ), Point3( 0, 0, -2 ) )
      val s = Sphere( None )
      val p = Point3( 0, 0, 2 )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() + Renderable( Transform(), s, Material( None ) ) )

      assert( !l.illuminates( p, w ) )
    }

    it( "should return true if an object is between the point and the light, but if that object is the renderable of the light" ) {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      val l = PointLight( r, Color( 1, 1, 1 ), Point3( 0, 0, -2 ) )
      val s = Sphere( None )
      val p = Point3( 0, 0, 2 )
      val w = World( SingleBackgroundColor( Color( 0, 0, 0 ) ), Set() + r )

      assert( l.illuminates( p, w ) )
    }

    it( "should calculate the constant attenuation correctly") {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      val pl = Point3( 0, 0, 0 )
      val p = Point3( 1, 0, 0 )

      val l = PointLight( r, Color( 1, 1, 1 ), pl )

      assert( l.intensity( p ) == 1 )

    }


    it( "should calculate the linear attenuation correctly") {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = PointLight( r, Color( 1, 1, 1 ), pl, 0, 0.5 )

      assert( l.intensity( p ) == 1 / (2*0.5) )
    }

    it( "should calculate the quadratic attenuation correctly") {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )

      val pl = Point3( 0, 0, 0 )
      val p = Point3( 2, 0, 0 )

      val l = PointLight( r, Color( 1, 1, 1 ), pl, 0, 0, 0.5 )

      assert( l.intensity( p ) == 1/(2*2*0.5) )

    }

    it( "should calculate the direction correctly") {
      val r = Renderable( Transform(), Sphere( None ), Material( None ) )


      val pl = Point3( 0, 0, 0 )
      val p = Point3( 3, -2, -4 )

      val d = (pl - p).normalized

      val l = PointLight( r, Color( 1, 1, 1 ), pl )

      assert( l.directionFrom( p ) == d )
    }
  }

}
