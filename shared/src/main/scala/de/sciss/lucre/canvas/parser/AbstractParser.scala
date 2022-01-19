package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the AbstractParser Java class of Apache Batik
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
import java.text.MessageFormat
import java.util.MissingResourceException


/**
 * This class is the superclass of all parsers. It provides localization
 * and error handling methods.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractParser.java 1802297 2017-07-18 13:58:12Z ssteiner $
 */
object AbstractParser {
  /**
   * The default resource bundle base name.
   */
  val BUNDLE_CLASSNAME = "org.apache.batik.parser.resources.Messages"
}

abstract class AbstractParser extends Parser {
  /**
   * The error handler.
   */
  protected var errorHandler: ErrorHandler = new DefaultErrorHandler

//  /**
//   * The localizable support.
//   */
//  protected var localizableSupport = new LocalizableSupport(AbstractParser.BUNDLE_CLASSNAME, classOf[AbstractParser].getClassLoader)

  /**
   * The normalizing reader.
   */
  protected var reader: NormalizingReader = null
  /**
   * The current character.
   */
  protected var current = 0

  /**
   * Returns the current character value.
   */
  def getCurrent: Int = current

//  /**
//   * Implements {@link org.apache.batik.i18n.Localizable# setLocale ( Locale )}.
//   */
//  override def setLocale(l: Locale): Unit = {
//    localizableSupport.setLocale(l)
//  }
//
//  /**
//   * Implements {@link org.apache.batik.i18n.Localizable# getLocale ( )}.
//   */
//  override def getLocale: Locale = localizableSupport.getLocale

  /**
   * Implements {@link
   * org.apache.batik.i18n.Localizable#formatMessage(String,Object[])}.
   */
  @throws[MissingResourceException]
  /*override*/ def formatMessage(key: String, args: Array[AnyRef]): String = {
    // localizableSupport.formatMessage(key, args)
    MessageFormat.format(/*getString(*/key/*)*/, args);
  }

  /**
   * Allow an application to register an error event handler.
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
  override def setErrorHandler(handler: ErrorHandler): Unit = {
    errorHandler = handler
  }

//  /**
//   * Parses the given reader
//   */
//  @throws[ParseException]
//  override def parse(r: Reader): Unit = {
//    try {
//      reader = new StreamNormalizingReader(r)
//      doParse()
//    } catch {
//      case e: IOException =>
//        errorHandler.error(new ParseException(createErrorMessage("io.exception", null), e))
//    }
//  }

//  /**
//   * Parses the given input stream. If the encoding is null,
//   * ISO-8859-1 is used.
//   */
//  @throws[ParseException]
//  def parse(is: InputStream, enc: String): Unit = {
//    try {
//      reader = new StreamNormalizingReader(is, enc)
//      doParse()
//    } catch {
//      case e: IOException =>
//        errorHandler.error(new ParseException(createErrorMessage("io.exception", null), e))
//    }
//  }

  /**
   * Parses the given string.
   */
  @throws[ParseException]
  override def parse(s: String): Unit =
    try {
      reader = new StringNormalizingReader(s)
      doParse()
    } catch {
      case e: IOException =>
        errorHandler.error(new ParseException(createErrorMessage("io.exception", null), e))
    }

  /**
   * Method responsible for actually parsing data after AbstractParser
   * has initialized itself.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def doParse(): Unit

  /**
   * Signals an error to the error handler.
   *
   * @param key  The message key in the resource bundle.
   * @param args The message arguments.
   */
  @throws[ParseException]
  protected def reportError(key: String, args: Array[AnyRef]): Unit =
    errorHandler.error(new ParseException(createErrorMessage(key, args), reader.getLine, reader.getColumn))

  /**
   * simple api to call often reported error.
   * Just a wrapper for reportError().
   *
   * @param expectedChar what caller expected
   * @param currentChar  what caller found
   */
  protected def reportCharacterExpectedError(expectedChar: Char, currentChar: Int): Unit =
    reportError("character.expected",
      Array[AnyRef](expectedChar.asInstanceOf[AnyRef], currentChar.asInstanceOf[AnyRef]))

  /**
   * simple api to call often reported error.
   * Just a wrapper for reportError().
   */
  protected def reportUnexpectedCharacterError(): Unit =
    reportError("character.unexpected", Array[AnyRef](current.asInstanceOf[AnyRef]))

  /*
   * Returns a localized error message.
   *
   * @param key  The message key in the resource bundle.
   * @param args The message arguments.
   */
  private def createErrorMessage(key: String, args: Array[AnyRef]): String =
    try formatMessage(key, args)
    catch {
      case _: MissingResourceException => key
    }

  /**
   * Returns the resource bundle base name.
   *
   * @return BUNDLE_CLASSNAME.
   */
  protected def getBundleClassName: String = AbstractParser.BUNDLE_CLASSNAME

  /**
   * Skips the whitespaces in the current reader.
   */
  @throws[IOException]
  protected def skipSpaces(): Unit = {
    while (true) {
      current match {
        case 0x20 | 0x09 | 0x0D | 0x0A => ()
        case _ => return
      }
      current = reader.read()
    }
  }

  /**
   * Skips the whitespaces and an optional comma.
   */
  @throws[IOException]
  protected def skipCommaSpaces(): Unit = {
    while ({
      current match {
        case 0x20 | 0x9 | 0xD | 0xA =>
          current = reader.read()
          true
        case _ => false
      }
    }) ()

    if (current == ',') {
      while ({
        current = reader.read()
        current match {
          case 0x20 | 0x9 | 0xD | 0xA => true
          case _ => false
        }
      }) ()
    }
  }
}
