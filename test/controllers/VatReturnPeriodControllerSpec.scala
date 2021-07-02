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
import helpers.ControllerTestSpec
import models.VatFlatRateModel
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import org.scalatest.Matchers.convertToAnyShouldWrapper
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.StateService

import scala.concurrent.Future
import uk.gov.hmrc.http.SessionKeys
import uk.gov.hmrc.http.cache.client.CacheMap

class VatReturnPeriodControllerSpec extends ControllerTestSpec {

  lazy val testMockStateService = mock[StateService]

  def createTestController() = {
    object TestController extends VatReturnPeriodController(mockConfig, mcc, testMockStateService,
      mockValidatedSession, mockForm, mockArticle, headUi, govUkTemplate, header_nav,
      footer,uiServiceInfo, reportAProblemLink, main_content, main_content_header, footerLinks,
      uiSidebar, uiInputGroup, uiform, uiErrorSummary)
    TestController
  }

  "Navigating to the landing page" must {
    val sessionId =  UUID.randomUUID().toString

    "there is no sessionId" must {
      lazy val request = FakeRequest("GET", "/")
      lazy val controller = createTestController()
      lazy val result = controller.vatReturnPeriod(request)

      "generate a sessionId and continue" in {
        status(result) shouldBe Status.OK
      }
    }

    "there is no previous model in keystore" must {
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"sessionId-$sessionId")

      lazy val controller = createTestController()
      lazy val result = controller.vatReturnPeriod(request)

      "return 200 " in {
        when(testMockStateService.fetchVatFlatRate()(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
          .thenReturn(Future.successful(None))

        status(result) shouldBe Status.OK
      }

      "navigate to the turnover page" in {
        val resultCompleted = await(result)
        Jsoup.parse(bodyOf(resultCompleted)).title shouldBe messages("vatReturnPeriod.title")
      }
    }

    "there is a model in keystore" must {
      val data = Some(VatFlatRateModel("annually", None, None))
      lazy val request = FakeRequest("GET", "/").withSession(SessionKeys.sessionId -> s"sessionId-$sessionId")
      lazy val controller = createTestController()

      lazy val result = controller.vatReturnPeriod(request)

      "return 200 " in {
        when(testMockStateService.fetchVatFlatRate()(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
          .thenReturn(Future.successful(data))
        
        status(result) shouldBe Status.OK
      }
      "navigate to the turnover page" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).title shouldBe messages("vatReturnPeriod.title")
      }
    }
  }

  "Calling the .submitVatReturnPeriod action" must {

    "not entering any data" must {
      lazy val request = FakeRequest()
        .withSession(SessionKeys.sessionId -> s"any-old-id")
      lazy val controller = createTestController()
      lazy val result = controller.submitVatReturnPeriod(request)

      "return 400" in {
        status(result) shouldBe Status.BAD_REQUEST
      }
      "fail with the correct error message" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).getElementsByClass("error-notification").text should include(messages("error.vatReturnPeriod.required"))
      }
    }

    "entering invalid data" must {
      lazy val request = FakeRequest()
        .withSession(SessionKeys.sessionId -> s"any-old-id")
        .withFormUrlEncodedBody(
          "vatReturnPeriod" -> "annually",
          "turnover" -> "x")

      lazy val controller = createTestController()
      lazy val result: Future[Result] = controller.submitVatReturnPeriod(request)

      "return 400" in {
        status(result) shouldBe Status.BAD_REQUEST
      }
      "fail with the correct error message" in {
        val futureResult = await(result)
        Jsoup.parse(bodyOf(futureResult)).getElementsByClass("error-notification").text should include(messages(""))
      }
    }


    "submitting a valid Vat Return Period" must {
      when(testMockStateService.saveVatFlatRate(ArgumentMatchers.any())(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
        .thenReturn(Future.successful(CacheMap("testId", Map())))

      lazy val request = FakeRequest()
        .withSession(SessionKeys.sessionId -> s"any-old-id")
        .withFormUrlEncodedBody(
          "vatReturnPeriod" -> "annually"
        )

      lazy val controller = createTestController()
      lazy val result = controller.submitVatReturnPeriod(request)

      "return 303" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the cost of goods page" in {
        redirectLocation(result) shouldBe Some(s"${controllers.routes.TurnoverController.turnover()}")
      }
    }

  }

}