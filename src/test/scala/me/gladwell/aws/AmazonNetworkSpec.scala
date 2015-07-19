// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import java.net.URI
import java.net.InetAddress.getByName
import me.gladwell.aws.test.TestConfiguration
import scala.util.{Failure, Success}

object AmazonNetworkSpec extends Specification with Mocks {

  class AmazonNetworkTestScope extends AmazonNetwork with MockDns with TestConfiguration with Scope

  "AmazonNetwork" should {
    "load IP ranges from a URL" in new AmazonNetworkTestScope {
      ipRanges().get must contain (CidrNotationIpPrefix("50.19.0.0/16"))
    }

    "correctly verify IP is in network ranges" in new AmazonNetworkTestScope {
      resolve("hosted") returns Success(getByName("54.239.98.1"))
      inNetwork("hosted") must_== Success(true)
    }

    "correctly verify IP is not in network ranges" in new AmazonNetworkTestScope {
      resolve("unhosted") returns Success(getByName("127.0.0.1"))
      inNetwork("unhosted") must_== Success(false)
    }

    "fail on network errors" in new AmazonNetworkTestScope {
      val exception = new Exception
      resolve("error") returns Failure(exception)
      inNetwork("error") must_== Failure(exception)
    }
  }

  "CIDR notation 192.168.0.1/29" should {
    "represent 1024 IPv4 address 192.168.0.1" in new AmazonNetworkTestScope {
      val ip = getByName("192.168.0.1")
      CidrNotationIpPrefix("192.168.0.1/29").inRange(ip) must beTrue
    }
  }

  
}
