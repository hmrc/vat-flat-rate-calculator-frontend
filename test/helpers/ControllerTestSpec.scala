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

package helpers

import java.nio.charset.Charset

import akka.actor.TypedActor.dispatcher
import akka.stream.Materializer
import akka.util.ByteString
import config.{AppConfig, ApplicationConfig}
import controllers.predicates.ValidatedSession
import forms.VatFlatRateForm
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.i18n.{Lang, MessagesApi}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{MessagesControllerComponents, Result}
import services.StateService
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts._
import utils.FakeApplication
import views.html.layouts.GovUkTemplate

import scala.language.implicitConversions

trait ControllerTestSpec extends PlaySpec with MockitoSugar with FakeApplication {

  implicit val mat: Materializer = fakeApplication.injector.instanceOf[Materializer]
  //implicit val executionContext: ExecutionContext = executionContext
  implicit val lang: Lang = Lang("en")

  lazy val messages: MessagesApi = fakeApplication.injector.instanceOf[MessagesApi]
  lazy val mcc: MessagesControllerComponents = fakeApplication.injector.instanceOf[MessagesControllerComponents]
  lazy val mockConfig: AppConfig = fakeApplication.injector.instanceOf[AppConfig]
  lazy val mockApplicationConfig: ApplicationConfig = fakeApplication.injector.instanceOf[ApplicationConfig]
  lazy val mockValidatedSession: ValidatedSession = fakeApplication.injector.instanceOf[ValidatedSession]
  lazy val mockStateService: StateService = fakeApplication.injector.instanceOf[StateService]
  lazy val mockForm: VatFlatRateForm = fakeApplication.injector.instanceOf[VatFlatRateForm]
  val mockMCC: MessagesControllerComponents = fakeApplication.injector.instanceOf[MessagesControllerComponents]
  lazy val mockArticle = fakeApplication.injector.instanceOf[Article]
  lazy val headUi = fakeApplication.injector.instanceOf[HeadWithTrackingConsent]
  lazy val govUkTemplate = fakeApplication.injector.instanceOf[GovUkTemplate]

  lazy val header_nav = fakeApplication.injector.instanceOf[HeaderNav]
  lazy val footer = fakeApplication.injector.instanceOf[Footer]
  lazy val uiServiceInfo = fakeApplication.injector.instanceOf[ServiceInfo]
  lazy val reportAProblemLink = fakeApplication.injector.instanceOf[ReportAProblemLink]
  lazy val main_content = fakeApplication.injector.instanceOf[MainContent]
  lazy val main_content_header = fakeApplication.injector.instanceOf[MainContentHeader]
  lazy val footerLinks = fakeApplication.injector.instanceOf[FooterLinks]

  lazy val uiSidebar = fakeApplication.injector.instanceOf[Sidebar]
  lazy val uiInputGroup = fakeApplication.injector.instanceOf[InputRadioGroup]
  lazy val uiform = fakeApplication.injector.instanceOf[FormWithCSRF]
  lazy val uiErrorSummary = fakeApplication.injector.instanceOf[ErrorSummary]


  import scala.concurrent.duration._
  import scala.concurrent.{Await, Future}

  implicit val defaultTimeout: FiniteDuration = 5 seconds

  implicit def extractAwait[A](future: Future[A]): A = await[A](future)

  def await[A](future: Future[A])(implicit timeout: Duration): A = Await.result(future, timeout)

  // Convenience to avoid having to wrap andThen() parameters in Future.successful
  implicit def liftFuture[A](v: A): Future[A] = Future.successful(v)

  def status(of: Result): Int = of.header.status

  def status(of: Future[Result])(implicit timeout: Duration): Int = status(Await.result(of, timeout))

  def jsonBodyOf(result: Result)(implicit mat: Materializer): JsValue = {
    Json.parse(bodyOf(result))
  }

  def jsonBodyOf(resultF: Future[Result])(implicit mat: Materializer): Future[JsValue] = {
    resultF.map(jsonBodyOf)
  }

  def bodyOf(result: Result)(implicit mat: Materializer): String = {
    val bodyBytes: ByteString = await(result.body.consumeData)
    bodyBytes.decodeString(Charset.defaultCharset().name)
  }

  def bodyOf(resultF: Future[Result])(implicit mat: Materializer): Future[String] = {
    resultF.map(bodyOf)
  }
}

