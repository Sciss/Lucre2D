/*
 *  Shape.scala
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

object Shape {
  object Empty extends Shape {
    override def render(g: Graphics2D): Unit = ()
  }
}
trait Shape extends Graphics.Elem
