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

package scaladelray.loader

import scaladelray.math.{Point3, Normal3}
import collection.mutable
import util.parsing.combinator.JavaTokenParsers
import java.io.FileReader
import scaladelray.texture.TexCoord2D
import scala.None
import scala.del.ray.geometry.TriangleMesh
import scaladelray.material.SingleColorMaterial
import scaladelray.Color

class OBJLoader extends JavaTokenParsers {

  def objFile : Parser[List[Any]] = rep( line )

  def line : Parser[Any] = vertex | normal | texCoord | face

  def vertex : Parser[Point3] = "v"~floatingPointNumber~floatingPointNumber~floatingPointNumber~opt( floatingPointNumber ) ^^ {
    case "v"~x~y~z~w => Point3( x.toDouble, y.toDouble, z.toDouble )
  }
  def normal : Parser[Normal3] = "vn"~floatingPointNumber~floatingPointNumber~floatingPointNumber ^^ {
    case "vn"~x~y~z => Normal3( x.toDouble, y.toDouble, z.toDouble )
  }
  def texCoord : Parser[TexCoord2D] = "vt"~floatingPointNumber~opt( floatingPointNumber )~opt( floatingPointNumber ) ^^ {
    case "vt"~u~v~w => TexCoord2D( u.toDouble, v.get.toDouble )
  }

  // Order matters
  def face : Parser[List[(Int,Option[Int],Option[Int])]] = "f"~rep( vertexAndNormal | vertexTexCoordNormal | vertexAndTexCoord | vertexOnly ) ^^ {
    case "f"~x => x
  }

  def vertexOnly : Parser[(Int,Option[Int],Option[Int])] = wholeNumber ^^ {
    case v => (v.toInt,None,None)
  }
  def vertexAndTexCoord : Parser[(Int,Option[Int],Option[Int])] = wholeNumber~"/"~wholeNumber ^^ {
    case v~"/"~vt => (v.toInt,Some(vt.toInt),None)
  }

  def vertexAndNormal : Parser[(Int,Option[Int],Option[Int])] = wholeNumber~"//"~wholeNumber ^^ {
    case v~"//"~vn => (v.toInt,None,Some(vn.toInt))
  }

  def vertexTexCoordNormal : Parser[(Int,Option[Int],Option[Int])] = wholeNumber~"/"~wholeNumber~"/"~wholeNumber ^^ {
    case v~"/"~vt~"/"~vn => (v.toInt,Some(vt.toInt),Some(vn.toInt))
  }

  private val vertices = mutable.MutableList[Point3]()
  private val normals = mutable.MutableList[Normal3]()
  private val texCoords = mutable.MutableList[TexCoord2D]()
  private val faces = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()

  private val verticesBuffer = mutable.MutableList[Point3]()
  private val normalsBuffer = mutable.MutableList[Normal3]()
  private val texCoordsBuffer = mutable.MutableList[TexCoord2D]()
  private val facesBuffer = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()

  def load( fileName : String ) : TriangleMesh = {
    val reader = new FileReader( "test.obj" )
    val result = parseAll( objFile, reader ).get

    vertices.clear()
    normals.clear()
    texCoords.clear()
    faces.clear()
    verticesBuffer.clear()
    normalsBuffer.clear()
    texCoordsBuffer.clear()
    facesBuffer.clear()

    var lastWasFace = false

    for( line <- result ) line match {
      case v : Point3 =>
        if( lastWasFace ) {
          constructFromBuffer
          lastWasFace = false
        }
        verticesBuffer += v

      case n : Normal3 =>
        if( lastWasFace ) {
          constructFromBuffer
          lastWasFace = false
        }
        normalsBuffer += n

      case t : TexCoord2D =>
        if( lastWasFace ) {
          constructFromBuffer
          lastWasFace = false
        }
        texCoordsBuffer += t

      case (f : List[(Int,Option[Int],Option[Int])] @unchecked) =>
        lastWasFace = true
        facesBuffer += f
    }
    constructFromBuffer
    new TriangleMesh( SingleColorMaterial( Color( 0, 0, 0 ) ), vertices.toArray, normals.toArray, texCoords.toArray, faces.toArray )

  }

  private def constructFromBuffer {
    var minVertex = Int.MaxValue
    var minTexCoord = Int.MaxValue
    var minNormal = Int.MaxValue

    for( face <- facesBuffer ) {
      for ( vertexData <- face ) {
        minVertex = if( vertexData._1 < minVertex ) vertexData._1 else minVertex
        minTexCoord = if ( vertexData._2.isDefined && vertexData._2.get < minTexCoord ) vertexData._2.get else minVertex
        minNormal = if ( vertexData._3.isDefined && vertexData._3.get < minNormal ) vertexData._3.get else minNormal
      }
    }

    val correctedFacesBuffer = for( face <- facesBuffer ) yield {
      for ( vertexData <- face ) yield {
        val correctedVertexIndex = vertexData._1 - minVertex
        val correctedTexCoord = if( vertexData._2.isDefined ) Some( vertexData._2.get - minTexCoord )  else None
        val correctedNormal = if( vertexData._3.isDefined ) Some( vertexData._3.get - minNormal )  else None
        (correctedVertexIndex,correctedTexCoord,correctedNormal)
      }
    }

    for( face <- correctedFacesBuffer ) {
      if ( face.size != 3 ) throw new UnsupportedOperationException( "Triangulation is not supported yet" )
      val finalFace = for( faceData <- face ) yield {
        val vertex = verticesBuffer( faceData._1 )
        val texCoord = faceData._2 match {
          case Some( x ) =>
            Some( texCoordsBuffer( x ) )
          case None =>
            None
        }
        val normal = faceData._3 match {
          case Some( x ) =>
            Some( normalsBuffer( x ) )
          case None =>
            None
        }
        var vIndex = vertices.indexOf( vertex )
        if ( vIndex == -1 ) {
          vertices += vertex
          vIndex = vertices.size - 1
        }

        val tIndex = if ( texCoord.isDefined ) {
          var i = texCoords.indexOf( texCoord.get )
          if ( i == -1 ) {
            texCoords += texCoord.get
            i = texCoords.size - 1
          }
          Some( i )
        } else {
          None
        }

        val nIndex = if ( normal.isDefined ) {
          var i = normals.indexOf( normal.get )
          if ( i == -1 ) {
            normals += normal.get
            i = texCoords.size - 1
          }
          Some( i )
        } else {
          None
        }
        (vIndex,tIndex,nIndex)
      }
      faces += finalFace
    }
    verticesBuffer.clear()
    normalsBuffer.clear()
    texCoordsBuffer.clear()
    facesBuffer.clear()
  }

}


object OBJLoader extends OBJLoader {

  def main( args : Array[String] ) {
    val objLoader = new OBJLoader()
    val mesh1 = objLoader.load( "test.obj" )
    val mesh2 = objLoader.load( "test.obj" )
    println( mesh1 )

  }

}
