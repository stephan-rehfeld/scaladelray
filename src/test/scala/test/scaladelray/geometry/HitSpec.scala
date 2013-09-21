package test.scaladelray.geometry

import org.scalatest.FunSpec
import scaladelray.math.{Normal3, Ray, Point3, Vector3}
import scaladelray.geometry.{Hit, Geometry}
import scaladelray.texture.TexCoord2D
import scaladelray.material.Material
import scaladelray.{Color, World}

class HitSpec extends FunSpec {

  describe( "A Hit" ) {
    it( "should consume a ray, geometry, t, normal, and texture coordinate as constructor parameter and provider them as value") {
      val r = Ray( Point3( 3, 5, 7 ), Vector3( 11, 13, 17 ) )
      val g = new Geometry( new Material() {
        def colorFor(hit: Hit, world: World, tracer: (Ray, World) => Color): Color = Color( 0, 0, 0 )
      }) {
        def <--(r: Ray): Set[Hit] =
          null
      }
      val t = 8.15
      val n = Normal3( 3, 5, 7 )
      val tc = TexCoord2D( 2, 3 )

      val hit = Hit( r, g, t, n ,tc )

      assert( hit.ray == r )
      assert( hit.geometry == g )
      assert( hit.t == t )
      assert( hit.n == n )
      assert( hit.texCoord2D == tc )

    }
  }

}
