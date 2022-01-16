import de.sciss.lucre.canvas.AWTGraphics2D
import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}

import java.awt
import java.awt.EventQueue
import javax.swing.{JComponent, JFrame, WindowConstants}

object AWTExample {
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

    val g = Graphics(
      Rect(width = %(100), height = %(100)).fill(Color.red),
      Circle(cx = 150, cy = 100, r = 80).fill(Color.green),
    )

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

    EventQueue.invokeLater { () =>
      val c = new JComponent {
        setPreferredSize(new awt.Dimension(300, 200))
        setOpaque(true)

        override def paintComponent(g: awt.Graphics): Unit = {
          val g2 = g.asInstanceOf[awt.Graphics2D]
          g2.setColor(awt.Color.white)
          val w = getWidth
          val h = getHeight
          g2.fillRect(0, 0, w, h)
          g2.setColor(awt.Color.black)
          g2.setRenderingHint(awt.RenderingHints.KEY_ANTIALIASING, awt.RenderingHints.VALUE_ANTIALIAS_ON)
          val gl = new AWTGraphics2D(g2, w, h)
          sq.foreach { elem =>
            elem.render(gl)
          }
        }
      }
      new JFrame("Lucre2D") {
        getContentPane.add(c)
        pack()
        setLocationRelativeTo(null)
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
      }
    }
  }
}
