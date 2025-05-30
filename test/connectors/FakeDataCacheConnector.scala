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

import play.api.libs.json.Format
import utils.CacheMap

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FakeDataCacheConnector extends DataCacheConnector {

  override def save[A](cacheId: String, key: String, value: A)(implicit fmt: Format[A]): Future[CacheMap] = Future(
    CacheMap(cacheId, Map())
  )

  override def fetch(cacheId: String): Future[Option[CacheMap]] = Future(Some(CacheMap(cacheId, Map())))

}
