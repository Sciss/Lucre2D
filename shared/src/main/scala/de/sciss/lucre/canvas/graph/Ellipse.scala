/*
 *  Ellipse.scala
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
import de.sciss.lucre.canvas.{Ellipse2D, Graphics2D, Paint => _Paint}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Ellipse {
  private final class Expanded[T <: Txn[T]](cx: IExpr[T, Len], cy: IExpr[T, Len],
                                            rx: IExpr[T, AutoLen], ry: IExpr[T, AutoLen],
                                            prSeq: Seq[IExpr[T, Presentation]])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      cx.changed ---> this
      cy.changed ---> this
      rx.changed ---> this
      ry.changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val cxV     = pull.expr(cx)
      val cyV     = pull.expr(cy)
      val rxV     = pull.expr(rx)
      val ryV     = pull.expr(ry)
      val prSeqV  = prSeq.map(pull.expr)
      value1(cxV = cxV, cyV = cyV, rxV = rxV, ryV = ryV, prSeqV)
    }

    def value(implicit tx: T): Shape = {
      val cxV     = cx.value
      val cyV     = cy.value
      val rxV     = rx.value
      val ryV     = ry.value
      val prSeqV  = prSeq.map(_.value)
      value1(cxV = cxV, cyV = cyV, rxV = rxV, ryV = ryV, prSeqV)
    }

    private def value1(cxV: Len, cyV: Len, rxV: AutoLen, ryV: AutoLen,
                       prSeqV: Seq[Presentation]): Shape = { (g: Graphics2D) =>
      val rxPx = rxV match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.width
        case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
      }
      // if (rxPx <= 0) return

      val ryPx = ryV match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.height
        case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
      }
      // if (rxPx <= 0) return

      val cxPx = cxV match {
        case Length.Px(n) => n
        case Fraction(f)  => f * g.width
      }
      val cyPx = cyV match {
        case Length.Px(n) => n
        case Fraction(f)  => f * g.height
      }

      val xPx = cxPx - rxPx
      val yPx = cyPx - ryPx
      val wPx = rxPx * 2
      val hPx = ryPx * 2
      prSeqV.foreach { prV =>
        prV.render(g)
      }
      val shp = new Ellipse2D.Double(x = xPx, y = yPx, w = wPx, h = hPx)
      g.fillStroke(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      cx.changed -/-> changed
      cy.changed -/-> changed
      rx.changed -/-> changed
      ry.changed -/-> changed
      prSeq.foreach(_.changed -/-> this)
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Ellipse(cx: Ex[Len] = 0, cy: Ex[Len] = 0, rx: Ex[AutoLen] = AutoLen(), ry: Ex[AutoLen] = AutoLen(),
                   pr: Seq[Ex[Presentation]] = Nil)
  extends Ex[Shape] {

  type Self = Ellipse

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint)) // XXX TODO DRY with `Rect`

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val cxEx  = cx.expand[T]
    val cyEx  = cy.expand[T]
    val rxEx  = rx.expand[T]
    val ryEx  = ry.expand[T]
    val prEx = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Ellipse.Expanded[T](cxEx, cyEx, rxEx, ryEx, prEx).init()
  }
}
