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

package helpers.ViewSpecHelpers

import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Lang

trait TurnoverViewMessages extends MockitoSugar {

  def turnoverTitle(period: String) =
    s"What was your turnover for the $period including VAT in pounds? - Check your VAT flat rate - GOV.UK"

  def turnoverHeading(period: String) = s"What was your turnover for the $period including VAT in pounds?"
  val turnoverIntro = "This is the total sales of all goods and services. If you're estimating, give realistic figures."
  def turnoverError(period: String) = s"Enter your turnover for the $period including VAT in pounds"
  val turnoverNegativeError         = "Enter an amount that&#x27;s £0 or more"
  val turnoverDecimalError          = "The amount you&#x27;ve entered has too many decimal places"
  val turnoverMaxError              = "Enter an amount less than £9,999,999,999.98"
  val turnoverContinue              = "Continue"

}
