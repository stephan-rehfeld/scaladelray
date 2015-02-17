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

package test.scaladelray.math

import org.scalatest.FunSpec
import scaladelray.math._
import scaladelray.math.Ray
import scaladelray.math.Point3
import scala.language.reflectiveCalls

class TransformableSpec extends FunSpec {

  describe( "A Transformable" ) {
    it( "should have a function called \"down\" transforms a ray downward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val r = Ray( Point3( 1, 2, 3 ), Vector3( 1, -1, 3 ).normalized )
          assert( down(r) == Ray( t.i * r.o, t.i * r.d ))
        }
      }
      transformable.performTest()
    }

    it( "should have a function called \"up\" transforms a ray upward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val r = Ray( Point3( 1, 2, 3 ), Vector3( 1, -1, 3 ).normalized )
          assert( up(r) == Ray( t.m * r.o, t.m * r.d ))
        }
      }
      transformable.performTest()
    }

    it( "should have a function called \"down\" transforms a point downward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val p = Point3( 1, 2, 3 )
          assert( down(p) == t.i * p )
        }
      }
      transformable.performTest()
    }
    it( "should have a function called \"up\" transforms a point upward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val p = Point3( 1, 2, 3 )
          assert( up(p) == t.m * p )
        }
      }
      transformable.performTest()
    }

    it( "should have a function called \"down\" transforms a vector downward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val v = Vector3( 1, -1, 3 )
          assert( down(v) == t.i * v )
        }
      }
      transformable.performTest()
    }
    it( "should have a function called \"up\" transforms a vector upward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val v = Vector3( 1, -1, 3 )
          assert( up(v) == t.m * v )
        }
      }
      transformable.performTest()
    }

    it( "should have a function called \"up\" transforms a normal upward a scene graph" ) {
      val t = Transform.translate( 1, -1, 10 ).scale( 1, 2, 4 ).rotateX( 1.23 ).rotateY( 2.4  ).rotateZ( -2 )
      val transformable = new Transformable( t ) {
        def performTest() = {
          val n = Vector3( 1, -1, 3 ).normalized.asNormal
          assert( up(n) == (t.i.transposed * n.asVector).normalized.asNormal )
        }
      }
      transformable.performTest()
    }
  }

}
