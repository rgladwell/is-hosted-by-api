// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.InetAddress

trait Network {

  trait IpPrefix {
    def inRange(address: InetAddress): Boolean
  }

  val ipRanges: Seq[IpPrefix]

}
