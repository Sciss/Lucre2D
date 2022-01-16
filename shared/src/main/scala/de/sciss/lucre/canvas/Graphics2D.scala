/*
 *  Graphics2D.scala
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

trait Graphics2D {
//  var composite: Composite

  var font: Font

  var fillStyle: Paint // Color

  def width : Double
  def height: Double

  def fillText(s: String, x: Double, y: Double): Unit

  def fillShape(s: Shape): Unit

  def translate(tx: Double, ty: Double): Unit
}
