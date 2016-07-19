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

package test.scaladelray.loader

import org.scalatest.FunSpec

import scaladelray.loader.OBJLoader
import scaladelray.math.d.{Normal3, Point3}
import scaladelray.texture.TexCoord2D

class OBJLoaderSpec extends FunSpec {

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

  describe( "The OBJLoader" ) {

    it( "should load a file that contains a cube with all vertices into a triangle mesh" ) {

      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v.obj", (recursions,faces) => recursions < 0, false )

      assert( mesh.vertices.contains( v1 ) )
      assert( mesh.vertices.contains( v2 ) )
      assert( mesh.vertices.contains( v3 ) )
      assert( mesh.vertices.contains( v4 ) )

      assert( mesh.vertices.contains( v5 ) )
      assert( mesh.vertices.contains( v6 ) )
      assert( mesh.vertices.contains( v7 ) )
      assert( mesh.vertices.contains( v8 ) )

      val triangleSets = for( face <- mesh.faces ) yield
        mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil

      assert( triangleSets.contains( v1 :: v2 :: v3 :: Nil ) )
      assert( triangleSets.contains( v3 :: v4 :: v1 :: Nil ) )
      assert( triangleSets.contains( v2 :: v6 :: v7 :: Nil ) )
      assert( triangleSets.contains( v7 :: v3 :: v2 :: Nil ) )
      assert( triangleSets.contains( v6 :: v5 :: v8 :: Nil ) )
      assert( triangleSets.contains( v8 :: v7 :: v6 :: Nil ) )
      assert( triangleSets.contains( v5 :: v1 :: v4 :: Nil ) )
      assert( triangleSets.contains( v4 :: v8 :: v5 :: Nil ) )
      assert( triangleSets.contains( v4 :: v3 :: v7 :: Nil ) )
      assert( triangleSets.contains( v7 :: v8 :: v4 :: Nil ) )
      assert( triangleSets.contains( v5 :: v6 :: v2 :: Nil ) )
      assert( triangleSets.contains( v2 :: v1 :: v5 :: Nil ) )

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
    }

    it( "should load a file that contains a cube with all vertices and normals into a triangle mesh" ) {
      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v-vn.obj", (recursions,faces) => recursions < 0, false )

      assert( mesh.vertices.contains( v1 ) )
      assert( mesh.vertices.contains( v2 ) )
      assert( mesh.vertices.contains( v3 ) )
      assert( mesh.vertices.contains( v4 ) )

      assert( mesh.vertices.contains( v5 ) )
      assert( mesh.vertices.contains( v6 ) )
      assert( mesh.vertices.contains( v7 ) )
      assert( mesh.vertices.contains( v8 ) )

      assert( mesh.normals.contains( n1 ) )
      assert( mesh.normals.contains( n2 ) )
      assert( mesh.normals.contains( n3 ) )
      assert( mesh.normals.contains( n4 ) )
      assert( mesh.normals.contains( n5 ) )
      assert( mesh.normals.contains( n6 ) )

      val triangleSets = for( face <- mesh.faces ) yield
        (mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil, mesh.normals( face.head._3.get ) :: mesh.normals( face( 1 )._3.get ) :: mesh.normals( face( 2 )._3.get ) :: Nil )

      assert( triangleSets.contains( (v1 :: v2 :: v3 :: Nil, n5 :: n5 :: n5 :: Nil ) ) )
      assert( triangleSets.contains( (v3 :: v4 :: v1 :: Nil, n5 :: n5 :: n5 :: Nil ) ) )
      assert( triangleSets.contains( (v2 :: v6 :: v7 :: Nil, n1 :: n1 :: n1 :: Nil ) ) )
      assert( triangleSets.contains( (v7 :: v3 :: v2 :: Nil, n1 :: n1 :: n1 :: Nil ) ) )
      assert( triangleSets.contains( (v6 :: v5 :: v8 :: Nil, n6 :: n6 :: n6 :: Nil ) ) )
      assert( triangleSets.contains( (v8 :: v7 :: v6 :: Nil, n6 :: n6 :: n6 :: Nil ) ) )
      assert( triangleSets.contains( (v5 :: v1 :: v4 :: Nil, n2 :: n2 :: n2 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v8 :: v5 :: Nil, n2 :: n2 :: n2 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v3 :: v7 :: Nil, n3 :: n3 :: n3 :: Nil ) ) )
      assert( triangleSets.contains( (v7 :: v8 :: v4 :: Nil, n3 :: n3 :: n3 :: Nil ) ) )
      assert( triangleSets.contains( (v5 :: v6 :: v2 :: Nil, n4 :: n4 :: n4 :: Nil ) ) )
      assert( triangleSets.contains( (v2 :: v1 :: v5 :: Nil, n4 :: n4 :: n4 :: Nil ) ) )

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
      assert( mesh.normals.length == 6 )
    }

    it( "should load a file that contains a cube with all vertices and texture coordinates into a triangle mesh" ) {
      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v-vt.obj", (recursions,faces) => recursions < 0, false )

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

      val triangleSets = for( face <- mesh.faces ) yield
        (mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil, mesh.texCoords( face.head._2.get ) :: mesh.texCoords( face( 1 )._2.get ) :: mesh.texCoords( face( 2 )._2.get ) :: Nil )

      assert( triangleSets.contains( (v1 :: v2 :: v3 :: Nil, t1 :: t2 :: t3 :: Nil ) ) )
      assert( triangleSets.contains( (v3 :: v4 :: v1 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )
      assert( triangleSets.contains( (v2 :: v6 :: v7 :: Nil, t1 :: t2 :: t3 :: Nil ) ) )
      assert( triangleSets.contains( (v7 :: v3 :: v2 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )
      assert( triangleSets.contains( (v6 :: v5 :: v8 :: Nil, t1 :: t2 :: t3 :: Nil ) ) )
      assert( triangleSets.contains( (v8 :: v7 :: v6 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )
      assert( triangleSets.contains( (v5 :: v1 :: v4 :: Nil, t1 :: t2 :: t3 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v8 :: v5 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v8 :: v5 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )
      assert( triangleSets.contains( (v4 :: v3 :: v7 :: Nil, t1 :: t2 :: t3 :: Nil ) ) )
      assert( triangleSets.contains( (v7 :: v8 :: v4 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )
      assert( triangleSets.contains( (v5 :: v6 :: v2 :: Nil, t1 :: t2 :: t3 :: Nil ) ) )
      assert( triangleSets.contains( (v2 :: v1 :: v5 :: Nil, t3 :: t4 :: t1 :: Nil ) ) )

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
      assert( mesh.texCoords.length == 4 )
    }

    it( "should load a file that contains a cube with all vertices, texture coordinates, and normals into a triangle mesh" ) {
      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v-vt-vn.obj", (recursions,faces) => recursions < 0, false )

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
        (mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil, mesh.texCoords( face.head._2.get ) :: mesh.texCoords( face( 1 )._2.get ) :: mesh.texCoords( face( 2 )._2.get ) :: Nil, mesh.normals( face( 0 )._3.get ) :: mesh.normals( face( 1 )._3.get ) :: mesh.normals( face( 2 )._3.get ) :: Nil  )

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

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
      assert( mesh.texCoords.length == 4 )
      assert( mesh.normals.length == 6 )
    }



    it( "should load a file that contains a cube where more than one block of faces exist" ) {
      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v.obj", (recursions,faces) => recursions < 0, false )

      assert( mesh.vertices.contains( v1 ) )
      assert( mesh.vertices.contains( v2 ) )
      assert( mesh.vertices.contains( v3 ) )
      assert( mesh.vertices.contains( v4 ) )

      assert( mesh.vertices.contains( v5 ) )
      assert( mesh.vertices.contains( v6 ) )
      assert( mesh.vertices.contains( v7 ) )
      assert( mesh.vertices.contains( v8 ) )

      val triangleSets = for( face <- mesh.faces ) yield
        mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil

      assert( triangleSets.contains( v1 :: v2 :: v3 :: Nil ) )
      assert( triangleSets.contains( v3 :: v4 :: v1 :: Nil ) )
      assert( triangleSets.contains( v2 :: v6 :: v7 :: Nil ) )
      assert( triangleSets.contains( v7 :: v3 :: v2 :: Nil ) )
      assert( triangleSets.contains( v6 :: v5 :: v8 :: Nil ) )
      assert( triangleSets.contains( v8 :: v7 :: v6 :: Nil ) )
      assert( triangleSets.contains( v5 :: v1 :: v4 :: Nil ) )
      assert( triangleSets.contains( v4 :: v8 :: v5 :: Nil ) )
      assert( triangleSets.contains( v4 :: v3 :: v7 :: Nil ) )
      assert( triangleSets.contains( v7 :: v8 :: v4 :: Nil ) )
      assert( triangleSets.contains( v5 :: v6 :: v2 :: Nil ) )
      assert( triangleSets.contains( v2 :: v1 :: v5 :: Nil ) )

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
    }

    it( "should load a file that contains a cube where the indices of the faces are that with a negative number" ) {

      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v-blocks-weird-indices.obj", (recursions,faces) => recursions < 0, false )

      assert( mesh.vertices.contains( v1 ) )
      assert( mesh.vertices.contains( v2 ) )
      assert( mesh.vertices.contains( v3 ) )
      assert( mesh.vertices.contains( v4 ) )

      assert( mesh.vertices.contains( v5 ) )
      assert( mesh.vertices.contains( v6 ) )
      assert( mesh.vertices.contains( v7 ) )
      assert( mesh.vertices.contains( v8 ) )

      val triangleSets = for( face <- mesh.faces ) yield
        mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil

      assert( triangleSets.contains( v1 :: v2 :: v3 :: Nil ) )
      assert( triangleSets.contains( v3 :: v4 :: v1 :: Nil ) )
      assert( triangleSets.contains( v2 :: v6 :: v7 :: Nil ) )
      assert( triangleSets.contains( v7 :: v3 :: v2 :: Nil ) )
      assert( triangleSets.contains( v6 :: v5 :: v8 :: Nil ) )
      assert( triangleSets.contains( v8 :: v7 :: v6 :: Nil ) )
      assert( triangleSets.contains( v5 :: v1 :: v4 :: Nil ) )
      assert( triangleSets.contains( v4 :: v8 :: v5 :: Nil ) )
      assert( triangleSets.contains( v4 :: v3 :: v7 :: Nil ) )
      assert( triangleSets.contains( v7 :: v8 :: v4 :: Nil ) )
      assert( triangleSets.contains( v5 :: v6 :: v2 :: Nil ) )
      assert( triangleSets.contains( v2 :: v1 :: v5 :: Nil ) )

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
    }

    it( "should load a file with comments that contains a cube with all vertices, texture coordinates, and normals into a triangle mesh" ) {
      val objLoader = new OBJLoader()
      val mesh = objLoader.load( "cube-v-vt-vn-comments.obj", (recursions,faces) => recursions < 0, false )

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
        (mesh.vertices( face.head._1 ) :: mesh.vertices( face( 1 )._1 ) :: mesh.vertices( face( 2 )._1 ) :: Nil, mesh.texCoords( face.head._2.get ) :: mesh.texCoords( face( 1 )._2.get ) :: mesh.texCoords( face( 2 )._2.get ) :: Nil, mesh.normals( face( 0 )._3.get ) :: mesh.normals( face( 1 )._3.get ) :: mesh.normals( face( 2 )._3.get ) :: Nil  )

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

      assert( mesh.vertices.length == 8 )
      assert( mesh.faces.length == 12 )
      assert( mesh.texCoords.length == 4 )
      assert( mesh.normals.length == 6 )
    }

  }

}
