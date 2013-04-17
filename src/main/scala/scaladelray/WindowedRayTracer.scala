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

package scaladelray

import camera.Camera
import swing.{Component, MainFrame, SimpleSwingApplication}
import java.awt.{Graphics2D, Dimension}
import java.awt.image.BufferedImage


abstract class WindowedRayTracer extends SimpleSwingApplication {

  def world : World
  def camera : ((Int,Int) => Camera)

  def top = new MainFrame {

    title = "WindowedRayTracer"
    minimumSize = new Dimension( 640, 480 )

    contents = new Component {
      override def paintComponent( g: Graphics2D ) = {
        val image = new BufferedImage(this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int], BufferedImage.TYPE_INT_ARGB)
        val model = image.getColorModel
        val raster = image.getRaster
        val cam = camera( this.size.getWidth.asInstanceOf[Int], this.size.getHeight.asInstanceOf[Int] )
        val myWorld = world
        for( x <- 0 until this.size.getWidth.asInstanceOf[Int] )
          for( y <- 0 until this.size.getHeight.asInstanceOf[Int] ) {
            val ray = cam( x, y )
            raster.setDataElements(x, this.size.getHeight.asInstanceOf[Int]-1-y, model.getDataElements((Tracer.standardTracer( ray, myWorld, 9 ).rgbInteger), null))


          }
        //raster.setDataElements(x, y, model.getDataElements((if (x == y) java.awt.Color.RED.getRGB else java.awt.Color.BLACK.getRGB), null))
        g.drawImage(image, 0, 0, null)
      }
    }
  }

}
