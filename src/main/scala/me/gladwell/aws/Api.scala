// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._

class Api extends unfiltered.filter.Plan {
  this: Views with Network with Dns =>

  object Address extends Params.Extract("address", Params.first)

  def intent = { 
    case GET(Path("/") & Params(Address(address))) =>{
      val ip = Domain(address).resolve()
      val result = ipRanges.exists{ prefix => prefix.inRange(ip)}
      Ok ~> resultView(result)
    } 
    case GET(Path("/")) => Ok ~> index()
  }

}

object Api extends Api with HerokuConfiguration
  with SystemEnvironmentVariables
  with HtmlViews
  with AmazonNetwork
  with Dns {

  def main(args: Array[String]) {
    unfiltered.jetty.Server.http(port).plan(this).run
  }

}
