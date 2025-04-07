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

package routes

import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.play.PlaySpec

class RoutesSpec extends PlaySpec {
  val baseUrl = "/check-your-vat-flat-rate"

  val vatReturnPeriod: String = baseUrl + "/vat-return-period"
  val turnover: String        = baseUrl + "/turnover"
  val costOfGoods: String     = baseUrl + "/cost-of-goods"
  val result: String          = baseUrl + "/result"
  val feedbackSurvey          = baseUrl + "/feedback-survey"

  "The route for the vatReturnPeriod action on the vatReturnPeriod controller" must {
    "be /check-your-vat-flat-rate/vat-return-period" in {
      controllers.routes.VatReturnPeriodController.onPageLoad.url shouldBe s"$vatReturnPeriod"
    }
  }

  "The route for the submitVatReturnPeriod action on the vatReturnPeriod controller" must {
    " be /check-your-vat-flat-rate/vat-return-period" in {
      controllers.routes.VatReturnPeriodController.onSubmit.url shouldBe s"$vatReturnPeriod"
    }
  }

  "The route for the turnover action on the turnover controller" must {
    " be /check-your-vat-flat-rate/turnover" in {
      controllers.routes.TurnoverController.onPageLoad.url shouldBe s"$turnover"
    }
  }

  "The route for the submit action on the turnover controller" must {
    " be /check-your-vat-flat-rate/turnover" in {
      controllers.routes.TurnoverController.onSubmit.url shouldBe s"$turnover"
    }
  }

  "The route for the costOfGoods action on the costOfGoods controller" must {
    "be /check-your-vat-flat-rate/cost-of-goods" in {
      controllers.routes.CostOfGoodsController.onPageLoad.url shouldBe s"$costOfGoods"
    }
  }

  "The route for the submit action on the costOfGoods controller" must {
    "be /check-your-vat-flat-rate/cost-of-goods" in {
      controllers.routes.CostOfGoodsController.onSubmit.url shouldBe s"$costOfGoods"
    }
  }

  "The route for the result action on the result controller" must {
    "be /check-your-vat-flat-rate/cost-of-goods" in {
      controllers.routes.ResultController.onPageLoad.url shouldBe s"$result"
    }
  }

  "The route for the feedback survey on the feedback survey controller" must {
    "be /check-your-vat-flat-rate/feedback-survey" in {
      controllers.routes.FeedbackSurveyController.redirectFeedbackSurvey.url shouldBe s"$feedbackSurvey"
    }
  }

}
