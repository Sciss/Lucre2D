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

package de.sciss.lucre.canvas.graph

import de.sciss.lucre.canvas.Graphics2D
import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.expr.graph.Ex

object Graphics {
  trait Elem {
    def render(g: Graphics2D): Unit
  }

  def apply(elem: Ex[Elem]*): Graphics = Graphics(elem: Ex[Seq[Elem]])
}
case class Graphics(/*width: Ex[Double], height: Ex[Double], */ elem: Ex[Seq[Elem]])
