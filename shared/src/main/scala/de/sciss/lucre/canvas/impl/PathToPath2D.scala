package de.sciss.lucre.canvas.impl

import de.sciss.lucre.canvas.Path2D
import de.sciss.lucre.canvas.parser.PathHandler

class PathToPath2D extends PathHandler {
  private val out = new Path2D.Float

  private var x       = 0f
  private var y       = 0f
  private var cx      = 0f
  private var cy      = 0f

//  var indent          = "    p."
//  var eol             = ";\n"

  private var ended   = false

  private var isFirst = false
  private var startX  = 0f
  private var startY  = 0f

  def result(): Path2D /*String*/ = {
    require(ended, "Path has not yet ended")
    out // .toString
  }

  def startPath(): Unit = {
    // log("startPath()")
    // newPath()
    x   = 0
    y   = 0
    cx  = 0
    cy  = 0
    isFirst = true
  }

  //    private def newPath(): Unit = {
  //      gpIdx += 1
  //      path   = s"p$gpIdx"
  //      // out.write(s"val $path = new GeneralPath(Path2D.WIND_EVEN_ODD)\n")
  //    }

  def endPath(): Unit = {
    // log("endPath()")
    require(!ended, "Path has already ended")
    // out.write(s"$path.closePath()")
    ended = true
  }

  def moveToRel(x3: Float, y3: Float): Unit = {
    // log(s"movetoRel($x3, $y3)")
    x  += x3
    y  += y3
    cx  = cx
    cy  = cy
    pathMoveTo(x, y)
  }

  def moveToAbs(x3: Float, y3: Float): Unit = {
    // log(s"movetoAbs($x3, $y3)")
    cx  = x3
    cy  = y3
    x   = x3
    y   = y3
    pathMoveTo(x, y)
  }

  def closePath(): Unit = {
    // log("closePath()")
    //      x   = 0
    //      y   = 0
    //      cx  = 0
    //      cy  = 0
    lineToAbs(startX, startY)

    //      x   = startX
    //      y   = startY
    //      cx  = startX
    //      cy  = startY
    // out.write(s"${indent}closePath();\n")
    isFirst = true

    //      out.write(s"g2.fill($path)\n")
    //      newPath()
  }

  def lineToRel(x3: Float, y3: Float): Unit = {
    // log(s"linetoRel($x3, $y3)")
    x += x3
    y += y3
    cx = x
    cy = y
    pathLineTo(x, y)
  }

  def lineToAbs(x3: Float, y3: Float): Unit = {
    // log(s"linetoAbs($x3, $y3)")
    x   = x3
    y   = y3
    cx  = x
    cy  = y
    pathLineTo(x, y)
  }

  def lineToHorizontalRel(x3: Float): Unit = {
    // log(s"linetoHorizontalRel($x3)")
    x += x3
    cx = x
    cy = y
    pathLineTo(x, y)
  }

  def lineToHorizontalAbs(x3: Float): Unit = {
    // log(s"linetoHorizontalAbs($x3)")
    x  = x3
    cx = x
    cy = y
    pathLineTo(x, y)
  }

  def lineToVerticalRel(y3: Float): Unit = {
    // log(s"linetoVerticalRel($y3)")
    cx = x
    y += y3
    cy = y
    pathLineTo(x, y)
  }

  def lineToVerticalAbs(y3: Float): Unit = {
    // log(s"linetoVerticalAbs($y3)")
    cx = x
    y  = y3
    cy = y
    pathLineTo(x, y)
  }

  def curveToCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
    // log(s"curvetoCubicRel($x1, $y1, $x2, $y2, $x3, $y3)")
    val x0  = x + x1
    val y0  = y + y1
    cx      = x + x2
    cy      = y + y2
    x      += x3
    y      += y3
    pathCurveTo(x0, y0, cx, cy, x, y)
  }

  def curveToCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
    // log(s"curvetoCubicAbs($x1, $y1, $x2, $y2, $x3, $y3)")
    cx = x2
    cy = y2
    x  = x3
    y  = y3
    pathCurveTo(x1, y1, cx, cy, x, y)
  }

  def curveToCubicSmoothRel(x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
    // log(s"curvetoCubicSmoothRel($x2, $y2, $x3, $y3)")
    val x1 = x * 2 - cx
    val y1 = y * 2 - cy
    cx     = x + x2
    cy     = y + y2
    x     += x3
    y     += y3
    pathCurveTo(x1, y1, cx, cy, x, y)
  }

  def curveToCubicSmoothAbs(x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
    // log(s"curvetoCubicSmoothAbs($x2, $y2, $x3, $y3)")
    val x1  = x * 2 - cx
    val y1  = y * 2 - cy
    cx      = x2
    cy      = y2
    x       = x3
    y       = y3
    pathCurveTo(x1, y1, cx, cy, x, y)
  }

  def curveToQuadraticRel(x1: Float, y1: Float, x2: Float, y2: Float): Unit = {
    // log(s"curvetoQuadraticRel($p1, $p2, $p3, $p4)")
    cx      = x + x1
    cy      = y + y1
    x      += x2
    y      += y2
    pathQuadTo(cx, cy, x, y)
  }

  def curveToQuadraticAbs(x1: Float, y1: Float, x2: Float, y2: Float): Unit = {
    // log(s"curvetoQuadraticAbs($p1, $p2, $p3, $p4)")
    cx = x1
    cy = y1
    x  = x2
    y  = y2
    pathQuadTo(cx, cy, x, y)
  }

  def curveToQuadraticSmoothRel(x2: Float, y2: Float): Unit = {
    // log(s"curvetoQuadraticSmoothRel($p1, $p2)")
    cx = x * 2 - cx
    cy = y * 2 - cy
    x  += x2
    y  += y2
    pathQuadTo(cx, cy, x, y)
  }

  def curveToQuadraticSmoothAbs(x2: Float, y2: Float): Unit = {
    // log(s"curvetoQuadraticSmoothAbs($p1, $p2)")
    // "The control point is assumed to be the
    // reflection of the control point on the previous command
    // relative to the current point."
    cx = x * 2 - cx
    cy = y * 2 - cy
    x  = x2
    y  = y2
    pathQuadTo(cx, cy, x, y)
  }

  def arcRel(p1: Float, p2: Float, p3: Float, p4: Boolean, p5: Boolean, p6: Float, p7: Float): Unit = {
    // log(s"arcRel($p1, $p2, $p3, $p4, $p5, $p6, $p7)")
    ???
  }

  def arcAbs(p1: Float, p2: Float, p3: Float, p4: Boolean, p5: Boolean, p6: Float, p7: Float): Unit = {
    // log(s"arcAbs($p1, $p2, $p3, $p4, $p5, $p6, $p7)")
    ???
  }

  private def pathMoveTo(x: Float, y: Float): Unit = {
    if (isFirst) {
      startX  = x
      startY  = y
      isFirst = false
    }
    // out.write(s"${indent}moveTo(${x}f, ${y}f)$eol")
    out.moveTo(x, y)
  }

  private def pathLineTo(x: Float, y: Float): Unit = {
    // out.write(s"${indent}lineTo(${x}f, ${y}f)$eol")
    out.lineTo(x, y)
  }

  private def pathCurveTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
    // out.write(s"${indent}curveTo(${x1}f, ${y1}f, ${x2}f, ${y2}f, ${x3}f, ${y3}f)$eol")
    out.curveTo(x1, y1, x2, y2, x3, y3)
  }

  private def pathQuadTo(x1: Float, y1: Float, x2: Float, y2: Float): Unit = {
    // out.write(s"${indent}quadTo(${x1}f, ${y1}f, ${x2}f, ${y2}f)$eol")
    out.quadTo(x1, y1, x2, y2)
  }
}
