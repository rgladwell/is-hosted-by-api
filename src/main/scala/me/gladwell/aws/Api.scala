// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._

class Api extends unfiltered.filter.Plan {
  this: Configuration with Views with Amazon =>

  object Address extends Params.Extract("address", Params.first)

  def intent = { 
    case GET(Path("/") & Params(Address(address))) => Ok ~> resultView(aws.isHosted(address.toString))
    case GET(Path("/")) => Ok ~> index()
  }

}

object Api extends Api with HerokuConfiguration
  with SystemEnvironmentVariables
  with HtmlViews
  with Amazon {

  def main(args: Array[String]) {
    unfiltered.jetty.Server.http(port).plan(this).run
  }

}
