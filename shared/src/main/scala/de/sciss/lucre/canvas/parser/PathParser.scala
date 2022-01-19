package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the PathParser Java class of Apache Batik
// as released under Apache License -- see original file header below.
// So it can be used in Scala.js.

/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

import java.io.IOException


/**
 * This class implements an event-based parser for the SVG path's d
 * attribute values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: PathParser.java 1802297 2017-07-18 13:58:12Z ssteiner $
 */
class PathParser() extends NumberParser {
  /**
   * The path handler used to report parse events.
   */
  protected var pathHandler: PathHandler = DefaultPathHandler

  /**
   * Allows an application to register a path handler.
   *
   * <p>If the application does not register a handler, all
   * events reported by the parser will be silently ignored.
   *
   * <p>Applications may register a new or different handler in the
   * middle of a parse, and the parser must begin using the new
   * handler immediately.</p>
   *
   * @param handler The transform list handler.
   */
  def setPathHandler(handler: PathHandler): Unit =
    pathHandler = handler

  /**
   * Returns the path handler in use.
   */
  def getPathHandler: PathHandler = pathHandler

  @throws[ParseException]
  @throws[IOException]
  override protected def doParse(): Unit = {
    pathHandler.startPath()
    current = reader.read()
    
    var loop = true

    while (loop) 
      try current match {
        case 0xD | 0xA | 0x20 | 0x9 =>
          current = reader.read()
  
        case 'z' | 'Z' =>
          current = reader.read()
          pathHandler.closePath()
  
        case 'm' => parse_m()
        case 'M' => parse_M()
        case 'l' => parse_l()
        case 'L' => parse_L()
        case 'h' => parse_h()
        case 'H' => parse_H()
        case 'v' => parse_v()
        case 'V' => parse_V()
        case 'c' => parse_c()
        case 'C' => parse_C()
        case 'q' => parse_q()
        case 'Q' => parse_Q()
        case 's' => parse_s()
        case 'S' => parse_S()
        case 't' => parse_t()
        case 'T' => parse_T()
        case 'a' => parse_a()
        case 'A' => parse_A()
  
        case -1 =>
          loop = false
  
        case _ =>
          reportUnexpected()
          // XXX TODO ok to continue?
      }
      catch {
        case e: ParseException =>
          errorHandler.error(e)
          skipSubPath()
      }
      
    skipSpaces()
    if (current != -1) reportError("end.of.stream.expected", Array[AnyRef](current.asInstanceOf[AnyRef]))
    pathHandler.endPath()
  }

  /**
   * Parses a 'm' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_m(): Unit = {
    current = reader.read()
    skipSpaces()
    val x = parseFloat()
    skipCommaSpaces()
    val y = parseFloat()
    pathHandler.moveToRel(x, y)
    val expectNumber = skipCommaSpaces2()
    parse_l_core(expectNumber)
  }

  /**
   * Parses a 'M' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_M(): Unit = {
    current = reader.read()
    skipSpaces()
    val x = parseFloat()
    skipCommaSpaces()
    val y = parseFloat()
    pathHandler.moveToAbs(x, y)
    val expectNumber = skipCommaSpaces2()
    parse_L_core(expectNumber)
  }

  /**
   * Parses a 'l' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_l(): Unit = {
    current = reader.read()
    skipSpaces()
    parse_l_core(true)
  }

  @throws[ParseException]
  @throws[IOException]
  protected def parse_l_core(expectNumber: Boolean): Unit = {
    var _expectNumber = expectNumber
    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (_expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.lineToRel(x, y)
      _expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'L' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_L(): Unit = {
    current = reader.read()
    skipSpaces()
    parse_L_core(true)
  }

  @throws[ParseException]
  @throws[IOException]
  protected def parse_L_core(expectNumber: Boolean): Unit = {
    var _expectNumber = expectNumber
    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (_expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.lineToAbs(x, y)
      _expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'h' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_h(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      pathHandler.lineToHorizontalRel(x)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'H' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_H(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      pathHandler.lineToHorizontalAbs(x)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'v' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_v(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      pathHandler.lineToVerticalRel(x)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'V' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_V(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      pathHandler.lineToVerticalAbs(x)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'c' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_c(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x1 = parseFloat()
      skipCommaSpaces()
      val y1 = parseFloat()
      skipCommaSpaces()
      val x2 = parseFloat()
      skipCommaSpaces()
      val y2 = parseFloat()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToCubicRel(x1, y1, x2, y2, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'C' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_C(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x1 = parseFloat()
      skipCommaSpaces()
      val y1 = parseFloat()
      skipCommaSpaces()
      val x2 = parseFloat()
      skipCommaSpaces()
      val y2 = parseFloat()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToCubicAbs(x1, y1, x2, y2, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'q' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_q(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x1 = parseFloat()
      skipCommaSpaces()
      val y1 = parseFloat()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToQuadraticRel(x1, y1, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'Q' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_Q(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x1 = parseFloat()
      skipCommaSpaces()
      val y1 = parseFloat()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToQuadraticAbs(x1, y1, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 's' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_s(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x2 = parseFloat()
      skipCommaSpaces()
      val y2 = parseFloat()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToCubicSmoothRel(x2, y2, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'S' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_S(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x2 = parseFloat()
      skipCommaSpaces()
      val y2 = parseFloat()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToCubicSmoothAbs(x2, y2, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 't' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_t(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToQuadraticSmoothRel(x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'T' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_T(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.curveToQuadraticSmoothAbs(x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'a' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_a(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val rx = parseFloat()
      skipCommaSpaces()
      val ry = parseFloat()
      skipCommaSpaces()
      val ax = parseFloat()
      skipCommaSpaces()
      var laf = false
      current match {
        case '0' => laf = false
        case '1' => laf = true
        case _ =>
          reportUnexpected()
          return
      }
      current = reader.read()
      skipCommaSpaces()
      var sf = false
      current match {
        case '0' => sf = false
        case '1' => sf = true
        case _ =>
          reportUnexpected()
          return
      }
      current = reader.read()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.arcRel(rx, ry, ax, laf, sf, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Parses a 'A' command.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parse_A(): Unit = {
    current = reader.read()
    skipSpaces()
    var expectNumber = true

    while (true) {
      current match {
        case '+' | '-' | '.'| '0' | '1' | '2' | '3' | '4' | '5' | '6'| '7' | '8' | '9' => ()
        case _ =>
          if (expectNumber) reportUnexpected()
          return
      }
      val rx = parseFloat()
      skipCommaSpaces()
      val ry = parseFloat()
      skipCommaSpaces()
      val ax = parseFloat()
      skipCommaSpaces()
      var laf = false
      current match {
        case '0' => laf = false
        case '1' => laf = true
        case _ =>
          reportUnexpected()
          return
      }
      current = reader.read()
      skipCommaSpaces()
      var sf = false
      current match {
        case '0' => sf = false
        case '1' => sf = true
        case _ =>
          reportUnexpected()
          return
      }
      current = reader.read()
      skipCommaSpaces()
      val x = parseFloat()
      skipCommaSpaces()
      val y = parseFloat()
      pathHandler.arcAbs(rx, ry, ax, laf, sf, x, y)
      expectNumber = skipCommaSpaces2()
    }
  }

  /**
   * Skips a sub-path.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def skipSubPath(): Unit =
    while (true) {
      current match {
        case -1 | 'm' | 'M' => return
        case _ => ()
      }
      current = reader.read()
    }

  @throws[ParseException]
  @throws[IOException]
  protected def reportUnexpected(): Unit = {
    reportUnexpectedCharacterError()
    skipSubPath()
  }

  /**
   * Skips the whitespaces and an optional comma.
   *
   * @return true if comma was skipped.
   */
  @throws[IOException]
  protected def skipCommaSpaces2(): Boolean = {
    while ({
      current match {
        case 0x20 | 0x9 | 0xD | 0xA =>
          current = reader.read()
          true
        case _ => false
      }
    }) ()

    if (current != ',') return false // no comma.

    while ({
      current = reader.read()
      current match {
        case 0x20 | 0x9 | 0xD | 0xA => true
        case _ => false
      }
    }) ()

    true // had comma
  }
}
