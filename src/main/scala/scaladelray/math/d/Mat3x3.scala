/*
 * Copyright 2016 Stephan Rehfeld
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

package scaladelray.math.d

/**
 * This class represents a 3x3 matrix. Points and directions can be multiplied with this matrix. The determinant of the
 * matrix is calculated during construction. It has three methods to replace the columns.
 *
 * @author Stephan Rehfeld
  * @param m11 The element in the first row and the first column.
 * @param m12 The element in the first row and the second column.
 * @param m13 The element in the first row and the third column.
 * @param m21 The element in the second row and the first column.
 * @param m22 The element in the second row and the second column.
 * @param m23 The element in the second row and the third column.
 * @param m31 The element in the third row and the first column.
 * @param m32 The element in the third row and the second column.
 * @param m33 The element in the third row and the third column.
 */
case class Mat3x3( m11 : Double, m12 : Double, m13 : Double,
                   m21 : Double, m22 : Double, m23 : Double,
                   m31 : Double, m32 : Double, m33 : Double ) extends Serializable {

  /**
   * The determinant of the matrix.
   */
  val determinant = m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m13 * m22 * m31 - m12 * m21 * m33 - m11 * m23 * m32

  /**
   * This method multiplies a direction with the matrix and returns the result as a direction.
   * @param v The direction that should be multiplied with the matrix.
   * @return The result of the multiplication.
   */
  def *( v : Direction3 ) = Direction3( m11 * v.x + m12 * v.y + m13 * v.z, m21 * v.x + m22 * v.y + m23 * v.z, m31 * v.x + m32 * v.y + m33 * v.z )

  /**
   * This method multiplies a point with the matrix and returns the result as a point.
   *
   * @param p The point that should be multiplied with the matrix.
   * @return The result of the multiplication.
   */
  def *( p : Point3 ) = Point3( m11 * p.x + m12 * p.y + m13 * p.z, m21 * p.x + m22 * p.y + m23 * p.z, m31 * p.x + m32 * p.y + m33 * p.z )

  /**
   * This method creates a matrix where the first column is replaced by the given direction.
   *
   * @param v The direction that should replace the first column.
   * @return The matrix with the replaced column.
   */
  def replaceCol1( v : Direction3 ) = Mat3x3( v.x, m12, m13,
                                          v.y, m22, m23,
                                          v.z, m32, m33 )

  /**
   * This method creates a matrix where the second column is replaced by the given direction.
   *
   * @param v The direction that should replace the second column.
   * @return The matrix with the replaced column.
   */
  def replaceCol2( v : Direction3 ) = Mat3x3( m11, v.x, m13,
                                          m21, v.y, m23,
                                          m31, v.z, m33 )

  /**
   * This method creates a matrix where the third column is replaced by the given direction.
   *
   * @param v The direction that should replace the third column.
   * @return The matrix with the replaced column.
   */
  def replaceCol3( v : Direction3 ) = Mat3x3( m11, m12, v.x,
                                          m21, m22, v.y,
                                          m31, m32, v.z )

}
