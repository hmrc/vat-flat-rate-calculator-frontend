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

package config

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait AppConfig {
  val contactFormServiceIdentifier: String
  val reportAProblemPartialUrl: String
  val reportAProblemNonJSUrl: String
  val businessTaxAccount: String
  val urBannerLink: String
  val feedbackSurvey: String
  val cacheTtl: Int
  val timeout: Int
  val timeoutCountdown: Int
}

@Singleton
class ApplicationConfig @Inject()(val config: ServicesConfig) extends AppConfig {

  private def loadConfig(key: String): String = config.getString(key)

  val appName: String = config.getString("appName")

  lazy val contactHost          = config.getString("contact-frontend.host")
  lazy val contactFormServiceIdentifier = "VFR"
  lazy val reportAProblemPartialUrl     = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl       = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val feedbackSurvey: String       = loadConfig("feedback-survey-frontend.url")
  lazy val timeout: Int                 = config.getInt("timeoutDialog.timeout-seconds")
  lazy val timeoutCountdown: Int        = config.getInt("timeoutDialog.timeout-countdown-seconds")

  //Business Tax Account
  lazy val businessTaxAccount: String = config.getString("business-tax-account.url")

  //Banner
  lazy val urBannerLink: String = "https://signup.take-part-in-research.service.gov.uk/?utm_campaign=VFRS_results&utm_source=Survey_Banner&utm_medium=other&t=HMRC&id=114"

  val cacheTtl: Int = config.getInt("mongodb.timeToLiveInSeconds")

}
