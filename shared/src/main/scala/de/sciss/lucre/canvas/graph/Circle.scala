/*
 *  Circle.scala
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

object Circle {
  private final class Expanded[T <: Txn[T]](cx: IExpr[T, Double], cy: IExpr[T, Double], r: IExpr[T, Double],
                                            prSeq: Seq[IExpr[T, Presentation]])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      cx.changed ---> this
      cy.changed ---> this
      r .changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val cxV     = pull.expr(cx)
      val cyV     = pull.expr(cy)
      val rV      = pull.expr(r )
      val prSeqV  = prSeq.map(pull.expr)
      value1(cxV = cxV, cyV = cyV, rV = rV, prSeqV = prSeqV)
    }

    def value(implicit tx: T): Shape = {
      val cxV     = cx.value
      val cyV     = cy.value
      val rV      = r .value
      val prSeqV  = prSeq.map(_.value)
      value1(cxV = cxV, cyV = cyV, rV = rV, prSeqV = prSeqV)
    }

    private def value1(cxV: Double, cyV: Double, rV: Double, prSeqV: Seq[Presentation]): Shape = { (g: Graphics2D) =>
      val xV = cxV - rV
      val yV = cyV - rV
      val wV = rV * 2
      val hV = wV
      prSeqV.foreach { prV =>
        prV.render(g)
      }
      val shp = new Ellipse2D.Double(x = xV, y = yV, w = wV, h = hV)
      g.fillStroke(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      cx.changed -/-> changed
      cy.changed -/-> changed
      r .changed -/-> changed
      prSeq.foreach(_.changed -/-> this)
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Circle(cx: Ex[Double] = 0, cy: Ex[Double] = 0, r: Ex[Double] = 0, pr: Seq[Ex[Presentation]] = Nil)
  extends Ex[Shape] {

  type Self = Circle

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
    val rEx   = r .expand[T]
    val prEx = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Circle.Expanded[T](cxEx, cyEx, rEx, prEx).init()
  }
}
