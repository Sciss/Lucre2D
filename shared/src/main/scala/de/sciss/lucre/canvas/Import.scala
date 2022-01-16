/*
 *  Import.scala
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

import scala.language.implicitConversions

object Import {
  implicit def intIsDoubleEx(i: Int): Ex[Double] = Const(i.toDouble)
}
