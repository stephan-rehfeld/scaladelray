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

  val min = img.min
  val max = img.max

  private var newMin = min
  private var newMax = max

  private def adjust( i : HDRImage, oldMin : Double, oldMax : Double, newMin : Double, newMax : Double ) : HDRImage = {
    val newImage = new HDRImage( i.width, i.height )
    for{
      x <- 0 until newImage.width
      y <- 0 until newImage.height
    } {
      val c = i( x, y )
      val r = (c.r - oldMin) * ((newMax-newMin)/(oldMax-oldMin)) + newMin
      val g = (c.g - oldMin) * ((newMax-newMin)/(oldMax-oldMin)) + newMin
      val b = (c.b - oldMin) * ((newMax-newMin)/(oldMax-oldMin)) + newMin
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

    val origMinLabel = new Label( "Original Min:")

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
    c.gridwidth = 1
    layout( origMin ) = c

    val origMaxLabel = new Label( "Original Max:")

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
    c.gridwidth = 1
    layout( origMax ) = c


    val mapToLabel = new Label( "Map to")

    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 0
    c.gridy = 3
    c.gridwidth = 2
    layout( mapToLabel ) = c

    val newMinLabel = new Label( "New Min:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.gridx = 0
    c.gridy = 4
    c.gridwidth = 1
    layout( newMinLabel ) = c

    val newMin = new TextField( BrightnessAdjustmentWindow.this.newMin.toString )  {
      listenTo( this )
      reactions += {
        case e : EditDone =>
          try {
            val v = text.toDouble
            if( v <= BrightnessAdjustmentWindow.this.newMax ) {
              BrightnessAdjustmentWindow.this.newMin = v
              val newImage = adjust( img, min, max, BrightnessAdjustmentWindow.this.newMin, BrightnessAdjustmentWindow.this.newMax )
              resultHistogram.img = newImage
              p.img = newImage
            } else
              text = BrightnessAdjustmentWindow.this.newMin.toString
          } catch {
            case _ : Throwable => text = BrightnessAdjustmentWindow.this.newMin.toString
          }
      }
    }


    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 4
    c.gridwidth = 1
    layout( newMin ) = c

    val newMaxLabel = new Label( "New Max:")

    c.fill = Fill.Horizontal
    c.weightx = 0.3
    c.gridx = 0
    c.gridy = 5
    c.gridwidth = 1
    layout( newMaxLabel ) = c

    val white = new TextField( BrightnessAdjustmentWindow.this.newMax.toString ) {
      listenTo( this )
      reactions += {
        case e : EditDone =>
          try {
            val v = text.toDouble
            if( v >=  BrightnessAdjustmentWindow.this.newMin ) {
              BrightnessAdjustmentWindow.this.newMax = v
              val newImage = adjust( img, min, max, BrightnessAdjustmentWindow.this.newMin, BrightnessAdjustmentWindow.this.newMax )
              resultHistogram.img = newImage
              p.img = newImage
            } else
              text = BrightnessAdjustmentWindow.this.newMax.toString

          } catch {
            case t : Throwable =>
              t.printStackTrace()
              text = BrightnessAdjustmentWindow.this.newMax.toString
          }
      }
    }

    c.fill = Fill.Horizontal
    c.weightx = 0.7
    c.gridx = 1
    c.gridy = 5
    c.gridwidth = 0
    layout( white ) = c

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
