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

import connectors.FakeDataCacheConnector
import controllers.actions.{DataRetrievalAction, FakeDataRetrievalAction}
import forms.vatReturnPeriodForm
import helpers.ControllerSpecBase
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import play.api.libs.json.{JsNumber, JsString}
import play.api.test.Helpers._
import utils.CacheMap
import views.html.home.result

class ResultControllerSpec extends ControllerSpecBase {

  val view = application.injector.instanceOf[result]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new ResultController(mcc, dataRetrievalAction, view)

  def viewAsString(resultCode: Int, showUserResearchPanel: Boolean) =
    view(resultCode, showUserResearchPanel)(fakeRequest, messages).toString

  def createAnswers(index: Int, turnover: Double, costofGoods: Double) =
    Map(
      "vatReturnPeriod" -> JsString(vatReturnPeriodForm.options(index).value),
      "turnover"        -> JsNumber(BigDecimal(turnover)),
      "costOfGoods"     -> JsNumber(BigDecimal(costofGoods))
    )

  "the previous question have not been answered" must {
    lazy val result = controller().onPageLoad()(fakeRequest)

    "return 303" in {
      status(result) shouldBe SEE_OTHER
    }

    "redirect to beginning of journey" in {
      redirectLocation(result) shouldBe Some(s"${controllers.routes.VatReturnPeriodController.onPageLoad}")
    }
  }

  "the previous questions have been answered" must {
    val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(0, 1000.00, 1000.00))))
    val result          = controller(getRelevantData).onPageLoad()(fakeRequest)

    "return 200" in {
      status(result) shouldBe OK
    }

    "return the results view" in {
      contentAsString(result) should include(
        messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
      )
    }
  }

  "show the correct results" must {
    "show result code 1" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(0, 2000, 500))))
      val result          = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) shouldBe viewAsString(1, false)
    }
    "show result code 2" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(0, 50001, 1000))))
      val result          = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) shouldBe viewAsString(2, false)
    }
    "show result code 3" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(0, 5000, 1000))))
      val result          = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) shouldBe viewAsString(3, false)
    }
    "show result code 4" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(1, 2000, 100))))
      val result          = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) shouldBe viewAsString(4, false)
    }
    "show result code 5" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(1, 12501, 250))))
      val result          = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) shouldBe viewAsString(5, false)
    }
    "show result code 6" in {
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, createAnswers(1, 12500, 250))))
      val result          = controller(getRelevantData).onPageLoad()(fakeRequest)
      contentAsString(result) shouldBe viewAsString(6, false)
    }
  }

}
