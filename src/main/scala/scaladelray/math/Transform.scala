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
class Transform private ( val m : Mat4x4, val i : Mat4x4 ) {

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
   * This method appends a translation by a given direction to the current transformation and returns
   * a new Transformation object.
   *
   * @param v The direction of the translation.
   * @return A new Transformation object with the appended translation.
   */
  def translate( v : Vector3 ) : Transform = this.translate( v.x, v.y, v.z )

  def scale( x : Double, y : Double, z : Double ) : Transform = {
    val t = Transform.scale( x, y, z )
    new Transform( m * t.m, t.i * i )
  }

  def rotateX( angle : Double ) : Transform = {
    val t = Transform.rotateX( angle )
    new Transform( m * t.m, t.i * i )
  }

  def rotateY( angle : Double ) : Transform = {
    val t = Transform.rotateY( angle )
    new Transform( m * t.m, t.i * i )
  }

  def rotateZ( angle : Double ) : Transform = {
    val t = Transform.rotateZ( angle )
    new Transform( m * t.m, t.i * i )
  }


  override def equals( obj: Any ) =
    obj match {
      case t : Transform =>
        this.m == t.m && this.i == t.i
      case _ =>
        false
    }


  def *( r : Ray ) = Ray( i * r.o, i * r.d )
  def *( n : Normal3 ) = (i.transposed * n.asVector).normalized.asNormal

}


object Transform {

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

  def translate( p : Point3 ) : Transform = Transform.translate( p.x, p.y, p.z )

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

}