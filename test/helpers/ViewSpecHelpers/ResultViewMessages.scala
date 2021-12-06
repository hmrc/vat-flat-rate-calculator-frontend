/*
 * Copyright 2021 HM Revenue & Customs
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

trait ResultViewMessages extends MockitoSugar {
  implicit val lang: Lang = Lang("en")
  val ResultTitle                         = "Your VAT calculation - Check your VAT flat rate - GOV.UK"
  val ResultHeading                       = "Use the 16.5% VAT flat rate"
  val ResultIntro                         = "Based on your answers, you are a limited cost business. This is because the cost of your goods for this year was under £1,000."
  val ResultProgressiveDisclosureHeader   = "You may need to do this calculation for every VAT period"
  val ResultProgressiveDisclosureTitle    = "Are you in your first year of VAT registration?"
  val ResultProgressiveDisclosureText     = "Apply a 1% discount in your first year of VAT registration."
  val ResultH2Text                        = "What happens next? "
  val ResultNextText1                     = "Use this rate from 1 April 2017 when you do your VAT return."
  val ResultNextText2                     = "If you're doing your VAT return go to your business account (opens in a new window)"
  val ResultNextText2Href                 = "http://localhost:9020/business-account"
  val ResultNextText3                     = "You may want to join or leave the Flat Rate Scheme (opens in a new window)"
  val ResultNextText3Href                 = "http://www.gov.uk/vat-flat-rate-scheme/join-or-leave-the-scheme"
  val ResultNextText4                     = "You may want to deregister from VAT (opens in a new window)"
  val ResultNextText4Href                 = "http://www.gov.uk/vat-registration/cancel-registration"
  val FeedbackSurveyText                  = "What did you think of this service?"
  val ResultBannerTitle                   = "Help improve HMRC services"
  val ResultBannerText                    = "Sign up to take part in user research (opens in new tab)"
  val ResultBannerClose                   = "No thanks"
  val ResultBannerTextHref                = "https://signup.take-part-in-research.service.gov.uk/?utm_campaign=VFRS_results&utm_source=Survey_Banner&utm_medium=other&t=HMRC&id=114"
}
