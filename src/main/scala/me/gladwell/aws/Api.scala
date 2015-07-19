// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._
import org.slf4s.Logging
import scala.util.{Success, Failure, Try}
import javax.servlet.http.HttpServletResponse
import me.gladwell.aws.net.Uri
import me.gladwell.aws.net.Network
import me.gladwell.aws.net.Dns

class Api extends unfiltered.filter.Plan with Cors with Logging {
  this: Views with Network with Dns =>

  object Address extends Params.Extract("address", Params.first)

  def intent = cors {

    case GET(Path("/") & Params(Address(address))) => {
      address match {
        case Uri(host) => lookup(host, address)
        case _         => lookup(address, address)
      }
    }

    case GET(Path("/")) => Ok ~> index()
  }

  def lookup(host: String, query: String) = {
    inNetwork(host) match {
      case Success(result) => Ok ~> resultView(NetworkLookup(result, host, query))
      case Failure(error) => errorHandler(error)
    }
  }

  def errorHandler(error: Throwable) = {
    log.error("error matching address to AWS network", error)
    InternalServerError ~> errorView(error)
  }
}

object Api extends Api with App
  with HerokuConfiguration
  with SystemEnvironmentVariables
  with HtmlViews
  with AmazonNetwork
  with Dns {

  log.info("Starting is-aws API version 0.1")
  unfiltered.jetty.Server.http(hostPort).plan(this).run

}
