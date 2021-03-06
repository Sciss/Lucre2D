package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the Parser Java class of Apache Batik
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
 * This interface represents a parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: Parser.java 1733416 2016-03-03 07:07:13Z gadams $
 */
trait Parser /*extends Localizable*/ {
//  /**
//   * Parses the given reader
//   */
//  @throws[ParseException]
//  def parse(r: java.io.Reader): Unit

  /**
   * Parses the given string
   */
  @throws[ParseException]
  def parse(s: String): Unit

  /**
   * Allows an application to register an error event handler.
   *
   * <p>If the application does not register an error event handler,
   * all error events reported by the parser will cause an exception
   * to be thrown.
   *
   * <p>Applications may register a new or different handler in the
   * middle of a parse, and the parser must begin using the new
   * handler immediately.</p>
   *
   * @param handler The error handler.
   */
  def setErrorHandler(handler: ErrorHandler): Unit
}
