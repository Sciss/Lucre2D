/*
 *  Graphics.scala
 *  (Lucre2D)
 *
 *  Copyright (c) 2022 Hanns Holger Rutz. All rights reserved.
 *
 *	This software is published under the GNU Affero General Public License v3+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package de.sciss.lucre.canvas.graph

import de.sciss.lucre.canvas.Graphics2D
import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.canvas.graph.impl.GraphicsBuilderImpl
import de.sciss.lucre.expr.graph.Ex

object Graphics {
  trait Elem {
    def render(g: Graphics2D): Unit
  }

//  def apply(elem: Ex[Elem]*): Graphics = Graphics(elem: Ex[Seq[Elem]])

  def use(fun: Builder => Unit): Graphics = {
    val b = new GraphicsBuilderImpl
    fun(b)
    b.result()
  }

  trait Builder {
    def point(x: Ex[Double], y: Ex[Double]): Unit

    def line(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double]): Unit

    def rect(a: Ex[Double], b: Ex[Double], c: Ex[Double], d: Ex[Double]): Unit

    def ellipse(a: Ex[Double], b: Ex[Double], c: Ex[Double], d: Ex[Double]): Unit

    def triangle(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double], x3: Ex[Double], y3: Ex[Double]): Unit

    def quad(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double],
             x3: Ex[Double], y3: Ex[Double], x4: Ex[Double], y4: Ex[Double]): Unit

    def arc(a: Ex[Double], b: Ex[Double], c: Ex[Double], d: Ex[Double], start: Ex[Double], stop: Ex[Double]): Unit

    def background(gray: Ex[Double]): Unit

    def fill  (gray: Ex[Double]): Unit
    def stroke(gray: Ex[Double]): Unit

    def fill(r: Ex[Double], g: Ex[Double], b: Ex[Double]): Unit

    def noFill  (): Unit
    def noStroke(): Unit

    def rectMode(mode: Int): Unit

    def strokeWeight(weight: Ex[Double]): Unit

    def translate(x: Ex[Double], y: Ex[Double]): Unit

    def scale(a: Ex[Double]): Unit
    def scale(x: Ex[Double], y: Ex[Double]): Unit

    def rotate(angle: Ex[Double]): Unit

    def pushMatrix(): Unit
    def popMatrix (): Unit

    def beginShape(): Unit
    def vertex(x: Ex[Double], y: Ex[Double]): Unit
    def endShape(mode: Ex[Boolean] = CLOSE): Unit

    def OPEN  : Boolean
    def CLOSE : Boolean

    /** Draw mode convention to use (x, y) to (width, height) */
    def CORNER: Int
    /** Draw mode convention to use (x1, y1) to (x2, y2) coordinates */
    def CORNERS: Int
    /** Draw mode from the center, and using the radius */
    def RADIUS: Int
    /**
     * Draw from the center, using second pair of values as the diameter.
     * Formerly called CENTER_DIAMETER in alpha releases.
     */
    def CENTER: Int
    /**
     * Synonym for the CENTER constant. Draw from the center,
     * using second pair of values as the diameter.
     */
    def DIAMETER: Int

    //    def Pi    : Double
    def TwoPi : Double
  }
}
case class Graphics(/*width: Ex[Double], height: Ex[Double], */ elem: Ex[Seq[Elem]])
