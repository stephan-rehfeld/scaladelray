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

package scaladelray.math

import scaladelray.math.d.{Mat4x4, Point3}

/**
 * This class represents a transform that is used by [[scaladelray.geometry.Node]] to transform the geometries within
 * the scene graph node. A new Transform object is not directly created by calling a constructor, but by using methods
 * of the companion object.
 *
 * The Transformation keeps track of the transformation matrix and the inverse matrix of it. New transformations are
 * appended by calling the transformation methods of this object. The transformation is appended on the right side.
 *
 * A transformation object is immutable.
 *
 * @author Stephan Rehfeld
 * @param m The transformation matrix of this transformation.
 * @param i The inverse transformation matrix of this transformation.
 */
class Transform private ( val m : Mat4x4, val i : Mat4x4 ) extends Serializable {

  /**
   * This method appends a translation by the given x, y, and z values to the current transformation and returns
   * a new Transformation object.
   *
   * @param x The x value for the translation.
   * @param y The y value for the translation.
   * @param z The z value for the translation.
   * @return A new Transformation object with the appended translation.
   */
  def translate( x : Double, y : Double, z : Double ) : Transform = {
    val t = Transform.translate( x, y, z )
    new Transform( m * t.m, t.i * i )
  }

  /**
   * This method appends a translation by a given point to the current transformation and returns
   * a new Transformation object.
   *
   * @param p The point of the translation.
   * @return A new Transformation object with the appended translation.
   */
  def translate( p : Point3 ) : Transform = this.translate( p.x, p.y, p.z )

  /**
   * This method appends a scale transformation by given factors to an existing transformation and returns a new
   * transformation object.
   *
   * @param x The scale factor for the x axis.
   * @param y The scale factor for the y axis.
   * @param z The scale factor for the z axis.
   * @return The new Transformation object with the appended scale transformation.
   */
  def scale( x : Double, y : Double, z : Double ) : Transform = {
    val t = Transform.scale( x, y, z )
    new Transform( m * t.m, t.i * i )
  }

  /**
   * This method appends a rotation around the x axis to the transformation and returns a new transformation object.
   *
   * @param angle The angle of the rotation in radians.
   * @return The new Transformation object with the appended rotation around the x axis.
   */
  def rotateX( angle : Double ) : Transform = {
    val t = Transform.rotateX( angle )
    new Transform( m * t.m, t.i * i )
  }

  /**
   * This method appends a rotation around the y axis to the transformation and returns a new transformation object.
   *
   * @param angle The angle of the rotation in radians.
   * @return The new Transformation object with the appended rotation around the y axis.
   */
  def rotateY( angle : Double ) : Transform = {
    val t = Transform.rotateY( angle )
    new Transform( m * t.m, t.i * i )
  }

  /**
   * This method appends a rotation around the z axis to the transformation and returns a new transformation object.
   *
   * @param angle The angle of the rotation in radians.
   * @return The new Transformation object with the appended rotation around the z axis.
   */
  def rotateZ( angle : Double ) : Transform = {
    val t = Transform.rotateZ( angle )
    new Transform( m * t.m, t.i * i )
  }

  /**
   * Multiplies two transforms.
   *
   * @param t The rhs.
   * @return The multiplied transforms.
   */
  def *( t : Transform ) = new Transform( t.m * m, i * t.i )


  override def equals( obj: Any ) =
    obj match {
      case t : Transform =>
        this.m == t.m && this.i == t.i
      case _ =>
        false
    }

}

/**
 * The companion object is the starting point to create a transformation.
 *
 * {{{
 *   val t = Transform.translate( 0, 2, 0 ).scale( 1, 2, 1 ).rotateX( 2.1 )
 * }}}
 *
 * @author Stephan Rehfeld
 */
object Transform {

  /**
   * This function creates a transformation that translates an object by a given x, y, and z value.
   *
   * @param x The x value for the translation.
   * @param y The y value for the translation.
   * @param z The z value for the translation.
   * @return A translate transformation with the given x, y, and z value.
   */
  def translate( x : Double, y : Double, z : Double ) = new Transform(
    Mat4x4( 1.0, 0.0, 0.0, x,
            0.0, 1.0, 0.0, y,
            0.0, 0.0, 1.0, z,
            0.0, 0.0, 0.0, 1.0 ),
    Mat4x4( 1.0, 0.0, 0.0, -x,
            0.0, 1.0, 0.0, -y,
            0.0, 0.0, 1.0, -z,
            0.0, 0.0, 0.0, 1.0 )
  )

  /**
   * This function creates a transformation that translates an object by a given point.
   *
   * @param p The point to which the object should be translated.
   * @return A translate transformation with the given direction.
   */
  def translate( p : Point3 ) : Transform = Transform.translate( p.x, p.y, p.z )

  /**
   * This function creates a scale transformation with the given factors for the x, y, and z axis.
   *
   * @param x The scale factor for the x axis.
   * @param y The scale factor for the y axis.
   * @param z The scale factor for the z axis.
   * @return A scale transformation with the given factors.
   */
  def scale( x : Double, y : Double, z : Double ) = new Transform(
    Mat4x4( x,   0.0, 0.0, 0.0,
            0.0, y,   0.0, 0.0,
            0.0, 0.0, z,   0.0,
            0.0, 0.0, 0.0, 1.0 ),

    Mat4x4( 1.0/x, 0.0,   0.0,   0.0,
            0.0,   1.0/y, 0.0,   0.0,
            0.0,   0.0,   1.0/z, 0.0,
            0.0,   0.0,   0.0,   1.0 )
  )

  /**
   * This function creates a rotation around the x axis.
   *
   * @param angle The angle in radians.
   * @return A rotation around the x axis.
   */
  def rotateX( angle : Double ) = new Transform(
    Mat4x4( 1.0, 0.0,               0.0,                0.0,
            0.0, math.cos( angle ), -math.sin( angle ), 0.0,
            0.0, math.sin( angle ), math.cos( angle ),  0.0,
            0.0, 0.0,                0.0,               1.0 ),
    Mat4x4( 1.0, 0.0,                0.0,               0.0,
            0.0, math.cos( angle ),  math.sin( angle ), 0.0,
            0.0, -math.sin( angle ), math.cos( angle ), 0.0,
            0.0, 0.0,                0.0,               1.0 )
  )

  /**
   * This function creates a rotation around the y axis.
   *
   * @param angle The angle in radians.
   * @return A rotation around the y axis.
   */
  def rotateY( angle : Double ) = new Transform(
      Mat4x4( math.cos( angle ),  0.0, math.sin( angle ), 0.0,
              0.0,                1.0, 0.0,               0.0,
              -math.sin( angle ), 0.0, math.cos( angle ), 0.0,
              0.0,                0.0, 0.0,               1.0 ),
      Mat4x4( math.cos( angle ), 0.0, -math.sin( angle ), 0.0,
              0.0,               1.0, 0.0,                0.0,
              math.sin( angle ), 0.0, math.cos( angle ),  0.0,
              0.0,               0.0, 0.0,                1.0 )
  )

  /**
   * This function creates a rotation around the z axis.
   *
   * @param angle The angle in radians.
   * @return A rotation around the z axis.
   */
  def rotateZ( angle : Double ) = new Transform(
    Mat4x4( math.cos( angle ), -math.sin( angle ), 0.0, 0.0,
            math.sin( angle ), math.cos( angle ),  0.0, 0.0,
            0.0,               0.0,                1.0, 0.0,
            0.0,               0.0,                0.0, 1.0 ),
    Mat4x4( math.cos( angle ),  math.sin( angle ), 0.0, 0.0,
            -math.sin( angle ), math.cos( angle ), 0.0, 0.0,
            0.0,                0.0,               1.0, 0.0,
            0.0,                0.0,               0.0, 1.0 )
  )

  def apply() = new Transform(
    Mat4x4( 1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0 ),

    Mat4x4( 1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0 )
  )

}