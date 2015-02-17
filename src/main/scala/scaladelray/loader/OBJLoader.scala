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
import scaladelray.texture.TexCoord2D
import scala.None
import scaladelray.geometry.TriangleMesh
import java.io.FileReader

/**
 * This class is a loader for the OBJ File format. It currently loads the vertices, normals, texture coordinates, and
 * face into a [[scaladelray.geometry.TriangleMesh]]. It assumes that only triangles are defined within the files.
 * Triangulation is not supported.
 *
 * @author Stephan Rehfeld
 */
class OBJLoader extends JavaTokenParsers {

  /**
   * Part of the parser implementation. Defines that an OBJ file consists of lines.
   *
   * @return A list that contains all parsed lines.
   */
  private def objFile : Parser[List[Any]] = rep( line )

  /**
   * Part of the parser implementation. Defines that a line is a vertex, normal, texture coordinate, face, or comment.
   *
   * @return The parsed line.
   */
  private def line : Parser[Any] = vertex | normal | texCoord | face | comment

  /**
   * Part of the parser implementation. Defines that a vertex starts with a "v" followed by three floating point numbers
   * and one optional number.
   *
   * @return The parsed vertex as [[scaladelray.math.Point3]].
   */
  private def vertex : Parser[Point3] = "v"~floatingPointNumber~floatingPointNumber~floatingPointNumber~opt( floatingPointNumber ) ^^ {
    case "v"~x~y~z~w => Point3( x.toDouble, y.toDouble, z.toDouble )
  }

  /**
   * Part of the parser implementation. Defines that a normal starts with a "vn" followed by three floating point
   * numbers.
   *
   * @return The parsed normal as [[scaladelray.math.Normal3]].
   */
  private def normal : Parser[Normal3] = "vn"~floatingPointNumber~floatingPointNumber~floatingPointNumber ^^ {
    case "vn"~x~y~z => Normal3( x.toDouble, y.toDouble, z.toDouble )
  }

  /**
   * Part of the parser implementation. Defines that a texture coordinate start with a "vt" followed by at least one
   * and up to three floating point numbers.
   *
   * 3D textures are currently not supported, so the third number is ignored.
   *
   * @return The parsed texture coordinate as [[scaladelray.texture.TexCoord2D]].
   */
  private def texCoord : Parser[TexCoord2D] = "vt"~floatingPointNumber~opt( floatingPointNumber )~opt( floatingPointNumber ) ^^ {
    case "vt"~u~v~w => TexCoord2D( u.toDouble, v.get.toDouble )
  }

  /**
   * Part of the parser implementation. Defines that a face consists of arbitrary number of indices where an index
   * word can consists of a combination of vertex index, normal index, and texture coordinate index according to
   * the OBJ file specification.
   *
   * @return A tuple that contains the parsed indices.
   */
  // Order matters
  private def face : Parser[List[(Int,Option[Int],Option[Int])]] = "f"~rep( vertexAndNormal | vertexTexCoordNormal | vertexAndTexCoord | vertexOnly ) ^^ {
    case "f"~x => x
  }

  /**
   * A parser that matches anything. Used for comments an unknown lines.
   *
   * @return A parser that matches anything.
   */
  def any: Parser[String] = """.*""".r

  private def comment : Parser[String] = "#"~any ^^ {
    case "#"~x => x
  }

  /**
   * Part of the parser implementation. Defines the parser for an index word that only contains the vertex index.
   *
   * @return A tuple that contains the index of the vertex and [[scala.None]] for all other indices.
   */
  private def vertexOnly : Parser[(Int,Option[Int],Option[Int])] = wholeNumber ^^ {
    case v => (v.toInt,None,None)
  }

  /**
   * Part of the parser implementation. Defines the parser for an index word that contains the vertex index and the
   * texture coordinate index.
   *
   * @return A tuple that contains the index of the vertex and the texture coordinate but [[scala.None]] for the normal.
   */
  private def vertexAndTexCoord : Parser[(Int,Option[Int],Option[Int])] = wholeNumber~"/"~wholeNumber ^^ {
    case v~"/"~vt => (v.toInt,Some(vt.toInt),None)
  }

  /**
   * Part of the parser implementation. Defines the parser for an index word that contains the vertex index and the
   * normal index.
   *
   * @return A tuple that contains the index of the vertex and the normal but [[scala.None]] for the texture coordinate.
   */
  private def vertexAndNormal : Parser[(Int,Option[Int],Option[Int])] = wholeNumber~"//"~wholeNumber ^^ {
    case v~"//"~vn => (v.toInt,None,Some(vn.toInt))
  }

  /**
   * Part of the parser implementation. Defines the parser for an index word that contains the vertex index, the texture
   * coordinate index, and the normal index.
   *
   * @return A tuple that contains all indices.
   */
  private def vertexTexCoordNormal : Parser[(Int,Option[Int],Option[Int])] = wholeNumber~"/"~wholeNumber~"/"~wholeNumber ^^ {
    case v~"/"~vt~"/"~vn => (v.toInt,Some(vt.toInt),Some(vn.toInt))
  }

  /**
   * A list where all vertices for the final triangle mesh are collected.
   */
  private val vertices = mutable.MutableList[Point3]()

  /**
   * A list where all normal for the final triangle mesh are collected.
   */
  private val normals = mutable.MutableList[Normal3]()

  /**
   * A list where all texture coordinates for the final triangle mesh are collected.
   */
  private val texCoords = mutable.MutableList[TexCoord2D]()

  /**
   * A map that holds the indices of all vertices. Used to speed up file loading.
   */
  private val verticesIndexMap = mutable.HashMap[Point3,Int]()

  /**
   * A map that holds the indices of all normals. Used to speed up file loading.
   */
  private val normalsIndexMap = mutable.HashMap[Normal3,Int]()

  /**
   * A map that holds the indices of all texture coordinates. Used to speed up file loading.
   */
  private val texCoordsIndexMap = mutable.HashMap[TexCoord2D,Int]()

  /**
   * A flag that indicates if fast load is enabled.
   */
  private var fastLoad = false

  /**
   * A list where the face for the model are constructed.
   */
  private val faces = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()

  /**
   * A buffer for the vertices.
   */
  private val verticesBuffer = mutable.MutableList[Point3]()

  /**
   * A buffer for the normals.
   */
  private val normalsBuffer = mutable.MutableList[Normal3]()

  /**
   * A buffer for the texture coordinates.
   */
  private val texCoordsBuffer = mutable.MutableList[TexCoord2D]()

  /**
   * A buffer for the faces.
   */
  private val facesBuffer = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()

  /**
   * A flag variable used due the parsing process the construct the geometry if a new block of vertices, texture
   * coordinates or normals are started after a face.
   */
  private var lastWasFace = true

  /**
   * This method loads a model from a OBJ file and returns the loaded geometry as triangle mesh.
   *
   * @param fileName The name of the file that contains the model.
   * @param fastLoad Loads the model faster but may consumes more memory.
   * @return The loaded model as triangle mesh.
   */
  def load( fileName : String, subDivideDecider : ((Int,Int) => Boolean ), fastLoad : Boolean ) : TriangleMesh = {

    assert( fileName != null, "The parameter 'fileName' must not be 'null'!" )

    this.fastLoad = fastLoad

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

      case _ =>

    }

    constructFromBuffer()

    new TriangleMesh( vertices.toArray, normals.toArray, texCoords.toArray, faces.toArray, subDivideDecider )

  }

  /**
   * Internal helper function to remove some redundant code. If the last line was a face, the geometry is constructed
   * from the buffer. It also adds the element of the line to the list.
   *
   * @param list The buffer list.
   * @param elem The element to add-
   * @tparam T The type of the element.
   */
  private def processData[T]( list : mutable.MutableList[T], elem : T ) {
    if( lastWasFace ) {
      constructFromBuffer()
      lastWasFace = false
    }
    list += elem
  }

  /**
   * The method constructs the already buffered geometry and writes the vertices, normals, and texture coordinates into
   * the final lists.
   */
  private def constructFromBuffer() {
    val (minVertex,minTexCoord,minNormal) = facesBuffer.foldLeft( (Int.MaxValue,Int.MaxValue,Int.MaxValue) )( (b,faceData) => faceData.foldLeft( b )( (mins,vertexData) => {
      (if( vertexData._1 < mins._1 ) vertexData._1 else mins._1,
        vertexData._2.fold( mins._2 )( (x) => if(x<mins._2) x else mins._2 ),
        vertexData._3.fold( mins._3 )( (x) => if(x<mins._3) x else mins._3 ) )
      }
    ))

    if( fastLoad ) {
      val vBegin = vertices.size
      val tBegin = texCoords.size
      val nBegin = normals.size

      vertices ++= verticesBuffer
      texCoords ++= texCoordsBuffer
      normals ++= normalsBuffer

      for( face <- facesBuffer ) {
        if ( face.size != 3 ) throw new UnsupportedOperationException( "Triangulation is not supported yet" )

        val finalFace = for ( faceData <- face ) yield {
          val vIndex = faceData._1 - minVertex + vBegin
          val tIndex = if( faceData._2.isDefined ) Some( faceData._2.get - minTexCoord + tBegin ) else None
          val nIndex = if( faceData._3.isDefined ) Some( faceData._3.get - minNormal + nBegin ) else None
          (vIndex,tIndex,nIndex)
        }
        faces += finalFace
      }
    } else {
      for( face <- facesBuffer ) {
        if ( face.size != 3 ) throw new UnsupportedOperationException( "Triangulation is not supported yet" )

        val finalFace = for ( faceData <- face ) yield {
          val vertex = verticesBuffer( faceData._1 - minVertex )

          val texCoord = faceData._2.fold[Option[TexCoord2D]]( None )( (x:Int) => Some( texCoordsBuffer( x - minTexCoord ) ) )
          val normal = faceData._3.fold[Option[Normal3]]( None )( (x:Int) => Some( normalsBuffer( x - minNormal ) ) )

          val vIndex = indexOfAndMaybeAdd( vertices, verticesIndexMap, vertex )
          val tIndex = if( texCoord.isDefined ) Some( indexOfAndMaybeAdd( texCoords, texCoordsIndexMap, texCoord.get ) ) else None
          val nIndex = if ( normal.isDefined ) Some( indexOfAndMaybeAdd( normals, normalsIndexMap, normal.get) ) else None

          (vIndex,tIndex,nIndex)

        }
        faces += finalFace
      }
    }

    clearBuffer()
  }

  /**
   * This method resets the parser.
   */
  private def reset() {
    vertices.clear()
    normals.clear()
    texCoords.clear()
    faces.clear()
    verticesIndexMap.clear()
    normalsIndexMap.clear()
    texCoordsIndexMap.clear()

    clearBuffer()

    lastWasFace = false
  }

  /**
   * This method clears all buffer.
   */
  private def clearBuffer() {
    verticesBuffer.clear()
    normalsBuffer.clear()
    texCoordsBuffer.clear()
    facesBuffer.clear()
  }

  /**
   * This method returns the index of the element in the list. If the element is not part of the list, it's added and
   * the index is returned.
   *
   * @param list The list from which the index should be retrieved.
   * @param indexMap A map that contains the index of each element in the list.
   * @param elem The element.
   * @tparam T The data type of the element.
   * @return The index of the element within the list.
   */
  private def indexOfAndMaybeAdd[T]( list : mutable.MutableList[T], indexMap : mutable.HashMap[T,Int], elem : T ) : Int = {
    indexMap.get( elem ) match {
      case Some( i ) => i
      case None =>
        list += elem
        val i = list.size - 1
        indexMap += (elem -> i )
        i

    }
  }

}

