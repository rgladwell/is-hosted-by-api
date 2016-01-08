// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import org.specs2.mutable.Specification
import java.net.InetAddress.getByName

object CidrNotationIpPrefixSpec extends Specification {

  "CIDR notation 208.68.36.0/22" should {
    val range = CidrNotationIpPrefix("208.68.36.0/22")

    "represent 1024 IPv4 address lower range 208.68.36.0" in {
      val ip = getByName("208.68.36.0")
      range.inRange(ip) must beTrue
    }

    "represent 1024 IPv4 address upper range 208.68.39.255" in {
      val ip = getByName("208.68.39.255")
      range.inRange(ip) must beTrue
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

  "CIDR notation" should {
    val range = CidrNotationIpPrefix("2604:A880::/32")

    "represent IPv6 address" in {
      val ip = getByName("2604:A880:0000:0000:0000:0000:0000:0000")
      range.inRange(ip) must beTrue
    }

    "not represent IPv4 address" in {
      val ipv4 = getByName("208.68.35.0")
      range.inRange(ipv4) must beFalse
    }
  }

}
