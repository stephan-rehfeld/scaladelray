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

package scaladelray.examples

import scaladelray.{Color, World}
import scaladelray.geometry.{Plane, Node, Geometry}
import scaladelray.math.{Transform, Vector3, Point3}
import scaladelray.material.{LambertMaterial, PhongMaterial}
import scaladelray.texture.{SingleColorTexture, ChessboardTexture}
import scaladelray.light.PointLight
import scaladelray.loader.OBJLoader
import scaladelray.camera.PerspectiveCamera
import scaladelray.ui.WindowedRayTracer


object LightingExample extends WindowedRayTracer {

  def world = World(
    backgroundColor = Color( 0, 0, 0 ),
    objects = Set[Geometry]() +

    //new Plane(
      //material = ReflectiveMaterial( SingleColorTexture( Color( 0, 0.5, 0 ) ), SingleColorTexture( Color( 0, 0.5, 0 ) ), 64, SingleColorTexture( Color( 1, 1, 1 ) ) ) //LambertMaterial( SingleColorTexture( Color( 1, 0, 0 ) ) ),// SingleColorMaterial( Color( 1, 0, 0 ) ),
    new Node(
                  /*Transform.translate( 0, -1, 0 ),//.rotateY( math.Pi).rotateX( -(math.Pi / 4.0) ),
                  new Plane(
                    material = PhongMaterial( ChessboardTexture( 1, 1 ), SingleColorTexture( Color( 1, 1, 1 ) ), 64 ) //LambertMaterial( SingleColorTexture( Color( 0, 1, 0 ) ) ),// SingleColorMaterial( Color( 0, 1, 0 ) ),
                  )*/

                  Transform.translate( 0, 0, -4 ),//.rotateY( math.Pi).rotateX( -(math.Pi / 4.0) ),
                  (new OBJLoader).load( "teddy.obj", LambertMaterial( SingleColorTexture( Color( 1, 1, 1 ) ) ), (recursions,faces) => recursions < 0 )
                  //new Sphere(
                  //  material =   LambertMaterial( InterpolatedImageTexture( "erde-low.jpg" ) )

                  //)
    )/* +
    new Node(
      Transform.translate( 0, -1, 0 ),
      new Plane( PhongMaterial( ChessboardTexture( 1, 1 ), SingleColorTexture( Color( 1, 1, 1 ) ), 64 ) )
    )*/,
    ambientLight = Color( 0.0, 0.0, 0.0 ),
    lightDescriptions = Set() + new PointLight(
      position = Point3( 0, 0, 0 ),
      color = Color( 1, 1, 1 )
    )


  )

  def camera = PerspectiveCamera(
    Point3( 0, 0, 0 ),
    Vector3( 0, 0, -1 ),
    Vector3( 0, 1, 0 ),
    _,
    _,
    math.Pi * 0.25
  )

}
