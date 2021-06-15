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

package config

import models.UIHelpersWrapper

import javax.inject.Inject
import play.api.{Configuration, Play, mvc}
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.Request
import play.mvc.Http.Request
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.frontend.http.FrontendErrorHandler
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts.{Footer, FooterLinks, HeadWithTrackingConsent, HeaderNav, MainContent, MainContentHeader, ServiceInfo, Sidebar}
import views.html.error_template
import views.html.layouts.GovUkTemplate

class VFRSErrorHandler @Inject()(val messagesApi: MessagesApi,
                                 val configuration: Configuration,
                                 appConfig: AppConfig,
                                 headUi: HeadWithTrackingConsent,
                                 govUkTemplate: GovUkTemplate,
                                 header_nav: HeaderNav,
                                 footer: Footer,
                                 uiServiceInfo: ServiceInfo,
                                 reportAProblemLink: ReportAProblemLink,
                                 main_content: MainContent,
                                 main_content_header: MainContentHeader,
                                 footerLinks: FooterLinks,
                                 uiSidebar: Sidebar,
                                 uiInputGroup: InputRadioGroup,
                                 uiform: FormWithCSRF,
                                 uiErrorSummary: ErrorSummary) extends FrontendErrorHandler {

  val uiHelpersWrapper = UIHelpersWrapper(uiSidebar, uiInputGroup, uiform, uiErrorSummary, footerLinks)

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)(implicit request: mvc.Request[_]): Html = {
    error_template(pageTitle, heading, message, appConfig, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink,
      main_content, main_content_header, uiHelpersWrapper)
  }

  override def internalServerErrorTemplate(implicit request: mvc.Request[_]): Html =
    standardErrorTemplate(
      Messages("techError.title"),
      Messages("techError.heading"),
      Messages("techError.para.1")
    )

}
