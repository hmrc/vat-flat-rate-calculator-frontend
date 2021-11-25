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

package views.home

import config.{AppConfig, ApplicationConfig}
import forms.VatFlatRateForm
import helpers.ViewSpecHelpers.VatReturnPeriodViewMessages
import models.UIHelpersWrapper
import org.jsoup.Jsoup
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.Injector
import play.api.test.FakeRequest
import views.html.home.vatReturnPeriod
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts.{Article, Footer, FooterLinks, HeadWithTrackingConsent, HeaderNav, MainContent, MainContentHeader, ServiceInfo, Sidebar}
import views.html.layouts.GovUkTemplate

class VatReturnPeriodViewSpec extends PlaySpec with GuiceOneAppPerSuite with VatReturnPeriodViewMessages {

  implicit lazy val fakeRequest = FakeRequest()
  def injector: Injector = app.injector
  def appConfig: AppConfig = injector.instanceOf[AppConfig]
  lazy val mockForm: VatFlatRateForm = injector.instanceOf[VatFlatRateForm]
  lazy val mockArticle = injector.instanceOf[Article]
  lazy val headUi = injector.instanceOf[HeadWithTrackingConsent]
  lazy val govUkTemplate = injector.instanceOf[GovUkTemplate]

  lazy val header_nav = injector.instanceOf[HeaderNav]
  lazy val footer = injector.instanceOf[Footer]
  lazy val uiServiceInfo = injector.instanceOf[ServiceInfo]
  lazy val reportAProblemLink = injector.instanceOf[ReportAProblemLink]
  lazy val main_content = injector.instanceOf[MainContent]
  lazy val main_content_header = injector.instanceOf[MainContentHeader]
  lazy val footerLinks = injector.instanceOf[FooterLinks]

  lazy val uiSidebar = injector.instanceOf[Sidebar]
  lazy val uiInputGroup = injector.instanceOf[InputRadioGroup]
  lazy val uiform = injector.instanceOf[FormWithCSRF]
  lazy val uiErrorSummary = injector.instanceOf[ErrorSummary]
  lazy val vatReturnPeriodView = injector.instanceOf[vatReturnPeriod]

  val mockConfig = fakeApplication.injector.instanceOf[ApplicationConfig]
  implicit lazy val mockMessage = fakeApplication.injector.instanceOf[MessagesControllerComponents].messagesApi.preferred(fakeRequest)

  val Period = "annually"


  "the VatReturnPeriod" must {
    lazy val VatReturnPeriodForm = mockForm.vatReturnPeriodForm.bind(Map("vatReturnPeriod" -> "annually",
      "turnover" -> "1000",
      "costOfGoods" -> "100"))
    lazy val view = vatReturnPeriodView(VatReturnPeriodForm)
    lazy val doc = Jsoup.parse(view.body)

    lazy val errorVatReturnPeriodForm = mockForm.vatReturnPeriodForm.bind(Map("" -> ""))
    lazy val errorView = vatReturnPeriodView(errorVatReturnPeriodForm)
    lazy val errorDoc = Jsoup.parse(errorView.body)


    "have the correct title" in {
      doc.title() shouldBe vatReturnPeriodTitle
    }

    "have the correct heading" in {
      doc.select("h1").text() shouldBe  vatReturnPeriodHeading
    }

    "have some introductory text" in {
      doc.select("div > p").eq(1).text shouldBe vatReturnPeriodIntro
    }

    "have a paragraph text" in {
      doc.select("div > p").eq(2).text shouldBe vatReturnPeriodPara
    }

    "have a 'annually' label on radio button" in {
      doc.select("label").first().text shouldBe vatReturnPeriodAnnually
    }

    "have a 'quarterly' label on radio button" in {
      doc.select("label").eq(1).text shouldBe vatReturnPeriodQuarterly
    }

    "display the correct error" in {
      errorVatReturnPeriodForm.hasErrors shouldBe true
      errorDoc.select("span.error-notification").eq(0).text shouldBe vatReturnPeriodError
    }

    "have a continue button" in{
      doc.select("button").text shouldBe vatReturnPeriodContinue
      doc.select("button").attr("type") shouldBe "submit"
    }

    "have a valid form" in{
      doc.select("form").attr("method") shouldBe "POST"
      doc.select("form").attr("action") shouldBe controllers.routes.VatReturnPeriodController.submitVatReturnPeriod().url
    }
  }

}
