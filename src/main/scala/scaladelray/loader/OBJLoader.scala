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
import scaladelray.material.{Material, SingleColorMaterial}
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

  private var lastWasFace = true

  def load( fileName : String, material : Material ) : TriangleMesh = {

    reset()

    val reader = new FileReader( fileName )
    val parseResult = parseAll( objFile, reader )
    val result = parseResult.get

    for( line <- result ) line match {
      case v : Point3 =>
        processData( verticesBuffer, v )

      case n : Normal3 =>
        processData( normalsBuffer, n )

      case t : TexCoord2D =>
        processData( texCoordsBuffer, t )

      case (f : List[(Int,Option[Int],Option[Int])] @unchecked) =>
        lastWasFace = true
        facesBuffer += f
    }

    constructFromBuffer()

    new TriangleMesh( material, vertices.toArray, normals.toArray, texCoords.toArray, faces.toArray )

  }

  private def processData[T]( list : mutable.MutableList[T], elem : T ) {
    if( lastWasFace ) {
      constructFromBuffer()
      lastWasFace = false
    }
    list += elem
  }

  private def constructFromBuffer() {
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
        val correctedTexCoordIndex = if( vertexData._2.isDefined ) Some( vertexData._2.get - minTexCoord )  else None
        val correctedNormalIndex = if( vertexData._3.isDefined ) Some( vertexData._3.get - minNormal )  else None
        (correctedVertexIndex,correctedTexCoordIndex,correctedNormalIndex)
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

        var vIndex = indexOfAndMaybeAdd( vertices, vertex )
        val tIndex = if( texCoord.isDefined ) Some( indexOfAndMaybeAdd( texCoords, texCoord.get ) ) else None
        val nIndex = if ( normal.isDefined ) Some( indexOfAndMaybeAdd( normals, normal.get) ) else None

        (vIndex,tIndex,nIndex)
      }
      faces += finalFace
    }
    clearBuffer()
  }

  private def reset() {
    vertices.clear()
    normals.clear()
    texCoords.clear()
    faces.clear()

    clearBuffer()

    lastWasFace = false
  }

  private def clearBuffer() {
    verticesBuffer.clear()
    normalsBuffer.clear()
    texCoordsBuffer.clear()
    facesBuffer.clear()
  }

  private def indexOfAndMaybeAdd[T]( list : mutable.MutableList[T], elem : T ) : Int = {
    var i = list.indexOf( elem )
    if( i == -1 ) {
      list += elem
      i = list.size - 1
    }
    i
  }

}


object OBJLoader extends OBJLoader {

  def main( args : Array[String] ) {
    val objLoader = new OBJLoader()
    //val teddy = objLoader.load( "teddy.obj" )
    //println( teddy )

  }

}
