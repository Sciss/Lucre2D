/*
 *  Presentation.scala
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

import de.sciss.lucre.canvas.graph.Fill.Op
import de.sciss.lucre.canvas.{Graphics2D, Paint}
import de.sciss.lucre.{IExpr, Txn}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.{Ex, UnaryOp}

sealed trait Presentation {
  def render(g: Graphics2D): Unit
}

object Fill {
  private final case class Op() extends UnaryOp.Op[Paint, Presentation] {
    override def apply(p: Paint): Presentation =
      new Presentation {
        override def render(g: Graphics2D): Unit = g.fillStyle = p
      }
  }
}
case class Fill(color: Ex[Paint]) extends Ex[Presentation] {
  type Repr[T <: Txn[T]] = IExpr[T, Presentation]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val colorEx = color.expand[T]
    import ctx.targets
    new UnaryOp.Expanded(Op(), colorEx, tx)
  }
}