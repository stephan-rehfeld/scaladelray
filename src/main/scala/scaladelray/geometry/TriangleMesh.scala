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

package scala.del.ray.geometry

import scaladelray.geometry.{Geometry, Hit}
import scaladelray.material.Material
import scaladelray.math._
import scaladelray.texture.TexCoord2D
import scaladelray.Constants
import scaladelray.math.Point3
import scaladelray.math.Ray
import scaladelray.texture.TexCoord2D
import scaladelray.math.Normal3
import scaladelray.math.Mat3x3
import scaladelray.geometry.Hit

class TriangleMesh( material : Material, val vertices : Array[Point3], val normals : Array[Normal3], val texCoords : Array[TexCoord2D], val faces : Array[List[(Int,Option[Int],Option[Int])]] ) extends Geometry( material ) {

  val (minX,minY,minZ,maxX,maxY,maxZ) =
    vertices.foldLeft( (Double.MaxValue,Double.MaxValue,Double.MaxValue,Double.MinValue,Double.MinValue,Double.MinValue) )( (v : (Double,Double,Double,Double,Double,Double),p : Point3) => {
              (if(p.x<v._1)p.x else v._1,
               if(p.y<v._2)p.y else v._2,
               if(p.z<v._3)p.z else v._3,
               if(p.x>v._4)p.x else v._4,
               if(p.y>v._5)p.y else v._5,
               if(p.z>v._6)p.z else v._6)
            } )

  val center = vertices.foldLeft( Vector3( 0, 0, 0 ) )( (b,a) => { b + a.asVector } ) / vertices.size

  def <--(r: Ray) : Set[Hit] = {
    val hits = collection.mutable.Set[Hit]()
    for( face <- faces ) {
      val a = vertices( face(0)._1 )
      val b = vertices( face(1)._1 )
      val c = vertices( face(2)._1 )

      val (an,bn,cn) = if( face(0)._3.isDefined ) {
        (normals(face(0)._3.get),normals(face(0)._3.get),normals(face(0)._3.get))
      } else {
        val n = (b-c x a-c).asNormal
        (n,n,n)
      }

      val (at,bt,ct) = if( face(0)._2.isDefined ) {
        (texCoords(face(0)._2.get),texCoords(face(0)._2.get),texCoords(face(0)._2.get))
      } else {
        val t = TexCoord2D( 0, 0 )
        (t,t,t)
      }

      val base = Mat3x3( a.x - b.x, a.x - c.x, r.d.x,
        a.y - b.y, a.y - c.y, r.d.y,
        a.z - b.z, a.z - c.z, r.d.z)

      val vec = a - r.o

      val beta = base.replaceCol1( vec ).determinant / base.determinant
      val gamma = base.replaceCol2( vec ).determinant / base.determinant
      val t = base.replaceCol3( vec ).determinant / base.determinant

      if( !(beta < 0.0 || gamma < 0.0 || beta + gamma > 1.0 || t < Constants.EPSILON) ) {
        val alpha = 1 - beta - gamma
        hits += Hit( r, this, t, an * alpha + bn * beta + cn * gamma, at * alpha + bt * beta + ct * gamma )
      }
    }
    hits.toSet
  }
}
