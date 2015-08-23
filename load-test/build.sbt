// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

// FIXME Performance test code does not compile in Eclipse

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.5",
  "io.gatling"            % "gatling-test-framework"    % "2.1.5"
)

enablePlugins(GatlingPlugin)

configs(Gatling)
