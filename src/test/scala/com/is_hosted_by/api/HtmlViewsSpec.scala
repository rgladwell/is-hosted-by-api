// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import scala.xml.XML
import org.specs2.matcher.XmlMatchers
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.io.ByteArrayOutputStream
import test._
import microtesia._
import urimplicit._
import scala.util.{Failure, Success}

object HtmlViewsSpec extends Specification with XmlMatchers with MockUnfilitered {

  trait TestHtmlViews extends HtmlViews with TestConfiguration with Scope {

    def body(view: View) = {
      val response = mockResponse()
      view(response)

      val buffer = response.outputStream match {
        case b: ByteArrayOutputStream => b
        case _ => throw new ClassCastException
      }

      new String(buffer.toByteArray)
    }

    def html(view: View) = {
      val parser = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())
      parser.loadString(body(view))
    }

    def microdata(view: View): MicrodataDocument = parseMicrodata(body(view)) match {
      case Success(md) => md
      case Failure(e) => throw e;
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

    "return link to style-sheet" in new TestHtmlViews {
      html(index) must \\("link", "href" -> "http://example.org/style.min.css")
    }
  }

  "The result view" should {

    val lookup = NetworkLookup("example.org", Some("Amazon"), true)

    "be HTML" in new TestHtmlViews {
      val response = mockResponse()
      resultView("http://example.org", lookup)(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return microdata" in new TestHtmlViews {
      microdata(resultView("http://example.org", lookup)).rootItems must not be empty
    }

    "return search action" in new TestHtmlViews {
      microdata(resultView("http://example.org", lookup)).rootItems(URI("http://schema.org/SearchAction")) must not be empty
    }

    "return hosted network" in new TestHtmlViews {
      val result = microdata(resultView("http://example.org", lookup)).rootItems(URI("http://schema.org/SearchAction")).head
      result("network") must be contain(MicrodataString("Amazon"))
      result("result").head must beLike { case item: MicrodataItem => item("network") must contain(MicrodataString("Amazon")) }
   }

    "return host looked up"  in new TestHtmlViews {
      val result = microdata(resultView("http://example.org", lookup)).rootItems(URI("http://schema.org/SearchAction")).head
      result("result").head must beLike { case item: MicrodataItem => item("host") must contain(MicrodataString("example.org")) }
    }

    "return result" in new TestHtmlViews {
      val result = microdata(resultView("http://example.org", lookup)).rootItems(URI("http://schema.org/SearchAction")).head
      result("result") must not be empty
    }

    "return an result with type `Thing`" in new TestHtmlViews {
      val result = microdata(resultView("http://example.org", lookup)).rootItems(URI("http://schema.org/SearchAction")).head
      result("result").head must beLike { case item: MicrodataItem => item.itemtype must beSome(URI("http://schema.org/Thing")) }
    }

    "return result description" in new TestHtmlViews {
      val result = microdata(resultView("http://example.org", lookup)).rootItems(URI("http://schema.org/SearchAction")).head
      result("result").head must beLike { case item: MicrodataItem => item("description") must not be empty }
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

    "return link to style-sheet" in new TestHtmlViews {
      html(index) must \\("link", "href" -> "http://example.org/style.min.css")
    }
  }

  "The error view" should {
    "be HTML" in new TestHtmlViews {
      val response = mockResponse()
      errorView(new Exception(""))(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return microdata" in new TestHtmlViews {
      microdata(errorView(new Exception(""))).rootItems must not be empty
    }

    "return search action" in new TestHtmlViews {
      microdata(errorView(new Exception(""))).rootItems(URI("http://schema.org/SearchAction")) must not be empty
    }

    "return an error" in new TestHtmlViews {
      microdata(errorView(new Exception(""))).rootItems(URI("http://schema.org/SearchAction")).head("error") must not be empty
    }

    "return an error with type `Thing`" in new TestHtmlViews {
      val result = microdata(errorView(new Exception(""))).rootItems(URI("http://schema.org/SearchAction")).head
      result("error").head must beLike { case item: MicrodataItem => item.itemtype must beSome(URI("http://schema.org/Thing")) }
    }

    "return error description" in new TestHtmlViews {
      val result = microdata(errorView(new Exception(""))).rootItems(URI("http://schema.org/SearchAction")).head
      result("error").head must beLike { case item: MicrodataItem => item("description") must not be empty }
    }

    "return link to remote hosted assets" in new TestHtmlViews {
      html(errorView(new Exception(""))) must \\("link", "href" -> "http://example.org/assets.html")
    }

    "return link to style-sheet" in new TestHtmlViews {
      html(index) must \\("link", "href" -> "http://example.org/style.min.css")
    }
  }

}
