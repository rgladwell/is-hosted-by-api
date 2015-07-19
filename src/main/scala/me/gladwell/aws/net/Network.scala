// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import java.net.InetAddress
import scala.util.Try
import org.slf4s.Logging

trait Network extends Logging {
  this: Dns =>

  trait IpPrefix {
    def inRange(address: InetAddress): Boolean
  }

  type IpRangeLoader = () => Try[Seq[IpPrefix]]

  val ipRanges: IpRangeLoader

  def inNetwork(address: String) = {
    val result = for {
      ip <- resolve(address)
      ranges <- ipRanges()
    } yield ranges.exists{ prefix => prefix.inRange(ip) }

    log.info(s"[$address] is in network=[$result]")

    result
  }

}
