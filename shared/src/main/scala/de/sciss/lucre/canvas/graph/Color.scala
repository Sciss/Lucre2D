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

import de.sciss.lucre.canvas.{Color => _Color}
import de.sciss.lucre.expr.graph.{Const, Ex}

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
}
//trait Color extends Paint {
//  def argb: Int
//}
//
//sealed trait Paint

