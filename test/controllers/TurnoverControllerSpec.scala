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

import common.Constants.maximumTurnover
import connectors.FakeDataCacheConnector
import controllers.actions.{DataRetrievalAction, FakeDataRetrievalAction}
import forms.{turnoverForm, vatReturnPeriodForm}
import helpers.ControllerSpecBase
import helpers.ViewSpecHelpers.TurnoverViewMessages
import models.ReturnPeriod
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import play.api.data.Form
import play.api.http.Status
import play.api.libs.json.{JsNumber, JsString}
import play.api.test.Helpers._
import utils.CacheMap
import views.html.errors.technicalError
import views.html.home.turnover

import scala.concurrent.ExecutionContext.Implicits.global

class TurnoverControllerSpec extends ControllerSpecBase with TurnoverViewMessages {

  val view               = application.injector.instanceOf[turnover]
  val technicalErrorView = application.injector.instanceOf[technicalError]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new TurnoverController(mcc, FakeDataCacheConnector, dataRetrievalAction, view, technicalErrorView)

  def viewAsString(form: Form[_] = turnoverForm(), period: String) = view(form, period)(fakeRequest, messages).toString

  def returnPeriodAnswer(index: Int = 0) = Map("vatReturnPeriod" -> JsString(vatReturnPeriodForm.options(index).value))

  "the question has previously not been answered" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer())))
    lazy val result     = controller(getRelevantData).onPageLoad()(fakeRequest)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "return the correct view" in {
      contentAsString(result) shouldBe viewAsString(period = ReturnPeriod.ANNUALLY.toString)
    }
  }

  "the question has previously been answered" must {
    val getRelevantData = new FakeDataRetrievalAction(
      Some(
        CacheMap(
          cacheMapId,
          Map(
            "vatReturnPeriod" -> JsString(vatReturnPeriodForm.options(0).value),
            "turnover"        -> JsNumber(BigDecimal(1000.00))
          )
        )
      )
    )
    lazy val result = controller(getRelevantData).onPageLoad()(fakeRequest)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "return the correct view" in {
      contentAsString(result) shouldBe viewAsString(
        turnoverForm().fill(BigDecimal(1000.00)),
        ReturnPeriod.ANNUALLY.toString
      )
    }
  }

  "show the correct content" must {
    "show the content for annual" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer(0))))
      lazy val result     = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) should include(turnoverHeading("year"))
    }
    "show the content for quarterly" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer(1))))
      lazy val result     = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) should include(turnoverHeading("quarter"))
    }
  }

  "valid data is submitted" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer())))
    val postRequest     = fakeRequest.withFormUrlEncodedBody(("turnover", "1000.00")).withMethod("POST")
    val result          = controller(getRelevantData).onSubmit()(postRequest)

    "return 303" in {
      status(result) shouldBe SEE_OTHER
    }
    "redirect to the cost of goods page" in {
      redirectLocation(result) shouldBe Some(s"${controllers.routes.CostOfGoodsController.onPageLoad}")
    }
  }

  "not entering any data" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer())))
    val postRequest     = fakeRequest.withFormUrlEncodedBody(("turnover", "")).withMethod("POST")
    val result          = controller(getRelevantData).onSubmit()(postRequest)

    "return 400" in {
      status(result) shouldBe BAD_REQUEST
    }
    "fail with the correct error message" in {
      contentAsString(result) should include(turnoverError("year"))
    }
  }

  "entering a negative number" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer())))
    val postRequest     = fakeRequest.withFormUrlEncodedBody(("turnover", "-1000.00")).withMethod("POST")
    val result          = controller(getRelevantData).onSubmit()(postRequest)

    "return 400" in {
      status(result) shouldBe BAD_REQUEST
    }
    "fail with the correct error message" in {
      contentAsString(result) should include(turnoverNegativeError)
    }
  }

  "entering a number with more than 2 decimal places" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer())))
    val postRequest     = fakeRequest.withFormUrlEncodedBody(("turnover", "0.001")).withMethod("POST")
    val result          = controller(getRelevantData).onSubmit()(postRequest)

    "return 400" in {
      status(result) shouldBe BAD_REQUEST
    }
    "fail with the correct error message" in {
      contentAsString(result) should include(turnoverDecimalError)
    }
  }

  "entering a number greater than the max limit" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, returnPeriodAnswer(0))))
    val postRequest =
      fakeRequest.withFormUrlEncodedBody(("turnover", (maximumTurnover + 1).toString)).withMethod("POST")
    val result = controller(getRelevantData).onSubmit()(postRequest)

    "return 400" in {
      status(result) shouldBe BAD_REQUEST
    }
    "fail with the correct error message" in {
      contentAsString(result) should include(turnoverMaxError)
    }
  }

  "loading turnover when no return period is given" must {
    lazy val result = controller().onPageLoad()(fakeRequest)

    "return 303" in {
      status(result) shouldBe SEE_OTHER
    }

    "redirect to the starting page" in {
      redirectLocation(result) shouldBe Some(s"${controllers.routes.VatReturnPeriodController.onPageLoad}")
    }
  }

  "submitting turnover when no return period is given" must {
    val postRequest = fakeRequest.withFormUrlEncodedBody(("turnover", "1000.00")).withMethod("POST")
    lazy val result = controller().onSubmit()(postRequest)

    "return 500" in {
      status(result) shouldBe INTERNAL_SERVER_ERROR
    }
  }

}
