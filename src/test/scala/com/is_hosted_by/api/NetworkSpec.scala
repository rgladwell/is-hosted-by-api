// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import net.IpPrefix
import scala.concurrent.Future
import java.net.InetAddress.getByName
import test.Eventually
import net.CidrNotationIpPrefix
import org.specs2.matcher.FutureMatchers

object NetworkSpec extends Specification with Mocks with Eventually with FutureMatchers {

  "Network" should {

    class TestNetwork extends Network with Scope with MockDns {

      val name: String = "test-network"
      val ipRanges: Seq[IpPrefix] = Seq(CidrNotationIpPrefix("54.239.98.0/24"))

    }

    "correctly verify IP is in network ranges" in new TestNetwork {
      resolve("hosted") returns Future{ getByName("54.239.98.1") }
      inNetwork("hosted") must beTrue.await
    }

    "correctly verify IP is not in network ranges" in new TestNetwork {
      resolve("unhosted") returns Future{ getByName("127.0.0.1") }
      inNetwork("unhosted") must beFalse.await
    }

    "fail on network errors" in new TestNetwork {
      resolve("error") returns Future{ throw new Exception }
      inNetwork("error").eventually must throwA[Exception]
    }
  }

}
