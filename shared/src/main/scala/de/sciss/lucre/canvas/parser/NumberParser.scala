package de.sciss.lucre.canvas.parser

// This is an adapted Scala translation of the NumberParser Java class of Apache Batik
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
 * This class represents a parser with support for numbers.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: NumberParser.java 1733416 2016-03-03 07:07:13Z gadams $
 */
object NumberParser {
  /**
   * Computes a float from mantissa and exponent.
   */
  def buildFloat(mant: Int, exp: Int): Float = {
    if (exp < -125 || mant == 0) return 0.0f
    if (exp >= 128) return if (mant > 0) java.lang.Float.POSITIVE_INFINITY
    else java.lang.Float.NEGATIVE_INFINITY
    if (exp == 0) return mant.toFloat
    val mantP = if (mant >= (1 << 26)) mant + 1 else mant  // round up trailing bits if they will be dropped.
    (if (exp > 0) mantP * pow10(exp) else mantP / pow10(-exp)).toFloat
  }

  /**
   * Array of powers of ten. Using double instead of float gives a tiny bit more precision.
   */
  private val pow10 = Array.tabulate[Double](128)(i => Math.pow(10, i))
}

abstract class NumberParser extends AbstractParser {
  /**
   * Parses the content of the buffer and converts it to a float.
   */
  @throws[ParseException]
  @throws[IOException]
  protected def parseFloat(): Float = {
    var mant      = 0
    var mantDig   = 0
    var mantPos   = true
    var mantRead  = false
    var exp       = 0
    var expDig    = 0
    var expAdj    = 0
    var expPos    = true

    current match {
      case '-' =>
        mantPos = false
        current = reader.read()
      case '+' =>
        current = reader.read()
      case _ => ()
    }

    // 'm1'

    def cont1(): Unit = {
      mantRead = true
      while ({
        if (mantDig < 9) {
          mantDig += 1
          mant = mant * 10 + (current - '0')
        }
        else expAdj += 1
        current = reader.read()
        current match {
          case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => true
          case _ => false
        }
      }) ()
    }

    current match {
      case '.' => ()
      case '0' =>
        mantRead = true
        while ({
          current = reader.read()
          current match {
            case '0' => true
            case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' =>
              cont1()
              false
            case '.' | 'e' | 'E' => false

            case _ => return 0.0f
          }
        }) ()

      case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => cont1()

      case _ =>
        reportUnexpectedCharacterError()
        return 0.0f
    }

    // 'm2'

    if (current == '.') {
      current = reader.read()

      def cont2(): Unit =
        while ({
          if (mantDig < 9) {
            mantDig += 1
            mant = mant * 10 + (current - '0')
            expAdj -= 1
          }
          current = reader.read()
          current match {
            case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => true
            case _ => false
          }
        }) ()

      current match {
        case 'e' | 'E' =>
          if (!mantRead) {
            reportUnexpectedCharacterError()
            return 0.0f
          }

        case '0' =>
          if (mantDig == 0) {
            while ({
              current = reader.read()
              expAdj -= 1
              current match {
                case '0' => true
                case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' =>
                  cont2()
                  false
                case _ =>
                  if (!mantRead) return 0.0f
                  false
              }
            }) ()
          } else cont2()

        case '1' | '2' | '3' | '4' | '5' | '6' | '7' |'8' | '9' => cont2()

        case _ => ()  // XXX TODO: correct?
      }
    }

    // exp

    current match {
      case 'e' | 'E' =>
        current = reader.read()

        def checkExp(): Boolean = {
          current = reader.read()
          current match {
            case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => true
            case _ => false
          }
        }

        val expOk = current match {
          case '-' =>
            expPos = false
            checkExp()
          case '+' =>
            checkExp()
          case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => true
          case _ => false
        }

        if (!expOk) {
          reportUnexpectedCharacterError()
          return 0f
        }

        // 'en'

        def cont3(): Unit =
          while ({
            if (expDig < 3) {
              expDig += 1
              exp = exp * 10 + (current - '0')
            }
            current = reader.read()
            current match {
              case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => true
              case _ => false
            }
          }) ()

        current match {
          case '0' =>
            while ({
              current = reader.read()
              current match {
                case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' =>
                  cont3()
                  false

                case '0'  => true
                case _    => false
              }
            }) ()

          case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' => cont3()
        }

      case _ => ()
    }

    if (!expPos) exp = -exp
    exp += expAdj
    if (!mantPos) mant = -mant
    NumberParser.buildFloat(mant, exp)
  }
}
