/*
 * Copyright 2015 Stephan Rehfeld
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

package scaladelray.math

/**
 * A transformable is an object that can be transformed by an affine transformation. Points, directions,
 * and rays can be multiplied with the transformation and its inverse.
 */
abstract class Transformable( val t : Transform ) {

  /**
   * This function transforms a ray "downward" a scene graph. Hence, it multiplies
   * the origin and the direction with the inverse of the transformation matrix.
   * A ray that was in the world coordinate system is the same ray in the local coordinate
   * system of the underlying nodes.
   *
   * @param r The ray to transform.
   * @return The ray transformed by the inverse of the transformation matrix.
   */
  protected def down( r : Ray ) : Ray = {
    Ray( down( r.o ), down( r.d ) )
  }

  /**
   * This function transforms a ray "upward" a scene graph. Hence, it multiplies
   * the origin and the direction with the transformation matrix.
   * A ray that was in the local coordinate system of the object is the same ray
   * in the world coordinate system.
   *
   * @param r The ray to transform.
   * @return The ray transformed by the transformation matrix.
   */
  protected def up( r : Ray ) : Ray = {
    Ray( up( r.o ), up( r.d ) )
  }

  /**
   * This function transforms a point "downward" a scene graph. Hence, it multiplies
   * the point with the inverse of the transformation matrix.
   * A point that was in the world coordinate system is the same point in the local coordinate
   * system of the underlying nodes.
   *
   * @param p The point to transform.
   * @return The point transformed by the inverse of the transformation matrix.
   */
  protected def down( p : Point3 ) : Point3 = {
    t.i * p
  }

  /**
   * This function transforms a point "upward" a scene graph. Hence, it multiplies
   * the point with the transformation matrix.
   * A point that was in the local coordinate system of the object is the same point
   * in the world coordinate system.
   *
   * @param p The point to transform.
   * @return The point transformed by the transformation matrix.
   */
  protected def up( p : Point3 ) : Point3 = {
    t.m * p
  }

  /**
   * This function transforms a vector "downward" a scene graph. Hence, it multiplies
   * the vector with the inverse of the transformation matrix.
   * A vector that was in the world coordinate system is the same vector in the local coordinate
   * system of the underlying nodes.
   *
   * @param v The vector to transform.
   * @return The vector transformed by the inverse of the transformation matrix.
   *
   */
  protected def down( v : Vector3 ) : Vector3 = {
    t.i * v
  }

  /**
   * This function transforms a vector "upward" a scene graph. Hence, it multiplies
   * the vector with the transformation matrix.
   * A vector that was in the local coordinate system of the object is the same vector
   * in the world coordinate system.
   *
   * @param v The vector to transform.
   * @return The vector transformed by the transformation matrix.
   */
  protected def up( v : Vector3 ) : Vector3 = {
    t.m * v
  }

  /**
   * This function transforms a vector "upward" a scene graph. Hence, it multiplies
   * the vector with the transposed inverse of the transformation matrix.
   * A normal that was in the local coordinate system of the object is the same normals
   * in the world coordinate system.
   *
   * @param n The normal to transform.
   * @return The normal transformed by the transformation matrix.
   */
  protected def up( n : Normal3 ) : Normal3 = {
    (t.i.transposed * n).normalized
  }

}
