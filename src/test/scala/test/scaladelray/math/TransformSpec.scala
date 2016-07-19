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

package test.scaladelray.math

import org.scalatest.FunSpec

import scaladelray.math._
import scaladelray.math.d.Mat4x4

class TransformSpec extends FunSpec {

  describe( "The Transform companion object" ) {
    it( "should have a function to create a translate transformation for a given x, y, and z value (includes inverse matrix)" ) {
      val t = Transform.translate( 2, 3, 5 )

      val m = Mat4x4( 1.0, 0.0, 0.0, 2.0,
                      0.0, 1.0, 0.0, 3.0,
                      0.0, 0.0, 1.0, 5.0,
                      0.0, 0.0, 0.0, 1.0 )

      val i = Mat4x4( 1.0, 0.0, 0.0, -2.0,
                      0.0, 1.0, 0.0, -3.0,
                      0.0, 0.0, 1.0, -5.0,
                      0.0, 0.0, 0.0, 1.0 )

      assert( t.m == m )
      assert( t.i == i )
    }

    it( "should have a function to create a scale transformation for a given x, y, and z value (includes inverse matrix)" ) {
      val t = Transform.scale( 2, 3, 5 )

      val m = Mat4x4( 2.0, 0.0, 0.0, 0.0,
                      0.0, 3.0, 0.0, 0.0,
                      0.0, 0.0, 5.0, 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      val i = Mat4x4( 1.0/2.0, 0.0, 0.0, 0.0,
                      0.0, 1.0/3.0, 0.0, 0.0,
                      0.0, 0.0, 1.0/5.0, 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      assert( t.m == m )
      assert( t.i == i )
    }


    it( "should have a function to create a rotation around the x axis for a given angle (includes inverse matrix)" ) {
      val t = Transform.rotateX( 2 )

      val m = Mat4x4( 1.0, 0.0, 0.0, 0.0,
                      0.0, math.cos( 2 ), -math.sin( 2 ), 0.0,
                      0.0, math.sin( 2 ), math.cos( 2 ), 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      val i = Mat4x4( 1.0, 0.0, 0.0, 0.0,
                      0.0, math.cos( 2 ), math.sin( 2 ), 0.0,
                      0.0, -math.sin( 2 ), math.cos( 2 ), 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      assert( t.m == m )
      assert( t.i == i )
    }

    it( "should have a function to create a rotation around the y axis for a given angle (includes inverse matrix)" ) {
      val t = Transform.rotateY( 2 )

      val m = Mat4x4( math.cos( 2 ), 0.0, math.sin( 2 ), 0.0,
                      0.0, 1.0, 0.0, 0.0,
                      -math.sin( 2 ), 0.0, math.cos( 2 ), 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      val i = Mat4x4( math.cos( 2 ), 0.0, -math.sin( 2 ), 0.0,
                      0.0, 1.0, 0.0, 0.0,
                      math.sin( 2 ), 0.0, math.cos( 2 ), 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      assert( t.m == m )
      assert( t.i == i )
    }

    it( "should have a function to create a rotation around the z axis for a given angle (includes inverse matrix)" ) {
      val t = Transform.rotateZ( 2 )

      val m = Mat4x4( math.cos( 2 ), -math.sin( 2 ), 0.0, 0.0,
                      math.sin( 2 ), math.cos( 2 ), 0.0, 0.0,
                      0.0, 0.0, 1.0, 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      val i = Mat4x4( math.cos( 2 ), math.sin( 2 ), 0.0, 0.0,
                      -math.sin( 2 ), math.cos( 2 ), 0.0, 0.0,
                      0.0, 0.0, 1.0, 0.0,
                      0.0, 0.0, 0.0, 1.0 )

      assert( t.m == m )
      assert( t.i == i )
    }
  }

  describe( "A Transform" ) {
    it( "should be comparable" ) {
      assert( Transform.translate( 2, 3, 5 ) ==  Transform.translate( 2, 3, 5 ) )
    }

    it( "should have a function to append a translate transformation to an existing transformation for a given x, y, and z value (includes inverse matrix)" ) {
      assert( Transform.translate( 2, 3, 5 ).translate( 7, 11, 13 ) == Transform.translate( 2 + 7, 3 + 11, 5 + 13 ) )
    }

    it( "should have a function to append a scale transformation to an existing transformation for a given x, y, and z value (includes inverse matrix)" ) {
      assert( Transform.scale( 2, 3, 5 ).scale( 7, 11, 13 ) == Transform.scale( 2 * 7, 3 * 11, 5 * 13 ) )
    }

    it( "should have a function to append a rotation around the x axis to an existing transformation for a given angle (includes inverse matrix)" ) {
      assert( Transform.rotateX( 2 ).rotateX( 3 ) == Transform.rotateX( 5 ) )
    }

    it( "should have a function to append a rotation around the y axis to an existing transformation for a given angle (includes inverse matrix)" ) {
      assert( Transform.rotateY( 2 ).rotateY( 3 ) == Transform.rotateY( 5 ) )
    }

    it( "should have a function to append a rotation around the z axis to an existing transformation for a given angle (includes inverse matrix)" ) {
      assert( Transform.rotateZ( 2 ).rotateZ( 3 ) == Transform.rotateZ( 5 ) )
    }

    it( "should have an overloaded *-operator that multiplies two transforms (includes inverse matrix)" ) {
      assert( Transform.rotateZ( 2 ) * Transform.rotateZ( 3 ) == Transform.rotateZ( 5 ) )
    }


  }

}
