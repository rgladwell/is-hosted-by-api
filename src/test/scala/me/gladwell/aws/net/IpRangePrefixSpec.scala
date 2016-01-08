// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import org.specs2.mutable.Specification
import java.net.InetAddress.getByName

object IpRangePrefixSpec extends Specification {

  "IPv4 range 208.68.36.0/22" should {

     val from = getByName("208.68.36.0")
     val to = getByName("208.68.39.255")
     val range = IpRangePrefix(from.getHostAddress, to.getHostAddress)

    "represent 1024 IPv4 address lower range 208.68.36.0" in {
      range.inRange(from) must beTrue
    }

    "represent 1024 IPv4 address upper range 208.68.39.255" in {
      val ip = getByName("208.68.39.255")
      range.inRange(to) must beTrue
    }

    "not represent 1024 IPv4 address 208.68.35.0" in {
      val ip = getByName("208.68.35.0")
      range.inRange(ip) must beFalse
    }

    "not represent IPv6 address" in {
      val ipv6 = getByName("2604:A880:0000:0000:0000:0000:0000:0000")
      range.inRange(ipv6) must beFalse
    }

  }

  "IPv6 Range" should {

    val from = getByName("2604:A880:0000:0000:0000:0000:0000:0000")
    val to = getByName("2604:A880:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF")
    val range = IpRangePrefix(from.getHostAddress, to.getHostAddress)

    "represent IPv6 address" in {
      range.inRange(from) must beTrue
    }

    "not represent IPv4 address" in {
      val ipv4 = getByName("208.68.35.0")
      range.inRange(ipv4) must beFalse
    }

  }
}
