// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

import com.typesafe.sbt.SbtStartScript

organization := "gladwell.me"

name := "is-aws-api"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-directives" % "0.8.4",
  "net.databinder" %% "unfiltered-filter" % "0.8.4",
  "net.databinder" %% "unfiltered-jetty" % "0.8.4",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "commons-net" % "commons-net" % "3.3",
  "org.slf4s" %% "slf4s-api" % "1.7.7",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "io.mth" %% "unfiltered-cors" % "0.3",
  "net.databinder" %% "unfiltered-specs2" % "0.8.4" % "test",
  "net.databinder" %% "dispatch-tagsoup" % "0.8.10" % "test"
)

seq(SbtStartScript.startScriptForClassesSettings: _*)

scalacOptions += "-feature"
