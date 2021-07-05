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

import sbt.Keys.{scalacOptions, _}
import sbt._
import play.routes.compiler.InjectedRoutesGenerator
import com.typesafe.sbt.digest.Import.digest
import com.typesafe.sbt.web.Import.pipelineStages
import com.typesafe.sbt.web.Import.Assets
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import com.timushev.sbt.updates.UpdatesPlugin.autoImport.moduleFilterRemoveValue
import uk.gov.hmrc._
import DefaultBuildSettings._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import play.sbt.routes.RoutesKeys.routesGenerator
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin

val appName = "vat-flat-rate-calculator-frontend"

lazy val appDependencies : Seq[ModuleID] = AppDependencies()
lazy val plugins : Seq[Plugins] = Seq.empty
lazy val playSettings : Seq[Setting[_]] = Seq.empty

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;models/.data/..*;" +
      "filters.*;.handlers.*;components.*;.*BuildInfo.*;.*FrontendAuditConnector.*;.*Routes.*;views.html.*;config.*;",
    ScoverageKeys.coverageMinimum := 90,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val microservice: Project = Project(appName, file("."))
  .enablePlugins(
    Seq(
      play.sbt.PlayScala,
      SbtAutoBuildPlugin,
      SbtDistributablesPlugin,
      SbtGitVersioning,
    ) ++ plugins : _*)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(playSettings : _*)
  .settings(scoverageSettings: _*)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(majorVersion := 0 )
  .settings(
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    routesGenerator := InjectedRoutesGenerator,
    pipelineStages in Assets := Seq(digest),
    scalaVersion := "2.12.12",
    scalacOptions += "-P:silencer:lineContentFilters=^\\w",
    scalacOptions += "-P:silencer:pathFilters=views;routes;--feature",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.1" cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % "1.7.1" % Provided cross CrossVersion.full
    )
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(integrationTestSettings())
  .settings(dependencyUpdatesFilter -= moduleFilter(organization = "org.scala-lang"))
  .settings(dependencyUpdatesFilter -= moduleFilter(organization = "com.typesafe.play"))
  .settings(dependencyUpdatesFilter -= moduleFilter(organization = "org.scalatest"))
  .settings(dependencyUpdatesFilter -= moduleFilter(organization = "org.scalameta"))
  .settings(dependencyUpdatesFilter -= moduleFilter(organization = "org.scalatestplus.play"))
  .settings(dependencyUpdatesFilter -= moduleFilter(organization = "org.scoverage"))
  .settings(dependencyUpdatesFailBuild := false)
