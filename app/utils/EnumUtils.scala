/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import play.api.Logging
import play.api.libs.json.{Format, JsError, JsString, JsSuccess, Reads, Writes}

object EnumUtils extends Logging {

  def enumReads[E](values: Array[E])(asString: E => String): Reads[E] =
    Reads {
      case JsString(value) =>
        values.find(enumValue => asString(enumValue) == value) match {
          case Some(enumValue) =>
            JsSuccess(enumValue)

          case None =>
            logger.warn(
              s"EnumUtils.enumReads - Enum does not contain the value: '$value'"
            )
            JsError(
              s"Enum does not contain the value: '$value'"
            )
        }

      case _ =>
        logger.warn("EnumUtils.enumReads - String value expected")
        JsError("String value expected")
    }

  def enumWrites[E](asString: E => String): Writes[E] =
    Writes(value => JsString(asString(value)))

  def enumFormat[E](values: Array[E])(asString: E => String): Format[E] =
    Format(
      enumReads(values)(asString),
      enumWrites(asString)
    )

}
