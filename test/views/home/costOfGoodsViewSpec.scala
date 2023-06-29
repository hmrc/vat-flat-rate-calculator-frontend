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

import helpers.ViewSpecHelpers.CostOfGoodsViewMessages
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec
import views.html.home.costOfGoods
import forms.costOfGoodsForm
import play.api.data.{Form, FormError}
import play.api.i18n.{Messages, MessagesApi}


class CostOfGoodsViewSpec extends PlaySpec with GuiceOneAppPerSuite with CostOfGoodsViewMessages {

  val view = app.injector.instanceOf[costOfGoods]

  implicit def messages: Messages = app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  def createView(form: Form[_] = costOfGoodsForm(), period: String) = view(form, period)(FakeRequest(), messages)
  def createErrorView(form: Form[_] = costOfGoodsForm(), period: String) = view(form.withError(FormError("costOfGoods", costOfGoodsError)), period)(FakeRequest(), messages)


  "the CostOfGoodsView" must {
      val doc = Jsoup.parse(createView(costOfGoodsForm(), "annually").toString())

    "have the correct title" in {
      doc.title() shouldBe costOfGoodTitle("year")
    }
    "have the correct heading" in {
      doc.select("h1").text() shouldBe  costOfGoodsHeading("year")
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
      val errorDoc = Jsoup.parse(createErrorView(costOfGoodsForm(), "annually").toString())
      errorDoc.select("#costOfGoods-error").text.contains(costOfGoodsError)
    }

    "have a continue button" in{
      doc.select("button").text shouldBe costOfGoodsContinue
      doc.select("button").attr("type") shouldBe "submit"
    }

    "have a valid form" in{
      doc.select("form").attr("method") shouldBe "POST"
      doc.select("form").attr("action") shouldBe controllers.routes.CostOfGoodsController.onSubmit.url
    }

  }

}
