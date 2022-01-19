import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}

object Example {
  /*
  <svg version="1.1"
       width="300" height="200"
       xmlns="http://www.w3.org/2000/svg">

    <rect width="100%" height="100%" fill="red" />

    <circle cx="150" cy="100" r="80" fill="green" />

    <text x="150" y="125" font-size="60" text-anchor="middle" fill="white">SVG</text>
  </svg>
   */

  def main(args: Array[String]): Unit = run()

  def run(): Unit = {
    import de.sciss.lucre.canvas.graph._

    val width     = 300
    val height    = 200

    val g = Graphics(Seq(
      Fill(Color.red),
      Rect(width = width /*%(100)*/, height = height /*%(100)*/),
      Fill(Color.green),
      Circle(cx = 150, cy = 100, r = 80),
    ))

    println(g)

    type S = InMemory
    type T = InMemory.Txn
    implicit val system: S = InMemory()
    val sq = system.step { implicit tx =>
      implicit val ws   : Workspace   [T] = Workspace.Implicits.dummy
      implicit val undo : UndoManager [T] = UndoManager()
      implicit val ctx  : Context     [T] = Context()
      val sqEx = g.elem.expand[T]
      sqEx.value
    }
    println(sq)
  }
}
