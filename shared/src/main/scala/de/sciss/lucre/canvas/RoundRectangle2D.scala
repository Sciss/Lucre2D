package de.sciss.lucre.canvas

// This is an adapted Scala translation of the RoundRectangle2D Java class of OpenJDK
// as released under GNU GPL 2 -- see original file header below.
// So it can be used in Scala.js.

/*
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
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
 * The {@code RoundRectangle2D} class defines a rectangle with
 * rounded corners defined by a location {@code (x,y)}, a
 * dimension {@code (w x h)}, and the width and height of an arc
 * with which to round the corners.
 * <p>
 * This class is the abstract superclass for all objects that
 * store a 2D rounded rectangle.
 * The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @author Jim Graham
 * @since 1.2
 */
object RoundRectangle2D {
  /**
   * The {@code Float} class defines a rectangle with rounded
   * corners all specified in {@code float} coordinates.
   *
   * @since 1.2
   */
  @SerialVersionUID(-3423150618393866922L)
  class Float() extends RoundRectangle2D with Serializable {
    /**
     * The X coordinate of this {@code RoundRectangle2D}.
     *
     * @since 1.2
     * @serial
     */
    var x: SFloat = 0.0f
    /**
     * The Y coordinate of this {@code RoundRectangle2D}.
     *
     * @since 1.2
     * @serial
     */
    var y: SFloat = 0.0f
    /**
     * The width of this {@code RoundRectangle2D}.
     *
     * @since 1.2
     * @serial
     */
    var width: SFloat = 0.0f
    /**
     * The height of this {@code RoundRectangle2D}.
     *
     * @since 1.2
     * @serial
     */
    var height: SFloat = 0.0f
    /**
     * The width of the arc that rounds off the corners.
     *
     * @since 1.2
     * @serial
     */
    var arcwidth: SFloat = 0.0f
    /**
     * The height of the arc that rounds off the corners.
     *
     * @since 1.2
     * @serial
     */
    var archeight: SFloat = 0.0f

    /**
     * Constructs and initializes a {@code RoundRectangle2D}
     * from the specified {@code float} coordinates.
     *
     * @param x    the X coordinate of the newly
     *             constructed {@code RoundRectangle2D}
     * @param y    the Y coordinate of the newly
     *             constructed {@code RoundRectangle2D}
     * @param w    the width to which to set the newly
     *             constructed {@code RoundRectangle2D}
     * @param h    the height to which to set the newly
     *             constructed {@code RoundRectangle2D}
     * @param arcw the width of the arc to use to round off the
     *             corners of the newly constructed
     *             {@code RoundRectangle2D}
     * @param arch the height of the arc to use to round off the
     *             corners of the newly constructed
     *             {@code RoundRectangle2D}
     * @since 1.2
     */
    def this(x: SFloat, y: SFloat, w: SFloat, h: SFloat, arcw: SFloat, arch: SFloat) = {
      this()
      setRoundRect(x, y, w, h, arcw, arch)
    }

    override def getX         : SDouble = x         .toDouble
    override def getY         : SDouble = y         .toDouble
    override def getWidth     : SDouble = width     .toDouble
    override def getHeight    : SDouble = height    .toDouble
    override def getArcWidth  : SDouble = arcwidth  .toDouble
    override def getArcHeight : SDouble = archeight .toDouble

    override def isEmpty: Boolean = (width <= 0.0f) || (height <= 0.0f)

    /**
     * Sets the location, size, and corner radii of this
     * {@code RoundRectangle2D} to the specified
     * {@code float} values.
     *
     * @param x    the X coordinate to which to set the
     *             location of this {@code RoundRectangle2D}
     * @param y    the Y coordinate to which to set the
     *             location of this {@code RoundRectangle2D}
     * @param w    the width to which to set this
     *             {@code RoundRectangle2D}
     * @param h    the height to which to set this
     *             {@code RoundRectangle2D}
     * @param arcw the width to which to set the arc of this
     *             {@code RoundRectangle2D}
     * @param arch the height to which to set the arc of this
     *             {@code RoundRectangle2D}
     * @since 1.2
     */
    def setRoundRect(x: SFloat, y: SFloat, w: SFloat, h: SFloat, arcw: SFloat, arch: SFloat): Unit = {
      this.x          = x
      this.y          = y
      this.width      = w
      this.height     = h
      this.arcwidth   = arcw
      this.archeight  = arch
    }

    override def setRoundRect(x: SDouble, y: SDouble, w: SDouble, h: SDouble, arcw: SDouble, arch: SDouble): Unit = {
      this.x          = x   .toFloat
      this.y          = y   .toFloat
      this.width      = w   .toFloat
      this.height     = h   .toFloat
      this.arcwidth   = arcw.toFloat
      this.archeight  = arch.toFloat
    }

    override def setRoundRect(rr: RoundRectangle2D): Unit = {
      this.x          = rr.getX         .toFloat
      this.y          = rr.getY         .toFloat
      this.width      = rr.getWidth     .toFloat
      this.height     = rr.getHeight    .toFloat
      this.arcwidth   = rr.getArcWidth  .toFloat
      this.archeight  = rr.getArcHeight .toFloat
    }

    override def getBounds2D = new Rectangle2D.Float(x, y, width, height)
  }

  /**
   * The {@code Double} class defines a rectangle with rounded
   * corners all specified in {@code double} coordinates.
   *
   * @since 1.2
   */
  @SerialVersionUID(1048939333485206117L)
  class Double() extends RoundRectangle2D with Serializable {
    var x         : SDouble = 0.0
    var y         : SDouble = 0.0
    var width     : SDouble = 0.0
    var height    : SDouble = 0.0
    var arcwidth  : SDouble = 0.0
    var archeight : SDouble = 0.0

    /**
     * Constructs and initializes a {@code RoundRectangle2D}
     * from the specified {@code double} coordinates.
     *
     * @param x    the X coordinate of the newly
     *             constructed {@code RoundRectangle2D}
     * @param y    the Y coordinate of the newly
     *             constructed {@code RoundRectangle2D}
     * @param w    the width to which to set the newly
     *             constructed {@code RoundRectangle2D}
     * @param h    the height to which to set the newly
     *             constructed {@code RoundRectangle2D}
     * @param arcW the width of the arc to use to round off the
     *             corners of the newly constructed
     *             {@code RoundRectangle2D}
     * @param arcH the height of the arc to use to round off the
     *             corners of the newly constructed
     *             {@code RoundRectangle2D}
     * @since 1.2
     */
    def this(x: SDouble, y: SDouble, w: SDouble, h: SDouble, arcW: SDouble, arcH: SDouble) = {
      this()
      setRoundRect(x, y, w, h, arcW, arcH)
    }

    override def getX         : SDouble = x
    override def getY         : SDouble = y
    override def getWidth     : SDouble = width
    override def getHeight    : SDouble = height
    override def getArcWidth  : SDouble = arcwidth
    override def getArcHeight : SDouble = archeight

    override def isEmpty: Boolean = (width <= 0.0f) || (height <= 0.0f)

    override def setRoundRect(x: SDouble, y: SDouble, w: SDouble, h: SDouble, arcw: SDouble, arch: SDouble): Unit = {
      this.x          = x
      this.y          = y
      this.width      = w
      this.height     = h
      this.arcwidth   = arcw
      this.archeight  = arch
    }

    override def setRoundRect(rr: RoundRectangle2D): Unit = {
      this.x          = rr.getX
      this.y          = rr.getY
      this.width      = rr.getWidth
      this.height     = rr.getHeight
      this.arcwidth   = rr.getArcWidth
      this.archeight  = rr.getArcHeight
    }

    override def getBounds2D = new Rectangle2D.Double(x, y, width, height)
  }
}

abstract class RoundRectangle2D protected() extends RectangularShape {

  /**
 * This is an abstract class that cannot be instantiated directly.
 * Type-specific implementation subclasses are available for
 * instantiation and provide a number of formats for storing
 * the information necessary to satisfy the various accessor
 * methods below.
 *
 * @see java.awt.geom.RoundRectangle2D.Float
 * @see java.awt.geom.RoundRectangle2D.Double
 * @since 1.2
 */
  /**
   * Gets the width of the arc that rounds off the corners.
   *
   * @return the width of the arc that rounds off the corners
   *         of this {@code RoundRectangle2D}.
   * @since 1.2
   */
  def getArcWidth: SDouble

  /**
   * Gets the height of the arc that rounds off the corners.
   *
   * @return the height of the arc that rounds off the corners
   *         of this {@code RoundRectangle2D}.
   * @since 1.2
   */
  def getArcHeight: SDouble

  /**
   * Sets the location, size, and corner radii of this
   * {@code RoundRectangle2D} to the specified
   * {@code double} values.
   *
   * @param x         the X coordinate to which to set the
   *                  location of this {@code RoundRectangle2D}
   * @param y         the Y coordinate to which to set the
   *                  location of this {@code RoundRectangle2D}
   * @param w         the width to which to set this
   *                  {@code RoundRectangle2D}
   * @param h         the height to which to set this
   *                  {@code RoundRectangle2D}
   * @param arcWidth  the width to which to set the arc of this
   *                  {@code RoundRectangle2D}
   * @param arcHeight the height to which to set the arc of this
   *                  {@code RoundRectangle2D}
   * @since 1.2
   */
  def setRoundRect(x: SDouble, y: SDouble, w: SDouble, h: SDouble, arcWidth: SDouble, arcHeight: SDouble): Unit

  /**
   * Sets this {@code RoundRectangle2D} to be the same as the
   * specified {@code RoundRectangle2D}.
   *
   * @param rr the specified {@code RoundRectangle2D}
   * @since 1.2
   */
  def setRoundRect(rr: RoundRectangle2D): Unit =
    setRoundRect(rr.getX, rr.getY, rr.getWidth, rr.getHeight, rr.getArcWidth, rr.getArcHeight)

  /**
   * {@inheritDoc }
   *
   * @since 1.2
   */
  override def setFrame(x: SDouble, y: SDouble, w: SDouble, h: SDouble): Unit =
    setRoundRect(x, y, w, h, getArcWidth, getArcHeight)

  override def contains(x: SDouble, y: SDouble): Boolean = {
    if (isEmpty) return false
    var rrx0 = getX
    var rry0 = getY
    val rrx1 = rrx0 + getWidth
    val rry1 = rry0 + getHeight
    // Check for trivial rejection - point is outside bounding rectangle
    if (x < rrx0 || y < rry0 || x >= rrx1 || y >= rry1) return false
    val aw = Math.min(getWidth, Math.abs(getArcWidth)) / 2.0
    val ah = Math.min(getHeight, Math.abs(getArcHeight)) / 2.0
    // Check which corner point is in and do circular containment
    // test - otherwise simple acceptance
    if (x >= { rrx0 += aw ; rrx0 } && x < { rrx0 = rrx1 - aw; rrx0 }) return true
    if (y >= { rry0 += ah ; rry0 } && y < { rry0 = rry1 - ah; rry0 }) return true
    val xS = (x - rrx0) / aw
    val yS = (y - rry0) / ah
    xS * xS + yS * yS <= 1.0
  }

  private def classify(coord: SDouble, left: SDouble, right: SDouble, arcsize: SDouble): Int =
    if      (coord < left)              0
    else if (coord < left   + arcsize)  1
    else if (coord < right  - arcsize)  2
    else if (coord < right)             3
    else                                4

  override def intersects(x: Double, y: Double, w: Double, h: Double): Boolean = {
    if (isEmpty || w <= 0 || h <= 0) return false
    val rrx0 = getX
    val rry0 = getY
    val rrx1 = rrx0 + getWidth
    val rry1 = rry0 + getHeight
    // Check for trivial rejection - bounding rectangles do not intersect
    if (x + w <= rrx0 || x >= rrx1 || y + h <= rry0 || y >= rry1) return false
    val aw = Math.min(getWidth, Math.abs(getArcWidth)) / 2.0
    val ah = Math.min(getHeight, Math.abs(getArcHeight)) / 2.0
    val x0class = classify(x, rrx0, rrx1, aw)
    val x1class = classify(x + w, rrx0, rrx1, aw)
    val y0class = classify(y, rry0, rry1, ah)
    val y1class = classify(y + h, rry0, rry1, ah)
    // Trivially accept if any point is inside inner rectangle
    if (x0class == 2 || x1class == 2 || y0class == 2 || y1class == 2) return true
    // Trivially accept if either edge spans inner rectangle
    if ((x0class < 2 && x1class > 2) || (y0class < 2 && y1class > 2)) return true
    // Since neither edge spans the center, then one of the corners
    // must be in one of the rounded edges.  We detect this case if
    // a [xy]0class is 3 or a [xy]1class is 1.  One of those two cases
    // must be true for each direction.
    // We now find a "nearest point" to test for being inside a rounded
    // corner.
    val xT = if (x1class == 1) x + w - (rrx0 + aw) else x - (rrx1 - aw)
    val yT = if (y1class == 1) y + h - (rry0 + ah) else y - (rry1 - ah)
    val xS = xT / aw
    val yS = yT / ah
    xS * xS + yS * yS <= 1.0
  }

  override def contains(x: SDouble, y: SDouble, w: SDouble, h: SDouble): Boolean = {
    if (isEmpty || w <= 0 || h <= 0) false
    else contains(x, y) && contains(x + w, y) && contains(x, y + h) && contains(x + w, y + h)
  }

  /**
   * Returns an iteration object that defines the boundary of this
   * {@code RoundRectangle2D}.
   * The iterator for this class is multi-threaded safe, which means
   * that this {@code RoundRectangle2D} class guarantees that
   * modifications to the geometry of this {@code RoundRectangle2D}
   * object do not affect any iterations of that geometry that
   * are already in process.
   *
   * @param at an optional {@code AffineTransform} to be applied to
   *           the coordinates as they are returned in the iteration, or
   *           {@code null} if untransformed coordinates are desired
   * @return the {@code PathIterator} object that returns the
   *         geometry of the outline of this
   *         {@code RoundRectangle2D}, one segment at a time.
   * @since 1.2
   */
  override def getPathIterator(at: AffineTransform) = new RoundRectIterator(this, at)

  /**
   * Returns the hashcode for this {@code RoundRectangle2D}.
   *
   * @return the hashcode for this {@code RoundRectangle2D}.
   * @since 1.6
   */
  override def hashCode: Int = {
    var bits =  java.lang.Double.doubleToLongBits(getX)
    bits +=     java.lang.Double.doubleToLongBits(getY)         * 37
    bits +=     java.lang.Double.doubleToLongBits(getWidth)     * 43
    bits +=     java.lang.Double.doubleToLongBits(getHeight)    * 47
    bits +=     java.lang.Double.doubleToLongBits(getArcWidth)  * 53
    bits +=     java.lang.Double.doubleToLongBits(getArcHeight) * 59
    bits.toInt ^ (bits >> 32).toInt
  }

  /**
   * Determines whether or not the specified {@code Object} is
   * equal to this {@code RoundRectangle2D}.  The specified
   * {@code Object} is equal to this {@code RoundRectangle2D}
   * if it is an instance of {@code RoundRectangle2D} and if its
   * location, size, and corner arc dimensions are the same as this
   * {@code RoundRectangle2D}.
   *
   * @param obj an {@code Object} to be compared with this
   *            {@code RoundRectangle2D}.
   * @return {@code true} if {@code obj} is an instance
   *         of {@code RoundRectangle2D} and has the same values;
   *         {@code false} otherwise.
   * @since 1.6
   */
  override def equals(obj: Any): Boolean = {
    (obj.asInstanceOf[AnyRef] eq this) ||
    (obj.isInstanceOf[RoundRectangle2D] && {
      val rr2d = obj.asInstanceOf[RoundRectangle2D]
      (  getX         == rr2d.getX        ) && (getY          == rr2d.getY        ) &&
        (getWidth     == rr2d.getWidth    ) && (getHeight     == rr2d.getHeight   ) &&
        (getArcWidth  == rr2d.getArcWidth ) && (getArcHeight  == rr2d.getArcHeight)
    })
  }
}
