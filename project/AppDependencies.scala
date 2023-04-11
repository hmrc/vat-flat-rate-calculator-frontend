/*
 * Copyright 2020 HM Revenue & Customs
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

import play.core.PlayVersion
import play.sbt.PlayImport._

import sbt._

object AppDependencies {

  val bootstrapVersion = "7.15.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-partials"              % "8.3.0-play-28",
    "uk.gov.hmrc" %% "http-caching-client"        % "10.0.0-play-28",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "7.3.0-play-28"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply(): Seq[ModuleID] = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc"            %% "bootstrap-test-play-28"     % bootstrapVersion,
        "org.scalatest"          %% "scalatest"                  % "3.0.8"             % scope,
        "org.jsoup"              %  "jsoup"                      % "1.15.1"            % scope,
        "org.mockito"            %  "mockito-core"               % "5.2.0"             % scope,
        "org.scalatestplus"      %% "scalatestplus-mockito"      % "1.0.0-M2"
      )
    }.test
  }

  def apply(): Seq[ModuleID] = compile ++ Test()

}
