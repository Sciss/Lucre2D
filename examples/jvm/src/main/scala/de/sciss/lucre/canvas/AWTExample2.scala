package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}

import java.awt
import java.awt.{BorderLayout, EventQueue}
import javax.swing.{JComponent, JFrame, WindowConstants}

object AWTExample2 {
  /*
    <?xml version="1.0" standalone="no"?>
    <svg width="200" height="250" version="1.1" xmlns="http://www.w3.org/2000/svg">

      <rect x="10" y="10" width="30" height="30" stroke="black" fill="transparent" stroke-width="5"/>
      <rect x="60" y="10" rx="10" ry="10" width="30" height="30" stroke="black" fill="transparent" stroke-width="5"/>

      <circle cx="25" cy="75" r="20" stroke="red" fill="transparent" stroke-width="5"/>
      <ellipse cx="75" cy="75" rx="20" ry="5" stroke="red" fill="transparent" stroke-width="5"/>

      <line x1="10" x2="50" y1="110" y2="150" stroke="orange" stroke-width="5"/>
      <polyline points="60 110 65 120 70 115 75 130 80 125 85 140 90 135 95 150 100 145"
          stroke="orange" fill="transparent" stroke-width="5"/>

      <polygon points="50 160 55 180 70 180 60 190 65 205 50 195 35 205 40 190 30 180 45 180"
          stroke="green" fill="transparent" stroke-width="5"/>

      <path d="M20,230 Q40,205 50,230 T90,230" fill="none" stroke="blue" stroke-width="5"/>
    </svg>
   */

  def main(args: Array[String]): Unit = run()

  def run(): Unit = {
    import de.sciss.lucre.canvas.graph._

    val g = Graphics(Seq(
      Stroke(Color.black),
      StrokeWidth(5),
      Fill.None,
      Rect(x = 10, y = 10, width = 30, height = 30),
      Rect(x = 60, y = 10, rx = 10, ry = 10, width = 30, height = 30),
      Stroke(Color.red),
      Circle(cx = 25, cy = 75, r = 20),
      Ellipse(cx = 75, cy = 75, rx = 20, ry = 5),
      Stroke(Color.orange),
      Line(x1 = 10, x2 = 50, y1 = 110, y2 = 150),
      Polyline(Seq[Double](60, 110, 65, 120, 70, 115, 75, 130, 80, 125, 85, 140, 90, 135, 95, 150, 100, 145)),
      Stroke(Color.green),
      Polygon(Seq[Double](50, 160, 55, 180, 70, 180, 60, 190, 65, 205, 50, 195, 35, 205, 40, 190, 30, 180, 45, 180)),
      Stroke(Color.blue),
      SvgPath("M20,230 Q40,205 50,230 T90,230"),
    ))

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
        setPreferredSize(new awt.Dimension(200, 250))

        override def paintComponent(g: awt.Graphics): Unit = {
          val g2 = g.asInstanceOf[awt.Graphics2D]
          val w = getWidth
          val h = getHeight
          g2.setRenderingHint(awt.RenderingHints.KEY_ANTIALIASING, awt.RenderingHints.VALUE_ANTIALIAS_ON)
          val gl = new AWTGraphics2D(g2, w, h)
          sq.foreach { elem =>
            elem.render(gl)
          }
        }
      }
      new JFrame("Lucre2D") {
        getContentPane.add(c, BorderLayout.CENTER)
        pack()
        setLocationRelativeTo(null)
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
      }
    }
  }
}
