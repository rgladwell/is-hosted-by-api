package me.gladwell.aws

import org.specs2.mutable.Specification
import me.gladwell.aws.test.TestConfiguration
import org.specs2.specification.Scope
import me.gladwell.aws.net.CidrNotationIpPrefix

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
