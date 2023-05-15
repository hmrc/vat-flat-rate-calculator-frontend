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

package controllers.predicates

import connectors.FakeDataCacheConnector
import controllers.{VatReturnPeriodController, routes}
import helpers.ControllerSpecBase
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.MessagesControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderNames
import views.html.home.vatReturnPeriod

import scala.concurrent.ExecutionContext.Implicits.global

class ValidatedSessionSpec extends ControllerSpecBase with MockitoSugar  {

  val vatReturnPeriodController = new VatReturnPeriodController(
    app.injector.instanceOf[MessagesControllerComponents],
    FakeDataCacheConnector,
    getEmptyCacheMap,
    app.injector.instanceOf[ValidatedSession],
    app.injector.instanceOf[vatReturnPeriod]
  )

  "ValidatedSession" must {
    "redirect to the timeout page" when {
      "the sessionId is missing" in {
        val result = vatReturnPeriodController.onPageLoad()(FakeRequest())
        status(result) shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(routes.TimeoutController.timeout.url)
      }
    }
    "navigate to the controller route" when {
      "the sessionId is present" in {
        val request = FakeRequest().withHeaders(HeaderNames.xSessionId -> "test-session-id")
        val result = vatReturnPeriodController.onPageLoad()(request)
        status(result) shouldBe OK
      }
    }
  }
}
