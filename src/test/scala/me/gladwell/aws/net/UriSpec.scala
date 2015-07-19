// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import org.specs2.mutable.Specification
import me.gladwell.aws.net._
import java.net.URI

object UriSpec extends Specification {
  
  "A URI" should {

    "match an address" in {
      "http://example.org" must beLike { case Uri(host) => host must_== "example.org" }
    }

    "not match a host" in {
      Uri.unapply("example.org") must beNone
    }

    "not match an IP address" in {
      Uri.unapply("192.173.55.87") must beNone
    }

    "correctly concatenate with another URI" in {
      new URI("http://example.org") + "/index.html" must_== new URI("http://example.org/index.html")
    }
  }

}
