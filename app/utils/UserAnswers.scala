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

import models.ReturnPeriod
import uk.gov.hmrc.http.cache.client.CacheMap

class UserAnswers(val cacheMap: CacheMap) extends MapFormats {

  def vatReturnPeriod: Option[ReturnPeriod.Value] = cacheMap.getEntry[ReturnPeriod.Value]("vatReturnPeriod")

  def turnover: Option[BigDecimal] = cacheMap.getEntry[BigDecimal]("turnover")

  def costOfGoods: Option[BigDecimal] = cacheMap.getEntry[BigDecimal]("costOfGoods")

}