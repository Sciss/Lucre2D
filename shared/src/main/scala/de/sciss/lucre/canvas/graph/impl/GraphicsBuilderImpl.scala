package de.sciss.lucre.canvas.graph
package impl

import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.expr.graph.Ex

class GraphicsBuilderImpl extends Graphics.Builder {
  private val sq = Seq.newBuilder[Ex[Elem]]

  override def background(gray: Ex[Double]): Unit = {
    val c = Color.Gray(gray)
    sq += Background(c)
  }

  override def line(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double]): Unit =
    sq += Line(x1, y1, x2, y2)

  override def translate(x: Ex[Double], y: Ex[Double]): Unit =
    sq += Translate(x, y)

  override def rotate(angle: Ex[Double]): Unit =
    sq += Rotate(angle)

  override def pushMatrix(): Unit =
    sq += PushMatrix()

  override def popMatrix(): Unit =
    sq += PopMatrix()

  def result(): Graphics = Graphics(sq.result())
}
