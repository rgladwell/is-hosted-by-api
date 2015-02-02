import com.typesafe.sbt.SbtStartScript

organization := "gladwell.me"

name := "is-aws-api"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.3"

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-directives" % "0.8.4",
  "net.databinder" %% "unfiltered-filter" % "0.8.4",
  "net.databinder" %% "unfiltered-jetty" % "0.8.4",
  "net.databinder" %% "unfiltered-specs2" % "0.8.4" % "test"
)

seq(SbtStartScript.startScriptForClassesSettings: _*)
