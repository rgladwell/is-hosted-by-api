// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import org.specs2.mutable.Specification
import test.TestConfiguration
import org.specs2.specification.Scope
import net.CidrNotationIpPrefix

object MicrodataNetworksSpec extends Specification {

  class TestMicrodataNetworks extends MicrodataNetworks
                                        with MicrodataNetworkParser
                                        with TestConfiguration
                                        with Scope

  "MicrodataNetworks" should {
    "load IP ranges from a URL" in new TestMicrodataNetworks {
      networks().map { _.head.ipRanges must contain (CidrNotationIpPrefix("104.236.0.0/16")) }.await
    }
  }
}
