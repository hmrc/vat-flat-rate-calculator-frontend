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

package utils

import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.data.validation.{Invalid, Valid}

class ValidationSpec extends PlaySpec with MockitoSugar with Validation {

  "calling verifyDecimalPlaces" must {

    "return false if there are too many decimal places" in {
      verifyDecimalPlaces("0.001") shouldBe false
    }

    "return true if there are 2 or less decimal places" in {
      verifyDecimalPlaces("0.01") shouldBe true
      verifyDecimalPlaces("0.1") shouldBe true
    }
  }

  "calling maximumValue" must {

    "return an error message if the value added is more than 9999999999.99" in {
      val result = maximumValue(1, "error.max").apply(2)
      result mustEqual Invalid("error.max")
    }

    "return valid if the value added is less than 9999999999.99" in {
      val result = maximumValue(1, "error.max").apply(0)
      result mustEqual Valid
    }

  }

  "calling minimumValue" must {

    "return valid for a number greater than the threshold" in {
      val result = minimumValue(1, "error.min").apply(2)
      result mustEqual Valid
    }
    "return an error message for a number below the threshold" in {
      val result = minimumValue(1, "error.min").apply(0)
      result mustEqual Invalid("error.min")
    }
  }
}