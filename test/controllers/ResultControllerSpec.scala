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
import models.{ResultModel, VatFlatRateModel}
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import org.scalatest.Matchers.convertToAnyShouldWrapper
import play.api.http.Status
import play.api.test.FakeRequest
import services.StateService
import play.api.test.Helpers._

import scala.concurrent.Future
import uk.gov.hmrc.http.SessionKeys
import views.html.home.result


class ResultControllerSpec extends ControllerTestSpec {

  def createTestController(data: Option[ResultModel]): ResultController = {

    lazy val resultView = fakeApplication.injector.instanceOf[result]
    object TestResultController extends ResultController(mcc, createMockStateService(), mockValidatedSession, resultView)
    def createMockStateService(): StateService = {
      val mockStateService = mock[StateService]

      when(mockStateService.fetchResultModel()(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
        .thenReturn(Future.successful(data))

      mockStateService
    }
    TestResultController
  }

  "Navigating to the result page without a model in keystore" must  {
    val data = None
    lazy val controller = createTestController(data)
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")

    lazy val result = controller.result(request)

    "redirect to beginning of journey" in {
      status(result) shouldBe 303
    }

    "navigate to the Enter VAT return page" in {
      redirectLocation(result) shouldBe Some(routes.VatReturnPeriodController.vatReturnPeriod().url)
    }
  }

  "Navigating to the result page with a model in keystore, resultCode 1" must {
    val data = Some(ResultModel(VatFlatRateModel("annually", Some(2000), Some(500)), 1))
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")
    lazy val controller = createTestController(data)
    lazy val result = controller.result(request)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "navigate to the result page" in {
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

  "Navigating to the result page with a model in keystore, resultCode 2" must {
    val data = Some(ResultModel(VatFlatRateModel("annually", Some(50001), Some(1000)), 2))
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")
    lazy val controller = createTestController(data)
    lazy val result = controller.result(request)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "navigate to the result page" in {
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

  "Navigating to the result page with a model in keystore, resultCode 3" must {
    val data = Some(ResultModel(VatFlatRateModel("annually", Some(5000), Some(1000)), 3))
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")
    lazy val controller = createTestController(data)
    lazy val result = controller.result(request)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "navigate to the result page" in {
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

  "Navigating to the result page with a model in keystore, resultCode 4" must {
    val data = Some(ResultModel(VatFlatRateModel("quarterly", Some(2000), Some(100)), 4))
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")
    lazy val controller = createTestController(data)
    lazy val result = controller.result(request)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "navigate to the result page" in {
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

  "Navigating to the result page with a model in keystore, resultCode 5" must {
    val data = Some(ResultModel(VatFlatRateModel("quarterly", Some(12501), Some(250)), 5))
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")
    lazy val controller = createTestController(data)
    lazy val result = controller.result(request)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "navigate to the result page" in {
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

  "Navigating to the result page with a model in keystore, resultCode 6" must {
    val data = Some(ResultModel(VatFlatRateModel("quarterly", Some(12500), Some(250)), 6))
    lazy val request = FakeRequest()
      .withSession(SessionKeys.sessionId -> s"any-old-id")
    lazy val controller = createTestController(data)
    lazy val result = controller.result(request)

    "return 200" in {
      status(result) shouldBe Status.OK
    }

    "navigate to the result page" in {
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("result.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

}
