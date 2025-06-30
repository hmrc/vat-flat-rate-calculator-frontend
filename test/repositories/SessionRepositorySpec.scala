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

package repositories

import config.ApplicationConfig
import connectors.DataCacheConnectorImpl
import helpers.SpecBase
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.mongo.test.{CleanMongoCollectionSupport, PlayMongoRepositorySupport}
import utils.CacheMap

import scala.concurrent.ExecutionContext.global
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class SessionRepositorySpec
    extends SpecBase
    with FutureAwaits
    with DefaultAwaitTimeout
    with PlayMongoRepositorySupport[DatedCacheMap]
    with CleanMongoCollectionSupport {

  implicit val ec: ExecutionContext = global

  protected def checkTtlIndex: Boolean                 = true
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = 30.seconds, interval = 100.millis)

  lazy val repository: MongoRepository = new MongoRepository(
    config = app.injector.instanceOf[ApplicationConfig],
    mongo = mongoComponent
  )

  lazy val mockDataCacheConnector = app.injector.instanceOf[DataCacheConnectorImpl]

  val userId        = "user-id"
  val anotherUserId = "another-user-id"

  lazy val costOfGoodsRecord: CacheMap = CacheMap(userId, Map("costOfGoods" -> Json.toJson("")))
  lazy val turnoverRecord: CacheMap    = CacheMap(anotherUserId, Map("turnover" -> Json.toJson("")))

  "SessionRepository" must {
    "return None when the repository is empty" in {
      await(repository.get(anotherUserId)) mustBe None
    }
    "populate the repository correctly" in {
      await(repository.upsert(costOfGoodsRecord)) mustBe true
      await(repository.get(userId)) mustBe Some(costOfGoodsRecord)

      await(repository.get(anotherUserId)) mustBe None
    }
    "populate the repository correctly with multiple records" in {
      await(repository.upsert(costOfGoodsRecord)) mustBe true
      await(repository.upsert(turnoverRecord)) mustBe true

      await(repository.get(userId)) mustBe Some(costOfGoodsRecord)
      await(repository.get(anotherUserId)) mustBe Some(turnoverRecord)
    }
  }

}
