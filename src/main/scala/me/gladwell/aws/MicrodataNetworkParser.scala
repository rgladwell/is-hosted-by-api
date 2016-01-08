// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import microtesia._
import me.gladwell.aws.net._
import scala.concurrent.Future
import java.io.InputStream
import java.net.URI

trait MicrodataNetworkParser extends NetworkParser {

  private class MicrodataNetwork(private val network: MicrodataItem) extends Network with Dns {

    override lazy val name = {
      val names =
        for {
          MicrodataProperty("name", MicrodataString(name)) <- network.properties
        } yield name

      names.headOption.get
    }

    private lazy val ranges = {
      def recurse(networks: Seq[MicrodataItem]): Seq[MicrodataValue] = {
        val ranges =
          for {
            network <- networks
            MicrodataProperty("ranges", range: MicrodataItem) <- network.properties
          } yield range

        val subnets: Seq[MicrodataItem] =
          for {
            network <- networks
            MicrodataProperty("sub-networks", subnet: MicrodataItem) <- network.properties
          } yield subnet

        if(subnets.isEmpty) ranges
        else ranges ++ recurse(subnets)
      }

      recurse(Seq(network))
    }

    private lazy val cidrs: Seq[IpPrefix] =
      for {
        MicrodataItem(properties, _, _) <- ranges
        MicrodataProperty("cidr", MicrodataString(cidr)) <- properties
      } yield CidrNotationIpPrefix(cidr)

    private lazy val ips: Seq[IpPrefix] =
      for {
        MicrodataItem(properties, _, _) <- ranges
        MicrodataProperty("from", MicrodataString(from)) <- properties
        MicrodataProperty("to", MicrodataString(to)) <- properties
      } yield IpRangePrefix(from, to)

    override lazy val ipRanges = cidrs ++ ips

  }

  override def parseNetwork(input: InputStream): Either[Exception, Seq[Network]] =
    parse(input)
      .right.map { document =>
          for {
            network <- document.findItems(new URI("https://ip-ranges.is-hosted-by.com/schemas/network.html"))
          } yield new MicrodataNetwork(network)
      }
      .left.map { invalid =>
        new RuntimeException(s"Error at line ${invalid.line} column ${invalid.column}: ${invalid.message}")
      }

}
