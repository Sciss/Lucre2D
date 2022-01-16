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

import de.sciss.lucre.canvas.{Graphics2D, Paint, Rectangle2D}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.{Ex, QuaternaryOp}
import de.sciss.lucre.{IChangeEvent, IExpr, Txn}
import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.impl.IDummyEvent

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
      val hPx = height match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.height
        case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
      }
      if (wPx > 0 && hPx > 0) {
        g.fillShape(new Rectangle2D.Double(x = xPx, y = yPx, w = wPx, h = hPx))
      }
    }
  }

  // XXX TODO listen to inputs
  private final class Expanded[T <: Txn[T]](x: IExpr[T, Len], y: IExpr[T, Len],
                                            width: IExpr[T, AutoLen], height: IExpr[T, AutoLen],
                                            rx: IExpr[T, AutoLen], ry: IExpr[T, AutoLen],
                                            prSet: Set[IExpr[T, Presentation]])
    extends IExpr[T, Shape] {

    override def value(implicit tx: T): Shape = new Shape {
      override def render(g: Graphics2D): Unit = {
        val wPx = width.value match {
          case Length.Px(n)   => n
          case Fraction(f)    => f * g.width
          case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
        }
        if (wPx <= 0) return

        val hPx = height.value match {
          case Length.Px(n)   => n
          case Fraction(f)    => f * g.height
          case AutoLen.Value  => 0.0  // XXX TODO what's this supposed to be?
        }
        if (hPx <= 0) return

        val xPx = x.value match {
          case Length.Px(n) => n
          case Fraction(f)  => f * g.width
        }
        val yPx = y.value match {
          case Length.Px(n) => n
          case Fraction(f)  => f * g.height
        }

        prSet.foreach { pr =>
          pr.value.render(g)
        }
        // XXX TODO use RoundRectangle2D if necessary
        g.fillShape(new Rectangle2D.Double(x = xPx, y = yPx, w = wPx, h = hPx))
      }
    }

    override def dispose()(implicit tx: T): Unit = ()

    override def changed: IChangeEvent[T, Shape] = IDummyEvent.change
  }
}
case class Rect(x     : Ex[Len]     = 0,
                y     : Ex[Len]     = 0,
                width : Ex[AutoLen] = AutoLen(),
                height: Ex[AutoLen] = AutoLen(),
                rx    : Ex[AutoLen] = AutoLen(),
                ry    : Ex[AutoLen] = AutoLen(),
                pr    : Set[Ex[Presentation]] = Set.empty,
               )
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[Paint]): Rect =
    copy(pr = pr + Fill(paint))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val xEx       = x     .expand[T]
    val yEx       = y     .expand[T]
    val widthEx   = width .expand[T]
    val heightEx  = height.expand[T]
     val rxEx     = rx    .expand[T]
     val ryEx     = ry    .expand[T]
     val prEx     = pr.map(_.expand[T]: IExpr[T, Presentation])
//    import ctx.targets
//    new QuaternaryOp.Expanded(Rect.Op(), xEx, yEx, widthEx, heightEx, tx)
    new Rect.Expanded[T](xEx, yEx, widthEx, heightEx, rxEx, ryEx, prEx)
  }
}
