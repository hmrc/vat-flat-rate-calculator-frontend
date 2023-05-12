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

import connectors.DataCacheConnector
import controllers.actions.DataRetrievalAction
import controllers.predicates.ValidatedSession
import forms.turnoverForm
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{errors, home => views}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TurnoverController @Inject()(mcc: MessagesControllerComponents,
                                   dataCacheConnector: DataCacheConnector,
                                   getData: DataRetrievalAction,
                                   validateSession: ValidatedSession,
                                   turnoverView: views.turnover,
                                   technicalErrorView: errors.technicalError)(implicit ec: ExecutionContext) extends FrontendController(mcc)
  with I18nSupport with Logging {

  def onPageLoad: Action[AnyContent] = (validateSession andThen getData) {
    implicit request =>
      val preparedForm = request.userAnswers.flatMap(x => x.turnover) match {
        case None => turnoverForm()
        case Some(value) =>  turnoverForm().fill(value)
      }
      request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
        case Some(value) => Ok(turnoverView(preparedForm, value.toString))
        case None =>
          logger.warn("[Turnover Controller]No model found in Keystore; redirecting back to landing page")
          Redirect(controllers.routes.VatReturnPeriodController.onSubmit)
      }
  }

  def onSubmit: Action[AnyContent] = (validateSession andThen getData).async {
    implicit request =>
      turnoverForm().bindFromRequest().fold(
        (formWithErrors: Form[_]) => {
          request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
            case Some(value) => Future.successful(BadRequest(turnoverView(formWithErrors, value.toString)))
            case _ =>
              logger.warn("[Turnover Controller]No model found in Keystore; redirecting back to landing page")
              Future.successful(InternalServerError(technicalErrorView()))
          }
        },
        value =>
          request.userAnswers.flatMap(x => x.vatReturnPeriod) match {
            case Some(_) => dataCacheConnector.save[BigDecimal](request.sessionId, "turnover", value).map(cacheMap =>
              Redirect(controllers.routes.CostOfGoodsController.onPageLoad))
            case _ => logger.warn("[Turnover Controller]No model found in Keystore for return Period; Internal server error")
              Future.successful(InternalServerError(technicalErrorView()))
          }
      )
  }

}
