package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}
import de.sciss.lucre.canvas.{Color => _Color}

import java.awt
import java.awt.{BorderLayout, EventQueue}
import javax.swing.event.ChangeEvent
import javax.swing.{JComponent, JFrame, JSlider, WindowConstants}

object AWTExample3 {
  /*
    Processing > examples > topics > fractals and L-systems > tree

   */

  def main(args: Array[String]): Unit = {
    val widthI  = args.indexOf("--width"  ) + 1
    val heightI = args.indexOf("--height" ) + 1
    val width   = if (widthI  == 0) 640 else args(widthI  ).toInt
    val height  = if (heightI == 0) 360 else args(heightI ).toInt
    run(width, height)
  }

  def run(width: Int, height: Int = 360): Unit = {
    import de.sciss.lucre.canvas.graph._
    import de.sciss.lucre.expr.graph._

    val angle = Var(45.0)

    /**
     * Recursive Tree
     * by Daniel Shiffman.
     *
     * Renders a simple tree-like structure via recursion.
     * The branching angle is calculated as a function of
     * the horizontal mouse location. Move the mouse left
     * and right to change the angle.
     */
    val g = Graphics.use { b =>
      import b._
      background(0.0)
      val theta = angle * math.Pi / 180

      def branch(h0: Double): Unit = {
        // Each branch will be 2/3rds the size of the previous one
        val h = h0 * 0.66

        // All recursive functions must have an exit condition!!!!
        // Here, ours is when the length of the branch is 2 pixels or less
        if (h > 2) {
          pushMatrix()        // Save the current state of transformation (i.e. where are we now)
          rotate(theta)       // Rotate by theta
          line(0, 0, 0, -h)   // Draw the branch
          translate(0, -h)    // Move to the end of the branch
          branch(h)           // Ok, now call myself to draw two new branches!!
          popMatrix()         // Whenever we get back here, we "pop" in order to restore the previous matrix state
          // Repeat the same thing, only branch off to the "left" this time!
          pushMatrix()
          rotate(-theta)
          line(0, 0, 0, -h)
          translate(0, -h)
          branch(h)
          popMatrix()
        }
      }

      // Start the tree from the bottom of the screen
      translate(width/2, height)
      val len = math.min(width/4, height/3)
      // Draw a line 120 pixels
      line(0, 0, 0, -len)
      // Move to the end of that line
      translate(0, -len)
      // Start the recursive branching!
      branch(len)
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
          g2.setColor(new java.awt.Color(0xffCCCCCC))
          g2.fillRect(0, 0, w, h)
          // g2.setColor(awt.Color.black)
          g2.setRenderingHint(awt.RenderingHints.KEY_ANTIALIASING, awt.RenderingHints.VALUE_ANTIALIAS_ON)
          val gl = new AWTGraphics2D(g2, w, h)
          // Processing defaults:
          gl.fillPaint    = _Color.RGB4(0x000)
          gl.strokePaint  = _Color.RGB4(0xFFF)
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
