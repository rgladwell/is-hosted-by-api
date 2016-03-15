// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import java.net.URI

trait Configuration {

  def hostPort: Int
  def awsIpRangeLocation: URI
  def assetsLocation: URI
  def ipRangeLocation: URI

}
