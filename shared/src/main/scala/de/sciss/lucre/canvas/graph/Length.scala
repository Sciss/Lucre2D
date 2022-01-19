///*
// *  Length.scala
// *  (Lucre2D)
// *
// *  Copyright (c) 2022 Hanns Holger Rutz. All rights reserved.
// *
// *	This software is published under the GNU Affero General Public License v3+
// *
// *
// *	For further information, please contact Hanns Holger Rutz at
// *	contact@sciss.de
// */
//
//package de.sciss.lucre.canvas.graph
//
//import de.sciss.lucre.expr.Context
//import de.sciss.lucre.expr.graph.{Const, Ex, UnaryOp}
//import de.sciss.lucre.{IExpr, Txn}
//
//import scala.language.implicitConversions
//
//object AutoLen {
//  implicit def px[A](in: A)(implicit view: A => Ex[Double ]): Ex[Length] = Length.Px(in)
//
//  def apply(): Ex[AutoLen] = Apply()
//
//  case object Value extends AutoLen
//
//  private final case class Apply() extends Ex[AutoLen] {
//    type Repr[T <: Txn[T]] = IExpr[T, AutoLen]
//
//    override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] =
//      new Const.Expanded(Value)
//  }
//}
///** An `AutoLen` can be either a `Len` or `AutoLen.Value` */
//sealed trait AutoLen
//
//object Len {
//}
///** A `Len` can be either a `Length` or a `Fraction`. */
//sealed trait Len extends AutoLen
//
//object Length {
////  def unapply(px: Px): Boolean = true
//
//  object Px {
//    def apply(n: Ex[Double]): Ex[Px] = Apply(n)
//
//    private final case class Op() extends UnaryOp.Op[Double, Px] {
//      override def apply(n: Double): Px = Px(n)
//    }
//
//    private final case class Apply(n: Ex[Double]) extends Ex[Px] {
//      type Repr[T <: Txn[T]] = IExpr[T, Px]
//
//      override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
//        val nEx = n.expand[T]
//        import ctx.targets
//        new UnaryOp.Expanded(Op(), nEx, tx)
//      }
//    }
//  }
//  case class Px(n: Double) extends Length
//}
//sealed trait Length extends Len
//
//object Fraction {
//  def apply(p: Ex[Double]): Ex[Fraction] = Apply(p)
//
//  private final case class Op() extends UnaryOp.Op[Double, Fraction] {
//    override def apply(p: Double): Fraction = Fraction(p * 0.01)
//  }
//
//  private final case class Apply(p: Ex[Double]) extends Ex[Fraction] {
//    type Repr[T <: Txn[T]] = IExpr[T, Fraction]
//
//    override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
//      val pEx = p.expand[T]
//      import ctx.targets
//      new UnaryOp.Expanded(Op(), pEx, tx)
//    }
//  }
//}
//case class Fraction private[lucre](f: Double) extends Len
//
//object % {
//  // XXX TODO: should change Lucre to promote Int to Double
//
////  def apply(p: Ex[Double]): Ex[Fraction] = ???
//  def apply[A](p: A)(implicit view: A => Ex[Double]): Ex[Fraction] = Fraction(p)
//}