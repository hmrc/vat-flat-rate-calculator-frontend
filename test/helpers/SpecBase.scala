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

package helpers

import config.ApplicationConfig
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.Injector
import play.api.mvc.{AnyContent, MessagesControllerComponents}
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderNames

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite {

  implicit val application: Application = app

  def injector: Injector = app.injector

  def frontendAppConfig: ApplicationConfig = injector.instanceOf[ApplicationConfig]

  def mcc: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  def fakeRequest: FakeRequest[AnyContent] =
    FakeRequest("", "").withHeaders(HeaderNames.xSessionId -> "test-session-id")

  def fakeRequestWithoutSession: FakeRequest[AnyContent] = FakeRequest("", "")

  def lang: Lang = Lang("en")

  def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit def messages: Messages = messagesApi.preferred(fakeRequest)
}
