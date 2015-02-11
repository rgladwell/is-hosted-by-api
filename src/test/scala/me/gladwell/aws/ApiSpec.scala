// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import dispatch.classic._
import unfiltered.response.Html5
import me.gladwell.aws.test.MockUnfilitered

object ApiSpec extends Specification with unfiltered.specs2.jetty.Served with MockUnfilitered {

  import dispatch._

  trait MockViews extends Views {
    override val index = mock[View]

    private val _resultView = mock[View]
    override def resultView(result: Boolean) = _resultView
    override def errorView(error: Throwable) = mock[View]
  }

  trait MockNetwork extends Network {
    override val ipRanges = Seq()
  }

  object TestApi extends Api
    with MockViews
    with MockNetwork
    with Dns {
  }

  def setup = { _.plan(TestApi) }

  def endpoint = url(s"http://localhost:$port")

  "The HTTP API" should {
    "return OK response for an index request" in {
      val response = mockResponse()
      TestApi.index.apply(any) returns Html5 { <p></p> }(response)
      status(endpoint) must_== 200
    }

    "return OK response for an address lookup" in {
      val response = mockResponse()
      TestApi.resultView(true).apply(any) returns Html5 { <p></p> }(response)
      status(endpoint / "?address=example.com") must_== 200
    }
  }

  def status(request: Request) = new Http x (request as_str) {
    case (code, _, _, _) => code
  }

}
