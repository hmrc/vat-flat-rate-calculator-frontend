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

import play.sbt.PlayImport._

import sbt._

object AppDependencies {

  val bootstrapVersion = "7.22.0"
  val mongoVersion = "1.3.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-partials"              % "8.4.0-play-28",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "7.21.0-play-28",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28"   % mongoVersion
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply(): Seq[ModuleID] = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc"            %% "bootstrap-test-play-28"     % bootstrapVersion    % scope,
        "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-28"    % mongoVersion        % scope,
        "org.scalatest"          %% "scalatest"                  % "3.0.8"             % scope,
        "org.jsoup"              %  "jsoup"                      % "1.16.1"            % scope,
        "org.mockito"            %  "mockito-core"               % "5.5.0"             % scope,
        "org.scalatestplus"      %% "scalatestplus-mockito"      % "1.0.0-M2"          % scope
      )
    }.test
  }

  def apply(): Seq[ModuleID] = compile ++ Test()

}
