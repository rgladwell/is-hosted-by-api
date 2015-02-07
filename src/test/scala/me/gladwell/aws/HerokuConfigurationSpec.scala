// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import java.net.URI

object HerokuConfigurationSpec extends Specification {

  class MockEnvironmentVariables(map: Map[String, String] = Map()) extends EnvironmentVariables with HerokuConfiguration with Scope {
    override val environmentVariables = map
  }

  "The Heroku Configuration" should {
    "return the port number from the PORT envvar" in new MockEnvironmentVariables(Map("PORT" -> "123456")) {
      port must_== 123456
    }

    "return port 8080 if the PORT envvar is unspecified" in new MockEnvironmentVariables {
      port must_== 8080
    }

    "return URL from the AWS_IPRANGE_LOCATION envvar" in new MockEnvironmentVariables(Map("AWS_IPRANGE_LOCATION" -> "http://example.com")) {
      awsIpRangeLocation must_== new URI("http://example.com")
    }

    "throw exception when AWS_IPRANGE_LOCATION envvar unset" in new MockEnvironmentVariables {
      awsIpRangeLocation must throwA[RuntimeException]
    }
  }

}
