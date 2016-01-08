// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.InetAddress
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import me.gladwell.aws.net.Dns
import me.gladwell.aws.net.IpPrefix

trait Network {
  this: Dns =>

  val ipRanges: Seq[IpPrefix]

  val name: String

  def inNetwork(address: String) : Future[Boolean] = {
    for {
      ip <- resolve(address)
    } yield ipRanges.exists{ prefix => prefix.inRange(ip) }
  }

}
