/*
 * Copyright 2022 HM Revenue & Customs
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

import common.Constants
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

object Validation {

  def isTwoDecimalPlaces: Constraint[BigDecimal] = Constraint("constraints.isTwoDecimalPlaces")({
    value =>
      val errors = value match {
        case n if n.scale <= 2 => Nil
        case _ => Seq(ValidationError("error.twoDecimalPlaces"))
      }
      if (errors.isEmpty) Valid else Invalid(errors)
  })

  def isPositive: Constraint[BigDecimal] = Constraint("constraints.isPositive")({
    value =>
      val errors = value match {
        case n if n >= 0 => Nil
        case _ => Seq(ValidationError("error.negative"))
      }
      if (errors.isEmpty) Valid else Invalid(errors)
  })

  def isLessThanMaximumTurnover: Constraint[BigDecimal] = Constraint("constraints.lessThanMaximumTurnover")({
    amount =>
      val errors = amount match {
        case am if am < Constants.maximumTurnover => Nil
        case _ => Seq(ValidationError("error.moreThanMaximumTurnover"))
      }
      if (errors.isEmpty) Valid else Invalid(errors)
  })

  }
