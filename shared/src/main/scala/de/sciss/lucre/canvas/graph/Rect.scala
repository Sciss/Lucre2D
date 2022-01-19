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

import de.sciss.lucre.canvas.{Graphics2D, Rectangle2D, RoundRectangle2D, Paint => _Paint}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Rect {
  private final class Expanded[T <: Txn[T]](x: IExpr[T, Double], y: IExpr[T, Double],
                                            width: IExpr[T, Double], height: IExpr[T, Double],
                                            rx: IExpr[T, Double], ry: IExpr[T, Double],
                                            prSeq: Seq[IExpr[T, Presentation]])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      x     .changed ---> this
      y     .changed ---> this
      width .changed ---> this
      height.changed ---> this
      rx    .changed ---> this
      ry    .changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val xV      = pull.expr(x)
      val yV      = pull.expr(y)
      val widthV  = pull.expr(width)
      val heightV = pull.expr(height)
      val rxV     = pull.expr(rx)
      val ryV     = pull.expr(ry)
      val prSeqV  = prSeq.map(pull.expr)
      value1(xV = xV, yV = yV, widthV = widthV, heightV = heightV,
        rxV = rxV, ryV = ryV, prSeqV = prSeqV)
    }

    override def value(implicit tx: T): Shape = {
      val xV      = x     .value
      val yV      = y     .value
      val widthV  = width .value
      val heightV = height.value
      val rxV     = rx    .value
      val ryV     = ry    .value
      val prSeqV  = prSeq.map(_.value)
        value1(xV = xV, yV = yV, widthV = widthV, heightV = heightV,
          rxV = rxV, ryV = ryV, prSeqV = prSeqV)
    }

    private def value1(xV: Double, yV: Double, widthV: Double, heightV: Double, rxV: Double, ryV: Double,
                       prSeqV: Seq[Presentation]): Shape = new Shape {
      override def render(g: Graphics2D): Unit = {
        // if (widthV <= 0) return
        // if (heightV <= 0) return
        prSeqV.foreach { prV =>
          prV.render(g)
        }

        val shp = if (rxV <= 0 && ryV <= 0)
          new Rectangle2D.Double(x = xV, y = yV, w = widthV, h = heightV)
        else
          new RoundRectangle2D.Double(x = xV, y = yV, w = widthV, h = heightV, arcW = rxV, arcH = ryV)
        g.fillStroke(shp)
      }
    }

    override def dispose()(implicit tx: T): Unit = {
      x     .changed -/-> this
      y     .changed -/-> this
      width .changed -/-> this
      height.changed -/-> this
      rx    .changed -/-> this
      ry    .changed -/-> this
      prSeq.foreach(_.changed -/-> this)
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
                pr    : Seq[Ex[Presentation]] = Nil,
               )
  extends Ex[Shape] {

  type Self = Rect

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint))

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val xEx       = x     .expand[T]
    val yEx       = y     .expand[T]
    val widthEx   = width .expand[T]
    val heightEx  = height.expand[T]
    val rxEx      = rx    .expand[T]
    val ryEx      = ry    .expand[T]
    val prEx      = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Rect.Expanded[T](xEx, yEx, widthEx, heightEx, rxEx, ryEx, prEx).init()
  }
}
