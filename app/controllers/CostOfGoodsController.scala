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

import common.ResultCodes
import config.AppConfig
import controllers.predicates.ValidatedSession
import forms.VatFlatRateForm
import models.{ResultModel, UIHelpersWrapper, VatFlatRateModel}
import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.StateService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts._
import views.html.layouts.GovUkTemplate
import views.html.{errors => errs, home => views}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class CostOfGoodsController @Inject()(config: AppConfig,
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
                                      uiErrorSummary: ErrorSummary) extends FrontendController(mcc) with I18nSupport with Logging {

  val uiHelpersWrapper  = UIHelpersWrapper(uiSidebar, uiInputGroup, uiform, uiErrorSummary, footerLinks)

  val costOfGoods: Action[AnyContent] = session.async { implicit request =>
    routeRequest(Ok, forms.costOfGoodsForm)
  }

  val submitCostOfGoods: Action[AnyContent] = session.async { implicit request =>
    forms.costOfGoodsForm.bindFromRequest.fold(
      errors => {
        logger.warn("Cost of Goods form could not be bound")
        routeRequest(BadRequest, errors)
      },
      success => {
        for {
          _        <- stateService.saveVatFlatRate(success)
          result   =  whichResult(success)
          _        <- stateService.saveResultModel(createResultModel(success, result))
          response <- Future.successful(Redirect(controllers.routes.ResultController.result()))
        } yield response
      }
    )
  }

  def routeRequest(res: Status, form: Form[VatFlatRateModel])(implicit req: Request[AnyContent], hc: HeaderCarrier): Future[Result] = {
    implicit val lang = req.lang
    for {
      vfrModel <- stateService.fetchVatFlatRate()
    } yield vfrModel match {
      case Some(model) =>
        model.vatReturnPeriod match {
          case s  if s.equalsIgnoreCase(messagesApi("vatReturnPeriod.option.annual"))    =>
            res(views.costOfGoods(config, form.fill(model), messagesApi("common.year"), article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper))
          case s  if s.equalsIgnoreCase(messagesApi("vatReturnPeriod.option.quarter"))   =>
            res(views.costOfGoods(config, form.fill(model), messagesApi("common.quarter"), article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, uiHelpersWrapper))
          case _ =>
            logger.warn(
              s"""Incorrect value found for Vat Return Period:
                 |Should be [${messagesApi("vatReturnPeriod.option.annual")}] or [${messagesApi("vatReturnPeriod.option.quarter")}] but found ${model.vatReturnPeriod}""".stripMargin
            )
            InternalServerError(errs.technicalError(config, article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, footerLinks, uiHelpersWrapper))
        }
      case _ =>
        res match {
          case Ok =>
            logger.warn("[CostOfGoods Controller]No model found in Keystore; redirecting back to landing page")
            Redirect(controllers.routes.VatReturnPeriodController.vatReturnPeriod())
          case BadRequest =>
            logger.warn("[CostOfGoods Controller]No VatFlatRate model found in Keystore")
            InternalServerError(errs.technicalError(config, article, headUi, govUkTemplate, header_nav, footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, footerLinks, uiHelpersWrapper))
        }
    }
  }

  def whichResult(model: VatFlatRateModel)(implicit req: Request[AnyContent]): Int = {
    if(model.vatReturnPeriod.equalsIgnoreCase(messagesApi("vatReturnPeriod.option.annual")(req.lang))){
      model match {
        case VatFlatRateModel(_,_,Some(cost)) if cost < 1000 => ResultCodes.ONE
        case VatFlatRateModel(_,Some(turnover),Some(cost)) if turnover*0.02 > cost => ResultCodes.TWO
        case _ => ResultCodes.THREE
      }
    } else {
      model match {
        case VatFlatRateModel(_,_,Some(cost)) if cost < 250 => ResultCodes.FOUR
        case VatFlatRateModel(_,Some(turnover),Some(cost)) if turnover*0.02 > cost => ResultCodes.FIVE
        case _ => ResultCodes.SIX
      }
    }
  }

  def createResultModel(model: VatFlatRateModel, resultCode: Int): ResultModel = {
    ResultModel(model, resultCode)
  }

}