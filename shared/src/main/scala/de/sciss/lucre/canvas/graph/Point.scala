/*
 *  Point.scala
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

object Point {
  private final class Expanded[T <: Txn[T]](x: IExpr[T, Double], y: IExpr[T, Double])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      x.changed ---> this
      y.changed ---> this
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val xV = pull.expr(x)
      val yV = pull.expr(y)
      value1(xV = xV, yV = yV)
    }

    def value(implicit tx: T): Shape = {
      val xV  = x.value
      val yV  = y.value
      value1(xV = xV, yV = yV)
    }

    private def value1(xV: Double, yV: Double): Shape = { (g: Graphics2D) =>
//      val shp = new Path2D.Double(WIND_NON_ZERO, 2)
//      shp.moveTo(xV, yV)
//      shp.closePath()
      val shp = new Line2D.Double(xV, yV, xV, yV) // XXX TODO what's the P5 impl?
      g.strokeShape(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      x.changed -/-> changed
      y.changed -/-> changed
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Point(x: Ex[Double] = 0, y: Ex[Double] = 0)
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val xEx  = x.expand[T]
    val yEx  = y.expand[T]
    import ctx.targets
    new Point.Expanded[T](x = xEx, y = yEx).init()
  }
}
