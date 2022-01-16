/*
 *  Paint.scala
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

package de.sciss.lucre.canvas

import de.sciss.lucre.expr.graph.{Const, Ex}

object Color {
  def red   : Ex[Color] = Const(new Color {})
  def green : Ex[Color] = Const(new Color {})
  def blue  : Ex[Color] = Const(new Color {})
}
trait Color extends Paint

sealed trait Paint
