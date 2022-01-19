/*
 *  Color.scala
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

import de.sciss.lucre.{IExpr, Txn}
import de.sciss.lucre.canvas.{Color => _Color}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.{Const, Ex, TernaryOp, UnaryOp}
import de.sciss.numbers.Implicits.doubleNumberWrapper

object Color {
  // https://www.w3.org/TR/css-color-3/#html4
  // basic
  def black     : Ex[_Color] = Const(_Color.ARGB8(0xFF000000))
  def silver    : Ex[_Color] = Const(_Color.ARGB8(0xFFC0C0C0))
  def gray      : Ex[_Color] = Const(_Color.ARGB8(0xFF808080))
  def white     : Ex[_Color] = Const(_Color.ARGB8(0xFFFFFFFF))
  def maroon    : Ex[_Color] = Const(_Color.ARGB8(0xFF800000))
  def red       : Ex[_Color] = Const(_Color.ARGB8(0xFFFF0000))
  def purple    : Ex[_Color] = Const(_Color.ARGB8(0xFF800080))
  def fuchsia   : Ex[_Color] = Const(_Color.ARGB8(0xFFFF00FF))
  def green     : Ex[_Color] = Const(_Color.ARGB8(0xFF008000))
  def lime      : Ex[_Color] = Const(_Color.ARGB8(0xFF00FF00))
  def olive     : Ex[_Color] = Const(_Color.ARGB8(0xFF808000))
  def yellow    : Ex[_Color] = Const(_Color.ARGB8(0xFFFFFF00))
  def navy      : Ex[_Color] = Const(_Color.ARGB8(0xFF000080))
  def blue      : Ex[_Color] = Const(_Color.ARGB8(0xFF0000FF))
  def teal      : Ex[_Color] = Const(_Color.ARGB8(0xFF008080))
  def aqua      : Ex[_Color] = Const(_Color.ARGB8(0xFF00FFFF))

  // extended
  def aliceBlue : Ex[_Color] = Const(_Color.ARGB8(0xFFF0F8FF))
  def orange    : Ex[_Color] = Const(_Color.ARGB8(0xFFFFA500))

  object Gray {
    private case class Op() extends UnaryOp.Op[Double, _Color] {
      override def apply(a: Double): _Color = {
        val amtI  = (a.clip(0.0, 1.0) * 255).toInt
        val value = 0xFF000000 | (amtI << 16) | (amtI << 8) | amtI
        _Color.ARGB8(value)
      }
    }
  }
  case class Gray(amt: Ex[Double]) extends Ex[_Color] {
    type Repr[T <: Txn[T]] = IExpr[T, _Color]

    override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
      val amtEx = amt.expand[T]
      import ctx.targets
      new UnaryOp.Expanded(Gray.Op(), amtEx, tx)
    }
  }

  object RGB {
    private case class Op() extends TernaryOp.Op[Double, Double, Double, _Color] {
      override def apply(r: Double, g: Double, b: Double): _Color = {
        val rI    = (r.clip(0.0, 1.0) * 255).toInt
        val gI    = (g.clip(0.0, 1.0) * 255).toInt
        val bI    = (b.clip(0.0, 1.0) * 255).toInt
        val value = 0xFF000000 | (rI << 16) | (gI << 8) | bI
        _Color.ARGB8(value)
      }
    }
  }
  case class RGB(r: Ex[Double], g: Ex[Double], b: Ex[Double]) extends Ex[_Color] {
    type Repr[T <: Txn[T]] = IExpr[T, _Color]

    override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
      val rEx = r.expand[T]
      val gEx = g.expand[T]
      val bEx = b.expand[T]
      import ctx.targets
      new TernaryOp.Expanded(RGB.Op(), rEx, gEx, bEx, tx)
    }
  }
}
//trait Color extends Paint {
//  def argb: Int
//}
//
//sealed trait Paint

