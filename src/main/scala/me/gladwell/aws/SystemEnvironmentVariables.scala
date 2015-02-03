// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

trait SystemEnvironmentVariables extends EnvironmentVariables {

  import scala.collection.JavaConverters._
  override def environmentVariables = System.getenv.asScala.toMap

}
