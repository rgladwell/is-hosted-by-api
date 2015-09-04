// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import scala.xml.XML
import org.specs2.matcher.XmlMatchers
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.io.ByteArrayOutputStream
import java.net.URI
import me.gladwell.aws.test._

object HtmlViewsSpec extends Specification with XmlMatchers with MockUnfilitered {

  trait TestHtmlViews extends HtmlViews with TestConfiguration with Scope {

    def html(view: View) = {
      val response = mockResponse()
      view(response)

      val buffer = response.outputStream match {
        case b: ByteArrayOutputStream => b
        case _ => throw new ClassCastException
      }

      val body = new String(buffer.toByteArray)
      val parser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
      parser.loadString(body)
    }

  }

  "The index view" should {
    "be HTML" in new TestHtmlViews {
      val response = mockResponse()
      index(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return a form" in new TestHtmlViews {
      html(index) must \\("form", "data-rel" -> "next")
    }

    "return a form with address field" in new TestHtmlViews {
      html(index) must \\("input", "name" -> "address")
    }

    "return link to remote hosted assets" in new TestHtmlViews {
      html(index) must \\("link", "href" -> "http://example.org/assets.html")
    }
  }

  "The result view" should {

    val lookup = NetworkLookup("example.org", true)

    "be HTML" in new TestHtmlViews {
      val response = mockResponse()
      resultView("http://example.org", lookup)(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return a result" in new TestHtmlViews {
      html(resultView("http://example.org", lookup)) must \\ ("div", "class" -> "h-network-lookup")
    }

    "return whether address is hosted on network" in new TestHtmlViews {
      html(resultView("http://example.org", lookup)) must \\ ("data", "class" -> "p-is-hosted", "value" -> "true")
    }

    "return host looked up" in new TestHtmlViews {
      html(resultView("http://example.org", lookup)) must \\ ("data", "class" -> "p-host") \> "example.org"
    }

    "return link to remote hosted assets" in new TestHtmlViews {
      html(resultView("http://example.org", lookup)) must \\("link", "href" -> "http://example.org/assets.html")
    }

    "return form for new lookups" in new TestHtmlViews {
      html(resultView("http://example.org", lookup)) must \\("form", "data-rel" -> "next")
    }

    "return form with pre-populated input field" in new TestHtmlViews {
      html(resultView("http://example.org", lookup)) must \\("input", "name" -> "address", "value" -> "http://example.org")
    }

    "classify validation errors" in new TestHtmlViews {
      val result = resultView("http://example.org", NetworkLookup("http://example.org", validation = Some(NoSuchDomainName)))
      html(result) must \\("h2", "title" -> NoSuchDomainName.id, "class" -> "p-invalid-input")
    }

    "return form with pre-populated input field on validation error" in new TestHtmlViews {
      val result = resultView("http://example.org", NetworkLookup("http://example.org", validation = Some(NoSuchDomainName)))
      html(result) must \\("input", "name" -> "address", "value" -> "http://example.org")
    }
  }

  "The error view" should {
    "be HTML" in new TestHtmlViews {
      val response = mockResponse()
      errorView(new Exception(""))(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return an error message" in new TestHtmlViews {
      html(errorView(new Exception(""))) must \\("div", "class" -> "h-error")
    }

    "return link to remote hosted assets" in new TestHtmlViews {
      html(errorView(new Exception(""))) must \\("link", "href" -> "http://example.org/assets.html")
    }
  }

}
