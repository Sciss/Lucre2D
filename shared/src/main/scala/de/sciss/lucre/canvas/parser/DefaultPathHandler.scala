package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the DefaultPathHandler Java class of Apache Batik
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
 * The class provides an adapter for PathHandler.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: DefaultPathHandler.java 1733416 2016-03-03 07:07:13Z gadams $
 */
object DefaultPathHandler extends PathHandler {
  /**
   * Implements {@link PathHandler# startPath ( )}.
   */
  @throws[ParseException]
  override def startPath(): Unit = {
  }

  /**
   * Implements {@link PathHandler# endPath ( )}.
   */
  @throws[ParseException]
  override def endPath(): Unit = {
  }

  /**
   * Implements {@link PathHandler# movetoRel ( float, float )}.
   */
  @throws[ParseException]
  override def moveToRel(x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# movetoAbs ( float, float )}.
   */
  @throws[ParseException]
  override def moveToAbs(x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# closePath ( )}.
   */
  @throws[ParseException]
  override def closePath(): Unit = {
  }

  /**
   * Implements {@link PathHandler# linetoRel ( float, float )}.
   */
  @throws[ParseException]
  override def lineToRel(x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# linetoAbs ( float, float )}.
   */
  @throws[ParseException]
  override def lineToAbs(x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# linetoHorizontalRel ( float )}.
   */
  @throws[ParseException]
  override def lineToHorizontalRel(x: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# linetoHorizontalAbs ( float )}.
   */
  @throws[ParseException]
  override def lineToHorizontalAbs(x: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# linetoVerticalRel ( float )}.
   */
  @throws[ParseException]
  override def lineToVerticalRel(y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# linetoVerticalAbs ( float )}.
   */
  @throws[ParseException]
  override def lineToVerticalAbs(y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#curvetoCubicRel(float,float,float,float,float,float)}.
   */
  @throws[ParseException]
  override def curveToCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#curvetoCubicAbs(float,float,float,float,float,float)}.
   */
  @throws[ParseException]
  override def curveToCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#curvetoCubicSmoothRel(float,float,float,float)}.
   */
  @throws[ParseException]
  override def curveToCubicSmoothRel(x2: Float, y2: Float, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#curvetoCubicSmoothAbs(float,float,float,float)}.
   */
  @throws[ParseException]
  override def curveToCubicSmoothAbs(x2: Float, y2: Float, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#curvetoQuadraticRel(float,float,float,float)}.
   */
  @throws[ParseException]
  override def curveToQuadraticRel(x1: Float, y1: Float, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#curvetoQuadraticAbs(float,float,float,float)}.
   */
  @throws[ParseException]
  override def curveToQuadraticAbs(x1: Float, y1: Float, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# curvetoQuadraticSmoothRel ( float, float )}.
   */
  @throws[ParseException]
  override def curveToQuadraticSmoothRel(x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link PathHandler# curvetoQuadraticSmoothAbs ( float, float )}.
   */
  @throws[ParseException]
  override def curveToQuadraticSmoothAbs(x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#arcRel(float,float,float,boolean,boolean,float,float)}.
   */
  @throws[ParseException]
  override def arcRel(rx: Float, ry: Float, xAxisRotation: Float, largeArcFlag: Boolean, sweepFlag: Boolean, x: Float, y: Float): Unit = {
  }

  /**
   * Implements {@link
   * PathHandler#arcAbs(float,float,float,boolean,boolean,float,float)}.
   */
  @throws[ParseException]
  override def arcAbs(rx: Float, ry: Float, xAxisRotation: Float, largeArcFlag: Boolean, sweepFlag: Boolean, x: Float, y: Float): Unit = {
  }
}
