package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.{Color => _Color}
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}

import java.awt
import java.awt.event.{MouseAdapter, MouseEvent}
import java.awt.{BorderLayout, EventQueue}
import javax.swing.event.ChangeEvent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.{JComponent, JFrame, JScrollPane, JSlider, JTree, WindowConstants}

object P5Examples {
  def main(args: Array[String]): Unit = {
    val widthI  = args.indexOf("--width"  ) + 1
    val heightI = args.indexOf("--height" ) + 1
    val width   = if (widthI  == 0) None else Some(args(widthI  ).toInt)
    val height  = if (heightI == 0) None else Some(args(heightI ).toInt)
//    run(width, height)

    EventQueue.invokeLater { () =>
      select(width, height)
    }
  }

  def select(widthOpt: Option[Int], heightOpt: Option[Int]): Unit = {
    val root = new DefaultMutableTreeNode("root")
    P5Example.categories.foreach { cat =>
      val nCateg = new DefaultMutableTreeNode(cat)
      cat.members.foreach { mem =>
        val nMem = new DefaultMutableTreeNode(mem)
        nCateg.add(nMem)
        mem.examples.foreach { ex =>
          val nEx = new DefaultMutableTreeNode(ex)
          nMem.add(nEx)
        }
      }
      root.add(nCateg)
    }
    val tree = new JTree(root)
    tree.setRootVisible(false)
    tree.setPreferredSize({
      val d = tree.getPreferredSize
      d.width = math.max(d.width, 240)
      d
    })
    tree.addMouseListener(new MouseAdapter {
      override def mousePressed(e: MouseEvent): Unit = {
        // val selRow  = tree.getRowForLocation  (e.getX, e.getY)
        val selPath = tree.getPathForLocation (e.getX, e.getY)
        if (/*selRow != -1 &&*/ e.getClickCount == 2) {
          selPath.getLastPathComponent match {
            case n: DefaultMutableTreeNode =>
              n.getUserObject match {
                case ex: P5Example =>
                  run(widthOpt = widthOpt, heightOpt = heightOpt, ex = ex)
                case _ =>
              }
          }
        }
      }
    })

    new JFrame("Examples from Processing") {
      getContentPane.add(new JScrollPane(tree,
//        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
      ),
        BorderLayout.CENTER)
      pack()
      setLocationRelativeTo(null)
      setLocation(getX/3, getY)
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      setVisible(true)
    }
  }

  def run(widthOpt: Option[Int], heightOpt: Option[Int], ex: P5Example): Unit = {
    import de.sciss.lucre.canvas.graph._
    import de.sciss.lucre.expr.graph._

//    val ex      = P5Example.Topics.Fractals.Tree
//    val ex      = P5Example.Basics.Arrays.Array2D
//    val ex      = P5Example.Basics.Control.Iteration
//    val ex      = P5Example.Basics.Form.ShapePrimitives
//    val ex      = P5Example.Basics.Math.SineCosine
//    val ex      = P5Example.Basics.Structure.Recursion
//    val ex      = P5Example.Basics.Transform.Scale
    val a       = Var[Int]((ex.aMin + ex.aMax) >> 1)
    val width   = widthOpt  .getOrElse(ex.defaultWidth)
    val height  = heightOpt .getOrElse(ex.defaultHeight)
    val g   = ex.make(a, width = width, height = height)
//    println(g)

    var sq: Seq[Graphics.Elem] = Nil
    var c: JComponent = null
    var angleEx: Var.Expanded[T, Int] = null

    type S = InMemory
    type T = InMemory.Txn
    implicit val system: S = InMemory()
    system.step { implicit tx =>
      implicit val ws   : Workspace   [T] = Workspace.Implicits.dummy
      implicit val undo : UndoManager [T] = UndoManager()
      implicit val ctx  : Context     [T] = Context()
      angleEx = a  .expand[T]
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
          gl.fillPaint    = _Color.RGB4(0xFFF)
          gl.strokePaint  = _Color.RGB4(0x000)
          sq.foreach { elem =>
            elem.render(gl)
          }
        }
      }
      val sl = new JSlider(ex.aMin, ex.aMax)
      sl.addChangeListener((_: ChangeEvent) => {
        val v = sl.getValue
        system.step { implicit tx =>
          val vEx = new Const.Expanded[T, Int](v)
          angleEx.update(vEx)
        }
      })
      new JFrame(ex.productPrefix) {
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
