// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

object HerokuConfigurationSpec extends Specification {

  class MockEnvironmentVariables(map: Map[String, String] = Map()) extends EnvironmentVariables with Scope {
    override val environmentVariables = map
  }

  "The Heroky Configuration" should {
    "return the port number from the PORT envvar" in new MockEnvironmentVariables(Map("PORT" -> "123456")) with HerokuConfiguration with Scope {
      port must_== 123456
    }
  }

  "The Heroky Configuration" should {
    "return port 8080 if the PORT envvar is unspecified" in new MockEnvironmentVariables with HerokuConfiguration with Scope {
      port must_== 8080
    }
  }

}
