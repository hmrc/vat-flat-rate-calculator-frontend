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

package controllers

import common.ResultCodes
import connectors.DataCacheConnector
import controllers.actions.DataRetrievalAction
import controllers.predicates.ValidatedSession
import forms.costOfGoodsForm
import javax.inject.{Inject, Singleton}
import models.VatFlatRateModel
import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{errors => errs, home => views}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CostOfGoodsController @Inject()(mcc: MessagesControllerComponents,
                                      dataCacheConnector: DataCacheConnector,
                                      getData: DataRetrievalAction,
                                      session: ValidatedSession,
                                      costOfGoodsView: views.costOfGoods,
                                      technicalErrorView: errs.technicalError)(implicit ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport with Logging {


  def onPageLoad: Action[AnyContent] = getData {
    implicit request =>
      val preparedForm = request.userAnswers.flatMap(x => x.costOfGoods) match {
        case None => costOfGoodsForm()
        case Some(value) =>  costOfGoodsForm().fill(value)
      }
      request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
        case Some(value) => Ok(costOfGoodsView(preparedForm, value.toString))
        case None =>
          logger.warn("[CostOfGoods Controller]No model found in Keystore; redirecting back to landing page")
          Redirect(controllers.routes.VatReturnPeriodController.onSubmit)
      }
  }

  def onSubmit: Action[AnyContent] = getData.async {
    implicit request =>
      costOfGoodsForm().bindFromRequest().fold(
        (formWithErrors: Form[_]) => {
          request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
            case Some(value) => Future.successful(BadRequest(costOfGoodsView(formWithErrors, value.toString)))
            case _ =>
              logger.warn("[CostOfGoods Controller]No model found in Keystore; Internal server error")
              Future.successful(InternalServerError(technicalErrorView()))
          }
        },
        value =>
          request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
            case Some(_) => dataCacheConnector.save[BigDecimal] (request.sessionId, "costOfGoods", value).map (cacheMap =>
              Redirect (controllers.routes.ResultController.onPageLoad) )
            case _ => logger.warn("[CostOfGoods Controller]No model found in Keystore for return Period; Internal server error")
              Future.successful(InternalServerError(technicalErrorView()))
          }
      )
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

}