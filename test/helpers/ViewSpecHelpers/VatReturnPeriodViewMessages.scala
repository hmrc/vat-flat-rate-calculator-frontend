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

trait VatReturnPeriodViewMessages extends MockitoSugar {
  implicit val lang: Lang      = Lang("en")
  val vatReturnPeriodTitle     = "How often do you do your VAT returns? - Check your VAT flat rate - GOV.UK"
  val vatReturnPeriodHeading   = "How often do you do your VAT returns?"
  val vatReturnPeriodIntro     = "If you're using the Flat Rate Scheme, select the period that matches your VAT return."
  val vatReturnPeriodPara      = "If you're thinking of joining the Flat Rate Scheme, select annually."
  val vatReturnPeriodAnnually  = "Annually"
  val vatReturnPeriodQuarterly = "Quarterly"
  val vatReturnPeriodContinue  = "Continue"
  val vatReturnPeriodError     = "This field is required"

}
