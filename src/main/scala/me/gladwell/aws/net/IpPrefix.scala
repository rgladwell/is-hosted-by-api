// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import java.net.InetAddress
import net.ripe.commons.ip.Ipv4Range
import net.ripe.commons.ip.Ipv6Range
import java.net.Inet4Address
import java.net.Inet6Address
import net.ripe.commons.ip.Ipv6
import net.ripe.commons.ip.Ipv4

trait IpPrefix {
  def inRange(address: InetAddress): Boolean
}

private class Ipv4RangePrefix(private val range: Ipv4Range) extends IpPrefix {

  override def inRange(ip: InetAddress): Boolean = ip match {
    case ipv4: Inet4Address => range.contains(Ipv4.of(ipv4.getHostAddress))
    case _ => false
  }

}

private class Ipv6RangePrefix(private val range: Ipv6Range) extends IpPrefix {

  override def inRange(ip: InetAddress): Boolean = ip match {
    case ipv6: Inet6Address => range.contains(Ipv6.of(ipv6.getHostAddress))
    case _ => false
  }

}
