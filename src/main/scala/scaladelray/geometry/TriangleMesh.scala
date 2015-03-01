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

package scaladelray.geometry

import scaladelray.Constants
import scaladelray.math._
import scaladelray.texture.{Texture, TexCoord2D}
import scaladelray.optimization.Octree
import scala.Array
import scala.collection.mutable

/**
 * An instance of this class represents a triangle mesh geometry. An octree is created to enhance rendering performance.
 * The triangle mesh is expected to be provided as an array of vertices and indices Normals and texture coordinates
 * can also be provided.
 *
 * Inidicies for vertices are mandatory. Normals and texture coordinates are optional.
 *
 * The depth of the octree is determined by the passed function subDivideDecider. The current recursion depth (first parameter)
 * and the number of triangles within the current node of the octree are passed to the subDivideDecider. If the decider
 * returns true, the current node is split to 8 more nodes.
 *
 * @param vertices An array that contains all vertices of the model.
 * @param normals An array that contains all normals of the model.
 * @param texCoords An array that contains all texture coordinates of the model.
 * @param faces An array that contains the description of the faces of the model. The first value on the tuple is the index of the vertex. The second one is the index of the texture coordinate. The third one is the normal.
 * @param subDivideDecider A function that decides if a node in the octree should be ddecidedinto 8 more bounding boxes.
 * @param normalMap An optional normal map for the triangle mesh.
 */
case class TriangleMesh( vertices : Array[Point3], normals : Array[Normal3], texCoords : Array[TexCoord2D], faces : Array[List[(Int,Option[Int],Option[Int])]], subDivideDecider : ((Int,Int) => Boolean ), normalMap : Option[Texture] ) extends Geometry with Serializable {

  /**
   * The smalltest and largest x, y, and z values.
   */
  val (minX,minY,minZ,maxX,maxY,maxZ) =
    vertices.foldLeft( (Double.MaxValue,Double.MaxValue,Double.MaxValue,Double.MinValue,Double.MinValue,Double.MinValue) )( (v : (Double,Double,Double,Double,Double,Double),p : Point3) => {
              (if(p.x<v._1)p.x else v._1,
               if(p.y<v._2)p.y else v._2,
               if(p.z<v._3)p.z else v._3,
               if(p.x>v._4)p.x else v._4,
               if(p.y>v._5)p.y else v._5,
               if(p.z>v._6)p.z else v._6)
            } )

  /**
   * The geometrical center of the object. The average of all vertices of the model.
   */
  val center = vertices.foldLeft( Vector3( 0, 0, 0 ) )( (b,a) => { b + a.asVector } ) / vertices.size

  /**
   * The left bottom far point of the bounding box of the model.
   */
  val lbf = Point3( minX, minY, minZ )

  /**
   * The right upper near point of the bounding box of the model.
   */
  val run = Point3( maxX, maxY, maxZ )

  /**
   * The octree to enhance rendering performance.
   */
  private val octree = generateOctree( 0, run, lbf, faces, subDivideDecider )

  /**
   * This recursive function constructs an octree to enhance rendering performance.
   *
   * @param recursionDepth The current recursions depth
   * @param run The right upper near point of the current bounding box.
   * @param lbf The left bottom far point of the current bounding box.
   * @param faces The faces for the current recusion depth.
   * @param subDivideDecider The subDevideDecider.
   * @return A octree that split the model into various nodes to enhance rendering performance.
   */
  private def generateOctree( recursionDepth : Int, run: Point3, lbf : Point3, faces : Array[List[(Int,Option[Int],Option[Int])]], subDivideDecider : ((Int,Int) => Boolean ) ) : Octree[Array[List[(Int,Option[Int],Option[Int])]]] = {
    if( subDivideDecider( recursionDepth, faces.size ) ) {
      val center = lbf + ((run - lbf) / 2.0)
      // for - yield does not work here
      val facesOfOctants = Octree.values.toList.map( _ -> mutable.MutableList[List[(Int,Option[Int],Option[Int])]]() ).toMap

      var thisNode  = mutable.MutableList[List[(Int,Option[Int],Option[Int])]]()
      for( face <- faces ) {
        val octants = (for( (v,_,_) <- face ) yield Octree.getOctantOfVertex( center, vertices(v) )).toSet
        if( octants.size > 1 ) {
          thisNode += face
        } else {
          facesOfOctants( octants.head ) += face
        }
      }
      if( thisNode.size == faces.size ) {
        new Octree[Array[List[(Int,Option[Int],Option[Int])]]]( run, lbf, Set(), faces )
      } else {
        new Octree[Array[List[(Int,Option[Int],Option[Int])]]]( run, lbf,
          Set() +
            generateOctree( recursionDepth + 1, run, center, facesOfOctants( Octree.RightUpperNear ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, Point3( center.x, run.y, run.z ), Point3( lbf.x, center.y, center.z ), facesOfOctants( Octree.LeftUpperNear ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, Point3( run.x, run.y, center.z ), Point3( center.x, center.y, lbf.z ), facesOfOctants( Octree.RightUpperFar ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, Point3( center.x, run.y, center.z ), Point3( lbf.x, center.y, lbf.z ), facesOfOctants( Octree.LeftUpperFar ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, Point3( run.x, center.y, run.z ), Point3( center.x, lbf.y, center.z ), facesOfOctants( Octree.RightLowerNear ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, Point3( center.x, center.y, run.z ), Point3( lbf.x, lbf.y, center.z ), facesOfOctants( Octree.LeftLowerNear ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, Point3( run.x, center.y, center.z ), Point3( center.x, lbf.y, lbf.z ), facesOfOctants( Octree.RightLowerFar ).toArray, subDivideDecider ) +
            generateOctree( recursionDepth + 1, center, lbf, facesOfOctants( Octree.LeftLowerFar ).toArray, subDivideDecider )
          , thisNode.toArray )
      }
    } else {
      new Octree[Array[List[(Int,Option[Int],Option[Int])]]]( run, lbf, Set(), faces )
    }
  }

  override def <--(r: Ray) : Set[GeometryHit] = {
    findHitsInOctree( octree, r )
  }


  /**
   * A function that traverses the octree recursivley to determine the hits with the model.
   *
   * @param node The current octree node.
   * @param r The ray.
   * @return A set of hits from this node and sub nodes.
   */
  private def findHitsInOctree( node : Octree[Array[List[(Int,Option[Int],Option[Int])]]], r : Ray ) : Set[GeometryHit] = {
    val hits = collection.mutable.Set[GeometryHit]()

    if( node <-- r ) {
      hits ++= findHitsInFaces( node.data, r )
      for( subNode <- node.nodes ) hits ++= findHitsInOctree( subNode, r )
    }

    hits.toSet
  }

  /**
   * This function finds the intersections between a ray and triangles, that are defined by the faces array.
   *
   * @param faces The faces array.
   * @param r The ray.
   * @return A set of hits between the ray and triangles from the faces array.
   */
  private def findHitsInFaces( faces : Array[List[(Int,Option[Int],Option[Int])]], r : Ray ) : Set[GeometryHit] = {
    val hits = collection.mutable.Set[GeometryHit]()
    for( face <- faces ) {
      val a = vertices( face(0)._1 )
      val b = vertices( face(1)._1 )
      val c = vertices( face(2)._1 )

      val (an,bn,cn) = if( face(0)._3.isDefined ) {
        (normals(face(0)._3.get),normals(face(1)._3.get),normals(face(2)._3.get))
      } else {
        val n = (b-c x a-c).asNormal
        (-n,-n,-n)
      }

      val (at,bt,ct) = if( face(0)._2.isDefined ) {
        (texCoords(face(0)._2.get),texCoords(face(1)._2.get),texCoords(face(2)._2.get))
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
        hits += GeometryHit( r, this, t, an * alpha + bn * beta + cn * gamma, at * alpha + bt * beta + ct * gamma )
      }
    }
    hits.toSet
  }
}
