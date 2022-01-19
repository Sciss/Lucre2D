//package de.sciss.lucre.canvas.parser
//
//// This is an adapted Scala translation of the StreamNormalizingReader Java class of Apache Batik
//// as released under Apache License -- see original file header below.
//// So it can be used in Scala.js.
//
///*
//
//   Licensed to the Apache Software Foundation (ASF) under one or more
//   contributor license agreements.  See the NOTICE file distributed with
//   this work for additional information regarding copyright ownership.
//   The ASF licenses this file to You under the Apache License, Version 2.0
//   (the "License"); you may not use this file except in compliance with
//   the License.  You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//
// */
//
//import java.io.IOException
//import java.io.InputStream
//import java.io.Reader
//import java.util
//import org.apache.batik.util.EncodingUtilities
//
//
///**
// * This class represents a NormalizingReader which handles streams of
// * bytes.
// *
// * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
// * @version $Id: StreamNormalizingReader.java 1733416 2016-03-03 07:07:13Z gadams $
// */
//object StreamNormalizingReader {
//}
//
///**
// * This constructor is intended for use by subclasses.
// */
//class StreamNormalizingReader protected() extends NormalizingReader {
//
//  /**
//   * The char decoder.
//   */
//  protected var charDecoder: CharDecoder = null
//  /**
//   * The next char.
//   */
//  protected var nextChar: Int = -1
//  /**
//   * The current line in the stream.
//   */
//  protected var line = 1
//  /**
//   * The current column in the stream.
//   */
//  protected var column = 0
//
//  /**
//   * Creates a new NormalizingReader.
//   *
//   * @param is  The input stream to decode.
//   * @param enc The standard encoding name. A null encoding means
//   *            ISO-8859-1.
//   */
//  def this(is: InputStream, enc: String)
//
//  /**
//   * Creates a new NormalizingReader. The encoding is assumed to be
//   * ISO-8859-1.
//   *
//   * @param is The input stream to decode.
//   */
//  def this(is: InputStream) {
//    this(is, null)
//  }
//
//  /**
//   * Creates a new NormalizingReader.
//   *
//   * @param r The reader to wrap.
//   */
//  def this(r: Reader) {
//    this()
//    charDecoder = new GenericDecoder(r)
//  }
//
//  /**
//   * Read a single character.  This method will block until a
//   * character is available, an I/O error occurs, or the end of the
//   * stream is reached.
//   */
//  @throws[IOException]
//  override def read: Int = {
//    var result = nextChar
//    if (result != -1) {
//      nextChar = -1
//      if (result == 13) {
//        column = 0
//        line += 1
//      }
//      else column += 1
//      return result
//    }
//    result = charDecoder.readChar
//    result match {
//      case 13 =>
//        column = 0
//        line += 1
//        val c = charDecoder.readChar
//        if (c == 10) return 10
//        nextChar = c
//        return 10
//      case 10 =>
//        column = 0
//        line += 1
//    }
//    result
//  }
//
//  /**
//   * Returns the current line in the stream.
//   */
//  override def getLine: Int = line
//
//  /**
//   * Returns the current column in the stream.
//   */
//  override def getColumn: Int = column
//
//  /**
//   * Close the stream.
//   */
//  @throws[IOException]
//  override def close(): Unit = {
//    charDecoder.dispose()
//    charDecoder = null
//  }
//
//  /**
//   * Creates the CharDecoder mapped with the given encoding name.
//   */
//  @throws[IOException]
//  protected def createCharDecoder(is: InputStream, enc: String): CharDecoder = {
//    val cdf = StreamNormalizingReader.charDecoderFactories.get(enc.toUpperCase).asInstanceOf[StreamNormalizingReader.CharDecoderFactory]
//    if (cdf != null) return cdf.createCharDecoder(is)
//    var e = EncodingUtilities.javaEncoding(enc)
//    if (e == null) e = enc
//    new GenericDecoder(is, e)
//  }
//}
