// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.test

import me.gladwell.aws.Configuration
import java.net.URI

trait TestConfiguration extends Configuration {
  override def hostPort = ???
  override def awsIpRangeLocation = getClass.getResource("/aws-ip-ranges.json").toURI
  override def assetsLocation = new URI("http://example.org")
  override def ipRangeLocation: URI = getClass.getResource("/microdata-networks.html").toURI
}
