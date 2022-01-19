package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the PathHandler Java class of Apache Batik
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

/**
 * This interface must be implemented and then registered as the
 * handler of a <code>PathParser</code> instance in order to be
 * notified of parsing events.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: PathHandler.java 1733416 2016-03-03 07:07:13Z gadams $
 */
trait PathHandler {
  /**
   * Invoked when the path starts.
   *
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def startPath(): Unit

  /**
   * Invoked when the path ends.
   *
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def endPath(): Unit

  /**
   * Invoked when a relative moveto command has been parsed.
   * <p>Command : <b>m</b>
   *
   * @param x the relative x coordinate for the end point
   * @param y the relative y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def moveToRel(x: Float, y: Float): Unit

  /**
   * Invoked when an absolute moveto command has been parsed.
   * <p>Command : <b>M</b>
   *
   * @param x the absolute x coordinate for the end point
   * @param y the absolute y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def moveToAbs(x: Float, y: Float): Unit

  /**
   * Invoked when a closepath has been parsed.
   * <p>Command : <b>z</b> | <b>Z</b>
   *
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def closePath(): Unit

  /**
   * Invoked when a relative line command has been parsed.
   * <p>Command : <b>l</b>
   *
   * @param x the relative x coordinates for the end point
   * @param y the relative y coordinates for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def lineToRel(x: Float, y: Float): Unit

  /**
   * Invoked when an absolute line command has been parsed.
   * <p>Command : <b>L</b>
   *
   * @param x the absolute x coordinate for the end point
   * @param y the absolute y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def lineToAbs(x: Float, y: Float): Unit

  /**
   * Invoked when an horizontal relative line command has been parsed.
   * <p>Command : <b>h</b>
   *
   * @param x the relative X coordinate of the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def lineToHorizontalRel(x: Float): Unit

  /**
   * Invoked when an horizontal absolute line command has been parsed.
   * <p>Command : <b>H</b>
   *
   * @param x the absolute X coordinate of the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def lineToHorizontalAbs(x: Float): Unit

  /**
   * Invoked when a vertical relative line command has been parsed.
   * <p>Command : <b>v</b>
   *
   * @param y the relative Y coordinate of the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def lineToVerticalRel(y: Float): Unit

  /**
   * Invoked when a vertical absolute line command has been parsed.
   * <p>Command : <b>V</b>
   *
   * @param y the absolute Y coordinate of the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def lineToVerticalAbs(y: Float): Unit

  /**
   * Invoked when a relative cubic bezier curve command has been parsed.
   * <p>Command : <b>c</b>
   *
   * @param x1 the relative x coordinate for the first control point
   * @param y1 the relative y coordinate for the first control point
   * @param x2 the relative x coordinate for the second control point
   * @param y2 the relative y coordinate for the second control point
   * @param x  the relative x coordinate for the end point
   * @param y  the relative y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float): Unit

  /**
   * Invoked when an absolute cubic bezier curve command has been parsed.
   * <p>Command : <b>C</b>
   *
   * @param x1 the absolute x coordinate for the first control point
   * @param y1 the absolute y coordinate for the first control point
   * @param x2 the absolute x coordinate for the second control point
   * @param y2 the absolute y coordinate for the second control point
   * @param x  the absolute x coordinate for the end point
   * @param y  the absolute y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float): Unit

  /**
   * Invoked when a relative smooth cubic bezier curve command has
   * been parsed. The first control point is assumed to be the
   * reflection of the second control point on the previous command
   * relative to the current point.
   * <p>Command : <b>s</b>
   *
   * @param x2 the relative x coordinate for the second control point
   * @param y2 the relative y coordinate for the second control point
   * @param x  the relative x coordinate for the end point
   * @param y  the relative y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToCubicSmoothRel(x2: Float, y2: Float, x: Float, y: Float): Unit

  /**
   * Invoked when an absolute smooth cubic bezier curve command has
   * been parsed. The first control point is assumed to be the
   * reflection of the second control point on the previous command
   * relative to the current point.
   * <p>Command : <b>S</b>
   *
   * @param x2 the absolute x coordinate for the second control point
   * @param y2 the absolute y coordinate for the second control point
   * @param x  the absolute x coordinate for the end point
   * @param y  the absolute y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToCubicSmoothAbs(x2: Float, y2: Float, x: Float, y: Float): Unit

  /**
   * Invoked when a relative quadratic bezier curve command has been parsed.
   * <p>Command : <b>q</b>
   *
   * @param x1 the relative x coordinate for the control point
   * @param y1 the relative y coordinate for the control point
   * @param x  the relative x coordinate for the end point
   * @param y  the relative x coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToQuadraticRel(x1: Float, y1: Float, x: Float, y: Float): Unit

  /**
   * Invoked when an absolute quadratic bezier curve command has been parsed.
   * <p>Command : <b>Q</b>
   *
   * @param x1 the absolute x coordinate for the control point
   * @param y1 the absolute y coordinate for the control point
   * @param x  the absolute x coordinate for the end point
   * @param y  the absolute x coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToQuadraticAbs(x1: Float, y1: Float, x: Float, y: Float): Unit

  /**
   * Invoked when a relative smooth quadratic bezier curve command
   * has been parsed. The control point is assumed to be the
   * reflection of the control point on the previous command
   * relative to the current point.
   * <p>Command : <b>t</b>
   *
   * @param x the relative x coordinate for the end point
   * @param y the relative y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToQuadraticSmoothRel(x: Float, y: Float): Unit

  /**
   * Invoked when an absolute smooth quadratic bezier curve command
   * has been parsed. The control point is assumed to be the
   * reflection of the control point on the previous command
   * relative to the current point.
   * <p>Command : <b>T</b>
   *
   * @param x the absolute x coordinate for the end point
   * @param y the absolute y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def curveToQuadraticSmoothAbs(x: Float, y: Float): Unit

  /**
   * Invoked when a relative elliptical arc command has been parsed.
   * <p>Command : <b>a</b>
   *
   * @param rx            the X axis radius for the ellipse
   * @param ry            the Y axis radius for the ellipse
   * @param xAxisRotation the rotation angle in degrees for the ellipse's
   *                      X-axis relative to the X-axis
   * @param largeArcFlag  the value of the large-arc-flag
   * @param sweepFlag     the value of the sweep-flag
   * @param x             the relative x coordinate for the end point
   * @param y             the relative y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def arcRel(rx: Float, ry: Float, xAxisRotation: Float, largeArcFlag: Boolean, sweepFlag: Boolean, x: Float, y: Float): Unit

  /**
   * Invoked when an absolute elliptical arc command has been parsed.
   * <p>Command : <b>A</b>
   *
   * @param rx            the X axis radius for the ellipse
   * @param ry            the Y axis radius for the ellipse
   * @param xAxisRotation the rotation angle in degrees for the ellipse's
   *                      X-axis relative to the X-axis
   * @param largeArcFlag  the value of the large-arc-flag
   * @param sweepFlag     the value of the sweep-flag
   * @param x             the absolute x coordinate for the end point
   * @param y             the absolute y coordinate for the end point
   * @throws ParseException if an error occurred while processing the path
   */
  @throws[ParseException]
  def arcAbs(rx: Float, ry: Float, xAxisRotation: Float, largeArcFlag: Boolean, sweepFlag: Boolean, x: Float, y: Float): Unit
}
