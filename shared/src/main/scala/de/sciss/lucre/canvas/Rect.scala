package de.sciss.lucre.canvas

import de.sciss.lucre.{IExpr, Txn}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import Import._

case class Rect(x: Ex[Len] = 0, y: Ex[Len] = 0, width: Ex[AutoLen] = AutoLen(), height: Ex[AutoLen] = AutoLen())
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = ???
}
