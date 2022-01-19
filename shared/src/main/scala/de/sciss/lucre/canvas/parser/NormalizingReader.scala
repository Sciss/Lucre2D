package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the NormalizingReader Java class of Apache Batik
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
import java.io.Reader


/**
 * This class represents a reader which normalizes the line break: \n,
 * \r, \r\n are replaced by \n.  The methods of this reader are not
 * synchronized.  The input is buffered.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: NormalizingReader.java 1733416 2016-03-03 07:07:13Z gadams $
 */
abstract class NormalizingReader extends Reader {
  /**
   * Read characters into a portion of an array.
   *
   * @param cbuf Destination buffer
   * @param off  Offset at which to start writing characters
   * @param len  Maximum number of characters to read
   * @return The number of characters read, or -1 if the end of the
   *         stream has been reached
   */
  @throws[IOException]
  override def read(cbuf: Array[Char], off: Int, len: Int): Int = {
    if (len == 0) return 0
    var c = read()
    if (c == -1) return -1
    var result = 0
    while ({
      cbuf(result + off) = c.toChar
      result += 1
      c = read()
      ;
      c != -1 && result < len
    }) ()
    result
  }

  /**
   * Returns the current line in the stream.
   */
  def getLine: Int

  /**
   * Returns the current column in the stream.
   */
  def getColumn: Int
}
