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

package controllers.actions

import connectors.DataCacheConnector
import helpers.SpecBase
import models.OptionalDataRequest
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import org.scalatest.RecoverMethods
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Request
import uk.gov.hmrc.http.SessionKeys
import utils.CacheMap

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}

class DataRetrievalActionSpec extends SpecBase with MockitoSugar with ScalaFutures with RecoverMethods {

  implicit val ec: ExecutionContext = global

  class Harness(dataCacheConnector: DataCacheConnector)
      extends DataRetrievalActionImpl(dataCacheConnector, mcc, frontendAppConfig) {
    def callTransform[A](request: Request[A]): Future[OptionalDataRequest[A]] = transform(request)
  }

  "Data Retrieval Action" when {

    "there is no data in the cache" must {
      "set userAnswers to 'None' in the request" in {
        val dataCacheConnector = mock[DataCacheConnector]
        when(dataCacheConnector.fetch(ArgumentMatchers.any())).thenReturn(Future(None))
        val action = new Harness(dataCacheConnector)

        val futureResult = action.callTransform(fakeRequest.withSession(SessionKeys.sessionId -> "id"))

        whenReady(futureResult)(result => result.userAnswers.isEmpty mustBe true)
      }
    }

    "there is data in the cache" must {
      "build a userAnswers object and add it to the request" in {
        val dataCacheConnector = mock[DataCacheConnector]
        when(dataCacheConnector.fetch(ArgumentMatchers.any())).thenReturn(Future(Some(new CacheMap("id", Map()))))
        val action = new Harness(dataCacheConnector)

        val futureResult = action.callTransform(fakeRequest.withSession(SessionKeys.sessionId -> "id"))

        whenReady(futureResult)(result => result.userAnswers.isDefined mustBe true)
      }
    }

    "there is no session Id in the request" must {
      "throw an exception" in {
        val dataCacheConnector = mock[DataCacheConnector]
        val action             = new Harness(dataCacheConnector)

        recoverToSucceededIf[IllegalStateException] {
          action.callTransform(fakeRequestWithoutSession)
        }
        application.stop()
      }
    }
  }

}
