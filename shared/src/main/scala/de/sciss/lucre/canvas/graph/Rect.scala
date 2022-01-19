/*
 *  Rect.scala
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

import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.canvas.{Graphics2D, Rectangle2D, RoundRectangle2D, Paint => _Paint}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Rect {
  private final class Expanded[T <: Txn[T]](x: IExpr[T, Len], y: IExpr[T, Len],
                                            width: IExpr[T, AutoLen], height: IExpr[T, AutoLen],
                                            rx: IExpr[T, AutoLen], ry: IExpr[T, AutoLen],
                                            prSeq: Seq[IExpr[T, Presentation]])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      x     .changed ---> this
      y     .changed ---> this
      width .changed ---> this
      height.changed ---> this
      rx    .changed ---> this
      ry    .changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val xV      = pull.expr(x)
      val yV      = pull.expr(y)
      val widthV  = pull.expr(width)
      val heightV = pull.expr(height)
      val rxV     = pull.expr(rx)
      val ryV     = pull.expr(ry)
      val prSeqV  = prSeq.map(pull.expr)
      value1(xV = xV, yV = yV, widthV = widthV, heightV = heightV,
        rxV = rxV, ryV = ryV, prSeqV = prSeqV)
    }

    override def value(implicit tx: T): Shape = {
      val xV      = x     .value
      val yV      = y     .value
      val widthV  = width .value
      val heightV = height.value
      val rxV     = rx    .value
      val ryV     = ry    .value
      val prSeqV  = prSeq.map(_.value)
        value1(xV = xV, yV = yV, widthV = widthV, heightV = heightV,
          rxV = rxV, ryV = ryV, prSeqV = prSeqV)
    }

    private def value1(xV: Len, yV: Len, widthV: AutoLen, heightV: AutoLen, rxV: AutoLen, ryV: AutoLen,
                       prSeqV: Seq[Presentation]): Shape = new Shape {
      override def render(g: Graphics2D): Unit = {
        val wPx = widthV match {
          case Length.Px(n)   => n
          case Fraction(f)    => f * g.width
          case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
        }
        // if (wPx <= 0) return

        val hPx = heightV match {
          case Length.Px(n)   => n
          case Fraction(f)    => f * g.height
          case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
        }
        // if (hPx <= 0) return

        val xPx = xV match {
          case Length.Px(n) => n
          case Fraction(f)  => f * g.width
        }
        val yPx = yV match {
          case Length.Px(n) => n
          case Fraction(f)  => f * g.height
        }

        val rxPx = rxV match {
          case Length.Px(n)   => n
          case Fraction(f)    => f * g.width
          case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
        }

        val ryPx = ryV match {
          case Length.Px(n)   => n
          case Fraction(f)    => f * g.height
          case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
        }

        prSeqV.foreach { prV =>
          prV.render(g)
        }

        val shp = if (rxPx <= 0 && ryPx <= 0)
          new Rectangle2D.Double(x = xPx, y = yPx, w = wPx, h = hPx)
        else
          new RoundRectangle2D.Double(x = xPx, y = yPx, w = wPx, h = hPx, arcW = rxPx, arcH = ryPx)
        g.fillStroke(shp)
      }
    }

    override def dispose()(implicit tx: T): Unit = {
      x     .changed -/-> this
      y     .changed -/-> this
      width .changed -/-> this
      height.changed -/-> this
      rx    .changed -/-> this
      ry    .changed -/-> this
      prSeq.foreach(_.changed -/-> this)
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Rect(x     : Ex[Len]     = 0,
                y     : Ex[Len]     = 0,
                width : Ex[AutoLen] = AutoLen(),
                height: Ex[AutoLen] = AutoLen(),
                rx    : Ex[AutoLen] = AutoLen(),
                ry    : Ex[AutoLen] = AutoLen(),
                pr    : Seq[Ex[Presentation]] = Nil,
               )
  extends Ex[Shape] {

  type Self = Rect

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint))

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val xEx       = x     .expand[T]
    val yEx       = y     .expand[T]
    val widthEx   = width .expand[T]
    val heightEx  = height.expand[T]
    val rxEx      = rx    .expand[T]
    val ryEx      = ry    .expand[T]
    val prEx      = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Rect.Expanded[T](xEx, yEx, widthEx, heightEx, rxEx, ryEx, prEx).init()
  }
}
