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

import scaladelray.math.d.{Direction3, Normal3, Point3}
import scaladelray.math.Ray
import scaladelray.texture.{TexCoord2D, Texture}


/**
 * This class represents a hit of a ray with a geometry.
 *
 * @author Stephan Rehfeld
 *
 * @param ray The ray that hits the geometry.
 * @param geometry The geometry hit by the ray.
 * @param t The t value of the hit.
 * @param sp The surface point at the intersection.
 */
case class GeometryHit( ray : Ray, geometry : Geometry, t : Double, sp : SurfacePoint ) extends Serializable

/**
 * An instance of this class describes a point on a surface in space with all its attributes.
 *
 * @param p Location of the point in space
 * @param n The normal at the point
 * @param tan The tangent at the point.
 * @param biTan The bitangent at the point.
 * @param t The texture coordinate at the point.
 */
case class SurfacePoint(p : Point3, n : Normal3, tan : Direction3, biTan : Direction3, t : TexCoord2D ) extends Serializable

/**
 * The base class for all geometries.
 *
 */
abstract class Geometry extends Serializable {

  /**
   * This arrow operator means "shoot the ray on the geometry." (Nice, isn't it?)
   *
   * It returns all hits between the geometry and the ray in a set. This may includes all hits where t is smaller than 0.
   *
   * @param r The ray that's been shot.
   * @return All hits between the ray an the geometry. Maybe empty.
   */
  def <-- ( r : Ray ) : Set[GeometryHit]

  /**
   * An optional normal map. It is assumed that the normal map is in tangent space.
   */
  val normalMap : Option[Texture]

  /**
   * The center point of the geometry.
   */
  val center : Point3

  /**
   * The lower bottom far point of the geometry.
   */
  val lbf : Point3

  /**
   * The right upper near point of the geometry.
   */
  val run : Point3

  /**
   * The main axis of the geometry
   */
  val axis : Direction3

}
