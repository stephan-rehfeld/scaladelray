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

package test.scaladelray.world

import org.scalatest.FunSpec
import scaladelray.texture.{TexCoord2D, Texture}
import scaladelray.Color
import scaladelray.world.Skybox
import scaladelray.math.{Vector3, Point3, Ray}


class SkyboxSpec extends FunSpec {

  case class TextureTestAdapter() extends Texture {

    var called = false
    var v: Option[TexCoord2D] = None

    override def apply( texCoord : TexCoord2D ) = {
      called = true
      v = Some( texCoord )
      Color( 0, 0, 0 )
    }
  }

  describe( "A Skybox" ) {
    it( "should request the color from the front texture if the ray primarily directs along the negative z-axis" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, 0.75, -1 ).normalized )
      s( r )
      assert( front.called )
      assert( !back.called )
      assert( !left.called )
      assert( !right.called )
      assert( !top.called )
      assert( !bottom.called )
    }

    it( "should request the color from the back texture if the ray primarily directs along the positive z-axis" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, 0.75, 1 ).normalized )
      s( r )
      assert( !front.called )
      assert( back.called )
      assert( !left.called )
      assert( !right.called )
      assert( !top.called )
      assert( !bottom.called )
    }

    it( "should request the color from the left texture if the ray primarily directs along the negative x-axis" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0.75, 0.5 ).normalized )
      s( r )
      assert( !front.called )
      assert( !back.called )
      assert( left.called )
      assert( !right.called )
      assert( !top.called )
      assert( !bottom.called )
    }

    it( "should request the color from the right texture if the ray primarily directs along the positive x-axis" )  {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0.75, 0.5 ).normalized )

      s( r )
      assert( !front.called )
      assert( !back.called )
      assert( !left.called )
      assert( right.called )
      assert( !top.called )
      assert( !bottom.called )
    }

    it( "should request the color from the top texture if the ray primarily directs along the positive y-axis" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.75, 1, 0.5 ).normalized )

      s( r )
      assert( !front.called )
      assert( !back.called )
      assert( !left.called )
      assert( !right.called )
      assert( top.called )
      assert( !bottom.called )
    }
    it( "should request the color from the bottom texture if the ray primarily directs along the negative y-axis" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.75, -1, 0.5 ).normalized )

      s( r )
      assert( !front.called )
      assert( !back.called )
      assert( !left.called )
      assert( !right.called )
      assert( !top.called )
      assert( bottom.called )
    }

    it( "should request the color at the coordinate 0.5/0.5 from the front texture when the direction of the ray is -z " ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, -1 ) )

      s( r )
      assert( front.v.isDefined )
      assert( front.v.get == TexCoord2D( 0.5, 0.5 ) )
    }

    it( "should request the color at the coordinate 0.5/0.5 from the back texture when the direction of the ray is z " ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, 1 ) )

      s( r )
      assert( back.v.isDefined )
      assert( back.v.get == TexCoord2D( 0.5, 0.5 ) )
    }

    it( "should request the color at the coordinate 0.5/0.5 from the left texture when the direction of the ray is -x " ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0, 0 ) )

      s( r )
      assert( left.v.isDefined )
      assert( left.v.get == TexCoord2D( 0.5, 0.5 ) )
    }

    it( "should request the color at the coordinate 0.5/0.5 from the right texture when the direction of the ray is x " ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0, 0 ) )

      s( r )
      assert( right.v.isDefined )
      assert( right.v.get == TexCoord2D( 0.5, 0.5 ) )
    }

    it( "should request the color at the coordinate 0.5/0.5 from the top texture when the direction of the ray is y " ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 1, 0 ) )

      s( r )
      assert( top.v.isDefined )
      assert( top.v.get == TexCoord2D( 0.5, 0.5 ) )
    }

    it( "should request the color at the coordinate 0.5/0.5 from the bottom texture when the direction of the ray is -y " ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, -1, 0 ) )

      s( r )
      assert( bottom.v.isDefined )
      assert( bottom.v.get == TexCoord2D( 0.5, 0.5 ) )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.5 < v <= 1.0 when the ray directs to the upper left side of the front texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, 0.5, -1 ).normalized )

      s( r )
      assert( front.v.isDefined )
      assert( 0.0 <= front.v.get.u && front.v.get.u < 0.5 )
      assert( 0.5 < front.v.get.v && front.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.5 < v <= 1.0 when the ray directs to the upper right side of the front texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, 0.5, -1 ).normalized )

      s( r )
      assert( front.v.isDefined )
      assert( 0.5 < front.v.get.u && front.v.get.u <= 1.0 )
      assert( 0.5 < front.v.get.v && front.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.0 <= v < 0.5 when the ray directs to the lower left side of the front texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, -0.5, -1 ).normalized )

      s( r )
      assert( front.v.isDefined )
      assert( 0.0 <= front.v.get.u && front.v.get.u < 0.5 )
      assert( 0.0 <= front.v.get.v && front.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.0 <= v < 0.5 when the ray directs to the lower right side of the front texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, -0.5, -1 ).normalized )

      s( r )
      assert( front.v.isDefined )
      assert( 0.5 < front.v.get.u && front.v.get.u <= 1.0 )
      assert( 0.0 <= front.v.get.v && front.v.get.v < 0.5 )
    }



        it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.5 < v <= 1.0 when the ray directs to the upper left side of the back texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, 0.5, 1 ).normalized )

      s( r )
      assert( back.v.isDefined )
      assert( 0.0 <= back.v.get.u && back.v.get.u < 0.5 )
      assert( 0.5 < back.v.get.v && back.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.5 < v <= 1.0 when the ray directs to the upper right side of the back texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, 0.5, 1 ).normalized )

      s( r )
      assert( back.v.isDefined )
      assert( 0.5 < back.v.get.u && back.v.get.u <= 1.0 )
      assert( 0.5 < back.v.get.v && back.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.0 <= v < 0.5 when the ray directs to the lower left side of the back texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, -0.5, 1 ).normalized )

      s( r )
      assert( back.v.isDefined )
      assert( 0.0 <= back.v.get.u && back.v.get.u < 0.5 )
      assert( 0.0 <= back.v.get.v && back.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.0 <= v < 0.5 when the ray directs to the lower right side of the back texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, -0.5, 1 ).normalized )

      s( r )
      assert( back.v.isDefined )
      assert( 0.5 < back.v.get.u && back.v.get.u <= 1.0 )
      assert( 0.0 <= back.v.get.v && back.v.get.v < 0.5 )
    }


    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.5 < v <= 1.0 when the ray directs to the upper left side of the left texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0.5, 0.5 ).normalized )

      s( r )
      assert( left.v.isDefined )
      assert( 0.0 <= left.v.get.u && left.v.get.u < 0.5 )
      assert( 0.5 < left.v.get.v && left.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.5 < v <= 1.0 when the ray directs to the upper right side of the left texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, 0.5, -0.5 ).normalized )

      s( r )
      assert( left.v.isDefined )
      assert( 0.5 < left.v.get.u && left.v.get.u <= 1.0 )
      assert( 0.5 < left.v.get.v && left.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.0 <= v < 0.5 when the ray directs to the lower left side of the left texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, -0.5, 0.5 ).normalized )

      s( r )
      assert( left.v.isDefined )
      assert( 0.0 <= left.v.get.u && left.v.get.u < 0.5 )
      assert( 0.0 <= left.v.get.v && left.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.0 <= v < 0.5 when the ray directs to the lower right side of the left texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -1, -0.5, -0.5 ).normalized )

      s( r )
      assert( left.v.isDefined )
      assert( 0.5 < left.v.get.u && left.v.get.u <= 1.0 )
      assert( 0.0 <= left.v.get.v && left.v.get.v < 0.5 )
    }


    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.5 < v <= 1.0 when the ray directs to the upper left side of the right texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0.5, -0.5 ).normalized )

      s( r )
      assert( right.v.isDefined )
      assert( 0.0 <= right.v.get.u && right.v.get.u < 0.5 )
      assert( 0.5 < right.v.get.v && right.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.5 < v <= 1.0 when the ray directs to the upper right side of the right texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, 0.5, 0.5 ).normalized )

      s( r )
      assert( right.v.isDefined )
      assert( 0.5 < right.v.get.u && right.v.get.u <= 1.0 )
      assert( 0.5 < right.v.get.v && right.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.0 <= v < 0.5 when the ray directs to the lower left side of the right texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, -0.5, -0.5 ).normalized )

      s( r )
      assert( right.v.isDefined )
      assert( 0.0 <= right.v.get.u && right.v.get.u < 0.5 )
      assert( 0.0 <= right.v.get.v && right.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.0 <= v < 0.5 when the ray directs to the lower right side of the right texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 1, -0.5, 0.5 ).normalized )

      s( r )
      assert( right.v.isDefined )
      assert( 0.5 < right.v.get.u && right.v.get.u <= 1.0 )
      assert( 0.0 <= right.v.get.v && right.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.5 < v <= 1.0 when the ray directs to the upper left side of the top texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, 1, 0.5 ).normalized )

      s( r )
      assert( top.v.isDefined )
      assert( 0.0 <= top.v.get.u && top.v.get.u < 0.5 )
      assert( 0.5 < top.v.get.v && top.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.5 < v <= 1.0 when the ray directs to the upper right side of the top texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, 1, 0.5 ).normalized )

      s( r )
      assert( top.v.isDefined )
      assert( 0.5 < top.v.get.u && top.v.get.u <= 1.0 )
      assert( 0.5 < top.v.get.v && top.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.0 <= v < 0.5 when the ray directs to the lower left side of the top texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, 1, -0.5 ).normalized )

      s( r )
      assert( top.v.isDefined )
      assert( 0.0 <= top.v.get.u && top.v.get.u < 0.5 )
      assert( 0.0 <= top.v.get.v && top.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.0 <= v < 0.5 when the ray directs to the lower right side of the top texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, 1, -0.5 ).normalized )

      s( r )
      assert( top.v.isDefined )
      assert( 0.5 < top.v.get.u && top.v.get.u <= 1.0 )
      assert( 0.0 <= top.v.get.v && top.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.5 < v <= 1.0 when the ray directs to the upper left side of the bottom texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, -1, -0.5 ).normalized )

      s( r )
      assert( bottom.v.isDefined )
      assert( 0.0 <= bottom.v.get.u && bottom.v.get.u < 0.5 )
      assert( 0.5 < bottom.v.get.v && bottom.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.5 < v <= 1.0 when the ray directs to the upper right side of the bottom texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, -1, -0.5 ).normalized )

      s( r )
      assert( bottom.v.isDefined )
      assert( 0.5 < bottom.v.get.u && bottom.v.get.u <= 1.0 )
      assert( 0.5 < bottom.v.get.v && bottom.v.get.v <= 1.0 )
    }

    it( "should request the color for a texture coordinate where 0.0 <= u < 0.5 and 0.0 <= v < 0.5 when the ray directs to the lower left side of the bottom texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( -0.5, -1, 0.5 ).normalized )

      s( r )
      assert( bottom.v.isDefined )
      assert( 0.0 <= bottom.v.get.u && bottom.v.get.u < 0.5 )
      assert( 0.0 <= bottom.v.get.v && bottom.v.get.v < 0.5 )
    }

    it( "should request the color for a texture coordinate where 0.5 < u <= 1.0 and 0.0 <= v < 0.5 when the ray directs to the lower right side of the bottom texture" ) {
      val front = TextureTestAdapter()
      val back = TextureTestAdapter()
      val left = TextureTestAdapter()
      val right = TextureTestAdapter()
      val top = TextureTestAdapter()
      val bottom = TextureTestAdapter()

      val s = Skybox( front, back, left, right, top, bottom )
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0.5, -1, 0.5 ).normalized )

      s( r )
      assert( bottom.v.isDefined )
      assert( 0.5 < bottom.v.get.u && bottom.v.get.u <= 1.0 )
      assert( 0.0 <= bottom.v.get.v && bottom.v.get.v < 0.5 )
    }

  }

}
