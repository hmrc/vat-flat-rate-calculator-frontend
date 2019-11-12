/*
 * Copyright 2019 HM Revenue & Customs
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
  val analyticsToken: String
  val analyticsHost: String
  val contactFormServiceIdentifier: String
  val reportAProblemPartialUrl: String
  val reportAProblemNonJSUrl: String
  val businessTaxAccount: String
  val urBannerLink: String
  val feedbackSurvey: String
  val googleTagManagerId: String
}

@Singleton
class ApplicationConfig @Inject()(val config: ServicesConfig) extends AppConfig {

  private def loadConfig(key: String): String = config.getString(key)

  private lazy val contactHost          = config.getString("contact-frontend.host")
  lazy val contactFormServiceIdentifier = "VFR"
  lazy val reportAProblemPartialUrl     = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl       = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val feedbackSurvey: String       = loadConfig("feedback-survey-frontend.url")

  //Business Tax Account
  lazy val businessTaxAccount: String = config.getString("business-tax-account.url")

  // GA
  lazy val analyticsToken: String = loadConfig("google-analytics.token")
  lazy val analyticsHost: String  = loadConfig("google-analytics.host")

  //Banner
  lazy val urBannerLink: String = "https://signup.take-part-in-research.service.gov.uk/?utm_campaign=VFRS_results&utm_source=Survey_Banner&utm_medium=other&t=HMRC&id=114"
  lazy val googleTagManagerId = loadConfig(s"google-tag-manager.id")

}
