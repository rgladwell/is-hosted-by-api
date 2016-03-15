// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api.test

import com.is_hosted_by.api.Configuration
import java.net.URI

trait TestConfiguration extends Configuration {
  override def hostPort = ???
  override def awsIpRangeLocation = getClass.getResource("/aws-ip-ranges.json").toURI
  override def assetsLocation = new URI("http://example.org")
  override def ipRangeLocation: URI = getClass.getResource("/microdata-networks.html").toURI
}
