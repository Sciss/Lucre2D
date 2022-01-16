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

import de.sciss.lucre.canvas.{Graphics2D, Rectangle2D}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.{Ex, QuaternaryOp}
import de.sciss.lucre.{IExpr, Txn}
import de.sciss.lucre.canvas.Import._

object Rect {
  private final case class Op() extends QuaternaryOp.Op[Len, Len, AutoLen, AutoLen, Shape] {
    override def apply(x: Len, y: Len, width: AutoLen, height: AutoLen): Shape = { (g: Graphics2D) =>
      val xPx = x match {
        case Length.Px(n) => n
        case Fraction(f)  => f * g.width
      }
      val yPx = y match {
        case Length.Px(n) => n
        case Fraction(f)  => f * g.height
      }
      val wPx = width match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.width
        case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
      }
      val hPx = width match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.height
        case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
      }
      if (wPx > 0 && hPx > 0) {
        g.fillShape(new Rectangle2D.Double(x = xPx, y = yPx, w = wPx, h = hPx))
      }
    }
  }
}
case class Rect(x     : Ex[Len]     = 0,
                y     : Ex[Len]     = 0,
                width : Ex[AutoLen] = AutoLen(),
                height: Ex[AutoLen] = AutoLen(),
                rx    : Ex[AutoLen] = AutoLen(),
                ry    : Ex[AutoLen] = AutoLen(),
                pr    : Seq[Presentation] = Nil,
               )
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[Paint]): Rect =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val xEx       = x     .expand[T]
    val yEx       = y     .expand[T]
    val widthEx   = width .expand[T]
    val heightEx  = height.expand[T]
    // val rxEx      = rx    .expand[T]  // XXX TODO
    // val ryEx      = ry    .expand[T]
    // val prEx   = pr    .expand[T]
    import ctx.targets
    new QuaternaryOp.Expanded(Rect.Op(), xEx, yEx, widthEx, heightEx, tx)
  }
}
