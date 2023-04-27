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

import java.util.UUID

import connectors.DataCacheConnector
import controllers.actions.DataRetrievalAction
import controllers.predicates.ValidatedSession
import forms.vatReturnPeriodForm
import javax.inject.{Inject, Singleton}
import models.ReturnPeriod
import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{home => views}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatReturnPeriodController @Inject()(mcc: MessagesControllerComponents,
                                          dataCacheConnector: DataCacheConnector,
                                          getData: DataRetrievalAction,
                                          session: ValidatedSession,
                                          vatReturnPeriodView: views.vatReturnPeriod)(implicit ec: ExecutionContext)
  extends FrontendController(mcc) with I18nSupport with Logging {

  def onPageLoad: Action[AnyContent] = getData {
    implicit request =>
      val preparedForm = request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
        case None => vatReturnPeriodForm()
        case Some(value) =>  vatReturnPeriodForm().fill(value)
      }
      Ok(vatReturnPeriodView(preparedForm))
  }

  def onSubmit: Action[AnyContent] = getData.async {
    implicit request =>
      vatReturnPeriodForm().bindFromRequest().fold(
        (formWithErrors: Form[_]) => {
          Future.successful(BadRequest(vatReturnPeriodView(formWithErrors)))
          },
        value =>
          dataCacheConnector.save[ReturnPeriod.Value](request.sessionId, "vatReturnPeriod", value).map(cacheMap =>
            Redirect(controllers.routes.TurnoverController.onPageLoad))
      )
  }
}
