package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the ErrorHandler Java class of Apache Batik
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
 * This interface must be implemented and then registred as the error handler
 * in order to be notified of parsing errors.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: ErrorHandler.java 1733416 2016-03-03 07:07:13Z gadams $
 */
trait ErrorHandler {
  /**
   * Called when a parse error occurs.
   */
  @throws[ParseException]
  def error(e: ParseException): Unit
}
