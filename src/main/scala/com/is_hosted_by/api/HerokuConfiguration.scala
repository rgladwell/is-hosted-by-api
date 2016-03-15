// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import util.Properties
import java.net.URI

trait HerokuConfiguration extends Configuration {
  this: EnvironmentVariables =>

  override def hostPort = environmentVariables.get("PORT").getOrElse("8080").toInt
  override def awsIpRangeLocation: URI = new URI(environmentVariables.get("AWS_IPRANGE_LOCATION").get)
  override def assetsLocation: URI = new URI(environmentVariables.get("IS_HOSTED_BY_ASSETS_LOCATION").get)
  override def ipRangeLocation: URI = new URI(environmentVariables.get("IPRANGES_LOCATION").get)

}
