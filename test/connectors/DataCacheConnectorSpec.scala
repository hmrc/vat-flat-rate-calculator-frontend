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

package connectors

import helpers.SpecBase
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import repositories.SessionRepository
import utils.CacheMap

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}

class DataCacheConnectorSpec extends SpecBase with FutureAwaits with DefaultAwaitTimeout {

  implicit val ec: ExecutionContext = global

  def remove(cacheId: String, key: String): Future[Boolean] =
    mockSessionRepository().get(cacheId).flatMap { optionalCacheMap =>
      optionalCacheMap.fold(Future(false)) { cacheMap =>
        val newCacheMap = cacheMap.copy(data = cacheMap.data - key)
        mockSessionRepository().upsert(newCacheMap)
      }
    }

  lazy val mockDataCacheConnector: DataCacheConnector = app.injector.instanceOf[DataCacheConnector]
  lazy val mockSessionRepository: SessionRepository   = app.injector.instanceOf[SessionRepository]

  val userId        = "user-id"
  val anotherUserId = "another-user-id"

  lazy val costOfGoodsRecord: CacheMap = CacheMap(userId, Map("costOfGoods" -> Json.toJson("costOfGoodsValue")))
  lazy val turnoverRecord: CacheMap    = CacheMap(userId, Map("turnover" -> Json.toJson("turnoverValue")))

  lazy val storedCache: CacheMap =
    CacheMap(userId, Map("costOfGoods" -> Json.toJson("costOfGoodsValue"), "turnover" -> Json.toJson("turnoverValue")))

  "DataCacheConnector" should {
    "save a CacheMap object" in {
      await(mockDataCacheConnector.save(userId, "costOfGoods", "costOfGoodsValue")) mustBe costOfGoodsRecord
      await(remove(userId, "costOfGoods")) mustBe true
    }

    "save multiple CacheMap objects" in {
      await(mockDataCacheConnector.save(userId, "costOfGoods", "costOfGoodsValue")) mustBe costOfGoodsRecord
      await(mockDataCacheConnector.save(userId, "turnover", "turnoverValue")) mustBe storedCache

      await(remove(userId, "costOfGoods")) mustBe true
      await(remove(userId, "turnover")) mustBe true
    }

    "fetch optional cache using id" in {
      await(mockDataCacheConnector.save(userId, "costOfGoods", "costOfGoodsValue")) mustBe costOfGoodsRecord
      await(mockDataCacheConnector.fetch(userId)) mustBe Some(costOfGoodsRecord)

      await(remove(userId, "costOfGoods")) mustBe true
    }

    "return None for a missing user session" in {
      await(mockDataCacheConnector.fetch(anotherUserId)) mustBe None
    }
  }

}
