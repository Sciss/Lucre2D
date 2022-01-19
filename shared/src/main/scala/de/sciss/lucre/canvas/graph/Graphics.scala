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
    def line(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double]): Unit

    def background(gray: Ex[Double]): Unit

    def translate(x: Ex[Double], y: Ex[Double]): Unit

    def rotate(angle: Ex[Double]): Unit

    def pushMatrix(): Unit
    def popMatrix (): Unit
  }
}
case class Graphics(/*width: Ex[Double], height: Ex[Double], */ elem: Ex[Seq[Elem]])
