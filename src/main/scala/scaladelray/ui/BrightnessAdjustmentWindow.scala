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

import scala.swing.{TextField, Label, GridBagPanel, Frame}
import java.awt.Dimension
import scaladelray.{Color, HDRImage}
import scala.swing.GridBagPanel.Fill
import scala.swing.event.EditDone

class BrightnessAdjustmentWindow( img : HDRImage, p : HDRImageWindow ) extends Frame {

  title = "Histogram"
  minimumSize = new Dimension( 400, 600 )
  size = new Dimension( 400, 600 )
  resizable = false
  visible = true

  val (min,max) = this.calcMinMax()

  private def calcMinMax() : (Double,Double) = {
    var min = Double.MaxValue
    var max = Double.MinValue

    for {
      x <- 0 until img.width
      y <- 0 until img.height
    } {
      val c = img( x, y )

      val b = (c.r + c.g + c.b) / 3.0
      min = math.min( min, b )
      max = math.max( max, b )
    }
    (min,max)
  }

  private var b = 0.0
  private var w = 1.0
  private var o = 0.0

  private def adjust( i : HDRImage, black : Double, white : Double, offset : Double ) : HDRImage = {
    val newImage = new HDRImage( i.width, i.height )
    for{
      x <- 0 until newImage.width
      y <- 0 until newImage.height
    } {
      val c = i( x, y )
      val r = (c.r - black) / (white-black) + offset
      val g = (c.g - black) / (white-black) + offset
      val b = (c.b - black) / (white-black) + offset
      newImage.set( x, y, Color( math.max(0,r), math.max(0,g), math.max(0,b)) )

    }
    newImage
  }

  contents = new GridBagPanel {
    val c = new Constraints
    val shouldFill = true

    if( shouldFill ) c.fill = Fill.Horizontal


    val originalHistogram = new HistogramComponent( img )

    c.fill = Fill.Both
    c.weightx = 0.5
    c.weighty = 0.5
    c.gridx = 0
    c.gridy = 0
    c.gridwidth = 2
    layout( originalHistogram ) = c

    val origMinLabel = new Label( "Min:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.weighty = 0.0
    c.gridx = 0
    c.gridy = 1
    c.gridwidth = 1
    layout( origMinLabel ) = c

    val origMin = new TextField( min.toString )
    origMin.editable = false
    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 1
    c.gridwidth = 0
    layout( origMin ) = c

    val origMaxLabel = new Label( "Max:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.gridx = 0
    c.gridy = 2
    c.gridwidth = 1
    layout( origMaxLabel ) = c

    val origMax = new TextField( max.toString )
    origMax.editable = false

    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 2
    c.gridwidth = 0
    layout( origMax ) = c

    val blackLabel = new Label( "Black:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.gridx = 0
    c.gridy = 3
    c.gridwidth = 1
    layout( blackLabel ) = c

    val black = new TextField( b.toString )  {
      listenTo( this )
      reactions += {
        case e : EditDone =>
          try {
            val v = text.toDouble
            if( v >= 0.0 && v <= w ) {
              b = v
              val newImage = adjust( img, b, w, o )
              resultHistogram.img = newImage
              p.img = newImage
            } else
              text = b.toString
          } catch {
            case _ : Throwable => text = b.toString
          }
      }
    }


    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 3
    c.gridwidth = 0
    layout( black ) = c

    val whiteLabel = new Label( "White:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.gridx = 0
    c.gridy = 4
    c.gridwidth = 1
    layout( whiteLabel ) = c

    val white = new TextField( w.toString ) {
      listenTo( this )
      reactions += {
        case e : EditDone =>
          try {
            val v = text.toDouble
            if( v >= 0.0 && v >=  b ) {
              w = v
              val newImage = adjust( img, b, w, o )
              resultHistogram.img = newImage
              p.img = newImage
            } else
              text = w.toString

          } catch {
            case _ : Throwable => text = w.toString
          }
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 4
    c.gridwidth = 0
    layout( white ) = c

    val offSetLabel = new Label( "Offset:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.gridx = 0
    c.gridy = 5
    c.gridwidth = 1
    layout( offSetLabel ) = c

    val offset = new TextField( o.toString ) {
      listenTo( this )
      reactions += {
        case e : EditDone =>
          try {
            val v = text.toDouble
            if( v >= 0.0 ) {
              o = v
              val newImage = adjust( img, b, w, o )
              resultHistogram.img = newImage
              p.img = newImage
            } else
              text = o.toString
          } catch {
            case _ : Throwable => text = o.toString
          }
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 5
    c.gridwidth = 0
    layout( offset ) = c

    val resultHistogram = new HistogramComponent( img )

    c.fill = Fill.Both
    c.weightx = 0.5
    c.weighty = 0.5
    c.gridx = 0
    c.gridy = 6
    c.gridwidth = 2
    layout( resultHistogram ) = c

  }

}
