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

import scala.swing.{Frame, Component}
import java.awt.{Graphics2D, Dimension}
import java.awt.image.BufferedImage
import scaladelray.HDRImage

class HDRImageWindow( img : HDRImage ) extends Frame {



  title = "WindowedRayTracer"
  minimumSize = new Dimension( img.width, img.height )
  size = new Dimension( img.width, img.height )
  resizable = false
  visible = true

  contents = new Component {
    override def paintComponent( g: Graphics2D ) = {
      val image = new BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB)
      val model = image.getColorModel
      val raster = image.getRaster

      for {
        x <- 0 until img.width
        y <- 0 until img.height} {
          val c = img( x, y )
          val color = new java.awt.Color( c.r.asInstanceOf[Float], c.g.asInstanceOf[Float], c.b.asInstanceOf[Float] )
          raster.setDataElements(x, img.height-1-y, model.getDataElements(color.getRGB, null))
      }
      g.drawImage(image, 0, 0, null)
    }
  }
}
