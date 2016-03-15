// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api.net

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import java.net.InetAddress.getLoopbackAddress
import org.specs2.matcher.FutureMatchers
import com.is_hosted_by.api.test.Eventually

object DnsSpec extends Specification with FutureMatchers with Eventually {

  "DNS" should {

    trait TestDns extends Dns with Scope

    "resolve domain names" in new TestDns {
      resolve("localhost") must beEqualTo(getLoopbackAddress).await
    }

    "fail on non-existent domain names" in new TestDns {
      resolve("non-existent").eventually must throwA[Exception]
    }

  }

}
