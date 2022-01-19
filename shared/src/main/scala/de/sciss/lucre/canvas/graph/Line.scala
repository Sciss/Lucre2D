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
import de.sciss.lucre.canvas.{Line2D, Graphics2D, Paint => _Paint}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Line {
  private final class Expanded[T <: Txn[T]](x1: IExpr[T, Len], y1: IExpr[T, Len],
                                            x2: IExpr[T, Len], y2: IExpr[T, Len],
                                            prSeq: Seq[IExpr[T, Presentation]])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      x1.changed ---> this
      y1.changed ---> this
      x2.changed ---> this
      y2.changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val cxV     = pull.expr(x1)
      val cyV     = pull.expr(y1)
      val rxV     = pull.expr(x2)
      val ryV     = pull.expr(y2)
      val prSeqV  = prSeq.map(pull.expr)
      value1(x1V = cxV, y1V = cyV, x2V = rxV, y2V = ryV, prSeqV)
    }

    def value(implicit tx: T): Shape = {
      val cxV     = x1.value
      val cyV     = y1.value
      val rxV     = x2.value
      val ryV     = y2.value
      val prSeqV  = prSeq.map(_.value)
      value1(x1V = cxV, y1V = cyV, x2V = rxV, y2V = ryV, prSeqV)
    }

    private def value1(x1V: Len, y1V: Len, x2V: Len, y2V: Len,
                       prSeqV: Seq[Presentation]): Shape = { (g: Graphics2D) =>
      val x1Px = x1V match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.width
      }
      val y1Px = y1V match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.height
      }
      val x2Px = x2V match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.width
      }

      val y2Px = y2V match {
        case Length.Px(n)   => n
        case Fraction(f)    => f * g.height
      }
      prSeqV.foreach { prV =>
        prV.render(g)
      }
      val shp = new Line2D.Double(x1 = x1Px, y1 = y1Px, x2 = x2Px, y2 = y2Px)
      g.fillStroke(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      x1.changed -/-> changed
      y1.changed -/-> changed
      x2.changed -/-> changed
      y2.changed -/-> changed
      prSeq.foreach(_.changed -/-> this)
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Line(x1: Ex[Len] = 0, y1: Ex[Len] = 0, x2: Ex[Len] = 0, y2: Ex[Len] = 0,
                pr: Seq[Ex[Presentation]] = Nil)
  extends Ex[Shape] {

  type Self = Line

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint)) // XXX TODO DRY with `Rect`

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val x1Ex  = x1.expand[T]
    val y1Ex  = y1.expand[T]
    val x2Ex  = x2.expand[T]
    val y2Ex  = y2.expand[T]
    val prSeqEx = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Line.Expanded[T](x1 = x1Ex, y1 = y1Ex, x2 = x2Ex, y2 = y2Ex, prSeq = prSeqEx).init()
  }
}
