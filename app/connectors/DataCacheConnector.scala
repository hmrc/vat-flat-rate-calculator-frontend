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

import com.google.inject.{ImplementedBy, Inject}
import play.api.libs.json.Format
import repositories.SessionRepository
import utils.{CacheMap, CascadeUpsert}

import scala.concurrent.{ExecutionContext, Future}

class DataCacheConnectorImpl @Inject()(
                                        val sessionRepository: SessionRepository,
                                        val cascadeUpsert: CascadeUpsert
                                      )(implicit ec: ExecutionContext) extends DataCacheConnector {

  def save[A](cacheId: String, key: String, value: A)(implicit fmt: Format[A]): Future[CacheMap] = {
    sessionRepository().get(cacheId).flatMap { optionalCacheMap =>
      val updatedCacheMap = cascadeUpsert(key, value, optionalCacheMap.getOrElse(new CacheMap(cacheId, Map())))
      sessionRepository().upsert(updatedCacheMap).map {_ => updatedCacheMap}
    }
  }

  def fetch(cacheId: String): Future[Option[CacheMap]] =
    sessionRepository().get(cacheId)
}

@ImplementedBy(classOf[DataCacheConnectorImpl])
trait DataCacheConnector {
  def save[A](cacheId: String, key: String, value: A)(implicit fmt: Format[A]): Future[CacheMap]

  def fetch(cacheId: String): Future[Option[CacheMap]]
}

