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

package services

import connectors.KeystoreConnector
import models.{ResultModel, VatFlatRateModel}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import uk.gov.hmrc.http.HeaderCarrier

class StateServiceSpec extends PlaySpec with MockitoSugar {

  implicit val hc: HeaderCarrier = mock[HeaderCarrier]
  val mockVatFlatRateModel: Option[VatFlatRateModel] = Some(VatFlatRateModel("annually", Some(10000.00), Some(10.00)))
  val mockResultModel: Option[ResultModel] = Some(ResultModel(VatFlatRateModel("annually", Some(10000.00), Some(10.00)), 2))

  def mockedStateService(): StateService = {
    val mockConnector: KeystoreConnector = mock[KeystoreConnector]
    val testData: Option[VatFlatRateModel] = mockVatFlatRateModel
    val returnedCacheMap = CacheMap("vatReturnPeriod", Map("data" -> Json.toJson(testData)))

    when(mockConnector.fetchAndGetFormData[VatFlatRateModel](ArgumentMatchers.any())(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(testData))

    when(mockConnector.saveFormData[VatFlatRateModel](ArgumentMatchers.any(), ArgumentMatchers.any())(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(returnedCacheMap))

    new StateService(mockConnector)
  }

  "Calling StateService .fetchData" must {
    "return a VatReturnPeriodModel when there is one in Keystore" in {
      val service = mockedStateService()
      val result: Future[Option[VatFlatRateModel]] = service.fetchVatFlatRate()

      await(result) shouldBe Some(VatFlatRateModel("annually", Some(10000.00), Some(10.00)))
    }
  }
  "Calling StateService .saveData" must {

    "return a Cachemap with a valid response" in {
      val testData = VatFlatRateModel("annually", Some(10000.00), Some(10.00))
      val returnedData = CacheMap("vatReturnPeriod", Map("data" -> Json.toJson(testData)))
      val service = mockedStateService()
      val result = service.saveVatFlatRate[VatFlatRateModel](testData)

      await(result) shouldBe returnedData
    }
  }

  "Calling StateService .fetchResultModel" must {
    "return a ResultModel when there is one in Keystore" in {
      val service = mockedStateService()
      val result: Future[Option[ResultModel]] = service.fetchResultModel()

      await(result) shouldBe Some(VatFlatRateModel("annually", Some(10000.00), Some(10.00)))
    }
  }
  "Calling StateService .saveResultModel" must {

    "return a Cachemap with a valid response" in {
      val testData = VatFlatRateModel("annually", Some(10000.00), Some(10.00))
      val returnedData = CacheMap("vatReturnPeriod", Map("data" -> Json.toJson(testData)))
      val service = mockedStateService()
      val result = service.saveResultModel[VatFlatRateModel](testData)

      await(result) shouldBe returnedData
    }
  }

}
