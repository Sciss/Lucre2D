package de.sciss.lucre.canvas.graph

import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.{IExpr, Txn}
import de.sciss.lucre.canvas.{Graphics2D, Paint, Rectangle2D}
import de.sciss.lucre.expr.Context
import de.sciss.lucre.expr.graph.{Ex, UnaryOp}

object Background {
  private final case class Op() extends UnaryOp.Op[Paint, Elem] {
    override def apply(p: Paint): Elem = (g: Graphics2D) => {
      val saved   = g.fillPaint
      g.fillPaint = p
      g.fillShape(new Rectangle2D.Double(0, 0, g.width, g.height))
      g.fillPaint = saved
    }
  }
}
case class Background(color: Ex[Paint]) extends Ex[Elem] {
  type Repr[T <: Txn[T]] = IExpr[T, Elem]

  override protected def mkRepr[T <: Txn[T]](implicit ctx: Context[T], tx: T): Repr[T] = {
    val colorEx = color.expand[T]
    import ctx.targets
    new UnaryOp.Expanded(Background.Op(), colorEx, tx)
  }
}
