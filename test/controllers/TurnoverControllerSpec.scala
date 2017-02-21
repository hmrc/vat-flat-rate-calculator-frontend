/*
 * Copyright 2017 HM Revenue & Customs
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

import helpers.ControllerTestSpec
import models.VatFlatRateModel
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import play.api.http.Status
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.StateService
import uk.gov.hmrc.play.http.SessionKeys

import scala.concurrent.Future

class TurnoverControllerSpec extends ControllerTestSpec {

  def createTestController(data: Option[VatFlatRateModel]): TurnoverController = {
    object TestController extends TurnoverController(mockConfig, messages, createMockStateService(), mockValidatedSession, mockForm)

    def createMockStateService(): StateService = {

      val mockStateService = mock[StateService]
      when(mockStateService.fetchVatFlatRate()(ArgumentMatchers.any(), ArgumentMatchers.any()))
        .thenReturn(Future.successful(data))

      mockStateService
    }
    TestController
  }


  "Calling the .turnover action" when {

    "there is no model in keystore" should {
      val data = None
      lazy val controller = createTestController(data)
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val result = controller.turnover(request)

      "return 303" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the landing page" in {
        redirectLocation(result) shouldBe Some(s"${routes.VatReturnPeriodController.vatReturnPeriod()}")
      }
    }

    "there is an annual model in keystore" should {

      val data = Some(VatFlatRateModel("annually", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController(data)
      lazy val result = controller.turnover(request)

      "return 200" in {
        status(result) shouldBe Status.OK
      }

      "navigate to the annual turnover page" in {
        Jsoup.parse(bodyOf(result)).title shouldBe Messages("turnover.title")
      }

    }

    "there is a quarterly model in keystsore" should {

      val data = Some(VatFlatRateModel("quarterly", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController(data)
      lazy val result = controller.turnover(request)
      "return 200" in {
        status(result) shouldBe Status.OK
      }

      "navigate to the quarterly turnover page" in {
        Jsoup.parse(bodyOf(result)).title shouldBe messages("turnover.title")
      }
    }

    "there is an incorrect model in keystore" should {

      val data = Some(VatFlatRateModel("wrong-model", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController(data)
      lazy val result = controller.turnover(request)

      "return 500" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "show the technical error page" in {
        Jsoup.parse(bodyOf(result)).title shouldBe Messages("techError.title")
      }
    }
  }

  "Calling the .submitTurnover action" when {

    "not entering any data" should {
      val data = Some(VatFlatRateModel("annually", None, None))
      lazy val request = FakeRequest()
          .withSession(SessionKeys.sessionId -> s"any-old-id")
        .withFormUrlEncodedBody(("turnover", ""))
      lazy val controller = createTestController(data)
      lazy val result = controller.submitTurnover(request)

      "return 400" in {
        status(result) shouldBe Status.BAD_REQUEST
      }
      "fail with the correct error message" in {
        Jsoup.parse(bodyOf(result)).getElementsByClass("error-notification").text should include(messages("error.required"))
      }
    }

    "submitting a valid turnover" should {
      val data = Some(VatFlatRateModel("annually", None, None))
      lazy val request = FakeRequest()
        .withSession(SessionKeys.sessionId -> s"any-old-id")
        .withFormUrlEncodedBody(("vatReturnPeriod","annually"),("turnover", "10000"))
      lazy val controller = createTestController(data)
      lazy val result = controller.submitTurnover(request)


      "return 303" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the cost of goods page" in {
        redirectLocation(result) shouldBe Some(s"${routes.CostOfGoodsController.costOfGoods()}")
      }
    }

  }
}
