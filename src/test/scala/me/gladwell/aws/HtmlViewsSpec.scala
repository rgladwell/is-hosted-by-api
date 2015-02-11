// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import scala.xml.XML
import org.specs2.matcher.XmlMatchers
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import me.gladwell.aws.test.MockUnfilitered
import java.io.ByteArrayOutputStream

object HtmlViewsSpec extends Specification with HtmlViews with XmlMatchers with MockUnfilitered {
 
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

  "The index view" should {
    "should be HTML" in {
      val response = mockResponse()
      index(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return a form" in {
      html(index) must \\("form")
    }

    "return a form with address field" in {
      html(index) must \\("input", "name" -> "address")
    }
  }

  "The result view" should {
    "should be HTML" in {
      val response = mockResponse()
      resultView(true)(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return a result" in {
      html(resultView(true)) must \\("span", "id" -> "is-aws")
    }
  }

  "The error view" should {
    "should be HTML" in {
      val response = mockResponse()
      errorView(new Exception())(response)
      there was one(response).header("Content-Type", "text/html; charset=utf-8")
    }

    "return an error message" in {
      html(errorView(new Exception("MESSAGE"))) must \\("div", "id" -> "error") \ ("p") \> "MESSAGE"
    }
  }

}
