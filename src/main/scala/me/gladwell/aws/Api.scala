// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._
import unfiltered.netty._
import scala.concurrent._
import me.gladwell.aws.net._
import org.slf4s.Logging
import io.netty.channel.ChannelHandler.Sharable
import java.net.UnknownHostException

@Sharable
class Api extends unfiltered.netty.future.Plan with Logging with ServerErrorResponse {
  this: Views with Network with Dns =>

  implicit def executionContext = ExecutionContext.Implicits.global

  object Address extends Params.Extract("address", Params.first)

  def intent = {

    case GET(Path("/") & Params(Address(address))) => {
      address match {
        case Uri(host) => lookup(host, address)
        case _         => lookup(address, address)
      }
    }

    case GET(Path("/")) => Future{ Ok ~> index() }

  }

  private def lookup(host: String, query: String) = {
    inNetwork(host) map { result =>
      log.info(s"[$host] is in network=[$result]")
      Ok ~> resultView(query, NetworkLookup(host, result))
    } recover {
      case unknown : UnknownHostException  => NotFound             ~> resultView(query, NetworkLookup(host, validation = Some(NoSuchDomainName)))
      case error   : Exception             => InternalServerError  ~> errorView(error)
    }
  }

}

object Api extends Api with App
  with HerokuConfiguration
  with SystemEnvironmentVariables
  with HtmlViews
  with AmazonNetwork
  with Dns
  with Cors {

  log.info("Starting is-aws API version 0.1")
  unfiltered.netty.Server.http(hostPort).handler(cors).plan(this).run

}
