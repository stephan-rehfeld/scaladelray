package test.scaladelray.geometry

import org.scalatest.FunSpec
import scaladelray.math.{Ray, Point3, Vector3}
import scaladelray.geometry.Plane

class PlaneSpec extends FunSpec {

  describe( "A Plane" ) {
    it( "should return one hit for a ray that comes from above the plane and directs downward" ) {
      val r = Ray( Point3( 0, 1, 0 ), Vector3( 0, -1, 0 ) )
      val p = new Plane( MaterialTestAdapter() )

      val hits = r --> p
      assert( hits.size == 1 )
      assert( hits.head.t == 1.0 )
    }

    it( "should return no hit for a ray that's direction is parallel to the plane but has the origin outside the plane" ) {
      val r = Ray( Point3( 0, 1, 0 ), Vector3( 0, 0, 1 ) )
      val p = new Plane( MaterialTestAdapter() )

      val hits = r --> p
      assert( hits.size == 0 )
    }
    it( "should return no hit for a ray that's direction is parallel to the plane and has the origin on the plane" ) {
      val r = Ray( Point3( 0, 0, 0 ), Vector3( 0, 0, 1 ) )
      val p = new Plane( MaterialTestAdapter() )

      val hits = r --> p
      assert( hits.size == 0 )
    }
  }

}
