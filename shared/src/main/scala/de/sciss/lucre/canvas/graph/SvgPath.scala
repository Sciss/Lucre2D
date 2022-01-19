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

import de.sciss.lucre.canvas.Graphics2D
import de.sciss.lucre.canvas.impl.PathToPath2D
import de.sciss.lucre.canvas.parser.PathParser
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import de.sciss.lucre.impl.IChangeEventImpl
import de.sciss.lucre.{IChangeEvent, IExpr, IPull, ITargets, Txn}

object SvgPath {
  private final class Expanded[T <: Txn[T]](d: IExpr[T, String])
                                           (implicit protected val targets: ITargets[T])
    extends IExpr[T, Shape] with IChangeEventImpl[T, Shape] {

    def init()(implicit tx: T): this.type = {
      d.changed ---> this
      this
    }

    override private[lucre] def pullChange(pull: IPull[T])(implicit tx: T, phase: IPull.Phase): Shape = {
      val dV = pull.expr(d)
      value1(dV = dV)
    }

    def value(implicit tx: T): Shape = {
      val dV = d.value
      value1(dV = dV)
    }

    private def value1(dV: String): Shape = { (g: Graphics2D) =>
      val pp2     = new PathToPath2D
      val parser  = new PathParser
      parser.setPathHandler(pp2)
      parser.parse(dV)
      val shp = pp2.result()
      g.fillStroke(shp)
    }

    override def dispose()(implicit tx: T): Unit = {
      d.changed -/-> changed
    }

    override def changed: IChangeEvent[T, Shape] = this
  }
}
case class SvgPath(d: Ex[String] = "")
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val dEx = d.expand[T]
    import ctx.targets
    new SvgPath.Expanded[T](d = dEx).init()
  }
}
