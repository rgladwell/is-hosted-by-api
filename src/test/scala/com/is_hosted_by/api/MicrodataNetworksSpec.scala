// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import org.specs2.mutable.Specification
import test.TestConfiguration
import org.specs2.specification.Scope
import net.CidrNotationIpPrefix
import urimplicit._
import com.xebialabs.restito.server.StubServer
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Condition.get
import com.xebialabs.restito.semantics.Action.resourceContent
import com.xebialabs.restito.semantics.Condition._
import com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp
import org.glassfish.grizzly.http.Method
import scala.concurrent.Await
import scala.concurrent.duration._
import org.specs2.mutable.After
import java.util.concurrent.TimeUnit

object MicrodataNetworksSpec extends Specification {

  trait ConfigWithMockServer extends TestConfiguration with After {
    val server = new StubServer().run()
    private val port = server.getPort

    override def ipRangeLocation: URI = URI(s"http://localhost:$port/")

    whenHttp(server).
      `match`(get("/")).
      `then`(resourceContent("microdata-networks.html"))

    def after = server.stop()
  }

  class TestMicrodataNetworks extends MicrodataNetworks
                                        with MicrodataNetworkParser
                                        with ConfigWithMockServer
                                        with Scope

  "MicrodataNetworks on network download should" >> {

    "load IP ranges from a URL" >> new TestMicrodataNetworks {
      networks().map { _.head.ipRanges must contain (CidrNotationIpPrefix("104.236.0.0/16")) }.await
    }

    "send user agent" >> new TestMicrodataNetworks {
      Await.result(networks(), FiniteDuration(2, TimeUnit.SECONDS))

      verifyHttp(server).once(
        method(Method.GET), withHeader("User-Agent", "is-hosted-by/1.0")
      )
    }
  }
}
