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

package controllers

import controllers.predicates.ValidatedSession
import forms.VatFlatRateForm
import javax.inject.{Inject, Singleton}
import models.VatFlatRateModel
import play.api.Logging
import play.api.data.Form
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc._
import services.StateService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{errors, home => views}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TurnoverController @Inject()(mcc: MessagesControllerComponents,
                                   stateService: StateService,
                                   session: ValidatedSession,
                                   forms: VatFlatRateForm,
                                   turnoverView: views.turnover,
                                   technicalErrorView: errors.technicalError) extends FrontendController(mcc)
  with I18nSupport with Logging {

  val turnover: Action[AnyContent] = session.async{ implicit request =>
    routeRequest(Ok, forms.turnoverForm)
  }

  val submitTurnover: Action[AnyContent] = session.async { implicit request =>
  forms.turnoverForm.bindFromRequest.fold(
      errors => {
        logger.info("Turnover form could not be bound")
        routeRequest(BadRequest, errors)
      },
      success => {
        stateService.saveVatFlatRate(success).map(
          _ => Redirect(controllers.routes.CostOfGoodsController.costOfGoods))
      }
    )
  }

  def routeRequest(res: Status, form: Form[VatFlatRateModel])(implicit req: Request[AnyContent], hc: HeaderCarrier): Future[Result] = {
    implicit val lang: Lang = req.lang
    for {
      vfrModel <- stateService.fetchVatFlatRate()
    } yield vfrModel match {
      case Some(model) =>
        model.vatReturnPeriod match {
          case s  if s.equalsIgnoreCase(messagesApi("vatReturnPeriod.option.annual"))    => res(turnoverView(form.fill(model), messagesApi("common.year")))
          case s  if s.equalsIgnoreCase(messagesApi("vatReturnPeriod.option.quarter"))   => res(turnoverView(form.fill(model), messagesApi("common.quarter")))
          case _ =>
            logger.warn(
              s"""Incorrect value found for Vat Return Period:
                 |Should be [${messagesApi("vatReturnPeriod.option.annual")}] or [${messagesApi("vatReturnPeriod.option.quarter")}] but found ${model.vatReturnPeriod}""".stripMargin
            )
            InternalServerError(technicalErrorView())
        }
      case _ =>
        res match {
          case Ok =>
            logger.warn("[Turnover Controller]No model found in Keystore; redirecting back to landing page")
            Redirect(controllers.routes.VatReturnPeriodController.vatReturnPeriod)
          case BadRequest =>
            logger.warn("[Turnover Controller]No VatFlatRate model found in Keystore")
            InternalServerError(technicalErrorView())
        }
    }
  }

}
