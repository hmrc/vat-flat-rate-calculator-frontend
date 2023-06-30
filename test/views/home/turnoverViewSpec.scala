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

import forms.{costOfGoodsForm, turnoverForm}
import helpers.ViewSpecHelpers.TurnoverViewMessages
import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.api.i18n.{Messages, MessagesApi}
import play.api.test.FakeRequest
import views.html.home.turnover

class TurnoverViewSpec extends PlaySpec with GuiceOneAppPerSuite with TurnoverViewMessages {

  val view = app.injector.instanceOf[turnover]

  implicit def messages: Messages = app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  def createView(form: Form[_] = turnoverForm(), period: String) = view(form, period)(FakeRequest(), messages)

  def createErrorView(form: Form[_] = turnoverForm(), period: String) = view(form.withError(FormError("turnover", turnoverError)), period)(FakeRequest(), messages)

    "the TurnoverView" must {

      val doc = Jsoup.parse(createView(costOfGoodsForm(), "annually").toString())

      "have the correct title" in {
        doc.title() shouldBe turnoverTitle("year")
      }

      "have the correct heading" in {
        doc.select("h1").text() shouldBe  turnoverHeading("year")
      }

      "have some introductory text" in {
        doc.getElementsByClass("govuk-hint").text shouldBe turnoverIntro
      }

      "have a £ symbol present" in {
        doc.getElementsByClass("govuk-input__prefix").text shouldBe "£"
      }

      "display the correct error" in {
        val errorDoc = Jsoup.parse(createErrorView(turnoverForm(), "annually").toString())
        errorDoc.select("#turnover-error").text.contains(turnoverError)
      }

      "have a continue button" in{
        doc.getElementsByClass("govuk-button").text shouldBe turnoverContinue
        doc.getElementsByClass("govuk-button").attr("type") shouldBe "submit"
      }

      "have a valid form" in{
        doc.select("form").attr("method") shouldBe "POST"
        doc.select("form").attr("action") shouldBe controllers.routes.TurnoverController.onSubmit.url
      }
    }
}
