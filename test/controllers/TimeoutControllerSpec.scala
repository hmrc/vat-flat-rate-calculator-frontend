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

import helpers.ControllerTestSpec
import org.jsoup.Jsoup
import org.scalatest.Matchers.convertToAnyShouldWrapper
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, defaultAwaitTimeout, status}
import views.html.fallback.timeout

class TimeoutControllerSpec extends ControllerTestSpec {

  class Setup {
    lazy val timeoutView = fakeApplication.injector.instanceOf[timeout]
    val controller = new TimeoutController(mcc, timeoutView)
  }

  "Calling the .timeout action" must {

    "return 200" in new Setup {
      lazy val request = FakeRequest("GET", "/")
      lazy val result = controller.timeout(request)
      status(result) shouldBe Status.OK
    }

    "navigate to the timeout page" in new Setup {
      lazy val request = FakeRequest("GET", "/")
      lazy val result = controller.timeout(request)
      val futureResult = await(result)
      Jsoup.parse(bodyOf(futureResult)).title shouldBe messages(s"""${messages("timeout.title")} - ${messages("service.name")} - GOV.UK""")
    }
  }

}
