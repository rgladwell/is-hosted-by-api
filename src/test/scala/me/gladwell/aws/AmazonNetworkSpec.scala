// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import java.net.InetAddress.getByName
import me.gladwell.aws.test.TestConfiguration
import org.specs2.matcher.FutureMatchers
import scala.concurrent.Future
import me.gladwell.aws.test.Eventually

object AmazonNetworkSpec extends Specification with Mocks with FutureMatchers with Eventually {

  class AmazonNetworkTestScope extends AmazonNetwork with MockDns with TestConfiguration with Scope

  "AmazonNetwork" should {
    "load IP ranges from a URL" in new AmazonNetworkTestScope {
      ipRanges() must contain (CidrNotationIpPrefix("50.19.0.0/16")).await
    }

    "correctly verify IP is in network ranges" in new AmazonNetworkTestScope {
      resolve("hosted") returns Future{ getByName("54.239.98.1") }
      inNetwork("hosted") must beTrue.await
    }

    "correctly verify IP is not in network ranges" in new AmazonNetworkTestScope {
      resolve("unhosted") returns Future{ getByName("127.0.0.1") }
      inNetwork("unhosted") must beFalse.await
    }

    "fail on network errors" in new AmazonNetworkTestScope {
      resolve("error") returns Future{ throw new Exception }
      inNetwork("error").eventually must throwA[Exception]
    }
  }

  "CIDR notation 192.168.0.1/29" should {
    "represent 1024 IPv4 address 192.168.0.1" in new AmazonNetworkTestScope {
      val ip = getByName("192.168.0.1")
      CidrNotationIpPrefix("192.168.0.1/29").inRange(ip) must beTrue
    }
  }

  
}
