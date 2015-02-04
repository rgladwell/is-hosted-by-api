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

  "The HTTP API" should {
    "return OK response for an index request" in {
      val status = new Http x (endpoint as_str) {
        case (code, _, _, _) => code
      }

      status must_== 200
    }

    "return html response for an index request" in {
      val contentType = new Http x (endpoint as_str) {
        case (_, response, _, _) => response.getHeaders("Content-Type")(0).getValue
      }

      contentType must startWith("text/html")
    }

    "return a form for an index request" in {
      val html = new Http x (endpoint as_tagsouped)
      html must \\("form")
    }

    "return a form with address field for an index request" in {
      val html = new Http x (endpoint as_tagsouped)
      html must \\("input", "name" -> "address")
    }

    "return OK response for an address lookup" in {
      val status = new Http x (endpoint / "?address=example.com" as_str) {
        case (code, _, _, _) => code
      }

      status must_== 200
    }

    "return html response for an address lookup" in {
      val contentType = new Http x (endpoint / "?address=example.com" as_str) {
        case (_, response, _, _) => response.getHeaders("Content-Type")(0).getValue
      }

      contentType must startWith("text/html")
    }

    "return a result for an address lookup" in {
      val html = new Http x (endpoint / "?address=example.com" as_tagsouped)
      html must \\("span", "id" -> "is-aws")
    }
  }

}
