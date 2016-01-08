// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.{After, Specification}
import dispatch.classic._
import java.net.InetAddress
import unfiltered.specs2.netty.Served
import org.specs2.specification.Scope
import unfiltered.specs2.Hosted
import javax.servlet.Filter
import org.specs2.mock.Mockito
import scala.io.Source
import org.specs2.matcher.XmlMatchers
import scala.xml.XML
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.net.URLEncoder
import me.gladwell.aws.test.TestConfiguration
import me.gladwell.aws.test.MockHtmlViews
import scala.concurrent.Future
import io.netty.channel.ChannelHandler
import java.net.UnknownHostException
import me.gladwell.aws.net.IpPrefix
import scala.concurrent.ExecutionContext

object ApiSpec extends Specification with Mocks with XmlMatchers {

  import dispatch._

  val hostedIpAddress =  mock[InetAddress]
  val unhostedIpAddress =  mock[InetAddress]

  trait MockNetworks extends Networks {

    import scala.concurrent.ExecutionContext.Implicits.global

    val networksMock = mock[() => Future[Seq[Network]]]
    override def networks()(implicit executor: ExecutionContext) = networksMock()

    networksMock() returns Future(Seq(mockNetwork))

    object mockNetwork extends Network with MockDns {
      case class MockIpPrefix(range: InetAddress) extends IpPrefix {
        def inRange(address: InetAddress): Boolean = (address == range)
      }

      override val name = "mock-network"

      override val ipRanges = Seq(MockIpPrefix(hostedIpAddress))

      resolve(anyString) returns Future{ throw new RuntimeException("mock exception") }
      resolve("hosted") returns Future{ hostedIpAddress }
      resolve("unhosted") returns Future{ unhostedIpAddress }
      resolve("unknown") returns Future{ throw new UnknownHostException("mock exception") }
    }

  }

  trait ServedScope extends Hosted with Scope with Cors with After {
    this: ChannelHandler =>

    import unfiltered.netty._

    lazy val server = Server.http(port).handler(cors).handler(this)

    server.start()

    def after = {
      server.stop()
      server.destroy()
    }
  }

  trait TestApiScope extends Api
    with MockHtmlViews
    with MockNetworks
    with TestConfiguration
    with ServedScope {

    def endpoint = url(s"http://localhost:$port")
  }

  "The HTTP API index endpoint" should {
    "on GET request" in {

      "return OK response" in new TestApiScope {
        status(endpoint) must_== 200
      }

      "allow cross-origin requests" in new TestApiScope {
        headers(endpoint <:< Map("Origin" -> "http://localhost")) must havePair("Access-Control-Allow-Origin" -> "*")
      }

    }
  }

  "The HTTP API address lookup endpoint" should {

    "on network lookup" in {

      "return OK response" in new TestApiScope {
        status(endpoint / "?address=unhosted") must_== 200
      }

      "return hosted view for hosted address" in new TestApiScope {
        html(body(endpoint / "?address=hosted")) must \\("span", "id" -> "is-aws") \> "true"
      }

      "return network name for hosted address" in new TestApiScope {
        html(body(endpoint / "?address=hosted")) must \\("span", "id" -> "network-name") \> "mock-network"
      }

      "return unhosted view for unhosted address" in new TestApiScope {
        html(body(endpoint / "?address=unhosted")) must \\("span", "id" -> "is-aws") \> "false"
      }

      "return error for error on DNS lookup" in new TestApiScope {
        status(endpoint / "?address=error") must_== 500
      }

      "return error view for error on DNS lookup" in new TestApiScope {
         html(body(endpoint / "?address=error")) must \\("div", "id" -> "error")
      }

      "return error view for error on aquiring network IP range" in new TestApiScope {
         networksMock() returns Future{ throw new RuntimeException("mock exception") }
         html(body(endpoint / "?address=unhosted")) must \\("div", "id" -> "error")
      }

      "allow cross-origin requests" in new TestApiScope {  
        headers(endpoint / "?address=unhosted" <:< Map("Origin" -> "http://localhost")) must havePair("Access-Control-Allow-Origin" -> "*")
      }

      "handle URLs" in new TestApiScope {
        val encoded = URLEncoder.encode("http://hosted:8080/path?name=value", "UTF-8");
        html(body(endpoint / s"?address=$encoded")) must \\("span", "id" -> "is-aws") \> "true"
      }

      "return not found for unknown DNS lookup" in new TestApiScope {
        status(endpoint / "?address=unknown") must_== 404
      }

      "return DNS record does not exist validation message for non-existant DNS record" in new TestApiScope {
        html(body(endpoint / "?address=unknown")) must \\ ("p", "class" -> "dns-does-not-exist")
      }

      "return query result for non-existant DNS record" in new TestApiScope {
        html(body(endpoint / s"?address=unknown")) must \\("span", "id" -> "is-aws") \> "false"
      }

      object negativeMockNetwork extends Network with MockDns {
        case class FailingMockIpPrefix() extends IpPrefix {
          def inRange(address: InetAddress): Boolean = false
        }

        override val ipRanges = Seq(FailingMockIpPrefix())
        override val name = "FailingMock Network"

        resolve("hosted") returns Future{ hostedIpAddress }
      }

      "return success if only last result is positive" in new TestApiScope {
        networksMock() returns Future{ Seq( negativeMockNetwork, mockNetwork ) }
        html(body(endpoint / "?address=hosted")) must \\("span", "id" -> "is-aws") \> "true"
      }

    }

  }

  def GET(request: Request): Unit = status(request)

  def status(request: Request) = new Http x (request as_str) {
    case (code, _, _, _) => code
  }

  def body(request: Request): String = new Http x (request as_str) {
    case (_, response, _, _) => {
      val content = response.getEntity.getContent
      Source.fromInputStream(content).getLines().mkString("\n")
    }
  }

  def html(content: String) = {
    val parser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
    parser.loadString(content)
  }

  def headers(request: Request) = new Http x (request as_str) {
    case (_, response, _, _) => {
      val headers = response.getAllHeaders.toList
      (headers.map { header => (header.getName, header.getValue) }).toMap
    }
  }

}
