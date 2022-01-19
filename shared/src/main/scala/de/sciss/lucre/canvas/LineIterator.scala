package de.sciss.lucre.canvas

// This is an adapted Scala translation of the LineIterator Java class of OpenJDK
// as released under GNU GPL 2 -- see original file header below.
// So it can be used in Scala.js.

/*
 * Copyright (c) 1997, 1999, Oracle and/or its affiliates. All rights reserved.
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

import de.sciss.lucre.canvas.PathIterator.{SEG_LINETO, SEG_MOVETO, WIND_NON_ZERO}

import java.util._


/**
 * A utility class to iterate over the path segments of a line segment
 * through the PathIterator interface.
 *
 * @author Jim Graham
 */
class LineIterator private[canvas](var line: Line2D, var affine: AffineTransform) extends PathIterator {
  private[canvas] var index = 0

  /**
   * Return the winding rule for determining the insideness of the
   * path.
   *
   * @see #WIND_EVEN_ODD
   * @see #WIND_NON_ZERO
   */
  override def getWindingRule: Int = WIND_NON_ZERO

  /**
   * Tests if there are more points to read.
   *
   * @return true if there are more points to read
   */
  override def isDone: Boolean = index > 1

  /**
   * Moves the iterator to the next segment of the path forwards
   * along the primary direction of traversal as long as there are
   * more points in that direction.
   */
  override def next(): Unit =
    index += 1

  /**
   * Returns the coordinates and type of the current path segment in
   * the iteration.
   * The return value is the path segment type:
   * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
   * A float array of length 6 must be passed in and may be used to
   * store the coordinates of the point(s).
   * Each point is stored as a pair of float x,y coordinates.
   * SEG_MOVETO and SEG_LINETO types will return one point,
   * SEG_QUADTO will return two points,
   * SEG_CUBICTO will return 3 points
   * and SEG_CLOSE will not return any points.
   *
   * @see #SEG_MOVETO
   * @see #SEG_LINETO
   * @see #SEG_QUADTO
   * @see #SEG_CUBICTO
   * @see #SEG_CLOSE
   */
  override def currentSegment(coords: Array[Float]): Int = {
    if (isDone) throw new NoSuchElementException("line iterator out of bounds")
    var tpe = 0
    if (index == 0) {
      coords(0) = line.getX1.toFloat
      coords(1) = line.getY1.toFloat
      tpe = SEG_MOVETO
    }
    else {
      coords(0) = line.getX2.toFloat
      coords(1) = line.getY2.toFloat
      tpe = SEG_LINETO
    }
    if (affine != null) affine.transform(coords, 0, coords, 0, 1)
    tpe
  }

  /**
   * Returns the coordinates and type of the current path segment in
   * the iteration.
   * The return value is the path segment type:
   * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
   * A double array of length 6 must be passed in and may be used to
   * store the coordinates of the point(s).
   * Each point is stored as a pair of double x,y coordinates.
   * SEG_MOVETO and SEG_LINETO types will return one point,
   * SEG_QUADTO will return two points,
   * SEG_CUBICTO will return 3 points
   * and SEG_CLOSE will not return any points.
   *
   * @see #SEG_MOVETO
   * @see #SEG_LINETO
   * @see #SEG_QUADTO
   * @see #SEG_CUBICTO
   * @see #SEG_CLOSE
   */
  override def currentSegment(coords: Array[Double]): Int = {
    if (isDone) throw new NoSuchElementException("line iterator out of bounds")
    var tpe = 0
    if (index == 0) {
      coords(0) = line.getX1
      coords(1) = line.getY1
      tpe = SEG_MOVETO
    }
    else {
      coords(0) = line.getX2
      coords(1) = line.getY2
      tpe = SEG_LINETO
    }
    if (affine != null) affine.transform(coords, 0, coords, 0, 1)
    tpe
  }
}
