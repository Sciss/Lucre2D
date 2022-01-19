/*
 *  Line.scala
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
import de.sciss.lucre.canvas.{Graphics2D, Line2D}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Line {
  private final class Expanded[T <: Txn[T]](x1: IExpr[T, Double], y1: IExpr[T, Double],
                                            x2: IExpr[T, Double], y2: IExpr[T, Double])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      x1.changed ---> this
      y1.changed ---> this
      x2.changed ---> this
      y2.changed ---> this
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val cxV     = pull.expr(x1)
      val cyV     = pull.expr(y1)
      val rxV     = pull.expr(x2)
      val ryV     = pull.expr(y2)
      value1(x1V = cxV, y1V = cyV, x2V = rxV, y2V = ryV)
    }

    def value(implicit tx: T): Shape = {
      val cxV     = x1.value
      val cyV     = y1.value
      val rxV     = x2.value
      val ryV     = y2.value
      value1(x1V = cxV, y1V = cyV, x2V = rxV, y2V = ryV)
    }

    private def value1(x1V: Double, y1V: Double, x2V: Double, y2V: Double): Shape = { (g: Graphics2D) =>
      val shp = new Line2D.Double(x1 = x1V, y1 = y1V, x2 = x2V, y2 = y2V)
      g.strokeShape(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      x1.changed -/-> changed
      y1.changed -/-> changed
      x2.changed -/-> changed
      y2.changed -/-> changed
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Line(x1: Ex[Double] = 0, y1: Ex[Double] = 0, x2: Ex[Double] = 0, y2: Ex[Double] = 0)
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val x1Ex  = x1.expand[T]
    val y1Ex  = y1.expand[T]
    val x2Ex  = x2.expand[T]
    val y2Ex  = y2.expand[T]
    import ctx.targets
    new Line.Expanded[T](x1 = x1Ex, y1 = y1Ex, x2 = x2Ex, y2 = y2Ex).init()
  }
}
