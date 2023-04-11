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

package forms

import javax.inject.Inject

import models.VatFlatRateModel
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import utils.Validation._

class VatFlatRateForm @Inject()(val messagesApi: MessagesApi)extends I18nSupport{

  val vatReturnPeriodForm = Form(
    mapping(
      "vatReturnPeriod" -> text,
      "turnover" -> optional(bigDecimal),
      "costOfGoods" -> optional(bigDecimal)
    )(VatFlatRateModel.apply)(VatFlatRateModel.unapply)
  )

  val turnoverForm = Form(
    mapping(
      "vatReturnPeriod" -> text,
      "turnover" -> optional(bigDecimal.verifying(isLessThanMaximumTurnover, isPositive, isTwoDecimalPlaces)).verifying("error.turnover.required", _.isDefined),
      "costOfGoods" -> optional(bigDecimal)
    )(VatFlatRateModel.apply)(VatFlatRateModel.unapply)
  )

  val costOfGoodsForm = Form(
    mapping(
      "vatReturnPeriod" -> text,
      "turnover" -> optional(bigDecimal),
      "costOfGoods" -> optional(bigDecimal.verifying(isLessThanMaximumTurnover, isPositive, isTwoDecimalPlaces)).verifying("error.costOfGoods.required", _.isDefined)
    )(VatFlatRateModel.apply)(VatFlatRateModel.unapply)
  )
}
