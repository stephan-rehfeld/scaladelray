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

package test.scaladelray.loader

import org.scalatest.FunSpec
import scaladelray.loader.ResourceManager
import scaladelray.material.SingleColorOldMaterial
import scaladelray.Color
import scaladelray.math.{Normal3, Point3}
import scaladelray.texture.TexCoord2D
import scaladelray.geometry.TriangleMesh


class ResourceManagerSpec extends FunSpec  {

  val v1 = Point3( -0.5, -0.5, 0.5  )
  val v2 = Point3( 0.5, -0.5, 0.5  )
  val v3 = Point3( 0.5, 0.5, 0.5  )
  val v4 = Point3( -0.5, 0.5, 0.5  )
  val v5 = Point3( -0.5, -0.5, -0.5  )
  val v6 = Point3( 0.5, -0.5, -0.5  )
  val v7 = Point3( 0.5, 0.5, -0.5  )
  val v8 = Point3( -0.5, 0.5, -0.5  )

  val n1 = Normal3( 1.0, 0.0, 0.0 )
  val n2 = Normal3( -1.0, 0.0, 0.0 )
  val n3 = Normal3( 0.0, 1.0, 0.0 )
  val n4 = Normal3( 0.0, -1.0, 0.0 )
  val n5 = Normal3( 0.0, 0.0, 1.0 )
  val n6 = Normal3( 0.0, 0.0, -1.0 )

  val t1 = TexCoord2D( 0.0, 0.0 )
  val t2 = TexCoord2D( 1.0, 0.0 )
  val t3 = TexCoord2D( 1.0, 1.0 )
  val t4 = TexCoord2D( 0.0, 1.0 )

  describe( "A ResourceManager" ) {
    it( "should be able to load a OBJ file") {
      val e = ResourceManager.load( "cube-v-vt-vn.obj", SingleColorOldMaterial( Color( 0, 0, 0 ) ), (recursions,faces) => recursions < 0, false )
      assert( e.isLeft )
      val g = e.left.get
      assert( g.isInstanceOf[TriangleMesh] )

      val mesh = g.asInstanceOf[TriangleMesh]

      assert( mesh.vertices.contains( v1 ) )
      assert( mesh.vertices.contains( v2 ) )
      assert( mesh.vertices.contains( v3 ) )
      assert( mesh.vertices.contains( v4 ) )

      assert( mesh.vertices.contains( v5 ) )
      assert( mesh.vertices.contains( v6 ) )
      assert( mesh.vertices.contains( v7 ) )
      assert( mesh.vertices.contains( v8 ) )

      assert( mesh.texCoords.contains( t1 ) )
      assert( mesh.texCoords.contains( t2 ) )
      assert( mesh.texCoords.contains( t3 ) )
      assert( mesh.texCoords.contains( t4 ) )

      assert( mesh.normals.contains( n1 ) )
      assert( mesh.normals.contains( n2 ) )
      assert( mesh.normals.contains( n3 ) )
      assert( mesh.normals.contains( n4 ) )
      assert( mesh.normals.contains( n5 ) )
      assert( mesh.normals.contains( n6 ) )

      val triangleSets = for( face <- mesh.faces ) yield
        (mesh.vertices( face( 0 )._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil, mesh.texCoords( face( 0 )._2.get ) :: mesh.texCoords( face( 1 )._2.get ) :: mesh.texCoords( face( 2 )._2.get ) :: Nil, mesh.normals( face( 0 )._3.get ) :: mesh.normals( face( 1 )._3.get ) :: mesh.normals( face( 2 )._3.get ) :: Nil  )

      assert( triangleSets.contains( (v1 :: v2 :: v3 :: Nil, t1 :: t2 :: t3 :: Nil, n5 :: n5 :: n5 :: Nil ) ) )
      assert( triangleSets.contains( (v3 :: v4 :: v1 :: Nil, t3 :: t4 :: t1 :: Nil, n5 :: n5 :: n5 :: Nil ) ) )
      assert( triangleSets.contains( (v2 :: v6 :: v7 :: Nil, t1 :: t2 :: t3 :: Nil, n1 :: n1 :: n1 :: Nil ) ) )
      assert( triangleSets.contains( (v7 :: v3 :: v2 :: Nil, t3 :: t4 :: t1 :: Nil, n1 :: n1 :: n1 :: Nil ) ) )
      assert( triangleSets.contains( (v6 :: v5 :: v8 :: Nil, t1 :: t2 :: t3 :: Nil, n6 :: n6 :: n6 :: Nil ) ) )
      assert( triangleSets.contains( (v8 :: v7 :: v6 :: Nil, t3 :: t4 :: t1 :: Nil, n6 :: n6 :: n6 :: Nil ) ) )
      assert( triangleSets.contains( (v5 :: v1 :: v4 :: Nil, t1 :: t2 :: t3 :: Nil, n2 :: n2 :: n2 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v8 :: v5 :: Nil, t3 :: t4 :: t1 :: Nil, n2 :: n2 :: n2 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v8 :: v5 :: Nil, t3 :: t4 :: t1 :: Nil, n2 :: n2 :: n2 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v3 :: v7 :: Nil, t1 :: t2 :: t3 :: Nil, n3 :: n3 :: n3 :: Nil ) ) )
      assert( triangleSets.contains( (v7 :: v8 :: v4 :: Nil, t3 :: t4 :: t1 :: Nil, n3 :: n3 :: n3 :: Nil ) ) )
      assert( triangleSets.contains( (v5 :: v6 :: v2 :: Nil, t1 :: t2 :: t3 :: Nil, n4 :: n4 :: n4 :: Nil ) ) )
      assert( triangleSets.contains( (v2 :: v1 :: v5 :: Nil, t3 :: t4 :: t1 :: Nil, n4 :: n4 :: n4 :: Nil ) ) )

      assert( mesh.vertices.size == 8 )
      assert( mesh.faces.size == 12 )
      assert( mesh.texCoords.size == 4 )
      assert( mesh.normals.size == 6 )

    }

    it( "should load a resource asynchronously") {
      val start = System.nanoTime()
      ResourceManager.preLoad( "assets/bunny.obj", SingleColorOldMaterial( Color( 0, 0, 0 ) ), (recursions,faces) => recursions < 0, true )
      val end = System.nanoTime()
      assert( (end - start) < 1000000000 )
    }

    it( "should cache loaded resources" ) {
      ResourceManager.preLoad( "assets/bunny.obj", SingleColorOldMaterial( Color( 0, 0, 0 ) ), (recursions,faces) => recursions < 0, true )
      Thread.sleep( 60000 )
      val start = System.nanoTime()
      val e1 = ResourceManager.load( "assets/bunny.obj", SingleColorOldMaterial( Color( 0, 0, 0 ) ), (recursions,faces) => recursions < 0, true )
      val end = System.nanoTime()
      assert( (end - start) < 1000000000 )
      assert( e1.isLeft )
      val g1 = e1.left.get


      val e2 = ResourceManager.load( "assets/bunny.obj", SingleColorOldMaterial( Color( 0, 0, 0 ) ), (recursions,faces) => recursions < 0, true )
      assert( e2.isLeft )
      val g2 = e2.left.get

      assert( g1 == g2 )
    }

    it( "should have a function to delete the cache" ) {
      ResourceManager.deleteCache()
    }
    it( "should report it if a file type is not supported support a file type" )  {
      assert( ResourceManager.load( "empty.ply", SingleColorOldMaterial( Color( 0, 0, 0 ) ), (recursions,faces) => recursions < 0, false ).isRight )
    }
  }

}
