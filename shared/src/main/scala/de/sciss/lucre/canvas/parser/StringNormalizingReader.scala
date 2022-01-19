package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the StringNormalizingReader Java class of Apache Batik
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
 * This class represents a NormalizingReader which handles Strings.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: StringNormalizingReader.java 1733416 2016-03-03 07:07:13Z gadams $
 */
class StringNormalizingReader(private var string: String) extends NormalizingReader {
  /**
   * The length of the string.
   */
  protected val /*var*/ length: Int = string.length
  /**
   * The index of the next character.
   */
  protected var next = 0
  /**
   * The current line in the stream.
   */
  protected var line = 1
  /**
   * The current column in the stream.
   */
  protected var column = 0

  /**
   * Read a single character.  This method will block until a
   * character is available, an I/O error occurs, or the end of the
   * stream is reached.
   */
  @throws[IOException]
  override def read(): Int = {
    val result: Int = if (length == next) -1
    else string.charAt({
      next += 1; next - 1
    })
    if (result <= 13) result match {
      case 13 =>
        column = 0
        line += 1
        val c = if (length == next) -1
        else string.charAt(next)
        if (c == 10) next += 1
        return 10
      case 10 =>
        column = 0
        line += 1
      case _ => ()
    }
    result
  }

  /**
   * Returns the current line in the stream.
   */
  override def getLine: Int = line

  /**
   * Returns the current column in the stream.
   */
  override def getColumn: Int = column

  /**
   * Close the stream.
   */
  @throws[IOException]
  override def close(): Unit =
    string = null
}
