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

package de.sciss.lucre.canvas

object Color {
  def parse(s: String): Color = ???

  /** 4-bit value 0xRGB */
  final case class RGB4(value: Int) extends Color {
    private lazy val _cssString = {
      val h = ((value & 0xFFF) | 0x1000).toHexString
      s"#${h.substring(1)}"
    }

    override def argb32: Int = {
      val r4 = (value >> 8) & 0xF
      val g4 = (value >> 4) & 0xF
      val b4 = value & 0xF
      val r8 = (r4 << 20) | (r4 << 16)
      val g8 = (g4 << 12) | (g4 <<  8)
      val b8 = (b4 <<  4) |  b4
      0xFF000000 | r8 | g8 | b8
    }

    override def cssString: String = _cssString
  }

  final case class ARGB8(value: Int) extends Color {
    override def argb32: Int = value

    private lazy val _cssString = {
      val rgba = (value << 8) | (value >>> 24)
      val h = (rgba & 0xFFFFFFFFL | 0x100000000L).toHexString
      s"#${h.substring(1)}"
    }

    def replaceAlpha(a: Int): ARGB8 = copy((value & 0x00FFFFFF) | (a << 24))

    override def cssString: String = _cssString
  }
}
sealed trait Color extends Paint {
  def cssString: String

  def argb32: Int
}

sealed trait Paint