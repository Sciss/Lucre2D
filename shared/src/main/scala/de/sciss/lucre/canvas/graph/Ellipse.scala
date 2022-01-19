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
  private final class Expanded[T <: Txn[T]](cx: IExpr[T, Double], cy: IExpr[T, Double],
                                            rx: IExpr[T, Double], ry: IExpr[T, Double],
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

    private def value1(cxV: Double, cyV: Double, rxV: Double, ryV: Double,
                       prSeqV: Seq[Presentation]): Shape = { (g: Graphics2D) =>
      // if (rxV <= 0) return
      // if (ryV <= 0) return

      val xV = cxV - rxV
      val yV = cyV - ryV
      val wV = rxV * 2
      val hV = ryV * 2
      prSeqV.foreach { prV =>
        prV.render(g)
      }
      val shp = new Ellipse2D.Double(x = xV, y = yV, w = wV, h = hV)
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
case class Ellipse(cx: Ex[Double] = 0, cy: Ex[Double] = 0, rx: Ex[Double], ry: Ex[Double],
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
