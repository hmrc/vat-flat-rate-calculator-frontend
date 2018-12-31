/*
 * Copyright 2018 HM Revenue & Customs
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

package config

import akka.actor.ActorSystem
import com.typesafe.config.Config
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Play}
import uk.gov.hmrc.http.cache.client.SessionCache
import uk.gov.hmrc.http.hooks.HttpHooks
import uk.gov.hmrc.http.{HttpDelete, HttpGet, HttpPost, HttpPut}
import uk.gov.hmrc.play.audit.http.HttpAuditing
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector => Auditing}
import uk.gov.hmrc.play.config.{AppName, ServicesConfig}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.frontend.config.LoadAuditingConfig
import uk.gov.hmrc.play.http.ws.{WSDelete, WSGet, WSPost, WSPut}


@Singleton
class FrontendAuditConnector @Inject()() extends Auditing with AppName with RunModeConfig {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

@Singleton
class FrontendAuthConnector @Inject()(override val http: WSHttp) extends AuthConnector with ServicesConfig with RunModeConfig {
  lazy val serviceUrl: String = baseUrl("auth")
}

object FrontendAuditConnector extends Auditing with AppName with RunModeConfig {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

trait Hooks extends HttpHooks with HttpAuditing {
  override val hooks = Seq(AuditingHook)
  override lazy val auditConnector: Auditing = FrontendAuditConnector
}

trait WSHttp extends HttpGet with WSGet with HttpPut with WSPut with HttpPost with WSPost with HttpDelete with WSDelete with Hooks with AppName

@Singleton class WSHttpImpl extends WSHttp {
  override protected def configuration: Option[Config] = Some(Play.current.configuration.underlying)

  override protected def appNameConfiguration: Configuration = Play.current.configuration

  override def actorSystem : ActorSystem = Play.current.actorSystem
}

@Singleton
class VfrSessionCache @Inject()(val http: WSHttp, appConfig: AppConfig) extends SessionCache with ServicesConfig with AppName with RunModeConfig {
  override lazy val domain: String = getConfString("cachable.session-cache.domain", throw new Exception(""))
  override lazy val baseUri: String = baseUrl("cachable.session-cache")
  override lazy val defaultSource: String = appName
}

