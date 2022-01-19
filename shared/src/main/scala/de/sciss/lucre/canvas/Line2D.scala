package de.sciss.lucre.canvas

// This is an adapted Scala translation of the Line2D Java class of OpenJDK
// as released under GNU GPL 2 -- see original file header below.
// So it can be used in Scala.js.

/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import java.io.Serializable

import scala.{Float => SFloat, Double => SDouble}

/**
 * This {@code Line2D} represents a line segment in {@code (x,y)}
 * coordinate space.
 * <p>
 * This class is only the abstract superclass for all objects that
 * store a 2D line segment.
 * The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @author Jim Graham
 * @since 1.2
 */
object Line2D {
  /**
   * A line segment specified with float coordinates.
   *
   * @since 1.2
   */
  @SerialVersionUID(6161772511649436349L)
  class Float() extends Line2D with Serializable {
    /**
     * The X coordinate of the start point of the line segment.
     *
     * @since 1.2
     * @serial
     */
    var x1: SFloat = 0.0f
    /**
     * The Y coordinate of the start point of the line segment.
     *
     * @since 1.2
     * @serial
     */
    var y1: SFloat = 0.0f
    /**
     * The X coordinate of the end point of the line segment.
     *
     * @since 1.2
     * @serial
     */
    var x2: SFloat = 0.0f
    /**
     * The Y coordinate of the end point of the line segment.
     *
     * @since 1.2
     * @serial
     */
    var y2: SFloat = 0.0f

    /**
     * Constructs and initializes a Line from the specified coordinates.
     *
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     * @since 1.2
     */
    def this(x1: SFloat, y1: SFloat, x2: SFloat, y2: SFloat) = {
      this()
      setLine(x1, y1, x2, y2)
    }

    /**
     * Constructs and initializes a {@code Line2D} from the
     * specified {@code Point2D} objects.
     *
     * @param p1 the start {@code Point2D} of this line segment
     * @param p2 the end {@code Point2D} of this line segment
     * @since 1.2
     */
    def this(p1: Point2D, p2: Point2D) = {
      this()
      setLine(p1, p2)
    }

    /**
     * {@inheritDoc }
     *
     * @since 1.2
     */
    override def getX1: SDouble = x1.toDouble
    override def getY1: SDouble = y1.toDouble
    override def getX2: SDouble = x2.toDouble
    override def getY2: SDouble = y2.toDouble

    override def getP1: Point2D = new Point2D.Float(x1, y1)
    override def getP2: Point2D = new Point2D.Float(x2, y2)

    override def setLine(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble): Unit = {
      this.x1 = x1.toFloat
      this.y1 = y1.toFloat
      this.x2 = x2.toFloat
      this.y2 = y2.toFloat
    }

    /**
     * Sets the location of the end points of this {@code Line2D}
     * to the specified float coordinates.
     *
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     * @since 1.2
     */
    def setLine(x1: SFloat, y1: SFloat, x2: SFloat, y2: SFloat): Unit = {
      this.x1 = x1
      this.y1 = y1
      this.x2 = x2
      this.y2 = y2
    }

    override def getBounds2D: Rectangle2D = {
      var x = 0.0f
      var y = 0.0f
      var w = 0.0f
      var h = 0.0f
      if (x1 < x2) {
        x = x1
        w = x2 - x1
      }
      else {
        x = x2
        w = x1 - x2
      }
      if (y1 < y2) {
        y = y1
        h = y2 - y1
      }
      else {
        y = y2
        h = y1 - y2
      }
      new Rectangle2D.Float(x, y, w, h)
    }
  }

  /**
   * A line segment specified with double coordinates.
   *
   * @since 1.2
   */
  @SerialVersionUID(7979627399746467499L)
  class Double() extends Line2D with Serializable {
    var x1: SDouble = 0.0
    var y1: SDouble = 0.0
    var x2: SDouble = 0.0
    var y2: SDouble = 0.0

    /**
     * Constructs and initializes a {@code Line2D} from the
     * specified coordinates.
     *
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     * @since 1.2
     */
    def this(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble) = {
      this()
      setLine(x1, y1, x2, y2)
    }

    def this(p1: Point2D, p2: Point2D) = {
      this()
      setLine(p1, p2)
    }

    override def getX1: SDouble = x1
    override def getY1: SDouble = y1
    override def getX2: SDouble = x2
    override def getY2: SDouble = y2

    override def getP1: Point2D = new Point2D.Double(x1, y1)
    override def getP2: Point2D = new Point2D.Double(x2, y2)

    override def setLine(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble): Unit = {
      this.x1 = x1
      this.y1 = y1
      this.x2 = x2
      this.y2 = y2
    }

    override def getBounds2D: Rectangle2D = {
      var x = 0.0
      var y = 0.0
      var w = 0.0
      var h = 0.0
      if (x1 < x2) {
        x = x1
        w = x2 - x1
      }
      else {
        x = x2
        w = x1 - x2
      }
      if (y1 < y2) {
        y = y1
        h = y2 - y1
      }
      else {
        y = y2
        h = y1 - y2
      }
      new Rectangle2D.Double(x, y, w, h)
    }
  }

  /**
   * Returns an indicator of where the specified point
   * {@code (px,py)} lies with respect to the line segment from
   * {@code (x1,y1)} to {@code (x2,y2)}.
   * The return value can be either 1, -1, or 0 and indicates
   * in which direction the specified line must pivot around its
   * first end point, {@code (x1,y1)}, in order to point at the
   * specified point {@code (px,py)}.
   * <p>A return value of 1 indicates that the line segment must
   * turn in the direction that takes the positive X axis towards
   * the negative Y axis.  In the default coordinate system used by
   * Java 2D, this direction is counterclockwise.
   * <p>A return value of -1 indicates that the line segment must
   * turn in the direction that takes the positive X axis towards
   * the positive Y axis.  In the default coordinate system, this
   * direction is clockwise.
   * <p>A return value of 0 indicates that the point lies
   * exactly on the line segment.  Note that an indicator value
   * of 0 is rare and not useful for determining collinearity
   * because of floating point rounding issues.
   * <p>If the point is colinear with the line segment, but
   * not between the end points, then the value will be -1 if the point
   * lies "beyond {@code (x1,y1)}" or 1 if the point lies
   * "beyond {@code (x2,y2)}".
   *
   * @param x1 the X coordinate of the start point of the
   *           specified line segment
   * @param y1 the Y coordinate of the start point of the
   *           specified line segment
   * @param x2 the X coordinate of the end point of the
   *           specified line segment
   * @param y2 the Y coordinate of the end point of the
   *           specified line segment
   * @param px the X coordinate of the specified point to be
   *           compared with the specified line segment
   * @param py the Y coordinate of the specified point to be
   *           compared with the specified line segment
   * @return an integer that indicates the position of the third specified
   *         coordinates with respect to the line segment formed
   *         by the first two specified coordinates.
   * @since 1.2
   */
  def relativeCCW(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble, px: SDouble, py: SDouble): Int = {
    val _x2 = x2 - x1
    val _y2 = y2 - y1
    var _px = px - x1
    var _py = py - y1
    var ccw = _px * _y2 - _py * _x2
    if (ccw == 0.0) { // The point is colinear, classify based on which side of
      // the segment the point falls on.  We can calculate a
      // relative value using the projection of px,py onto the
      // segment - a negative value indicates the point projects
      // outside of the segment in the direction of the particular
      // endpoint used as the origin for the projection.
      ccw = _px * _x2 + _py * _y2
      if (ccw > 0.0) { // Reverse the projection to be relative to the original x2,y2
        // x2 and y2 are simply negated.
        // px and py need to have (x2 - x1) or (y2 - y1) subtracted
        //    from them (based on the original values)
        // Since we really want to get a positive answer when the
        //    point is "beyond (x2,y2)", then we want to calculate
        //    the inverse anyway - thus we leave x2 & y2 negated.
        _px -= _x2
        _py -= _y2
        ccw = _px * _x2 + _py * _y2
        if (ccw < 0.0) ccw = 0.0
      }
    }
    if (ccw < 0.0) -1
    else if ((ccw > 0.0)) 1
    else 0
  }

  /**
   * Tests if the line segment from {@code (x1,y1)} to
   * {@code (x2,y2)} intersects the line segment from {@code (x3,y3)}
   * to {@code (x4,y4)}.
   *
   * @param x1 the X coordinate of the start point of the first
   *           specified line segment
   * @param y1 the Y coordinate of the start point of the first
   *           specified line segment
   * @param x2 the X coordinate of the end point of the first
   *           specified line segment
   * @param y2 the Y coordinate of the end point of the first
   *           specified line segment
   * @param x3 the X coordinate of the start point of the second
   *           specified line segment
   * @param y3 the Y coordinate of the start point of the second
   *           specified line segment
   * @param x4 the X coordinate of the end point of the second
   *           specified line segment
   * @param y4 the Y coordinate of the end point of the second
   *           specified line segment
   * @return {@code true} if the first specified line segment
   *         and the second specified line segment intersect
   *         each other; {@code false} otherwise.
   * @since 1.2
   */
  def linesIntersect(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble,
                     x3: SDouble, y3: SDouble, x4: SDouble, y4: SDouble): Boolean =
    (  relativeCCW(x1, y1, x2, y2, x3, y3) * relativeCCW(x1, y1, x2, y2, x4, y4) <= 0) &&
      (relativeCCW(x3, y3, x4, y4, x1, y1) * relativeCCW(x3, y3, x4, y4, x2, y2) <= 0)

  /**
   * Returns the square of the distance from a point to a line segment.
   * The distance measured is the distance between the specified
   * point and the closest point between the specified end points.
   * If the specified point intersects the line segment in between the
   * end points, this method returns 0.0.
   *
   * @param x1 the X coordinate of the start point of the
   *           specified line segment
   * @param y1 the Y coordinate of the start point of the
   *           specified line segment
   * @param x2 the X coordinate of the end point of the
   *           specified line segment
   * @param y2 the Y coordinate of the end point of the
   *           specified line segment
   * @param px the X coordinate of the specified point being
   *           measured against the specified line segment
   * @param py the Y coordinate of the specified point being
   *           measured against the specified line segment
   * @return a double value that is the square of the distance from the
   *         specified point to the specified line segment.
   * @see #ptLineDistSq(double, double, double, double, double, double)
   * @since 1.2
   */
  def ptSegDistSq(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble, px: SDouble, py: SDouble): SDouble = {
    // Adjust vectors relative to x1,y1
    // x2,y2 becomes relative vector from x1,y1 to end of segment
    val _x2 = x2 - x1
    val _y2 = y2 - y1
    // px,py becomes relative vector from x1,y1 to test point
    var _px = px - x1
    var _py = py - y1
    var dotProd = _px * _x2 + _py * _y2
    var projLenSq = 0.0
    if (dotProd <= 0.0) { // px,py is on the side of x1,y1 away from x2,y2
      // distance to segment is length of px,py vector
      // "length of its (clipped) projection" is now 0.0
      projLenSq = 0.0
    }
    else { // switch to backwards vectors relative to x2,y2
      // x2,y2 are already the negative of x1,y1=>x2,y2
      // to get px,py to be the negative of px,py=>x2,y2
      // the dot product of two negated vectors is the same
      // as the dot product of the two normal vectors
      _px = _x2 - _px
      _py = _y2 - _py
      dotProd = _px * _x2 + _py * _y2
      if (dotProd <= 0.0) { // px,py is on the side of x2,y2 away from x1,y1
        // distance to segment is length of (backwards) px,py vector
        projLenSq = 0.0
      }
      else { // px,py is between x1,y1 and x2,y2
        // dotprod is the length of the px,py vector
        // projected on the x2,y2=>x1,y1 vector times the
        // length of the x2,y2=>x1,y1 vector
        projLenSq = dotProd * dotProd / (_x2 * _x2 + _y2 * _y2)
      }
    }
    // Distance to line is now the length of the relative point
    // vector minus the length of its projection onto the line
    // (which is zero if the projection falls outside the range
    //  of the line segment).
    var lenSq = _px * _px + _py * _py - projLenSq
    if (lenSq < 0) lenSq = 0
    lenSq
  }

  /**
   * Returns the distance from a point to a line segment.
   * The distance measured is the distance between the specified
   * point and the closest point between the specified end points.
   * If the specified point intersects the line segment in between the
   * end points, this method returns 0.0.
   *
   * @param x1 the X coordinate of the start point of the
   *           specified line segment
   * @param y1 the Y coordinate of the start point of the
   *           specified line segment
   * @param x2 the X coordinate of the end point of the
   *           specified line segment
   * @param y2 the Y coordinate of the end point of the
   *           specified line segment
   * @param px the X coordinate of the specified point being
   *           measured against the specified line segment
   * @param py the Y coordinate of the specified point being
   *           measured against the specified line segment
   * @return a double value that is the distance from the specified point
   *         to the specified line segment.
   * @see #ptLineDist(double, double, double, double, double, double)
   * @since 1.2
   */
  def ptSegDist(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble, px: SDouble, py: SDouble): SDouble =
    Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py))

  /**
   * Returns the square of the distance from a point to a line.
   * The distance measured is the distance between the specified
   * point and the closest point on the infinitely-extended line
   * defined by the specified coordinates.  If the specified point
   * intersects the line, this method returns 0.0.
   *
   * @param x1 the X coordinate of the start point of the specified line
   * @param y1 the Y coordinate of the start point of the specified line
   * @param x2 the X coordinate of the end point of the specified line
   * @param y2 the Y coordinate of the end point of the specified line
   * @param px the X coordinate of the specified point being
   *           measured against the specified line
   * @param py the Y coordinate of the specified point being
   *           measured against the specified line
   * @return a double value that is the square of the distance from the
   *         specified point to the specified line.
   * @see #ptSegDistSq(double, double, double, double, double, double)
   * @since 1.2
   */
  def ptLineDistSq(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble, px: SDouble, py: SDouble): SDouble = {
    val _x2 = x2 - x1
    val _y2 = y2 - y1
    val _px = px - x1
    val _py = py - y1
    val dotProd = _px * _x2 + _py * _y2
    // projected on the x1,y1=>x2,y2 vector times the
    // length of the x1,y1=>x2,y2 vector
    val projLenSq = dotProd * dotProd / (_x2 * _x2 + _y2 * _y2)
    var lenSq = _px * _px + _py * _py - projLenSq
    if (lenSq < 0) lenSq = 0
    lenSq
  }

  /**
   * Returns the distance from a point to a line.
   * The distance measured is the distance between the specified
   * point and the closest point on the infinitely-extended line
   * defined by the specified coordinates.  If the specified point
   * intersects the line, this method returns 0.0.
   *
   * @param x1 the X coordinate of the start point of the specified line
   * @param y1 the Y coordinate of the start point of the specified line
   * @param x2 the X coordinate of the end point of the specified line
   * @param y2 the Y coordinate of the end point of the specified line
   * @param px the X coordinate of the specified point being
   *           measured against the specified line
   * @param py the Y coordinate of the specified point being
   *           measured against the specified line
   * @return a double value that is the distance from the specified
   *         point to the specified line.
   * @see #ptSegDist(double, double, double, double, double, double)
   * @since 1.2
   */
  def ptLineDist(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble, px: SDouble, py: SDouble): SDouble =
    Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py))
}

/**
 * This is an abstract class that cannot be instantiated directly.
 * Type-specific implementation subclasses are available for
 * instantiation and provide a number of formats for storing
 * the information necessary to satisfy the various accessory
 * methods below.
 *
 * @see java.awt.geom.Line2D.Float
 * @see java.awt.geom.Line2D.Double
 * @since 1.2
 */
abstract class Line2D protected() extends Shape with Cloneable {
  /**
   * Returns the X coordinate of the start point in double precision.
   *
   * @return the X coordinate of the start point of this
   *         {@code Line2D} object.
   * @since 1.2
   */
  def getX1: SDouble

  /**
   * Returns the Y coordinate of the start point in double precision.
   *
   * @return the Y coordinate of the start point of this
   *         {@code Line2D} object.
   * @since 1.2
   */
  def getY1: SDouble

  /**
   * Returns the start {@code Point2D} of this {@code Line2D}.
   *
   * @return the start {@code Point2D} of this {@code Line2D}.
   * @since 1.2
   */
  def getP1: Point2D

  /**
   * Returns the X coordinate of the end point in double precision.
   *
   * @return the X coordinate of the end point of this
   *         {@code Line2D} object.
   * @since 1.2
   */
  def getX2: SDouble

  /**
   * Returns the Y coordinate of the end point in double precision.
   *
   * @return the Y coordinate of the end point of this
   *         {@code Line2D} object.
   * @since 1.2
   */
  def getY2: SDouble

  /**
   * Returns the end {@code Point2D} of this {@code Line2D}.
   *
   * @return the end {@code Point2D} of this {@code Line2D}.
   * @since 1.2
   */
  def getP2: Point2D

  /**
   * Sets the location of the end points of this {@code Line2D} to
   * the specified double coordinates.
   *
   * @param x1 the X coordinate of the start point
   * @param y1 the Y coordinate of the start point
   * @param x2 the X coordinate of the end point
   * @param y2 the Y coordinate of the end point
   * @since 1.2
   */
  def setLine(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble): Unit

  /**
   * Sets the location of the end points of this {@code Line2D} to
   * the specified {@code Point2D} coordinates.
   *
   * @param p1 the start {@code Point2D} of the line segment
   * @param p2 the end {@code Point2D} of the line segment
   * @since 1.2
   */
  def setLine(p1: Point2D, p2: Point2D): Unit =
    setLine(p1.getX, p1.getY, p2.getX, p2.getY)

  /**
   * Sets the location of the end points of this {@code Line2D} to
   * the same as those end points of the specified {@code Line2D}.
   *
   * @param l the specified {@code Line2D}
   * @since 1.2
   */
  def setLine(l: Line2D): Unit =
    setLine(l.getX1, l.getY1, l.getX2, l.getY2)

  /**
   * Returns an indicator of where the specified point
   * {@code (px,py)} lies with respect to this line segment.
   * See the method comments of
   * {@link # relativeCCW ( double, double, double, double, double, double)}
   * to interpret the return value.
   *
   * @param px the X coordinate of the specified point
   *           to be compared with this {@code Line2D}
   * @param py the Y coordinate of the specified point
   *           to be compared with this {@code Line2D}
   * @return an integer that indicates the position of the specified
   *         coordinates with respect to this {@code Line2D}
   * @see #relativeCCW(double, double, double, double, double, double)
   * @since 1.2
   */
  def relativeCCW(px: SDouble, py: SDouble): Int = Line2D.relativeCCW(getX1, getY1, getX2, getY2, px, py)

  /**
   * Returns an indicator of where the specified {@code Point2D}
   * lies with respect to this line segment.
   * See the method comments of
   * {@link # relativeCCW ( double, double, double, double, double, double)}
   * to interpret the return value.
   *
   * @param p the specified {@code Point2D} to be compared
   *          with this {@code Line2D}
   * @return an integer that indicates the position of the specified
   *         {@code Point2D} with respect to this {@code Line2D}
   * @see #relativeCCW(double, double, double, double, double, double)
   * @since 1.2
   */
  def relativeCCW(p: Point2D): Int = Line2D.relativeCCW(getX1, getY1, getX2, getY2, p.getX, p.getY)

  /**
   * Tests if the line segment from {@code (x1,y1)} to
   * {@code (x2,y2)} intersects this line segment.
   *
   * @param x1 the X coordinate of the start point of the
   *           specified line segment
   * @param y1 the Y coordinate of the start point of the
   *           specified line segment
   * @param x2 the X coordinate of the end point of the
   *           specified line segment
   * @param y2 the Y coordinate of the end point of the
   *           specified line segment
   * @return {@code true} if this line segment and the specified line segment
   *         intersect each other; {@code false} otherwise.
   * @since 1.2
   */
  def intersectsLine(x1: SDouble, y1: SDouble, x2: SDouble, y2: SDouble): Boolean =
    Line2D.linesIntersect(x1, y1, x2, y2, getX1, getY1, getX2, getY2)

  /**
   * Tests if the specified line segment intersects this line segment.
   *
   * @param l the specified {@code Line2D}
   * @return {@code true} if this line segment and the specified line
   *         segment intersect each other;
   *         {@code false} otherwise.
   * @since 1.2
   */
  def intersectsLine(l: Line2D): Boolean =
    Line2D.linesIntersect(l.getX1, l.getY1, l.getX2, l.getY2, getX1, getY1, getX2, getY2)

  /**
   * Returns the square of the distance from a point to this line segment.
   * The distance measured is the distance between the specified
   * point and the closest point between the current line's end points.
   * If the specified point intersects the line segment in between the
   * end points, this method returns 0.0.
   *
   * @param px the X coordinate of the specified point being
   *           measured against this line segment
   * @param py the Y coordinate of the specified point being
   *           measured against this line segment
   * @return a double value that is the square of the distance from the
   *         specified point to the current line segment.
   * @see #ptLineDistSq(double, double)
   * @since 1.2
   */
  def ptSegDistSq(px: SDouble, py: SDouble): SDouble =
    Line2D.ptSegDistSq(getX1, getY1, getX2, getY2, px, py)

  /**
   * Returns the square of the distance from a {@code Point2D} to
   * this line segment.
   * The distance measured is the distance between the specified
   * point and the closest point between the current line's end points.
   * If the specified point intersects the line segment in between the
   * end points, this method returns 0.0.
   *
   * @param pt the specified {@code Point2D} being measured against
   *           this line segment.
   * @return a double value that is the square of the distance from the
   *         specified {@code Point2D} to the current
   *         line segment.
   * @see #ptLineDistSq(Point2D)
   * @since 1.2
   */
  def ptSegDistSq(pt: Point2D): SDouble = Line2D.ptSegDistSq(getX1, getY1, getX2, getY2, pt.getX, pt.getY)

  /**
   * Returns the distance from a point to this line segment.
   * The distance measured is the distance between the specified
   * point and the closest point between the current line's end points.
   * If the specified point intersects the line segment in between the
   * end points, this method returns 0.0.
   *
   * @param px the X coordinate of the specified point being
   *           measured against this line segment
   * @param py the Y coordinate of the specified point being
   *           measured against this line segment
   * @return a double value that is the distance from the specified
   *         point to the current line segment.
   * @see #ptLineDist(double, double)
   * @since 1.2
   */
  def ptSegDist(px: SDouble, py: SDouble): SDouble = Line2D.ptSegDist(getX1, getY1, getX2, getY2, px, py)

  /**
   * Returns the distance from a {@code Point2D} to this line
   * segment.
   * The distance measured is the distance between the specified
   * point and the closest point between the current line's end points.
   * If the specified point intersects the line segment in between the
   * end points, this method returns 0.0.
   *
   * @param pt the specified {@code Point2D} being measured
   *           against this line segment
   * @return a double value that is the distance from the specified
   *         {@code Point2D} to the current line
   *         segment.
   * @see #ptLineDist(Point2D)
   * @since 1.2
   */
  def ptSegDist(pt: Point2D): SDouble = Line2D.ptSegDist(getX1, getY1, getX2, getY2, pt.getX, pt.getY)

  /**
   * Returns the square of the distance from a point to this line.
   * The distance measured is the distance between the specified
   * point and the closest point on the infinitely-extended line
   * defined by this {@code Line2D}.  If the specified point
   * intersects the line, this method returns 0.0.
   *
   * @param px the X coordinate of the specified point being
   *           measured against this line
   * @param py the Y coordinate of the specified point being
   *           measured against this line
   * @return a double value that is the square of the distance from a
   *         specified point to the current line.
   * @see #ptSegDistSq(double, double)
   * @since 1.2
   */
  def ptLineDistSq(px: SDouble, py: SDouble): SDouble = Line2D.ptLineDistSq(getX1, getY1, getX2, getY2, px, py)

  /**
   * Returns the square of the distance from a specified
   * {@code Point2D} to this line.
   * The distance measured is the distance between the specified
   * point and the closest point on the infinitely-extended line
   * defined by this {@code Line2D}.  If the specified point
   * intersects the line, this method returns 0.0.
   *
   * @param pt the specified {@code Point2D} being measured
   *           against this line
   * @return a double value that is the square of the distance from a
   *         specified {@code Point2D} to the current
   *         line.
   * @see #ptSegDistSq(Point2D)
   * @since 1.2
   */
  def ptLineDistSq(pt: Point2D): SDouble = Line2D.ptLineDistSq(getX1, getY1, getX2, getY2, pt.getX, pt.getY)

  /**
   * Returns the distance from a point to this line.
   * The distance measured is the distance between the specified
   * point and the closest point on the infinitely-extended line
   * defined by this {@code Line2D}.  If the specified point
   * intersects the line, this method returns 0.0.
   *
   * @param px the X coordinate of the specified point being
   *           measured against this line
   * @param py the Y coordinate of the specified point being
   *           measured against this line
   * @return a double value that is the distance from a specified point
   *         to the current line.
   * @see #ptSegDist(double, double)
   * @since 1.2
   */
  def ptLineDist(px: SDouble, py: SDouble): SDouble = Line2D.ptLineDist(getX1, getY1, getX2, getY2, px, py)

  /**
   * Returns the distance from a {@code Point2D} to this line.
   * The distance measured is the distance between the specified
   * point and the closest point on the infinitely-extended line
   * defined by this {@code Line2D}.  If the specified point
   * intersects the line, this method returns 0.0.
   *
   * @param pt the specified {@code Point2D} being measured
   * @return a double value that is the distance from a specified
   *         {@code Point2D} to the current line.
   * @see #ptSegDist(Point2D)
   * @since 1.2
   */
  def ptLineDist(pt: Point2D): SDouble = Line2D.ptLineDist(getX1, getY1, getX2, getY2, pt.getX, pt.getY)

  /**
   * Tests if a specified coordinate is inside the boundary of this
   * {@code Line2D}.  This method is required to implement the
   * {@link Shape} interface, but in the case of {@code Line2D}
   * objects it always returns {@code false} since a line contains
   * no area.
   *
   * @param x the X coordinate of the specified point to be tested
   * @param y the Y coordinate of the specified point to be tested
   * @return {@code false} because a {@code Line2D} contains
   *         no area.
   * @since 1.2
   */
  override def contains(x: SDouble, y: SDouble) = false

  /**
   * Tests if a given {@code Point2D} is inside the boundary of
   * this {@code Line2D}.
   * This method is required to implement the {@link Shape} interface,
   * but in the case of {@code Line2D} objects it always returns
   * {@code false} since a line contains no area.
   *
   * @param p the specified {@code Point2D} to be tested
   * @return {@code false} because a {@code Line2D} contains
   *         no area.
   * @since 1.2
   */
  override def contains(p: Point2D) = false

  /**
   * {@inheritDoc }
   *
   * @since 1.2
   */
  override def intersects(x: SDouble, y: SDouble, w: SDouble, h: SDouble): Boolean =
    intersects(new Rectangle2D.Double(x, y, w, h))

  override def intersects(r: Rectangle2D): Boolean =
    r.intersectsLine(getX1, getY1, getX2, getY2)

  /**
   * Tests if the interior of this {@code Line2D} entirely contains
   * the specified set of rectangular coordinates.
   * This method is required to implement the {@code Shape} interface,
   * but in the case of {@code Line2D} objects it always returns
   * false since a line contains no area.
   *
   * @param x the X coordinate of the upper-left corner of the
   *          specified rectangular area
   * @param y the Y coordinate of the upper-left corner of the
   *          specified rectangular area
   * @param w the width of the specified rectangular area
   * @param h the height of the specified rectangular area
   * @return {@code false} because a {@code Line2D} contains
   *         no area.
   * @since 1.2
   */
  override def contains(x: SDouble, y: SDouble, w: SDouble, h: SDouble) = false

  /**
   * Tests if the interior of this {@code Line2D} entirely contains
   * the specified {@code Rectangle2D}.
   * This method is required to implement the {@code Shape} interface,
   * but in the case of {@code Line2D} objects it always returns
   * {@code false} since a line contains no area.
   *
   * @param r the specified {@code Rectangle2D} to be tested
   * @return {@code false} because a {@code Line2D} contains
   *         no area.
   * @since 1.2
   */
  override def contains(r: Rectangle2D) = false

//  override def getBounds: Rectangle = getBounds2D.getBounds

  /**
   * Returns an iteration object that defines the boundary of this
   * {@code Line2D}.
   * The iterator for this class is not multi-threaded safe,
   * which means that this {@code Line2D} class does not
   * guarantee that modifications to the geometry of this
   * {@code Line2D} object do not affect any iterations of that
   * geometry that are already in process.
   *
   * @param at the specified {@link AffineTransform}
   * @return a {@link PathIterator} that defines the boundary of this
   *         {@code Line2D}.
   * @since 1.2
   */
  override def getPathIterator(at: AffineTransform) = new LineIterator(this, at)

  /**
   * Returns an iteration object that defines the boundary of this
   * flattened {@code Line2D}.
   * The iterator for this class is not multi-threaded safe,
   * which means that this {@code Line2D} class does not
   * guarantee that modifications to the geometry of this
   * {@code Line2D} object do not affect any iterations of that
   * geometry that are already in process.
   *
   * @param at       the specified {@code AffineTransform}
   * @param flatness the maximum amount that the control points for a
   *                 given curve can vary from colinear before a subdivided
   *                 curve is replaced by a straight line connecting the
   *                 end points.  Since a {@code Line2D} object is
   *                 always flat, this parameter is ignored.
   * @return a {@code PathIterator} that defines the boundary of the
   *         flattened {@code Line2D}
   * @since 1.2
   */
  override def getPathIterator(at: AffineTransform, flatness: Double) = new LineIterator(this, at)

  /**
   * Creates a new object of the same class as this object.
   *
   * @return a clone of this instance.
   * @exception OutOfMemoryError            if there is not enough memory.
   * @see java.lang.Cloneable
   * @since 1.2
   */
  override def clone: Any = try super.clone
  catch {
    case e: CloneNotSupportedException =>
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e)
  }
}
