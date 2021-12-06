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
import helpers.ViewSpecHelpers.CostOfGoodsViewMessages
import models.UIHelpersWrapper
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import org.jsoup.Jsoup
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec
import play.api.Application
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{AnyContentAsEmpty, DefaultMessagesControllerComponents, MessagesControllerComponents}
import views.html.home.costOfGoods
import uk.gov.hmrc.play.views.html.helpers.{ErrorSummary, FormWithCSRF, InputRadioGroup, ReportAProblemLink}
import uk.gov.hmrc.play.views.html.layouts.{Article, Footer, FooterLinks, HeadWithTrackingConsent, HeaderNav, MainContent, MainContentHeader, ServiceInfo, Sidebar}
import views.html.layouts.GovUkTemplate

class CostOfGoodsViewSpec extends PlaySpec with GuiceOneAppPerSuite with CostOfGoodsViewMessages {

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
  lazy val costOfGoodsView = injector.instanceOf[costOfGoods]

  val mockConfig = fakeApplication.injector.instanceOf[ApplicationConfig]
  implicit lazy val mockMessage = fakeApplication.injector.instanceOf[MessagesControllerComponents].messagesApi.preferred(fakeRequest)

  val costOfGoodsPeriod = "annually"


  "the CostOfGoodsView" must {
    lazy val CostOfGoodsForm = mockForm.costOfGoodsForm.bind(Map("vatReturnPeriod" -> "annually",
                                                                 "turnover" -> "1000",
                                                               "costOfGoods" -> "100"))


    lazy val view = costOfGoodsView(CostOfGoodsForm, costOfGoodsPeriod)
    lazy val doc = Jsoup.parse(view.body)

    lazy val errorCostOfGoodsForm = mockForm.costOfGoodsForm.bind(Map("vatReturnPeriod" -> "annually"))

    lazy val errorView = costOfGoodsView(errorCostOfGoodsForm, costOfGoodsPeriod)
    lazy val errorDoc = Jsoup.parse(errorView.body)

    "have the correct title" in {
      doc.title() shouldBe costOfGoodTitle
    }

    "have the correct heading" in {
      doc.select("h1").text() shouldBe  costOfGoodsHeading(costOfGoodsPeriod)
    }

    "have some introductory text" in {
      doc.select("div > p").eq(0).text shouldBe costOfGoodsParagraph1
    }

    "have a h2 with text" in {
      doc.select("#main-content > div > div > h2").text() shouldBe costOfGoodDontInclude
    }

    "have a list of bulletpoints" in {
      doc.select("#main-content > div > div > ul > li:nth-child(1)").text() shouldBe costOfGoodsBullet1
      doc.select("#main-content > div > div > ul > li:nth-child(2)").text() shouldBe costOfGoodsBullet2
      doc.select("#main-content > div > div > ul > li:nth-child(3)").text() shouldBe costOfGoodsBullet3
      doc.select("#main-content > div > div > ul > li:nth-child(4)").text() shouldBe costOfGoodsBullet4
      doc.select("#main-content > div > div > ul > li:nth-child(5)").text() shouldBe costOfGoodsBullet5
      doc.select("#main-content > div > div > ul > li:nth-child(6)").text() shouldBe costOfGoodsBullet6
      doc.select("#main-content > div > div > ul > li:nth-child(7)").text() shouldBe costOfGoodsBullet7
      doc.select("#main-content > div > div > ul > li:nth-child(8)").text() shouldBe costOfGoodsBullet8
      doc.select("#main-content > div > div > ul > li:nth-child(9)").text() shouldBe costOfGoodsBullet9
    }

    "have text that contains a link" in {
      doc.select("#main-content > div > div > div:nth-child(5) > p").text() shouldBe costOfGoodsParagraph2
      doc.select("p").eq(2).attr("href") shouldBe ""
    }

    "have a £ symbol present" in {
      doc.select("#main-content > div > div > form > div.govuk-form-group > div.govuk-input__wrapper > div").text shouldBe "£"
    }

    "display the correct error" in {
      errorCostOfGoodsForm.hasErrors shouldBe true
      errorDoc.select("#costOfGoods-error").text.contains(costOfGoodsError)
    }

    "have a continue button" in{
      doc.select("button").text shouldBe costOfGoodsContinue
      doc.select("button").attr("type") shouldBe "submit"
    }

    "have a valid form" in{
      doc.select("form").attr("method") shouldBe "POST"
      doc.select("form").attr("action") shouldBe controllers.routes.CostOfGoodsController.submitCostOfGoods().url
    }

  }

}
