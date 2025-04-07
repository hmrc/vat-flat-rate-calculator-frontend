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

package forms

import play.api.data.{Form, FormError}
import play.api.data.Forms._
import play.api.data.format.Formatter
import common.Constants.maximumTurnover
import utils.Validation

object turnoverForm extends Validation {

  def apply(): Form[BigDecimal] =
    Form(
      single(
        "turnover" -> of(turnOverFormatter("error.turnover.required", "error.invalidNumber"))
          .verifying(minimumValue[BigDecimal](0, "error.negative"))
          .verifying(maximumValue[BigDecimal](maximumTurnover, "error.moreThanMaximumTurnover"))
      )
    )

  def turnOverFormatter(errorKeyBlank: String, errorKeyInvalid: String) = new Formatter[BigDecimal] {

    def bind(key: String, data: Map[String, String]) =
      data.get(key) match {
        case None                               => produceError(key, errorKeyBlank)
        case Some("")                           => produceError(key, errorKeyBlank)
        case Some(s) if !verifyDecimalPlaces(s) => produceError(key, "error.twoDecimalPlaces")
        case Some(s) if s.matches(decimalRegex) => Right(BigDecimal(s))
        case _                                  => produceError(key, errorKeyInvalid)
      }

    def unbind(key: String, value: BigDecimal) = Map(key -> value.toString)
  }

}
