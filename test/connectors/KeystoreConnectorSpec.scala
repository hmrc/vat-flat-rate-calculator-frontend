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

package connectors

import java.util.UUID
import config.ApplicationConfig
import helpers.ControllerTestSpec
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatest.Matchers.convertToAnyShouldWrapper
import play.api.libs.json.Json
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future
import uk.gov.hmrc.http.{HeaderCarrier, SessionId}

import scala.concurrent.ExecutionContext.Implicits.global

class KeystoreConnectorSpec extends ControllerTestSpec {

  lazy val sessionId: String = UUID.randomUUID.toString
  lazy val config: ApplicationConfig = mock[ApplicationConfig]
  lazy val vfrSessionCache: VfrSessionCache = mock[VfrSessionCache]

  lazy implicit val hc: HeaderCarrier = HeaderCarrier(sessionId = Some(SessionId(sessionId.toString)))

  lazy val target = new KeystoreConnector(config, vfrSessionCache)

  "KeystoreConnector .fetchFormData" must {

    val testData = Some("hello")

    when(vfrSessionCache.fetchAndGetEntry[String](ArgumentMatchers.eq("String"))(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(testData))

    "should be able to retrieve a String" in {
      lazy val result = target.fetchAndGetFormData[String]("String")
      await(result) shouldBe Some("hello")
    }
  }

  "KeystoreConnector .saveFormData" must {
    val testData = "hello"
    val returnedCacheMap = CacheMap("key", Map("data" -> Json.toJson(testData)))

    when(vfrSessionCache.cache[String](ArgumentMatchers.anyString(), ArgumentMatchers.anyString())(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(returnedCacheMap))

    "save data to keystore" in {
      lazy val result = target.saveFormData[String]("key", testData)

      await(result) shouldBe returnedCacheMap
    }
  }
}
