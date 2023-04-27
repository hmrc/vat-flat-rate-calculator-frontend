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

import models.ReturnPeriod
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter
import utils.{InputOption, Validation}

object vatReturnPeriodForm extends Validation{

  def apply(): Form[ReturnPeriod.Value] =
    Form(single("vatReturnPeriod" -> of(ReturnPeriodFormatter)))

  def options: Seq[InputOption] = Seq(
    returnPeriodInputOption(ReturnPeriod.ANNUALLY.toString, "vatReturnPeriod"),
    returnPeriodInputOption(ReturnPeriod.MONTHLY.toString, "vatReturnPeriod-2")
  )

  private def returnPeriodInputOption(option: String, id: String) = {
    new InputOption(
      id =  id,
      value = option,
      messageKey = s"vatReturnPeriod.option.$option"
    )
  }

  private def ReturnPeriodFormatter = new Formatter[ReturnPeriod.Value] {
    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) if optionIsValid(s) => Right(ReturnPeriod.withName(s))
      case None => produceError(key, "error.vatReturnPeriod.required")
      case _ => produceError(key, "unknownErrorKey")
    }

    def unbind(key: String, value: ReturnPeriod.Value) = Map(key -> value.toString)
  }
}
