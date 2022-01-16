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
  def red   : Ex[_Color] = Const(_Color.RGB4(0xF00))
  def green : Ex[_Color] = Const(_Color.RGB4(0x0F0))
  def blue  : Ex[_Color] = Const(_Color.RGB4(0x00F))
}
//trait Color extends Paint {
//  def argb: Int
//}
//
//sealed trait Paint

