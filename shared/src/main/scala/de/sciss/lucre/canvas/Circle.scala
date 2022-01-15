package de.sciss.lucre.canvas

import de.sciss.lucre.{IExpr, Txn}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.Ex
import Import._

case class Circle(cx: Ex[Len] = 0, cy: Ex[Len] = 0, r: Ex[Length] = 0)
  extends Ex[Shape] {

  type Repr[T <: Txn[T]] = IExpr[T, Shape]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = ???
}
