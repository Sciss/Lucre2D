package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.{Color => _Color}
import de.sciss.lucre.edit.UndoManager
import de.sciss.lucre.expr.Context
import de.sciss.lucre.{InMemory, Workspace}
import de.sciss.numbers.Implicits.doubleNumberWrapper
import org.rogach.scallop.{ScallopConf, ScallopOption => Opt}

import java.awt
import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{BorderLayout, EventQueue}
import java.util.TimerTask
import javax.swing.event.ChangeEvent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.{JComponent, JFrame, JScrollPane, JSlider, JTree, SwingUtilities, WindowConstants}

object P5Examples {
  case class Config(widthOpt: Option[Int] = None, heightOpt: Option[Int] = None,
                    example: Option[P5Example] = None, fullScreen: Boolean = false,
                    animate: Boolean = false, animatePeriod: Double = 2.0,
                    animateFPS: Double = 30.0, animateTri: Boolean = false,
                   )

  def main(args: Array[String]): Unit = {
    def findExample(name: String): Option[P5Example] = {
      val nameL = name.toLowerCase
      P5Example.examples.find(_.productPrefix.toLowerCase == nameL)
    }

    object p extends ScallopConf(args) {
      printedName = "P5Examples"
      private val default = Config()

      val width: Opt[Int] = opt(
        descr = "Window width in pixels.",
        validate = x => x >= 0
      )
      val height: Opt[Int] = opt(
        descr = "Window height in pixels.",
        validate = x => x >= 0
      )
      val example: Opt[String] = opt(
        descr = "Name of example to launch",
        validate = findExample(_).isDefined
      )
      val fullScreen: Opt[Boolean] = toggle(name = "full-screen", default = Some(default.fullScreen),
        descrYes = "Put into fullscreen mode",
      )
      val animate: Opt[Boolean] = toggle(default = Some(default.animate),
        descrYes = "Animate the slider control",
      )
      val animatePeriod: Opt[Double] = opt(name = "animate-period", default = Some(default.animatePeriod),
        descr = s"Animation period in seconds (default: ${default.animatePeriod})",
        validate = x => x > 0.0
      )
      val animateFPS: Opt[Double] = opt(name = "animate-fps", default = Some(default.animateFPS),
        descr = s"Animation frequency in frames per second (default: ${default.animateFPS})",
        validate = x => x > 0.0
      )
      val animateTri: Opt[Boolean] = toggle(name = "animate-tri", default = Some(default.animateTri),
        descrYes = "Animate as 'triangle', forward-backward",
      )

      verify()
      val config: Config = Config(
        widthOpt      = width  .toOption,
        heightOpt     = height .toOption,
        example       = example.toOption.flatMap(findExample),
        fullScreen    = fullScreen(),
        animate       = animate(),
        animatePeriod = animatePeriod(),
        animateFPS    = animateFPS(),
        animateTri    = animateTri(),
      )
    }

    EventQueue.invokeLater { () =>
      val c = p.config
      c.example match {
        case Some(ex) => run(c, ex)
        case None     => select(p.config)
      }
    }
  }

  def select(c: Config): Unit = {
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
                  run(c = c, ex = ex)
                case _ =>
              }

            case _ => throw new IllegalStateException()
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

  def run(c: Config, ex: P5Example): Unit = {
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
    val width   = c.widthOpt  .getOrElse(ex.defaultWidth)
    val height  = c.heightOpt .getOrElse(ex.defaultHeight)
    val g   = ex.make(a, width = width, height = height)
//    println(g)

    var sq: Seq[Graphics.Elem] = Nil
    var comp: JComponent = null
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
          comp.repaint()
          comp.getToolkit.sync()
        })
      }
      sq = sqEx.value
    }

    //    de.sciss.lucre.Log.event.level = de.sciss.log.Level.Debug

    EventQueue.invokeLater { () =>
      comp = new JComponent {
        setPreferredSize(new awt.Dimension(width, height))
        setOpaque(true)

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

      def setAngleFromUI(v: Int): Unit =
        system.step { implicit tx =>
          val vEx = new Const.Expanded[T, Int](v)
          angleEx.update(vEx)
        }

      val sl = new JSlider(ex.aMin, ex.aMax)
      sl.addChangeListener((_: ChangeEvent) => {
        val v = sl.getValue
        setAngleFromUI(v)
      })
      new JFrame { fr =>
        if (c.fullScreen) {
          setUndecorated(true)
        } else {
          setTitle(ex.productPrefix)
        }

        getContentPane.add(comp, BorderLayout.CENTER)
        if (!c.fullScreen) getContentPane.add(sl, BorderLayout.SOUTH)
        pack()
        if (!c.fullScreen) setLocationRelativeTo(null)
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)

        if (c.fullScreen) toggleFullScreen(fr)
      }

      if (c.animate) {
        val sch = new java.util.Timer
        val dly = math.max(1, (1000 / c.animateFPS + 0.5).toInt)
        val tt = new TimerTask {
          private var frameCount  = 0
          private val modulus     = if (c.animateTri) 2.0 else 1.0

          override def run(): Unit = {
            frameCount += 1
            val time    = frameCount * dly * 0.001
            val phase0  = (time / c.animatePeriod) % modulus
            val phase   = if (phase0 < 1.0) phase0 else 2.0 - phase0
            val v       = (phase.linLin(0, 1.0, ex.aMin, ex.aMax) + 0.5).toInt
            setAngleFromUI(v)
          }
        }
        sch.scheduleAtFixedRate(tt, dly, dly)
      }
    }
  }

  def toggleFullScreen(frame: javax.swing.JFrame): Unit = {
    val gc = frame /*.peer*/.getGraphicsConfiguration
    val sd = gc.getDevice
    val w  = SwingUtilities.getWindowAncestor(frame /*.peer*/.getRootPane)
    sd.setFullScreenWindow(if (sd.getFullScreenWindow == w) null else w)
    frame.addKeyListener(new KeyAdapter {
      override def keyPressed(e: KeyEvent): Unit =
        if (e.getKeyCode == KeyEvent.VK_ESCAPE) sys.exit() // frame.dispose() // sd.setFullScreenWindow(null)
    })
  }
}
