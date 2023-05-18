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
import controllers.actions.{DataRetrievalAction, ValidatedSession}

import javax.inject.Inject
import models.VatFlatRateModel
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{home => views}

import scala.util.Random

class ResultController @Inject()(mcc: MessagesControllerComponents,
                                 dataCacheConnector: DataCacheConnector,
                                 getData: DataRetrievalAction,
                                 validateSession: ValidatedSession,
                                 resultView: views.result) extends FrontendController(mcc)
  with I18nSupport with Logging {

  def onPageLoad: Action[AnyContent] = (validateSession andThen getData) {
    implicit request =>
      request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
        case Some(value) =>
          val resultModel = new VatFlatRateModel(value.toString, request.userAnswers.flatMap(x => x.turnover), request.userAnswers.flatMap(x => x.costOfGoods))
          resultModel match {
            case VatFlatRateModel(_, Some(_), Some(_)) =>
              Ok(resultView(whichResult(resultModel), setURPanelFlag))
            case _ =>
              logger.warn("ResultModel could not be retrieved")
              Redirect(controllers.routes.VatReturnPeriodController.onSubmit)
          }
        case _ => logger.warn("vat return period could not be retrieved")
          Redirect(controllers.routes.VatReturnPeriodController.onSubmit)
      }
  }

  private[controllers] def setURPanelFlag(implicit hc: HeaderCarrier): Boolean = {
    val random = new Random()
    val seed = getLongFromSessionID(hc)
    random.setSeed(seed)
    random.nextInt(3) == 0
  }

  private[controllers] def getLongFromSessionID(hc: HeaderCarrier): Long = {
    val session = hc.sessionId.map(_.value).getOrElse("0")
    val numericSessionValues = session.replaceAll("[^0-9]", "") match {
      case "" => "0"
      case num => num
    }
    numericSessionValues.takeRight(10).toLong
  }

  def whichResult(model: VatFlatRateModel): Int = {
    if(model.vatReturnPeriod.equalsIgnoreCase("annually")){
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
