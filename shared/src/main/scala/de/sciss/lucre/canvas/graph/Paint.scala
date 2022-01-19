package de.sciss.lucre.canvas.graph

import de.sciss.lucre.canvas.{Paint => _Paint}
import de.sciss.lucre.expr.graph.{Const, Ex}

object Paint {
  def transparent: Ex[_Paint] = Const(_Paint.Transparent)
}
