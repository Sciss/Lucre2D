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
import de.sciss.lucre.canvas.{Ellipse2D, Graphics2D}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Circle {
  private final class Expanded[T <: Txn[T]](cx: IExpr[T, Double], cy: IExpr[T, Double], r: IExpr[T, Double])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      cx.changed ---> this
      cy.changed ---> this
      r .changed ---> this
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val cxV     = pull.expr(cx)
      val cyV     = pull.expr(cy)
      val rV      = pull.expr(r )
      value1(cxV = cxV, cyV = cyV, rV = rV)
    }

    def value(implicit tx: T): Shape = {
      val cxV     = cx.value
      val cyV     = cy.value
      val rV      = r .value
      value1(cxV = cxV, cyV = cyV, rV = rV)
    }

    private def value1(cxV: Double, cyV: Double, rV: Double): Shape = { (g: Graphics2D) =>
      val xV = cxV - rV
      val yV = cyV - rV
      val wV = rV * 2
      val hV = wV
      val shp = new Ellipse2D.Double(x = xV, y = yV, w = wV, h = hV)
      g.fillStroke(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      cx.changed -/-> changed
      cy.changed -/-> changed
      r .changed -/-> changed
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Circle(cx: Ex[Double] = 0, cy: Ex[Double] = 0, r: Ex[Double] = 0)
  extends Ex[Shape] {

  type Self = Circle

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val cxEx  = cx.expand[T]
    val cyEx  = cy.expand[T]
    val rEx   = r .expand[T]
    import ctx.targets
    new Circle.Expanded[T](cxEx, cyEx, rEx).init()
  }
}
