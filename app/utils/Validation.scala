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

import forms.vatReturnPeriodForm.options
import play.api.data.FormError
import play.api.data.validation.{Constraint, Invalid, Valid}

trait Validation {

  def produceError(key: String, error: String, args: Any*): Left[Seq[FormError], Nothing] = Left(Seq(FormError(key, error, args)))

  val decimalRegex = """^[+-]?[0-9]{1,11}(?:\.[0-9]{1,2})?$"""

  protected def minimumValue[A](minimum: A, errorKey: String, errorArgs: Any*)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>
        import ev._
        if (input >= minimum) {
          Valid
        } else {
          Invalid(errorKey, errorArgs:_*)
        }
    }

  protected def maximumValue[A](maximum: A, errorKey: String, errorArgs: Any*)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>
        import ev._
        if (input < maximum) {
          Valid
        } else {
          Invalid(errorKey, errorArgs:_*)
        }
    }

  def verifyDecimalPlaces(input: String) = {
    if (input.contains(".")) {
      val decimalPlace = input.length - input.indexOf(".") - 1
      if (decimalPlace <= 2) true
      else false
    }
    else true
  }

  def optionIsValid(value: String) = options.exists(o => o.value == value)

}
