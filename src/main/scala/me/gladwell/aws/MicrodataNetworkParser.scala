// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import microtesia._
import microtesia.formats._
import me.gladwell.aws.net._
import java.io.InputStream
import java.net.URI
import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Failure, Success, Try}

trait MicrodataNetworkParser extends NetworkParser {

  private case class NetworkRange(cidr: Option[String], from: Option[String], to: Option[String])

  private case class MicrodataNetwork(name: String, ranges: Seq[NetworkRange], `sub-networks`: Seq[MicrodataNetwork]) extends Network with Dns {

    private def forAllSubNetworks[T](f: (MicrodataNetwork) => Seq[T]): Seq[T] = {
      val results = for {
        network <- `sub-networks`
      } yield f(network)

      results.flatten
    }

    private lazy val cidrs: Seq[IpPrefix] = {
      val cidrs = for {
        NetworkRange(Some(cidr), _, _) <- ranges
      } yield CidrNotationIpPrefix(cidr)

      cidrs ++ forAllSubNetworks{ _.cidrs }
    }

    private lazy val ips: Seq[IpPrefix] = {
      val ips = for {
        NetworkRange(_, Some(from), Some(to)) <- ranges
      } yield IpRangePrefix(from, to)

      ips ++ forAllSubNetworks{ _.ips }
    }

    override lazy val ipRanges = cidrs ++ ips

  }

  override def parseNetwork(input: InputStream)(implicit context: ExecutionContext): Future[Seq[Network]] = {
    val itemtype = new URI("https://ip-ranges.is-hosted-by.com/schemas/network.html")

    val tryNetworks = parseMicrodata(input).map { _.convertRootsTo[MicrodataNetwork](itemtype) }

    tryNetworks.map {
      case Success(networks)             => networks
      case Failure(e: InvalidMicrodata) => throw new RuntimeException(s"Error at line ${e.line} column ${e.column}: ${e.message}")
      case Failure(e)                   => throw e
    }
  }

}
