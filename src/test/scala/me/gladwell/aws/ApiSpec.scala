// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import dispatch.classic._
import unfiltered.response.Html5
import me.gladwell.aws.test.MockUnfilitered
import java.net.InetAddress
import unfiltered.specs2.jetty.Served
import org.specs2.specification.Scope
import unfiltered.specs2.Hosted

object ApiSpec extends Specification with MockUnfilitered {

  import dispatch._

  val hostedIpAddress =  mock[InetAddress]
  val unhostedIpAddress =  mock[InetAddress]

  trait MockViews extends Views {
    override val index = mock[View]

    val hostedView = mock[View]
    val unhostedView = mock[View]

    override def resultView(result: Boolean) = if(result) hostedView else unhostedView

    override def errorView(error: Throwable) = mock[View]
  }

  trait MockNetwork extends Network {
    case class MockIpPrefix(range: InetAddress) extends IpPrefix {
      def inRange(address: InetAddress): Boolean = (address == range)
    }

    override val ipRanges = Seq(MockIpPrefix(hostedIpAddress))
  }

  trait MockDns extends Dns {
    override val resolve = mock[Resolver]
  }

  trait ServedScope extends Hosted with Scope {
    import unfiltered.jetty._

    lazy val server = setup(Server.http(port))

    def setup: (Server => Server)

    server.start()
  }

  trait TestApiScope extends ServedScope {
    object TestApi extends Api
      with MockViews
      with MockNetwork
      with MockDns {
    }

    def setup = { _.plan(TestApi) }

    def endpoint = url(s"http://localhost:$port")
  }

  "The HTTP API" should {
    "return OK response for an index request" in new TestApiScope {
      val response = mockResponse()
      TestApi.index.apply(any) returns Html5 { <p></p> }(response)
      status(endpoint) must_== 200
    }

    "return OK response for an address lookup" in new TestApiScope {
      val response = mockResponse()
      TestApi.hostedView.apply(any) returns Html5 { <p></p> }(response)
      TestApi.unhostedView.apply(any) returns Html5 { <p></p> }(response)
      TestApi.resolve("hosted") returns hostedIpAddress
      TestApi.resolve("unhosted") returns unhostedIpAddress

      status(endpoint / "?address=unhosted") must_== 200
    }

    "return hosted view for hosted address" in new TestApiScope {
      val response = mockResponse()
      TestApi.hostedView.apply(any) returns Html5 { <p></p> }(response)
      TestApi.unhostedView.apply(any) returns Html5 { <p></p> }(response)
      TestApi.resolve("hosted") returns hostedIpAddress
      TestApi.resolve("unhosted") returns unhostedIpAddress

      GET(endpoint / "?address=hosted")

      there was one(TestApi.hostedView).apply(any)
    }

    "return unhosted view for unhosted address" in new TestApiScope {
      val response = mockResponse()
      TestApi.hostedView.apply(any) returns Html5 { <p></p> }(response)
      TestApi.unhostedView.apply(any) returns Html5 { <p></p> }(response)
      TestApi.resolve("hosted") returns hostedIpAddress
      TestApi.resolve("unhosted") returns unhostedIpAddress

      GET(endpoint / "?address=unhosted")

      there was one(TestApi.unhostedView).apply(any)
    }
  }

  def GET(request: Request) = Http(request as_str)

  def status(request: Request) = new Http x (request as_str) {
    case (code, _, _, _) => code
  }

}
