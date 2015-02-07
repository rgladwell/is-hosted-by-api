// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import util.Properties
import java.net.URI

trait HerokuConfiguration extends Configuration {
  this: EnvironmentVariables =>

  override def port = environmentVariables.get("PORT").getOrElse("8080").toInt
  override def awsIpRangeLocation: URI = new URI(environmentVariables.get("AWS_IPRANGE_LOCATION").get)

}
