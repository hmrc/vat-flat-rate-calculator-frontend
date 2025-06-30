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

import sbt.Keys.*
import sbt.Keys.scalacOptions
import sbt.*
import play.routes.compiler.InjectedRoutesGenerator
import com.typesafe.sbt.digest.Import.digest
import com.typesafe.sbt.web.Import.pipelineStages
import com.typesafe.sbt.web.Import.Assets
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.*
import DefaultBuildSettings.*
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import play.sbt.routes.RoutesKeys.routesGenerator
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin

val appName = "vat-flat-rate-calculator-frontend"

lazy val plugins: Seq[Plugins]         = Seq.empty
lazy val playSettings: Seq[Setting[?]] = Seq.empty

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;models/.data/..*;" +
      "filters.*;.handlers.*;components.*;.*BuildInfo.*;.*FrontendAuditConnector.*;.*Routes.*;views.html.*;config.*;.*EnumUtils",
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting  := true
  )
}

lazy val microservice: Project = Project(appName, file("."))
  .enablePlugins(
    Seq(
      play.sbt.PlayScala,
      SbtDistributablesPlugin
    ) ++ plugins: _*
  )
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(playSettings *)
  .settings(scoverageSettings *)
  .settings(scalaSettings *)
  .settings(defaultSettings() *)
  .settings(majorVersion := 0)
  .settings(
    libraryDependencies ++= AppDependencies.all,
    retrieveManaged          := true,
    routesGenerator          := InjectedRoutesGenerator,
    Assets / pipelineStages  := Seq(digest),
    scalaVersion             := "2.13.16",
    PlayKeys.playDefaultPort := 9080
  )
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._",
      "uk.gov.hmrc.govukfrontend.views.html.components.implicits._"
    )
  )
  .settings(
    scalacOptions ++= Seq(
      "-feature",
      "-Wconf:cat=unused&src=routes/.*:s",
      "-Wconf:cat=unused&src=views/.*:s",
      "-Wconf:cat=unused-imports&src=html/.*:s"
    )
  )
