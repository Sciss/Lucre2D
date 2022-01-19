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

import de.sciss.lucre.canvas.{Graphics2D, Rectangle2D, RoundRectangle2D}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Rect {
  private final class Expanded[T <: Txn[T]](x: IExpr[T, Double], y: IExpr[T, Double],
                                            width: IExpr[T, Double], height: IExpr[T, Double],
                                            rx: IExpr[T, Double], ry: IExpr[T, Double])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      x     .changed ---> this
      y     .changed ---> this
      width .changed ---> this
      height.changed ---> this
      rx    .changed ---> this
      ry    .changed ---> this
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val xV      = pull.expr(x)
      val yV      = pull.expr(y)
      val widthV  = pull.expr(width)
      val heightV = pull.expr(height)
      val rxV     = pull.expr(rx)
      val ryV     = pull.expr(ry)
      value1(xV = xV, yV = yV, widthV = widthV, heightV = heightV,
        rxV = rxV, ryV = ryV)
    }

    override def value(implicit tx: T): Shape = {
      val xV      = x     .value
      val yV      = y     .value
      val widthV  = width .value
      val heightV = height.value
      val rxV     = rx    .value
      val ryV     = ry    .value
        value1(xV = xV, yV = yV, widthV = widthV, heightV = heightV,
          rxV = rxV, ryV = ryV)
    }

    private def value1(xV: Double, yV: Double, widthV: Double, heightV: Double, rxV: Double, ryV: Double): Shape =
      (g: Graphics2D) => {
        val shp = if (rxV <= 0 && ryV <= 0)
          new Rectangle2D.Double(x = xV, y = yV, w = widthV, h = heightV)
        else
          new RoundRectangle2D.Double(x = xV, y = yV, w = widthV, h = heightV, arcW = rxV, arcH = ryV)
        g.fillStroke(shp)
      }

    override def dispose()(implicit tx: T): Unit = {
      x     .changed -/-> this
      y     .changed -/-> this
      width .changed -/-> this
      height.changed -/-> this
      rx    .changed -/-> this
      ry    .changed -/-> this
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Rect(x     : Ex[Double]  = 0.0,
                y     : Ex[Double]  = 0.0,
                width : Ex[Double]  = 0.0,
                height: Ex[Double]  = 0.0,
                rx    : Ex[Double]  = 0.0,
                ry    : Ex[Double]  = 0.0,
               )
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val xEx       = x     .expand[T]
    val yEx       = y     .expand[T]
    val widthEx   = width .expand[T]
    val heightEx  = height.expand[T]
    val rxEx      = rx    .expand[T]
    val ryEx      = ry    .expand[T]
    import ctx.targets
    new Rect.Expanded[T](xEx, yEx, widthEx, heightEx, rxEx, ryEx).init()
  }
}
