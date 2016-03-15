// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import unfiltered.request._
import unfiltered.response._
import unfiltered.netty._
import scala.concurrent._
import net._
import org.slf4s.Logging
import io.netty.channel.ChannelHandler.Sharable
import java.net.UnknownHostException
import me.gladwell.futures._
import urimplicit._

@Sharable
class Api extends unfiltered.netty.future.Plan with Logging with ServerErrorResponse {
  this: Views with Networks =>

  implicit def executionContext = ExecutionContext.Implicits.global

  object Address extends Params.Extract("address", Params.first)

  def intent = {

    case GET(Path("/") & Params(Address(address))) => {
      address match {
        case Uri(_, host) => lookup(host, address)
        case _            => lookup(address, address)
      }
    }

    case GET(Path("/")) => Future{ Ok ~> index() }

  }

  private def lookup(host: String, query: String) = {
    val future = networks()
                  .flatMap { _.map { n => n.inNetwork(host).map((_,n.name)) }.findWithFailure(_._1) }
                  .map{ t => NetworkLookup(host, t.map(_._2), t.map(_._1).getOrElse(false)) }
                  .map { result =>
                    log.info(s"[$host] is in network=[$result]")
                    Ok ~> resultView(query, result)
                  }

    future onFailure {
      case error : Exception =>log.error(s"Failed to perform network lookup for host=[$host", error)
    }

    future recover {
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
  with MicrodataNetworks
  with MicrodataNetworkParser
  with Cors {

  log.info("Starting is-aws API version 0.1")
  unfiltered.netty.Server.http(hostPort).handler(cors).plan(this).run

}
