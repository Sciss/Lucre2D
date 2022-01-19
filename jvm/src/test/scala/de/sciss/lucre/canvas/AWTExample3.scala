package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.canvas.graph.Graphics.Elem
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}

import java.awt
import java.awt.{BorderLayout, EventQueue}
import javax.swing.event.ChangeEvent
import javax.swing.{JComponent, JFrame, JSlider, WindowConstants}

object AWTExample3 {
  /*
    Processing > examples > topics > fractals and L-systems > tree

   */

  def main(args: Array[String]): Unit = run()

  def run(): Unit = {
    import de.sciss.lucre.canvas.graph._
    import de.sciss.lucre.expr.graph._

    val angle = Var(45.0)

    val width   = 640
    val height  = 360

    val g: Graphics = {
      val theta = angle * math.Pi / 180

      def branch(h0: Double): Seq[Ex[Elem]] = {
        // Each branch will be 2/3rds the size of the previous one
        val h = h0 * 0.66

        // All recursive functions must have an exit condition!!!!
        // Here, ours is when the length of the branch is 2 pixels or less
        if (h <= 2) Nil else {
          Seq(
            PushMatrix(),       // Save the current state of transformation (i.e. where are we now)
            Rotate(theta),      // Rotate by theta
            Line(0, 0, 0, -h).stroke(Color.black),  // Draw the branch
            Translate(0, -h),   // Move to the end of the branch
          ) ++ branch(h) ++ Seq(  // Ok, now call myself to draw two new branches!!
            PopMatrix(),        // Whenever we get back here, we "pop" in order to restore the previous matrix state
            // Repeat the same thing, only branch off to the "left" this time!
            PushMatrix(),
            Rotate(-theta),
            Line(0, 0, 0, -h).stroke(Color.black),
            Translate(0, -h)
          ) ++ branch(h) :+ PopMatrix()
        }
      }

      // Start the tree from the bottom of the screen
      Graphics(
        Seq(
          Translate(width/2, height),
          // Draw a line 120 pixels
          Line(0, 0, 0, -120).stroke(Color.black),
          // Move to the end of that line
          Translate(0, -120),
          // Start the recursive branching!
        ) ++ branch(120)
      )
    }

    println(g)

    var sq: Seq[Graphics.Elem] = Nil
    var c: JComponent = null
    var angleEx: Var.Expanded[T, Double] = null

    type S = InMemory
    type T = InMemory.Txn
    implicit val system: S = InMemory()
    system.step { implicit tx =>
      implicit val ws   : Workspace   [T] = Workspace.Implicits.dummy
      implicit val undo : UndoManager [T] = UndoManager()
      implicit val ctx  : Context     [T] = Context()
      angleEx = angle  .expand[T]
      val sqEx = g.elem.expand[T]
      sqEx.changed.react { implicit tx => upd =>
        tx.afterCommit(EventQueue.invokeLater { () =>
          sq = upd.now
          c.repaint()
        })
      }
      sq = sqEx.value
    }

    //    de.sciss.lucre.Log.event.level = de.sciss.log.Level.Debug

    EventQueue.invokeLater { () =>
      c = new JComponent {
        setPreferredSize(new awt.Dimension(width, height))
        // setOpaque(true)

        override def paintComponent(g: awt.Graphics): Unit = {
          val g2 = g.asInstanceOf[awt.Graphics2D]
          val w = getWidth
          val h = getHeight
          // g2.setColor(awt.Color.white)
          // g2.fillRect(0, 0, w, h)
          // g2.setColor(awt.Color.black)
          g2.setRenderingHint(awt.RenderingHints.KEY_ANTIALIASING, awt.RenderingHints.VALUE_ANTIALIAS_ON)
          val gl = new AWTGraphics2D(g2, w, h)
          sq.foreach { elem =>
            elem.render(gl)
          }
        }
      }
      val sl = new JSlider(0, 90)
      sl.addChangeListener((_: ChangeEvent) => {
        val v = sl.getValue
        system.step { implicit tx =>
          val vEx = new Const.Expanded[T, Double](v.toDouble)
          angleEx.update(vEx)
        }
      })
      new JFrame("Lucre2D") {
        getContentPane.add(c, BorderLayout.CENTER)
        getContentPane.add(sl, BorderLayout.SOUTH)
        pack()
        setLocationRelativeTo(null)
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
      }
    }
  }
}
