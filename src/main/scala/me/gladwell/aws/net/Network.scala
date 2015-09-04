// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import java.net.InetAddress
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait Network {
  this: Dns =>

  trait IpPrefix {
    def inRange(address: InetAddress): Boolean
  }

  type IpRangeLoader = () => Future[Seq[IpPrefix]]

  val ipRanges: IpRangeLoader

  def inNetwork(address: String) : Future[Boolean] = {
    for {
      ip <- resolve(address)
      ranges <- ipRanges()
    } yield ranges.exists{ prefix => prefix.inRange(ip) }
  }

}
