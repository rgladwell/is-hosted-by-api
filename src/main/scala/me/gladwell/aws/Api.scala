// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._
import org.slf4s.Logging
import scala.util.{Success, Failure, Try}
import javax.servlet.http.HttpServletResponse

class Api extends unfiltered.filter.Plan with Cors with Logging {
  this: Views with Network with Dns =>

  object Address extends Params.Extract("address", Params.first)

  def intent = cors {

    case GET(Path("/") & Params(Address(address))) => {
      inNetwork(address) match {
        case Success(result) => Ok ~> resultView(result)
        case Failure(error) => errorHandler(error)
      }
    }

    case GET(Path("/")) => Ok ~> index()

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
  unfiltered.jetty.Server.http(port).plan(this).run

}
