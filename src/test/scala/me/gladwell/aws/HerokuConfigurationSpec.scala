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
      hostPort must_== 123456
    }

    "return port 8080 if the PORT envvar is unspecified" in new MockEnvironmentVariables {
      hostPort must_== 8080
    }

    "return URL from the AWS_IPRANGE_LOCATION envvar" in new MockEnvironmentVariables(Map("AWS_IPRANGE_LOCATION" -> "http://example.com")) {
      awsIpRangeLocation must_== new URI("http://example.com")
    }

    "throw exception when AWS_IPRANGE_LOCATION envvar unset" in new MockEnvironmentVariables {
      awsIpRangeLocation must throwA[RuntimeException]
    }

    "return URL from the IS_AWS_ASSETS_LOCATION envvar" in new MockEnvironmentVariables(Map("IS_AWS_ASSETS_LOCATION" -> "http://example.com")) {
      assetsLocation must_== new URI("http://example.com")
    }

    "throw exception when IS_AWS_ASSETS_LOCATION envvar unset" in new MockEnvironmentVariables {
      assetsLocation must throwA[RuntimeException]
    }

    "return URL from the IPRANGES_LOCATION envvar" in new MockEnvironmentVariables(Map("IPRANGES_LOCATION" -> "http://ip-range.example.com")) {
      ipRangeLocation must_== new URI("http://ip-range.example.com")
    }

    "throw exception when IPRANGES_LOCATION envvar unset" in new MockEnvironmentVariables {
      ipRangeLocation must throwA[RuntimeException]
    }
  }

}
