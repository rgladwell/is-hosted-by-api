// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import java.net.URI
import java.net.InetAddress.getByName

object AmazonNetworkSpec extends Specification {

  trait TestIpRangeLocation extends Configuration {
    override def port = ???
    override def awsIpRangeLocation = getClass.getResource("/aws-ip-ranges.json").toURI
  }

  class AmazonNetworkTestScope extends AmazonNetwork with TestIpRangeLocation with Scope

  "AmazonNetwork" should {
    "load IP ranges from a URL" in new AmazonNetworkTestScope {
      ipRanges must contain (CidrNotationIpPrefix("50.19.0.0/16"))
    }
  }

  "CIDR notation 192.168.0.1/29" should {
    "represent 1024 IPv4 address 192.168.0.1" in new AmazonNetworkTestScope {
      val ip = getByName("192.168.0.1")
      CidrNotationIpPrefix("192.168.0.1/29").inRange(ip) must beTrue
    }
  }
}
