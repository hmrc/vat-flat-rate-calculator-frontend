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

package controllers.actions

import models.OptionalDataRequest
import play.api.Application
import play.api.mvc.{Request, _}
import utils.{CacheMap, UserAnswers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class FakeDataRetrievalAction(cacheMapToReturn: Option[CacheMap])(implicit app: Application)
    extends DataRetrievalAction {

  override def executionContext: ExecutionContext = global

  override def parser: BodyParser[AnyContent] =
    app.injector.instanceOf[MessagesControllerComponents].parsers.defaultBodyParser

  override protected def transform[A](request: Request[A]): Future[OptionalDataRequest[A]] = {
    val userAnswers: Option[UserAnswers] = cacheMapToReturn.map {
      new UserAnswers(_) {}
    }

    Future(OptionalDataRequest(request, "id", userAnswers))
  }

}
