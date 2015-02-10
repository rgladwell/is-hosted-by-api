// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._
import org.slf4s.Logging
import scala.util.{Success, Failure, Try}

class Api extends unfiltered.filter.Plan with Logging {
  this: Views with Network with Dns =>

  object Address extends Params.Extract("address", Params.first)

  def isAws(address: String) = Try {
    val ip = Domain(address).resolve()
    ipRanges.exists{ prefix => prefix.inRange(ip)}
  }

  def intent = {
    case GET(Path("/") & Params(Address(address))) =>{
      isAws(address) match {
        case Success(result) => Ok ~> resultView(result)
        case Failure(error) => {
          log.error("error matching address to AWS network", error)
          InternalServerError ~> errorView(error)
        }
      }
    }
    case GET(Path("/")) => Ok ~> index()
  }

}

object Api extends Api with App
  with HerokuConfiguration
  with SystemEnvironmentVariables
  with HtmlViews
  with AmazonNetwork
  with Dns
  with Logging {

  log.info("Starting is-aws API version 0.1")
  unfiltered.jetty.Server.http(port).plan(this).run

}
