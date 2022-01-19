/*
 *  Matrix.scala
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
import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.{Const, Ex}
import de.sciss.lucre.{IExpr, Txn}

object PushMatrix {
  private object Value extends Graphics.Elem {
    override def render(g: Graphics2D): Unit =
      g.pushMatrix()
  }
}
case class PushMatrix() extends Ex[Elem] {

  type Repr[T <: Txn[T]] = IExpr[T, Elem]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] =
    new Const.Expanded(PushMatrix.Value)
}

object PopMatrix {
  private object Value extends Graphics.Elem {
    override def render(g: Graphics2D): Unit =
      g.popMatrix()
  }
}
case class PopMatrix() extends Ex[Elem] {

  type Repr[T <: Txn[T]] = IExpr[T, Elem]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] =
    new Const.Expanded(PopMatrix.Value)
}
