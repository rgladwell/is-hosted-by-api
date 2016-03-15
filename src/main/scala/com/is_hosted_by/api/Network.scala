// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import java.net.InetAddress
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import net._

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
