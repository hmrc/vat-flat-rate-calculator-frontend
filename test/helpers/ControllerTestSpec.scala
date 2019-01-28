/*
 * Copyright 2019 HM Revenue & Customs
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

import akka.stream.Materializer
import config.{AppConfig, ApplicationConfig}
import controllers.predicates.ValidatedSession
import forms.VatFlatRateForm
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import play.api.i18n.MessagesApi
import play.api.inject.Injector
import services.StateService
import uk.gov.hmrc.play.test.UnitSpec

trait ControllerTestSpec extends UnitSpec with MockitoSugar with OneAppPerSuite {

  val injector: Injector = app.injector
  implicit val mat: Materializer = injector.instanceOf[Materializer]

  lazy val messages: MessagesApi = injector.instanceOf[MessagesApi]
  lazy val mockConfig: AppConfig = injector.instanceOf[AppConfig]
  lazy val mockApplicationConfig: ApplicationConfig = injector.instanceOf[ApplicationConfig]
  lazy val mockValidatedSession: ValidatedSession = injector.instanceOf[ValidatedSession]
  lazy val mockStateService: StateService = injector.instanceOf[StateService]
  lazy val mockForm: VatFlatRateForm = injector.instanceOf[VatFlatRateForm]

}
