/*
 *  Graphics.scala
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

import de.sciss.lucre.expr.graph.Ex

object Graphics {
  trait Elem

  def apply(elem: Ex[Elem]*): Graphics = Graphics(elem: Ex[Seq[Elem]])
}
case class Graphics(/*width: Ex[Double], height: Ex[Double], */ elem: Ex[Seq[Graphics.Elem]])
