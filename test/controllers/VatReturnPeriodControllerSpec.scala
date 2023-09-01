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

import controllers.actions.{DataRetrievalAction, FakeDataRetrievalAction}
import helpers.ControllerSpecBase
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import play.api.http.Status
import play.api.test.Helpers._
import connectors.FakeDataCacheConnector
import forms.vatReturnPeriodForm
import play.api.data.Form
import play.api.libs.json.JsString
import models.ReturnPeriod
import org.scalatestplus.play.PlaySpec
import utils.CacheMap

import scala.concurrent.ExecutionContext.Implicits.global
import views.html.home.vatReturnPeriod

class VatReturnPeriodControllerSpec extends PlaySpec with ControllerSpecBase {

  val view = application.injector.instanceOf[vatReturnPeriod]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new VatReturnPeriodController(mcc, FakeDataCacheConnector, dataRetrievalAction, mockValidatedSession, view)

  def viewAsString(form: Form[_] = vatReturnPeriodForm()) = view(form)(fakeRequest, messages).toString

  "VatReturnPeriodController" must {

    "the question has previously not been answered" must {
      lazy val result = controller().onPageLoad()(fakeRequest)

      "return 200" in {
        status(result) shouldBe Status.OK
      }

      "return the correct view" in {
        contentAsString(result) shouldBe viewAsString()
      }
    }

    "the question has previously been answered" must {
      val validData = Map("vatReturnPeriod" -> JsString(vatReturnPeriodForm.options.head.value))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))
      lazy val result = controller(getRelevantData).onPageLoad()(fakeRequest)

      "return 200" in {
        status(result) shouldBe Status.OK
      }

      "return the correct view" in {
        contentAsString(result) shouldBe viewAsString(vatReturnPeriodForm().fill(ReturnPeriod.ANNUALLY))
      }
    }

    "valid data is submitted" must {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("vatReturnPeriod", vatReturnPeriodForm.options.head.value)).withMethod("POST")
      val result = controller().onSubmit()(postRequest)

      "return 303" in {
        status(result) shouldBe SEE_OTHER
      }
      "redirect to the cost of goods page" in {
        redirectLocation(result) shouldBe Some(s"${controllers.routes.TurnoverController.onPageLoad}")
      }
    }

    "not entering any data" must {
      val postRequest = fakeRequest.withMethod("POST")
      val result = controller().onSubmit()(postRequest)

      "return 400" in {
        status(result) shouldBe BAD_REQUEST
      }
      "fail with the correct error message" in {
        contentAsString(result) should include(messages("error.vatReturnPeriod.required"))
      }
    }

  }
}