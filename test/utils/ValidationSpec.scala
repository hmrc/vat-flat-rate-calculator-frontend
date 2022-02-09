/*
 * Copyright 2022 HM Revenue & Customs
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

package utils

import assets.TestForm
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

class ValidationSpec extends PlaySpec with MockitoSugar {

  "calling isTwoDecimalPlaces" must {
    "return an error message if there are too many decimal places" in {
      val data = Map(
        "turnover" -> "10000.003"
      )
      val boundForm = TestForm.testForm.bind(data)
      boundForm.errors.map(_.message) shouldBe List("error.twoDecimalPlaces")
    }
  }
  "calling isLessThanMaximumTurnover" must {
    "return an error message if the value added is more than 9999999999.99" in {
      val data = Map(
        "turnover" -> "9999999999.99"
      )
      val boundForm = TestForm.testForm.bind(data)
      boundForm.errors.map(_.message) shouldBe List("error.moreThanMaximumTurnover")
    }
  }
  "calling isPositive" must {
    "return an error message if the value added is negative" in {
      val data = Map(
        "turnover" -> "-100"
      )
      val boundForm = TestForm.testForm.bind(data)
      boundForm.errors.map(_.message) shouldBe List("error.negative")
    }
  }
}