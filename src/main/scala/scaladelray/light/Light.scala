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

package scaladelray.light

import scaladelray.{World, Color}
import scaladelray.math.{Vector3, Point3}

trait Light {
  val color : Color
  def illuminates( point : Point3, world : World ) : List[Boolean]
  def directionFrom( point : Point3 ) : List[Vector3]
  def intensity( point : Point3 ) : List[Double]
  def samplingPoints : Int

}
