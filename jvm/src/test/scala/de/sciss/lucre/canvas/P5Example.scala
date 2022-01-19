package de.sciss.lucre.canvas

import de.sciss.lucre.canvas.graph.Graphics
import de.sciss.lucre.expr.graph.{Ex, Var}
import de.sciss.lucre.canvas.Import._
import de.sciss.numbers.Implicits._

object P5Example {
  trait Category  extends Product { def members : Seq[Member    ] }
  trait Member    extends Product { def examples: Seq[P5Example ] }

  val categories: Seq[Category] = Seq(
    Basics, Topics
  )

  case object Basics extends Category {
    override val members: Seq[Member] = Seq(
      Arrays, Control, Form, Math, Structure, Transform
    )

    case object Arrays extends Member {
      override val examples: Seq[P5Example] = Seq(
        Array, Array2D
      )

      /**
       * Array.
       *
       * An array is a list of data. Each piece of data in an array
       * is identified by an index number representing its position in
       * the array. Arrays are zero based, which means that the first
       * element in the array is [0], the second element is [1], and so on.
       * In this example, an array named "coswave" is created and
       * filled with the cosine values. This data is displayed three
       * separate ways on the screen.
       */
      case object Array extends P5Example(0, 90, 640, 360) {
        override def make(a: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._
          import math._

          val cosWave = scala.Array.tabulate(width) { i =>
            val amount = i.linLin(0, width, 0.0, Pi)
            abs(cos(amount))
          }
          background(1.0)

          var y1 = 0
          var y2 = height/3
          for (i <- 0 until width) {
            stroke(cosWave(i))
            line(i, y1, i, y2)
          }

          y1 = y2
          y2 = y1 + y1
          for (i <- 0 until width) {
            stroke(cosWave(i) / 4)
            line(i, y1, i, y2)
          }

          y1 = y2
          y2 = height
          for (i <- 0 until width) {
            stroke(1.0 - cosWave(i))
            line(i, y1, i, y2)
          }
        }
      }

      /**
       * Array 2D.
       *
       * Demonstrates the syntax for creating a two-dimensional (2D) array.
       * Values in a 2D array are accessed through two index values.
       * 2D arrays are useful for storing images. In this example, each dot
       * is colored in relation to its distance from the center of the image.
       */
      case object Array2D extends P5Example(0, 90, 640, 360) {
        override def make(a: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._
          import math._

          def dist(x1: Double, y1: Double, x2: Double, y2: Double) =
            sqrt((x2 - x1).squared + (y2 - y1).squared)

          val maxDistance = dist(width / 2, height / 2, width, height)
          val distances = scala.Array.ofDim[Double](width, height)
          for(y <- 0 until height) {
            for (x <- 0 until width) {
              val distance = dist(width / 2, height / 2, x, y)
              distances(x)(y) = distance / maxDistance
            }
          }
          val spacer = 10
          strokeWeight(6)
          background(0)
          // This embedded loop skips over values in the arrays based on
          // the spacer variable, so there are more values in the array
          // than are drawn here. Change the value of the spacer variable
          // to change the density of the points
          for (y <- 0 until height by spacer) {
            for (x <- 0 until width by spacer) {
              stroke(distances(x)(y))
              point(x + spacer / 2, y + spacer / 2)
            }
          }
        }
      }
    }
    case object Control extends Member {
      override val examples: Seq[P5Example] = Seq(
        Conditionals1, Iteration
      )

      /**
       * Conditionals 1.
       *
       * Conditions are like questions.
       * They allow a program to decide to take one action if
       * the answer to a question is "true" or to do another action
       * if the answer to the question is "false."<br />
       * The questions asked within a program are always logical
       * or relational statements. For example, if the variable 'i' is
       * equal to zero then draw a line.
       */
      case object Conditionals1 extends P5Example(0, 90, 640, 360) {
        override def make(a: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._

          background(0)
          for (i <- 10 until width by 10) { // If 'i' divides by 20 with no remainder draw
            // the first line, else draw the second line
            if ((i % 20) == 0) {
              stroke(1.0)
              line(i, 80, i, height / 2)
            }
            else {
              stroke(0.6)
              line(i, 20, i, 180)
            }
          }
        }
      }

      /**
       * Iteration.
       *
       * Iteration with a "for" structure to construct repetitive forms.
       */
      case object Iteration extends P5Example(0, 90, 640, 360) {
        override def make(a: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._

          var y = 0
          val num = 14

          background(0.4)
          noStroke()

          // White bars
          fill(1.0)
          y = 60
          for (_ <- 0 until num / 3) {
            rect(50, y, 475, 10)
            y += 20
          }

          // Gray bars
          fill(0.2)
          y = 40
          for (_ <- 0 until num) {
            rect(405, y, 30, 10)
            y += 20
          }
          y = 50
          for (_ <- 0 until num) {
            rect(425, y, 30, 10)
            y += 20
          }

          // Thin lines
          y = 45
          fill(0.0)
          for (_ <- 0 until num - 1) {
            rect(120, y, 40, 1)
            y += 20
          }
        }
      }
    }
    case object Form extends Member {
      override val examples: Seq[P5Example] = Seq(
        RegularPolygon, ShapePrimitives
      )

      /**
       * Conditionals 1.
       *
       * Conditions are like questions.
       * They allow a program to decide to take one action if
       * the answer to a question is "true" or to do another action
       * if the answer to the question is "false."<br />
       * The questions asked within a program are always logical
       * or relational statements. For example, if the variable 'i' is
       * equal to zero then draw a line.
       */
      case object RegularPolygon extends P5Example(0, 320, 640, 360) {
        override def make(frameCount: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._
          import math._

          def polygon(x: Float, y: Float, radius: Float, nPoints: Int): Unit = {
            val angle = TwoPi / nPoints
            beginShape()
            var a = 0.0
            while (a < TwoPi) {
              val sx = x + cos(a) * radius
              val sy = y + sin(a) * radius
              vertex(sx, sy)

              a += angle
            }
            endShape(CLOSE)
          }

          background(0.4)
          pushMatrix()
          translate(width * 0.2, height * 0.5)
          rotate(frameCount / 200.0)
          polygon(0, 0, 82, 3) // Triangle

          popMatrix()
          pushMatrix()
          translate(width * 0.5, height * 0.5)
          rotate(frameCount / 50.0)
          polygon(0, 0, 80, 20) // Icosagon

          popMatrix()
          pushMatrix()
          translate(width * 0.8, height * 0.5)
          rotate(frameCount / -100.0)
          polygon(0, 0, 70, 7) // Heptagon

          popMatrix()
        }
      }

      /**
       * Shape Primitives.
       *
       * The basic shape primitive functions are triangle(),
       * rect(), quad(), ellipse(), and arc(). Squares are made
       * with rect() and circles are made with ellipse(). Each
       * of these functions requires a number of parameters to
       * determine the shape's position and size.
       *//**
       * Shape Primitives.
       *
       * The basic shape primitive functions are triangle(),
       * rect(), quad(), ellipse(), and arc(). Squares are made
       * with rect() and circles are made with ellipse(). Each
       * of these functions requires a number of parameters to
       * determine the shape's position and size.
       */
      case object ShapePrimitives extends P5Example(0, 320, 640, 360) {
        override def make(frameCount: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._
          import math._

          background(0.0)
          noStroke()

          fill(0.8)
          triangle(18, 18, 18, 360, 81, 360)

          fill(0.4)
          rect(81, 81, 63, 63)

          fill(0.8)
          quad(189, 18, 216, 18, 216, 360, 144, 360)

          fill(1.0)
          ellipse(252, 144, 72, 72)

          fill(0.8)
          triangle(288, 18, 351, 360, 288, 360)

          fill(1.0)
          arc(479, 300, 280, 280, Pi, TwoPi)
        }
      }
    }
    case object Math extends Member {
      override val examples: Seq[P5Example] = Seq(
        SineCosine
      )

      /**
       * Sine Cosine.
       *
       * Linear movement with sin() and cos().
       * Numbers between 0 and PI*2 (TWO_PI which angles roughly 6.28)
       * are put into these functions and numbers between -1 and 1 are
       * returned. These values are then scaled to produce larger movements.
       */
      case object SineCosine extends P5Example(0, 320, 640, 360) {
        override def make(frameCount: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._
          import math._

          val angle1 = frameCount * 2
          val angle2 = frameCount * 3
          val scalar: Ex[Double] = 70.0

          noStroke()
          rectMode(CENTER)

          background(0)
          val ang1  = angle1 * Pi / 180 // radians(angle1)
          val ang2  = angle2 * Pi / 180 // radians(angle2)
          val wH: Ex[Double] = width  * 0.5
          val hH: Ex[Double] = height * 0.5
          val x1    = wH + (scalar * ang1.cos)
          val x2    = wH + (scalar * ang2.cos)
          val y1    = hH + (scalar * ang1.sin)
          val y2    = hH + (scalar * ang2.sin)
          fill(1.0)
          rect(width * 0.5, height * 0.5, 140, 140)
          fill(0.0, 0.4, 0.6)
          ellipse(x1, height * 0.5 - 120, scalar, scalar)
          ellipse(x2, height * 0.5 + 120, scalar, scalar)
          fill(1.0, 0.8, 0.0)
          ellipse(width * 0.5 - 120, y1, scalar, scalar)
          ellipse(width * 0.5 + 120, y2, scalar, scalar)
        }
      }
    }
    case object Structure extends Member {
      override val examples: Seq[P5Example] = Seq(
        Recursion
      )

      /**
       * Recursion.
       *
       * A demonstration of recursion, which means functions call themselves.
       * Notice how the drawCircle() function calls itself at the end of its block.
       * It continues to do this until the variable "level" is equal to 1.
       */
      case object Recursion extends P5Example(0, 320, 640, 360) {
        override def make(frameCount: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._

          noStroke()
          drawCircle(width / 2, 280, 6)

          def drawCircle(x: Int, radius: Int, level: Int): Unit = {
            val tt = level / 8.0
            fill(tt)
            ellipse(x, height / 2, radius * 2, radius * 2)
            if (level > 1) {
              val levelN = level - 1
              drawCircle(x - radius / 2, radius / 2, levelN)
              drawCircle(x + radius / 2, radius / 2, levelN)
            }
          }
        }
      }
    }
    case object Transform extends Member {
      override val examples: Seq[P5Example] = Seq(
        Scale
      )

      /**
       * Scale
       * by Denis Grutze.
       *
       * Parameters for the scale() function are values specified
       * as decimal percentages. For example, the method call scale(2.0)
       * will increase the dimension of the shape by 200 percent.
       * Objects always scale from the origin.
       */
      case object Scale extends P5Example(0, 180, 640, 360) {
        override def make(frameCount: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._

          noStroke()
          rectMode(CENTER)

          background(0.4)
          val a = frameCount * 0.04
          val s = a.cos * 2
          translate(width / 2, height / 2)
          scale(s)
          fill(0.2)
          rect(0, 0, 50, 50)
          translate(75, 0)
          fill(1.0)
          scale(s)
          rect(0, 0, 50, 50)
        }
      }
    }
  }
  case object Topics extends Category {
    override val members: Seq[Member] = Seq(
      Fractals
    )

    case object Fractals extends Member {
      override val examples: Seq[P5Example] = Seq(
        Tree
      )

      /**
       * Recursive Tree
       * by Daniel Shiffman.
       *
       * Renders a simple tree-like structure via recursion.
       * The branching angle is calculated as a function of
       * the horizontal mouse location. Move the mouse left
       * and right to change the angle.
       */
      case object Tree extends P5Example(0, 90, 640, 360) {
        override def make(a: Var[Int], width: Int, height: Int): Graphics = Graphics.use { b =>
          import b._
          background(0.0)
          stroke(1.0)

          val theta = a * math.Pi / 180

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
      }
    }
  }
}
abstract class P5Example(val aMin: Int, val aMax: Int, val defaultWidth: Int, val defaultHeight: Int) extends Product {
  def make(a: Var[Int], width: Int = defaultWidth, height: Int = defaultHeight): Graphics
}