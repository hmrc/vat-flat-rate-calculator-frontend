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

import controllers.predicates.ValidatedSession
import forms.VatFlatRateForm
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.data.FormError
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import services.StateService
import uk.gov.hmrc.http.SessionKeys
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{home => views}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class VatReturnPeriodController @Inject()(mcc: MessagesControllerComponents,
                                          stateService: StateService,
                                          session: ValidatedSession,
                                          forms: VatFlatRateForm,
                                          vatReturnPeriodView: views.vatReturnPeriod)
  extends FrontendController(mcc) with I18nSupport with Logging {

  val vatReturnPeriod: Action[AnyContent] = Action.async { implicit request =>

    if(request.session.get(SessionKeys.sessionId).isEmpty){
      val sessionId = UUID.randomUUID().toString
      Future.successful(Ok(vatReturnPeriodView(forms.vatReturnPeriodForm))
        .withSession(request.session + (SessionKeys.sessionId -> s"session-$sessionId")))
    } else {
      for {
        vatReturnPeriod <- stateService.fetchVatFlatRate()
      } yield vatReturnPeriod match {
        case Some(model) => Ok(vatReturnPeriodView(forms.vatReturnPeriodForm.fill(model)))
        case _           => Ok(vatReturnPeriodView(forms.vatReturnPeriodForm))
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
        Future.successful(BadRequest(vatReturnPeriodView(formWithErrors)))
      },
      success => {
        stateService.saveVatFlatRate(success).map(
          _ => Redirect(controllers.routes.TurnoverController.turnover())
        )
      }
    )
  }
}
