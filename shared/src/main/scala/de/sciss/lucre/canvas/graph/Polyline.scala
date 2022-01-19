/*
 *  Polyline.scala
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

import de.sciss.lucre.canvas.Path2D.WIND_NON_ZERO
import de.sciss.lucre.canvas.{Graphics2D, Path2D, Paint => _Paint}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Polyline {
  private[graph] final class Expanded[T <: Txn[T]](points: IExpr[T, Seq[Double]], prSeq: Seq[IExpr[T, Presentation]],
                                            close: Boolean)
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      points.changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val pointsV = pull.expr(points)
      val prSeqV  = prSeq.map(pull.expr)
      value1(pointsV = pointsV, prSeqV = prSeqV)
    }

    def value(implicit tx: T): Shape = {
      val pointsV = points.value
      val prSeqV  = prSeq.map(_.value)
      value1(pointsV = pointsV, prSeqV = prSeqV)
    }

    private def value1(pointsV: Seq[Double], prSeqV: Seq[Presentation]): Shape =
      new Shape {
        override def render(g: Graphics2D): Unit = {
          val sz = pointsV.size >> 1
          if (sz == 0) return
          prSeqV.foreach { prV =>
            prV.render(g)
          }
          val shp = new Path2D.Double(WIND_NON_ZERO, pointsV.size)
          val it  = pointsV.iterator
          shp.moveTo(it.next(), it.next())
          var i = 1
          while (i < sz) {
            shp.lineTo(it.next(), it.next())
            i += 1
          }
          if (close) shp.closePath()
          g.fillStroke(shp)
        }
      }

    override def dispose()(implicit tx: T): Unit = {
      points.changed -/-> changed
      prSeq.foreach(_.changed -/-> this)
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Polyline(points: Ex[Seq[Double]] = Nil, pr: Seq[Ex[Presentation]] = Nil)
  extends Ex[Shape] {

  type Self = Polyline

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint)) // XXX TODO DRY with `Rect`

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val pointsEx  = points.expand[T]
    val prSeqEx   = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Polyline.Expanded[T](points = pointsEx, prSeq = prSeqEx, close = false).init()
  }
}

case class Polygon(points: Ex[Seq[Double]] = Nil, pr: Seq[Ex[Presentation]] = Nil)
  extends Ex[Shape] {

  type Self = Polygon

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint)) // XXX TODO DRY with `Rect`

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val pointsEx  = points.expand[T]
    val prSeqEx   = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Polyline.Expanded[T](points = pointsEx, prSeq = prSeqEx, close = true).init()
  }
}