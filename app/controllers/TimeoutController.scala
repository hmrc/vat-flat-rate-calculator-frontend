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

package controllers

import config.AppConfig
import controllers.predicates.ValidatedSession
import models.UIHelpersWrapper
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.StateService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts.{Article, Footer, FooterLinks, HeadWithTrackingConsent, HeaderNav, MainContent, MainContentHeader, ServiceInfo, Sidebar}
import views.html.fallback
import views.html.layouts.GovUkTemplate

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class TimeoutController @Inject()(config: AppConfig,
                                  mcc: MessagesControllerComponents,
                                  stateService: StateService,
                                  session: ValidatedSession,
                                  article: Article,
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
                                  uiErrorSummary: ErrorSummary) extends FrontendController(mcc) with I18nSupport {

  val uiHelpersWrapper  = UIHelpersWrapper(uiSidebar, uiInputGroup, uiform, uiErrorSummary, footerLinks)

  val timeout: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(fallback.timeout(config, article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper)))
  }
}
