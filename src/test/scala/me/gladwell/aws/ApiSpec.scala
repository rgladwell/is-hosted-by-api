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
import javax.servlet.Filter
import scala.util.{Success, Failure}

object ApiSpec extends Specification with MockUnfilitered {

  import dispatch._

  val hostedIpAddress =  mock[InetAddress]
  val unhostedIpAddress =  mock[InetAddress]

  trait MockViews extends Views {
    override val index = mock[View]

    val hostedView = mock[View]
    val unhostedView = mock[View]

    override def resultView(result: Boolean) = if(result) hostedView else unhostedView

    val errorView = mock[View]

    override def errorView(error: Throwable) = errorView
  }

  trait MockNetwork extends Network {
    case class MockIpPrefix(range: InetAddress) extends IpPrefix {
      def inRange(address: InetAddress): Boolean = (address == range)
    }

    override val ipRanges = mock[IpRangeLoader]
  }

  trait MockDns extends Dns {
    override val resolve = mock[Resolver]
  }

  trait ServedScope extends Hosted with Scope {
    this: Filter =>

    import unfiltered.jetty._

    lazy val server = Server.http(port).plan(this)

    server.start()
  }

  trait TestApiScope extends Api
    with MockViews
    with MockNetwork
    with MockDns
    with ServedScope {

    def endpoint = url(s"http://localhost:$port")
  }

  "The HTTP API" should {
    "return OK response for an index request" in new TestApiScope {
      val response = mockResponse()
      index.apply(any) returns Html5 { <p></p> }(response)
      status(endpoint) must_== 200
    }

    "return OK response for an address lookup" in new TestApiScope {
      val response = mockResponse()
      hostedView.apply(any) returns Html5 { <p></p> }(response)
      unhostedView.apply(any) returns Html5 { <p></p> }(response)
      resolve("hosted") returns Success(hostedIpAddress)
      resolve("unhosted") returns Success(unhostedIpAddress)
      ipRanges.apply() returns Success(Seq(MockIpPrefix(hostedIpAddress)))

      status(endpoint / "?address=unhosted") must_== 200
    }

    "return hosted view for hosted address" in new TestApiScope {
      val response = mockResponse()
      hostedView.apply(any) returns Html5 { <p></p> }(response)
      unhostedView.apply(any) returns Html5 { <p></p> }(response)
      resolve("hosted") returns Success(hostedIpAddress)
      resolve("unhosted") returns Success(unhostedIpAddress)
      ipRanges.apply() returns Success(Seq(MockIpPrefix(hostedIpAddress)))

      GET(endpoint / "?address=hosted")

      there was one(hostedView).apply(any)
    }

    "return unhosted view for unhosted address" in new TestApiScope {
      val response = mockResponse()
      hostedView.apply(any) returns Html5 { <p></p> }(response)
      unhostedView.apply(any) returns Html5 { <p></p> }(response)
      resolve("hosted") returns Success(hostedIpAddress)
      resolve("unhosted") returns Success(unhostedIpAddress)
      ipRanges.apply() returns Success(Seq(MockIpPrefix(hostedIpAddress)))

      GET(endpoint / "?address=unhosted")

      there was one(unhostedView).apply(any)
    }

    "return error view for error on DNS lookup" in new TestApiScope {
      val response = mockResponse()
      hostedView.apply(any) returns Html5 { <p></p> }(response)
      unhostedView.apply(any) returns Html5 { <p></p> }(response)
      errorView.apply(any) returns Html5 { <p></p> }(response)
      resolve(any) returns Failure(new RuntimeException("mock exception"))
      ipRanges.apply() returns Success(Seq(MockIpPrefix(hostedIpAddress)))

      GET(endpoint / "?address=unhosted")

      there was one(errorView).apply(any)
    }

    "return error view for error on aquiring network IP range" in new TestApiScope {
      val response = mockResponse()
      hostedView.apply(any) returns Html5 { <p></p> }(response)
      unhostedView.apply(any) returns Html5 { <p></p> }(response)
      errorView.apply(any) returns Html5 { <p></p> }(response)
      resolve("hosted") returns Success(hostedIpAddress)
      resolve("unhosted") returns Success(unhostedIpAddress)
      ipRanges.apply() returns Failure(new RuntimeException("mock exception"))

      GET(endpoint / "?address=unhosted")

      there was one(errorView).apply(any)
    }
  }

  def GET(request: Request): Unit = status(request)

  def status(request: Request) = new Http x (request as_str) {
    case (code, _, _, _) => code
  }


}
