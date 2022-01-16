/*
 *  Composite.scala
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

object Composite {
  def parse(s: String): Composite = s match {
    case SourceOver .name => SourceOver
    case ColorBurn  .name => ColorBurn
  }

  final case object SourceOver extends Composite {
    final val name = "source-over"
  }

  final case object ColorBurn extends Composite {
    final val name = "color-burn"
  }
}
sealed trait Composite {
  def name: String
}
