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

import config.ApplicationConfig
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Format
import uk.gov.hmrc.http._
import uk.gov.hmrc.http.cache.client.{CacheMap, SessionCache}
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class KeystoreConnector @Inject()(appConfig: ApplicationConfig,
                                  sessionCache: VfrSessionCache) {

  implicit val hc: HeaderCarrier = HeaderCarrier().withExtraHeaders("Accept" -> "applications/vnd.hmrc.1.0+json")

  def saveFormData[T](key: String, data: T)(implicit hc: HeaderCarrier, ec: ExecutionContext, formats: Format[T]): Future[CacheMap] = {
    sessionCache.cache(key, data)
  }

  def fetchAndGetFormData[T](key: String)(implicit hc: HeaderCarrier, ec: ExecutionContext, formats: Format[T]): Future[Option[T]] = {
    sessionCache.fetchAndGetEntry(key)
  }
}

@Singleton
class VfrSessionCache @Inject()(val http: DefaultHttpClient, appConfig: ApplicationConfig) extends SessionCache {
  override lazy val domain: String = appConfig.config.getConfString("cachable.session-cache.domain", throw new Exception(""))
  override lazy val baseUri: String = appConfig.config.baseUrl("cachable.session-cache")
  override lazy val defaultSource: String = "vat-flat-rate-calculator-frontend"
}

