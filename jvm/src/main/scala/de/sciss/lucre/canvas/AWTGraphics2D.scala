/*
 *  AWTGraphics.scala
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

package de.sciss.lucre.canvas

import java.awt.{BasicStroke, Rectangle}
import java.awt.geom.{AffineTransform => JAffineTransform}

class AWTGraphics2D(_peer: java.awt.Graphics2D, val width: Double, val height: Double)
  extends Graphics2D {

//  private var _composite: Composite = Composite.SourceOver
  private var _font: Font = Font("SansSerif", 12)
  private var _fillPaint  : Paint   = Color.RGB4(0)
  private var _strokePaint: Paint   = Paint.Transparent
  private var _strokeWidth: Double  = 1.0
  private var _paint: Paint = null
  private var _paintSet: Boolean = false
  private var _strokeWidthSet: Double = 1.0
  private var _matrixStack = List.empty[JAffineTransform]

//  def newPeer(peer: java.awt.Graphics2D): AWTGraphics2D = {
//    val res = new AWTGraphics2D(peer)
////    res.composite = this.composite
//    res.font      = this.font
//    res.fillStyle = this.fillStyle
//    res
//  }

//  override def composite: Composite = _composite
//  override def composite_=(value: Composite): Unit = {
//    _composite = value
//    val c = value match {
//      case Composite.SourceOver => java.awt.AlphaComposite.SrcOver
//      case Composite.ColorBurn  => new ColorBurnComposite(1f)
//    }
//    _peer.setComposite(c)
//  }

  override def translate(tx: Double, ty: Double): Unit =
    _peer.translate(tx, ty)

  override def rotate(a: Double, x: Double, y: Double): Unit =
    if (x == 0 && y == 0) _peer.rotate(a) else _peer.rotate(a, x, y)

  override def scale(x: Double, y: Double): Unit =
    _peer.scale(x, y)

  override def pushMatrix(): Unit =
    _matrixStack = _peer.getTransform :: _matrixStack

  override def popMatrix(): Unit =
    _matrixStack match {
      case head :: tail =>
        _peer.setTransform(head)
        _matrixStack = tail
      case _ => () // ignore errors?
    }

  override def font: Font = _font
  override def font_=(value: Font): Unit = {
    _font = value
    val f = new java.awt.Font(value.family, java.awt.Font.PLAIN, value.sizePx)
    _peer.setFont(f)
  }

  override def fillPaint: Paint = _fillPaint
  override def fillPaint_=(value: Paint): Unit = {
    _fillPaint = value
  }

  override def strokePaint: Paint = _strokePaint
  override def strokePaint_=(value: Paint): Unit = {
    _strokePaint = value
  }

  override def strokeWidth: Double = _strokeWidth
  override def strokeWidth_=(value: Double): Unit = {
    _strokeWidth = value
  }

  private def setFill(): Boolean = {
    if (_paint != _fillPaint) {
      _paint = _fillPaint
      _fillPaint match {
        case c: Color =>
          val cAWT = new java.awt.Color(c.argb32, true)
          peer.setColor(cAWT)
          _paintSet = true

        case Paint.Transparent =>
          _paintSet = false

        //
        //      case Color.ARGB8(argb8) =>
        //        val c = new java.awt.Color(argb8, true)
        //        peer.setColor(c)
      }
    }
    _paintSet
  }

  private def setStroke(): Boolean = {
    if (_paint != _strokePaint) {
      _paint = _strokePaint
      _strokePaint match {
        case c: Color =>
          val cAWT = new java.awt.Color(c.argb32, true)
          peer.setColor(cAWT)
          _paintSet = true

        case Paint.Transparent =>
          _paintSet = false
      }
    }
    _paintSet && {
      if (_strokeWidthSet != _strokeWidth) {
        if (_strokeWidth > 0.0) peer.setStroke(new BasicStroke(_strokeWidth.toFloat))
        _strokeWidthSet = _strokeWidth
      }
      _strokeWidthSet > 0.0
    }
  }

  def peer: java.awt.Graphics2D = _peer

  override def fillText(s: String, x: Double, y: Double): Unit =
    if (setFill()) _peer.drawString(s, x.toFloat, y.toFloat)

  private final class WrapPathIterator(peer: PathIterator) extends java.awt.geom.PathIterator {
    override def getWindingRule: Int = peer.getWindingRule

    override def isDone: Boolean = peer.isDone

    override def next(): Unit = peer.next()

    override def currentSegment(coords: Array[Float ]): Int = peer.currentSegment(coords)
    override def currentSegment(coords: Array[Double]): Int = peer.currentSegment(coords)
  }

  private object WrapShape extends java.awt.Shape {
    var current: Shape = null
    private val atPeer    = new AffineTransform()
    private val atMatrix  = new Array[Double](6)

    override def getBounds: Rectangle = ???

    override def getBounds2D: java.awt.geom.Rectangle2D = {
      val r = current.getBounds2D
      new java.awt.geom.Rectangle2D.Double(r.getX, r.getY, r.getWidth, r.getHeight)
    }

    override def contains(x: Double, y: Double): Boolean =
      current.contains(x, y)

    override def contains(p: java.awt.geom.Point2D): Boolean =
      contains(p.getX, p.getY)

    override def intersects(x: Double, y: Double, w: Double, h: Double): Boolean =
      current.intersects(x, y, w, h)

    override def intersects(r: java.awt.geom.Rectangle2D): Boolean =
      intersects(r.getX, r.getY, r.getWidth, r.getHeight)

    override def contains(x: Double, y: Double, w: Double, h: Double): Boolean =
      current.contains(x, y, w, h)

    override def contains(r: java.awt.geom.Rectangle2D): Boolean =
      contains(r.getX, r.getY, r.getWidth, r.getHeight)

    private def wrapTransform(at: java.awt.geom.AffineTransform): AffineTransform =
      if (at == null) null else {
        val m = atMatrix
        at.getMatrix(m)
        val res = atPeer
        res.setTransform(m(0),m(1),m(2),m(3),m(4),m(5))
        res
      }

    override def getPathIterator(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator = {
      val atP = wrapTransform(at)
      new WrapPathIterator(current.getPathIterator(atP))
    }

    override def getPathIterator(at: java.awt.geom.AffineTransform, flatness: Double): java.awt.geom.PathIterator = {
      val atP = wrapTransform(at)
      new WrapPathIterator(current.getPathIterator(atP, flatness))
    }
  }

  override def fillShape(s: Shape): Unit = if (setFill()) {
    WrapShape.current = s
    _peer.fill(WrapShape)
  }

  override def strokeShape(s: Shape): Unit = if (setStroke()) {
    WrapShape.current = s
    _peer.draw(WrapShape)
  }

  override def fillStroke(s: Shape): Unit = {
    fillShape   (s)
    strokeShape (s)
  }
}
