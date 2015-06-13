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

import scaladelray.math.{Vector3, Point3, Ray, Transform}


/**
 * A scene graph node that combines several geometries under one node and applies a transformation on the geometries.
 *
 * @param t The transformation of the objects.
 * @param nodes The geometries of the node.
 */
case class Node( t : Transform, nodes : Geometry* ) extends Geometry with Serializable {

  override val normalMap = None

  override def <--(r: Ray) : Set[GeometryHit] = {
    var hits = Set[GeometryHit]()
    val transformedRay = Ray( t.i * r.o, t.i * r.d )
    for( node <- nodes ) {
      hits = hits | (transformedRay --> node )
    }
    for( hit <- hits ) yield GeometryHit( r, hit.geometry, hit.t, SurfacePoint( t.m * hit.sp.p, (t.i.transposed * hit.sp.n).normalized, (t.i.transposed * hit.sp.tan).normalized, (t.i.transposed * hit.sp.biTan).normalized,  hit.sp.t ) )
  }


  override val center: Point3 = (nodes.foldLeft( Vector3( 0, 0, 0 ) )( (v,g) => v + g.center.asVector ) / nodes.size).asPoint

  override val lbf: Point3 = nodes.foldLeft( Point3( Double.MaxValue, Double.MaxValue, Double.MaxValue ) )( (v,g) => Point3( math.min( v.x, g.lbf.x ), math.min( v.y, g.lbf.y ), math.min( v.z, g.lbf.z ) ) )

  override val run: Point3 = nodes.foldLeft( Point3( Double.MinValue, Double.MinValue, Double.MinValue ) )( (v,g) => Point3( math.max( v.x, g.run.x ), math.max( v.y, g.run.y ), math.max( v.z, g.run.z ) ) )

  override val axis = nodes.foldLeft( Vector3( 0, 0, 0 ) )( (v,g) => v + g.axis ) / nodes.size
}
