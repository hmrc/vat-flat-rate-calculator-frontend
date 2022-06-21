/*
 * Copyright 2022 HM Revenue & Customs
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
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.Matchers.convertToAnyShouldWrapper
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.StateService

import scala.concurrent.Future
import uk.gov.hmrc.http.SessionKeys
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.errors.technicalError
import views.html.home.turnover

class TurnoverControllerSpec extends ControllerTestSpec {

  lazy val testMockStateService = mock[StateService]

  def createTestController(): TurnoverController = {
    lazy val turnoverView = fakeApplication.injector.instanceOf[turnover]
    lazy val technicalErrorView = fakeApplication.injector.instanceOf[technicalError]
    object TestController extends TurnoverController(mcc, testMockStateService,
      mockValidatedSession, mockForm, turnoverView, technicalErrorView)
    TestController
  }

  def createMock(data: Option[VatFlatRateModel]): OngoingStubbing[Future[Option[VatFlatRateModel]]] ={
    when(testMockStateService.fetchVatFlatRate()(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(data))
  }


  "Calling the .turnover action" when {

    "there is no model in keystore" must {
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController()

      lazy val result = controller.turnover(request)

      "return 303" in {
        createMock(None)
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the landing page" in {
        createMock(None)
        redirectLocation(result) shouldBe Some(s"${routes.VatReturnPeriodController.vatReturnPeriod}")
      }
    }

    "Calling the .submitTurnover action with a badRequest and getting an Internal Server Error" when {

      "there is no model in keystore" must {
        lazy val request = FakeRequest("POST", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
          .withFormUrlEncodedBody(("turnover", ""))
        lazy val controller = createTestController()


        lazy val result = controller.submitTurnover(request)

        "return 500" in {
          createMock(None)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }
//
    "there is an annual model in keystore" when {

      val data = Some(VatFlatRateModel("annually", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController()

      lazy val result = controller.turnover(request)

      "return 200" in {
        createMock(data)
        status(result) shouldBe Status.OK
      }

      "navigate to the annual turnover page" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("turnover.title")} - ${messages("service.name")} - GOV.UK""")
      }

    }

    "there is a quarterly model in keystore" when {

      val data = Some(VatFlatRateModel("quarterly", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController()
      lazy val result = controller.turnover(request)

      "return 200" in {
        createMock(data)
        status(result) shouldBe Status.OK
      }

      "navigate to the quarterly turnover page" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("turnover.title")} - ${messages("service.name")} - GOV.UK""")
      }
    }

    "there is an incorrect model in keystore" must {

      val data = Some(VatFlatRateModel("wrong-model", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController()
      lazy val result = controller.turnover(request)

      "return 500" in {
        createMock(data)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "show the technical error page" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("techError.title")} - ${messages("service.name")} - GOV.UK""")
      }
    }
  }

  "Calling the .submitTurnover action" when {

    "not entering any data" must {
      val data = Some(VatFlatRateModel("annually", None, None))
      lazy val request = FakeRequest()
          .withSession(SessionKeys.sessionId -> s"any-old-id")
        .withFormUrlEncodedBody(("turnover", ""))
      lazy val controller = createTestController()
      lazy val result = controller.submitTurnover(request)

      "return 400" in {
        createMock(data)
        status(result) shouldBe Status.BAD_REQUEST
      }
      "fail with the correct error message" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).getElementsByClass("govuk-error-message").text should include(messages("error.turnover.required"))
      }
    }

    "submitting a valid turnover" when {
      lazy val request = FakeRequest()
        .withSession(SessionKeys.sessionId -> s"any-old-id")
        .withFormUrlEncodedBody(("vatReturnPeriod","annually"),("turnover", "10000"))
        .withMethod(POST)
      lazy val controller = createTestController()
      lazy val result = controller.submitTurnover(request)


      "return 303" in {
        when(testMockStateService.saveVatFlatRate(ArgumentMatchers.any())(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
          .thenReturn(Future.successful(CacheMap("testId", Map())))

        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the cost of goods page" in {
        redirectLocation(result) shouldBe Some(s"${routes.CostOfGoodsController.costOfGoods}")
      }
    }

  }
}
