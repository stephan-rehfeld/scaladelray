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


class Transform private ( val m : Mat4x4, val i : Mat4x4 ) {

  def translate( x : Double, y : Double, z : Double ) : Transform = {
    val t = Transform.translate( x, y, z )
    new Transform( m * t.m, t.i * i )
  }

  def translate( p : Point3 ) : Transform = this.translate( p.x, p.y, p.z )

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