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

import controllers.predicates.ValidatedSession
import javax.inject.Inject
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.StateService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{home => views}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class ResultController @Inject()(mcc: MessagesControllerComponents,
                                 stateService: StateService,
                                 session: ValidatedSession,
                                 resultView: views.result) extends FrontendController(mcc)
  with I18nSupport with Logging {

  val result: Action[AnyContent] = session.async { implicit request =>
    val showUserResearchPanel = setURPanelFlag
    stateService.fetchResultModel.map {
      case Some(model)  => Ok(resultView(model.result, showUserResearchPanel))
      case None         =>
        logger.warn("ResultModel could not be retrieved from Keystore")
        Redirect(controllers.routes.VatReturnPeriodController.vatReturnPeriod())
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

}
