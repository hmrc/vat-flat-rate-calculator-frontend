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

import javax.inject.{Inject, Singleton}
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Indexes.ascending
import org.mongodb.scala.model.{IndexModel, IndexOptions, ReplaceOptions}
import play.api.libs.json.{Format, JsValue, Json, OFormat}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats
import uk.gov.hmrc.mongo.play.json.{Codecs, PlayMongoRepository}
import utils.CacheMap

import java.time.Instant
import scala.concurrent.duration.SECONDS
import scala.concurrent.{ExecutionContext, Future}

case class DatedCacheMap(id: String,
                         data: Map[String, JsValue],
                         lastUpdated: Instant = Instant.now())

object DatedCacheMap {
  implicit val dateFormat: Format[Instant] = MongoJavatimeFormats.instantFormat
  implicit val formats: OFormat[DatedCacheMap] = Json.format[DatedCacheMap]

  def apply(cacheMap: CacheMap): DatedCacheMap = DatedCacheMap(cacheMap.id, cacheMap.data)
}

class MongoRepository(config: ApplicationConfig, mongo: MongoComponent)(implicit ec: ExecutionContext)
  extends PlayMongoRepository[DatedCacheMap](
    collectionName = config.appName,
    mongoComponent = mongo,
    domainFormat = DatedCacheMap.formats,
    indexes = Seq(
      IndexModel(ascending("lastUpdated"), IndexOptions()
        .name("userAnswersExpiry")
        .expireAfter(config.cacheTtl.toLong, SECONDS))
    )
    , extraCodecs = Seq(Codecs.playFormatCodec(CacheMap.formats))
  ) {

  def upsert(cm: CacheMap): Future[Boolean] = {
    val dcm = DatedCacheMap(cm)
    collection.replaceOne(
      filter      = equal("id", dcm.id),
      replacement = dcm,
      options     = ReplaceOptions().upsert(true)
    ).toFuture().map(_.wasAcknowledged())
  }

  def get(id: String): Future[Option[CacheMap]] =
    collection.find[CacheMap](and(equal("id", id))).headOption()

}

@Singleton
class SessionRepository @Inject()(config: ApplicationConfig, mongoComponent: MongoComponent)(implicit ec: ExecutionContext) {

  private lazy val sessionRepository = new MongoRepository(config, mongoComponent)

  def apply(): MongoRepository = sessionRepository
}

