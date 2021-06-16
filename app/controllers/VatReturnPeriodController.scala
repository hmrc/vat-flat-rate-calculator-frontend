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

import java.util.UUID
import config.AppConfig
import controllers.predicates.ValidatedSession
import forms.VatFlatRateForm
import models.UIHelpersWrapper

import javax.inject.{Inject, Singleton}
import play.api.{Logger, Logging}
import play.api.data.FormError
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import services.StateService
import uk.gov.hmrc.http.SessionKeys
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts.{Article, Footer, FooterLinks, HeadWithTrackingConsent, HeaderNav, MainContent, MainContentHeader, ServiceInfo, Sidebar}
import views.html.layouts.GovUkTemplate
//import views.html.{home => views}
import views.html.{home => views}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class VatReturnPeriodController @Inject()(config: AppConfig,
                                          mcc: MessagesControllerComponents,
                                          stateService: StateService,
                                          session: ValidatedSession,
                                          forms: VatFlatRateForm,
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
                                          uiErrorSummary: ErrorSummary)
  extends FrontendController(mcc) with I18nSupport with Logging {

  val uiHelpersWrapper  = UIHelpersWrapper(uiSidebar, uiInputGroup, uiform, uiErrorSummary, footerLinks)

  val vatReturnPeriod: Action[AnyContent] = Action.async { implicit request =>

    if(request.session.get(SessionKeys.sessionId).isEmpty){
      val sessionId = UUID.randomUUID().toString
      Future.successful(Ok(views.vatReturnPeriod(config, forms.vatReturnPeriodForm, article, headUi, govUkTemplate,
        header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper))
        .withSession(request.session + (SessionKeys.sessionId -> s"session-$sessionId")))
    } else {
      for {
        vatReturnPeriod <- stateService.fetchVatFlatRate()
      } yield vatReturnPeriod match {
        case Some(model) => Ok(views.vatReturnPeriod(config, forms.vatReturnPeriodForm.fill(model), article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper))
        case _           => Ok(views.vatReturnPeriod(config, forms.vatReturnPeriodForm, article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper))
      }
    }
  }

  val submitVatReturnPeriod: Action[AnyContent] = session.async{ implicit request =>

    forms.vatReturnPeriodForm.bindFromRequest.fold(
      errors => {
        logger.warn("VatReturnPeriod form could not be bound")
        val formWithErrors = errors.copy(errors = errors.errors.map {
          errs => FormError(errs.key, if (errs.message.contains("error.required")) Messages("error.vatReturnPeriod.required") else errs.message)
        })
        Future.successful(BadRequest(views.vatReturnPeriod(config, formWithErrors, article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper)))
      },
      success => {
        stateService.saveVatFlatRate(success).map(
          _ => Redirect(controllers.routes.TurnoverController.turnover())
        )
      }
    )
  }
}
