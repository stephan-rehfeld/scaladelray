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

  def vertex : Parser[Point3] = "v"~doubleNumber~doubleNumber~doubleNumber~opt( doubleNumber ) ^^ {
    case "v"~x~y~z~w => Point3( x, y, z )
  }
  def normal : Parser[Normal3] = "vn"~doubleNumber~doubleNumber~doubleNumber ^^ {
    case "vn"~x~y~z => Normal3( x, y, z )
  }
  def texCoord : Parser[TexCoord2D] = "vt"~doubleNumber~opt( doubleNumber )~opt( doubleNumber ) ^^ {
    case "vt"~u~v~w => TexCoord2D( u, v.get )
  }

  // Order matters
  def face : Parser[List[(Int,Option[Int],Option[Int])]] = "f"~rep( vertexAndNormal | vertexTexCoordNormal | vertexAndTexCoord | vertexOnly ) ^^ {
    case "f"~x => x
  }

  def vertexOnly : Parser[(Int,Option[Int],Option[Int])] = intNumber ^^ {
    case v => (v,None,None)
  }
  def vertexAndTexCoord : Parser[(Int,Option[Int],Option[Int])] = intNumber~"/"~intNumber ^^ {
    case v~"/"~vt => (v,Some(vt),None)
  }

  def vertexAndNormal : Parser[(Int,Option[Int],Option[Int])] = intNumber~"//"~intNumber ^^ {
    case v~"//"~vn => (v,None,Some(vn))
  }

  def vertexTexCoordNormal : Parser[(Int,Option[Int],Option[Int])] = intNumber~"/"~intNumber~"/"~intNumber ^^ {
    case v~"/"~vt~"/"~vn => (v,Some(vt),Some(vn))
  }

  def doubleNumber : Parser[Double] = floatingPointNumber ^^ (_.toDouble)
  def intNumber : Parser[Int] = decimalNumber ^^ (_.toInt)

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

    for( line <- result ) line match {
      case v : Point3 =>
        vertices += v

      case n : Normal3 =>
        normals += n

      case t : TexCoord2D =>
        texCoords += t

      case (f : List[(Int,Option[Int],Option[Int])] @unchecked) =>
        faces += f
    }
  }
}
