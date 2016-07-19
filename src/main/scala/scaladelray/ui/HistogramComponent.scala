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

package scaladelray.ui

import java.awt.Graphics2D

import scala.swing.Component
import scaladelray.HDRImage

class HistogramComponent( private var _img : HDRImage ) extends Component {

  private val min = 0.0 // Double.MaxValue
  private val max = 1.0 // Double.MinValue

  def img = _img
  def img_=( newImage : HDRImage ) {
    _img = newImage
    this.repaint()
  }

  override def paintComponent( g: Graphics2D ) = {

    val w = this.size.getWidth.toInt
    val h = this.size.getHeight.toInt

    val his =  new Array[Int]( w )
    val binSize = (max - min) / (w-2)

    for {
      x <- 0 until _img.size.width
      y <- 0 until _img.size.height
    } {
      val c = _img( x, y )
      val b = (c.r + c.g + c.b) / 3.0
      if( b < min ) his(0) = his(0) + 1
      else if( b > max ) his(w-1) = his(w-1) + 1
      else {
        val i = (b / binSize).toInt
        his( i + 1 ) = his( i + 1 ) + 1
      }
    }

    var maxH = Int.MinValue
    for( v <- his ) maxH = math.max( v, maxH )
    g.setColor( java.awt.Color.black )
    for( i <- 0 until w ) {
      g.drawLine( i, h-1, i, h-1-(h*(his(i).toDouble/maxH.toDouble)).toInt )
    }

  }

}
