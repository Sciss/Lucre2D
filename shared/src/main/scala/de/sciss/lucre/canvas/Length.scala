package de.sciss.lucre.canvas

import de.sciss.lucre.expr.graph.Ex

object AutoLen {
  implicit def px[A](in: A)(implicit view: A => Ex[Double ]): Ex[Length] = ???
//  implicit def pxI[A](in: A)(implicit view: A => Ex[Int    ]): Ex[Length] = ???

  def apply(): Ex[AutoLen] = ???
}
sealed trait AutoLen

object Len {
//  object Auto extends AutoLen
}
sealed trait Len extends AutoLen

object Length {
}
sealed trait Length extends Len

case class Fraction(p: Double) extends Len

object % {
  // XXX TODO: should change Lucre to promote Int to Double

//  def apply(p: Ex[Double]): Ex[Fraction] = ???
  def apply[A](p: A)(implicit view: A => Ex[Double]): Ex[Fraction] = ???
}