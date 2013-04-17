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

case class Mat4x4( m11 : Double, m12 : Double, m13 : Double, m14 : Double,
                   m21 : Double, m22 : Double, m23 : Double, m24 : Double,
                   m31 : Double, m32 : Double, m33 : Double, m34 : Double,
                   m41 : Double, m42 : Double, m43 : Double, m44 : Double ) {

  def *( v : Vector3 ) = Vector3( m11 * v.x + m12 * v.y + m13 * v.z,
                                  m21 * v.x + m22 * v.y + m23 * v.z,
                                  m31 * v.x + m32 * v.y + m33 * v.z )

  def *( p : Point3 ) = Point3( m11 * p.x + m12 * p.y + m13 * p.z + m14,
                                m21 * p.x + m22 * p.y + m23 * p.z + m24,
                                m31 * p.x + m32 * p.y + m33 * p.z + m34 )

  def *( m : Mat4x4 ) = Mat4x4( m11*m.m11+m12*m.m21+m13*m.m31+m14*m.m41, m11*m.m12+m12*m.m22+m13*m.m32+m14*m.m42, m11*m.m13+m12*m.m23+m13*m.m33+m14*m.m43, m11*m.m14+m12*m.m24+m13*m.m34+m14*m.m44,
                                m21*m.m11+m22*m.m21+m23*m.m31+m24*m.m41, m21*m.m12+m22*m.m22+m23*m.m32+m24*m.m42, m21*m.m13+m22*m.m23+m23*m.m33+m24*m.m43, m21*m.m14+m22*m.m24+m23*m.m34+m24*m.m44,
                                m31*m.m11+m32*m.m21+m33*m.m31+m34*m.m41, m31*m.m12+m32*m.m22+m33*m.m32+m34*m.m42, m31*m.m13+m32*m.m23+m33*m.m33+m34*m.m43, m31*m.m14+m32*m.m24+m33*m.m34+m34*m.m44,
                                m41*m.m11+m42*m.m21+m43*m.m31+m44*m.m41, m41*m.m12+m42*m.m22+m43*m.m32+m44*m.m42, m41*m.m13+m42*m.m23+m43*m.m33+m44*m.m43, m41*m.m14+m42*m.m24+m43*m.m34+m44*m.m44 )

  def transposed = Mat4x4( m11, m21, m31, m41,
                           m12, m22, m32, m42,
                           m13, m23, m33, m43,
                           m14, m24, m34, m44
  )


}