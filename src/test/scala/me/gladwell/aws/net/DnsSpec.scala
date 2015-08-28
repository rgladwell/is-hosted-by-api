// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import scala.util.Success
import java.net.InetAddress.getLoopbackAddress

object DnsSpec extends Specification {

  "Dns" should {

    trait TestDns extends Dns with Scope

    "resolve DNS names" in new TestDns {
      resolve("localhost") must beSuccessfulTry.withValue(getLoopbackAddress)
    }

    "fail on non-existent DNS" in new TestDns {
      resolve("non-existent") must beFailedTry
    }

  }

}
