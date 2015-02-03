// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import util.Properties

trait HerokuConfiguration extends Configuration {
  this: EnvironmentVariables =>

  override def port = environmentVariables.get("PORT").getOrElse("8080").toInt

}
