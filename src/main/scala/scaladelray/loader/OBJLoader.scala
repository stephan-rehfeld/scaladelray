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

  def vertexOnly : Parser[(Int,Option[Int],Option[Int])] = decimalNumber ^^ {
    case v => (v.toInt,None,None)
  }
  def vertexAndTexCoord : Parser[(Int,Option[Int],Option[Int])] = decimalNumber~"/"~decimalNumber ^^ {
    case v~"/"~vt => (v.toInt,Some(vt.toInt),None)
  }

  def vertexAndNormal : Parser[(Int,Option[Int],Option[Int])] = decimalNumber~"//"~decimalNumber ^^ {
    case v~"//"~vn => (v.toInt,None,Some(vn.toInt))
  }

  def vertexTexCoordNormal : Parser[(Int,Option[Int],Option[Int])] = decimalNumber~"/"~decimalNumber~"/"~decimalNumber ^^ {
    case v~"/"~vt~"/"~vn => (v.toInt,Some(vt.toInt),Some(vn.toInt))
  }

}


object OBJLoader extends OBJLoader {

  def load( fileName : String ) {

  }

  def main( args : Array[String] ) {
    val reader = new FileReader( "test.obj" )
    val result = parseAll( objFile, reader ).get

    val vertices = mutable.MutableList[Point3]()
    val normals = mutable.MutableList[Normal3]()
    val texCoords = mutable.MutableList[TexCoord2D]()
    val faces = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()

    val verticesBuffer = mutable.MutableList[Point3]()
    val normalsBuffer = mutable.MutableList[Normal3]()
    val texCoordsBuffer = mutable.MutableList[TexCoord2D]()
    val facesBuffer = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()

    var lastWasFace = false

    for( line <- result ) line match {
      case v : Point3 =>
        if( lastWasFace ) {
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
            if ( faces.size != 3 ) throw new UnsupportedOperationException( "Triangulation is not supported yet" )

          }
        }
        verticesBuffer += v


      case n : Normal3 =>
        normalsBuffer += n

      case t : TexCoord2D =>
        texCoordsBuffer += t

      case (f : List[(Int,Option[Int],Option[Int])] @unchecked) =>
        facesBuffer += f
    }

  }
}
