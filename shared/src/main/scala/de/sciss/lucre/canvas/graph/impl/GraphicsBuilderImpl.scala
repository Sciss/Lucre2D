package de.sciss.lucre.canvas.graph
package impl

import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.expr.graph.Ex

class GraphicsBuilderImpl extends Graphics.Builder {
  private val sq          = Seq.newBuilder[Ex[Elem]]
  private var _rectMode   = CORNER

  override def background(gray: Ex[Double]): Unit = {
    val c = Color.Gray(gray)
    sq += Background(c)
  }

  override def fill(gray: Ex[Double]): Unit = {
    val c = Color.Gray(gray)
    sq += Fill(c)
  }

  override def fill(r: Ex[Double], g: Ex[Double], b: Ex[Double]): Unit = {
    val c = Color.RGB(r, g, b)
    sq += Fill(c)
  }

  override def stroke(gray: Ex[Double]): Unit = {
    val c = Color.Gray(gray)
    sq += Stroke(c)
  }

  override def noFill(): Unit =
    sq += Fill(Paint.transparent)

  override def noStroke(): Unit =
    sq += Stroke(Paint.transparent)

  override def strokeWeight(weight: Ex[Double]): Unit =
    sq += StrokeWidth(weight)

  override def point(x: Ex[Double], y: Ex[Double]): Unit =
    sq += Point(x, y)

  override def line(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double]): Unit =
    sq += Line(x1, y1, x2, y2)

  override def rect(a: Ex[Double], b: Ex[Double], c: Ex[Double], d: Ex[Double]): Unit = {
    val r =
      if      (_rectMode == CORNER) Rect(a, b, c, d)
      else if (_rectMode == CENTER) Rect(a - (c * 0.5), b - (d * 0.5), c, d)
      else throw new NotImplementedError(s"rectMode ${_rectMode}")
    sq += r
  }

  override def rectMode(mode: Int): Unit =
    _rectMode = mode

  override def ellipse(a: Ex[Double], b: Ex[Double], c: Ex[Double], d: Ex[Double]): Unit = {
    // XXX TODO: 'ellipseMode'
    sq += Ellipse(a, b, c * 0.5, d * 0.5)
  }

  override def triangle(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double],
                        x3: Ex[Double], y3: Ex[Double]): Unit = {
    beginShape() // XXX TODOO: TRIANGLES
    vertex(x1, y1)
    vertex(x2, y2)
    vertex(x3, y3)
    endShape()
  }

  override def quad(x1: Ex[Double], y1: Ex[Double], x2: Ex[Double], y2: Ex[Double],
                    x3: Ex[Double], y3: Ex[Double], x4: Ex[Double], y4: Ex[Double]): Unit = {
    beginShape() // XXX TODOO: QUADS
    vertex(x1, y1)
    vertex(x2, y2)
    vertex(x3, y3)
    vertex(x4, y4)
    endShape()
  }

  override def arc(a: Ex[Double], b: Ex[Double], c: Ex[Double], d: Ex[Double],
                   start: Ex[Double], stop: Ex[Double]): Unit = {
    println("TODO: arc")
  }

  override def translate(x: Ex[Double], y: Ex[Double]): Unit =
    sq += Translate(x, y)

  override def rotate(angle: Ex[Double]): Unit =
    sq += Rotate(angle)

  override def scale(a: Ex[Double]): Unit =
    sq += Scale(a, a)

  override def scale(x: Ex[Double], y: Ex[Double]): Unit =
    sq += Scale(x, y)

  override def pushMatrix(): Unit =
    sq += PushMatrix()

  override def popMatrix(): Unit =
    sq += PopMatrix()

  override def TwoPi    : Double  = math.Pi * 2

  override def OPEN     : Boolean = false
  override def CLOSE    : Boolean = true

  override def CORNER   : Int = 0
  override def CORNERS  : Int = 1
  override def RADIUS   : Int = 2
  override def CENTER   : Int = 3
  override def DIAMETER : Int = 3

  private val _vertexBuilder = Seq.newBuilder[Ex[Double]]

  override def beginShape(): Unit =
    _vertexBuilder.clear()

  override def vertex(x: Ex[Double], y: Ex[Double]): Unit = {
    _vertexBuilder += x
    _vertexBuilder += y
  }

  override def endShape(mode: Ex[Boolean]): Unit =
    sq += Poly(_vertexBuilder.result(), mode)

  def result(): Graphics = Graphics(sq.result())
}
