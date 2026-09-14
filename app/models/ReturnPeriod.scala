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

package models

import play.api.libs.json.{Format, Reads, Writes}
import utils.EnumUtils

enum ReturnPeriod(val value: String) {
  case ANNUALLY extends ReturnPeriod("annually")
  case MONTHLY  extends ReturnPeriod("quarterly")
}

object ReturnPeriod {

  def withName(value: String): ReturnPeriod =
    ReturnPeriod.values
      .find(_.value == value)
      .getOrElse(
        throw new NoSuchElementException(
          s"No ReturnPeriod found for value '$value'"
        )
      )

  val enumReads: Reads[ReturnPeriod] =
    EnumUtils.enumReads(ReturnPeriod.values)(_.value)

  val enumWrites: Writes[ReturnPeriod] =
    EnumUtils.enumWrites(_.value)

  given enumFormats: Format[ReturnPeriod] =
    EnumUtils.enumFormat(ReturnPeriod.values)(_.value)

}
