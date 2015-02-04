// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import dispatch.classic._
import org.specs2.matcher.XmlMatchers

object ApiSpec extends Specification with unfiltered.specs2.jetty.Served with XmlMatchers {

  import dispatch._
  import tagsoup.TagSoupHttp._

  def setup = { _.plan(Api) }

  def endpoint = url(s"http://localhost:$port")

  def status(request: Request) = new Http x (request as_str) {
    case (code, _, _, _) => code
  }

  def contentType(request: Request) = new Http x (request as_str) {
    case (_, response, _, _) => response.getHeaders("Content-Type")(0).getValue
  }

  def html(request: Request) = new Http x (request as_tagsouped)

  "The HTTP API" should {
    "return OK response for an index request" in {
      status(endpoint) must_== 200
    }

    "return html response for an index request" in {
      contentType(endpoint) must startWith("text/html")
    }

    "return a form for an index request" in {
      html(endpoint) must \\("form")
    }

    "return a form with address field for an index request" in {
      html(endpoint) must \\("input", "name" -> "address")
    }

    "return OK response for an address lookup" in {
      status(endpoint / "?address=example.com") must_== 200
    }

    "return html response for an address lookup" in {
      contentType(endpoint / "?address=example.com") must startWith("text/html")
    }

    "return a result for an address lookup" in {
      html(endpoint / "?address=example.com")  must \\("span", "id" -> "is-aws")
    }
  }

}
