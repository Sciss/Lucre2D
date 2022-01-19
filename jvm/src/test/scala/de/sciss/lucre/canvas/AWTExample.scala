package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.Import._
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}

import java.awt
import java.awt.event.ActionEvent
import java.awt.{BorderLayout, EventQueue}
import javax.swing.event.ChangeEvent
import javax.swing.{JComboBox, JComponent, JFrame, JSlider, WindowConstants}

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
    import de.sciss.lucre.expr.graph._

    val width     = 300
    val height    = 200

    val radius    = Var(80.0)
    val colorIdx  = Var[Int](0)
    val color     = (Seq(Color.green, Color.blue): Ex[Seq[Color]]).applyOption(colorIdx).getOrElse(Color.red)

    val g = Graphics(Seq(
      Rect(width = width /*%(100)*/, height = height /*%(100)*/).fill(Color.red),
      Circle(cx = 150, cy = 100, r = radius).fill(color),
    ))

    println(g)

    var sq: Seq[Graphics.Elem] = Nil
    var c: JComponent = null
    var radiusEx  : Var.Expanded[T, Double] = null
    var colorIdxEx: Var.Expanded[T, Int   ] = null

    type S = InMemory
    type T = InMemory.Txn
    implicit val system: S = InMemory()
    system.step { implicit tx =>
      implicit val ws   : Workspace   [T] = Workspace.Implicits.dummy
      implicit val undo : UndoManager [T] = UndoManager()
      implicit val ctx  : Context     [T] = Context()
      radiusEx    = radius  .expand[T]
      colorIdxEx  = colorIdx.expand[T]
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
      val cb = new JComboBox(Array("Green", "Blue"))
      cb.addActionListener((_: ActionEvent) => {
        val v = cb.getSelectedIndex
        system.step { implicit tx =>
          val vEx = new Const.Expanded[T, Int](v)
          colorIdxEx.update(vEx)
        }
      })
      val sl = new JSlider(0, 100)
      sl.addChangeListener((_: ChangeEvent) => {
        val v = sl.getValue
        system.step { implicit tx =>
          val vEx = new Const.Expanded[T, Double](v.toDouble)
          radiusEx.update(vEx)
        }
      })
      new JFrame("Lucre2D") {
        getContentPane.add(c, BorderLayout.CENTER)
        getContentPane.add(sl, BorderLayout.SOUTH)
        getContentPane.add(cb, BorderLayout.NORTH)
        pack()
        setLocationRelativeTo(null)
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
      }
    }
  }
}
