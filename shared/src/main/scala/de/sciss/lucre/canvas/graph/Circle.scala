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
import de.sciss.lucre.expr.graph.{Ex, TernaryOp}
import de.sciss.lucre.{IExpr, Txn}

object Circle {
  private final case class Op() extends TernaryOp.Op[Len, Len, Length, Shape] {
    override def apply(cx: Len, cy: Len, r: Length): Shape = { (g: Graphics2D) =>
      val cxPx = cx match {
        case Length.Px(n) => n
        case Fraction(f)  => f * g.width
      }
      val cyPx = cy match {
        case Length.Px(n) => n
        case Fraction(f)  => f * g.height
      }
      val rPx = r match {
        case Length.Px(n) => n
      }
      if (rPx > 0) {
        val xPx = cxPx - rPx
        val yPx = cyPx - rPx
        val wPx = rPx * 2
        val hPx = wPx
        g.fillShape(new Ellipse2D.Double(x = xPx, y = yPx, w = wPx, h = hPx))
      }
    }
  }
}
case class Circle(cx: Ex[Len] = 0, cy: Ex[Len] = 0, r: Ex[Length] = 0, pr: Seq[Presentation] = Nil)
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[Paint]): Circle =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val cxEx  = cx.expand[T]
    val cyEx  = cy.expand[T]
    val rEx   = r .expand[T]
    // val prEx = pr.expand[T]  // XXX TODO
    import ctx.targets
    new TernaryOp.Expanded(Circle.Op(), cxEx, cyEx, rEx, tx)
  }
}
