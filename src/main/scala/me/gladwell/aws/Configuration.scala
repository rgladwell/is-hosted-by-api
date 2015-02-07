// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.URI

trait Configuration {

  def port: Int
  def awsIpRangeLocation: URI

}