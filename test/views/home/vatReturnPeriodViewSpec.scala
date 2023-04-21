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

package views.home

import forms.vatReturnPeriodForm
import helpers.ViewSpecHelpers.VatReturnPeriodViewMessages
import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.api.i18n.{Messages, MessagesApi}
import play.api.test.FakeRequest
import views.html.home.vatReturnPeriod

class VatReturnPeriodViewSpec extends PlaySpec with GuiceOneAppPerSuite with VatReturnPeriodViewMessages {

  val view = app.injector.instanceOf[vatReturnPeriod]

  implicit def messages: Messages = app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  def createView(form: Form[_] = vatReturnPeriodForm()) = view(form)(FakeRequest(), messages)

  def createErrorView(form: Form[_] = vatReturnPeriodForm()) = view(form.withError(FormError("vatReturnPeriod", vatReturnPeriodError)))(FakeRequest(), messages)


  "the VatReturnPeriod" must {
      val doc = Jsoup.parse(createView(vatReturnPeriodForm()).toString())

    "have the correct title" in {
      doc.title() shouldBe vatReturnPeriodTitle
    }

    "have the correct heading" in {
      doc.getElementsByClass("govuk-heading-l").text() shouldBe  vatReturnPeriodHeading
    }

    "have some introductory text" in {
      doc.select("div > p").eq(0).text shouldBe vatReturnPeriodIntro
    }

    "have a paragraph text" in {
      doc.select("#main-content > div > div > div.form-group.govuk-body > p:nth-child(2)").text shouldBe vatReturnPeriodPara
    }

    "have a 'annually' label on radio button" in {
      doc.select("label").first().text shouldBe vatReturnPeriodAnnually
    }

    "have a 'quarterly' label on radio button" in {
      doc.select("label").eq(1).text shouldBe vatReturnPeriodQuarterly
    }

    "display the correct error" in {
      val errorDoc = Jsoup.parse(createErrorView(vatReturnPeriodForm()).toString())
      errorDoc.select("#vatReturnPeriod-error").text.contains(vatReturnPeriodError)
    }

    "have a continue button" in{
      doc.select("button").text shouldBe vatReturnPeriodContinue
      doc.select("button").attr("type") shouldBe "submit"
    }

    "have a valid form" in{
      doc.select("form").attr("method") shouldBe "POST"
      doc.select("form").attr("action") shouldBe controllers.routes.VatReturnPeriodController.onSubmit.url
    }
  }

}
