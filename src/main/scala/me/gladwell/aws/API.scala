// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._
import util.Properties

class API extends unfiltered.filter.Plan {
  this: Configuration =>

  def intent = {
    case GET(_) => Ok ~> ResponseString("Unfiltered on Heroku!")
  }

}

object API extends API with HerokuConfiguration with SystemEnvironmentVariables {

  def main(args: Array[String]) {
    unfiltered.jetty.Http(port).plan(this).run
  }

}
