/*
 *  Path.scala
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

import de.sciss.lucre.canvas.impl.PathToPath2D
import de.sciss.lucre.canvas.parser.PathParser
import de.sciss.lucre.canvas.{Graphics2D, Paint => _Paint}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object Path {
  private final class Expanded[T <: Txn[T]](d: IExpr[T, String],
                                            prSeq: Seq[IExpr[T, Presentation]])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      d.changed ---> this
      prSeq.foreach(_.changed ---> this)
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val dV = pull.expr(d)
      val prSeqV = prSeq.map(pull.expr)
      value1(dV = dV, prSeqV = prSeqV)
    }

    def value(implicit tx: T): Shape = {
      val dV = d.value
      val prSeqV = prSeq.map(_.value)
      value1(dV = dV, prSeqV = prSeqV)
    }

    private def value1(dV: String, prSeqV: Seq[Presentation]): Shape = { (g: Graphics2D) =>
      prSeqV.foreach { prV =>
        prV.render(g)
      }
      val pp2     = new PathToPath2D
      val parser  = new PathParser
      parser.setPathHandler(pp2)
      parser.parse(dV)
      val shp = pp2.result()
      g.fillStroke(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      d.changed -/-> changed
      prSeq.foreach(_.changed -/-> this)
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class Path(d: Ex[String] = "", pr: Seq[Ex[Presentation]] = Nil)
  extends Ex[Shape] {

  type Self = Path

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  def fill(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Fill]) :+ Fill(paint)) // XXX TODO DRY with `Rect`

  def noFill(): Self = fill(Paint.transparent)

  def stroke(paint: Ex[_Paint]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[Stroke]) :+ Stroke(paint))

  def strokeWidth(w: Ex[Double]): Self =
    copy(pr = pr.filterNot(_.isInstanceOf[StrokeWidth]) :+ StrokeWidth(w))

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val dEx = d.expand[T]
    val prSeqEx = pr.map(_.expand[T]: IExpr[T, Presentation])
    import ctx.targets
    new Path.Expanded[T](d = dEx, prSeq = prSeqEx).init()
  }
}
