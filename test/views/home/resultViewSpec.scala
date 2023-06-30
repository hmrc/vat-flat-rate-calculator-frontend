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

import helpers.ViewSpecHelpers.ResultViewMessages
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec
import play.api.i18n.{Messages, MessagesApi}
import views.html.home.result

class ResultViewSpec extends PlaySpec with GuiceOneAppPerSuite with ResultViewMessages {

  val view = app.injector.instanceOf[result]

  implicit def messages: Messages = app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  def createView(resultCode: Int, showUserResearchPanel: Boolean) = view(resultCode, showUserResearchPanel)(FakeRequest(), messages)

  "the ResultView" must {

    val doc = Jsoup.parse(createView(1, true).toString())

    "have the correct title" in {
      doc.title() shouldBe ResultTitle
    }

    "have the correct heading" in {
      doc.select("h1").text() shouldBe  ResultHeading
    }

    "have some introductory text" in {
      doc.select("div > p").eq(0).text() shouldBe ResultIntro
    }

    "have a header for progressive disclosure" in {
      doc.select("strong").text() should include(ResultProgressiveDisclosureHeader)
    }

    "have a progressive disclosure" in {
      doc.select("span.summary").text() shouldBe ResultProgressiveDisclosureTitle
    }

    "have text within progressive disclosure" in {
      doc.select("p.govuk-body:nth-child(1)").text() shouldBe ResultProgressiveDisclosureText
    }

    "have a h2 with text" in {
      doc.select("h2").text() should include(ResultH2Text)
    }

    "have instructions on what happens next" in {
      doc.select("p.govuk-body:nth-child(2)").text() shouldBe ResultNextText1

      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(3)").text() shouldBe ResultNextText2
      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(3) > a").attr("href") shouldBe ResultNextText2Href

      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(4)").text() shouldBe ResultNextText3
      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(4) > a").attr("href") shouldBe ResultNextText3Href

      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(5)").text() shouldBe ResultNextText4
      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(5) > a").attr("href") shouldBe ResultNextText4Href

      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(6)").text() shouldBe FeedbackSurveyText
      doc.select("#main-content > div > div > div:nth-child(4) > p:nth-child(6) > a").attr("href") shouldBe FeedbackSurveyTextHref
    }
  }
}
