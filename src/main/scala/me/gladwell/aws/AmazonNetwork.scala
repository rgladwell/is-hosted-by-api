// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import scala.concurrent._
import java.net.InetAddress
import scala.io.Source.fromInputStream
import org.slf4s.Logging
import me.gladwell.aws.net._

trait AmazonNetwork extends Networks with Logging {
  this: Configuration =>

  private def amazon()(implicit executor: ExecutionContext) = {
    Future {
      log.info(s"downloading Amazon IP ranges from url=[$awsIpRangeLocation]")

      import org.json4s._
      import org.json4s.native.JsonMethods._

      val json = parse(fromInputStream(awsIpRangeLocation.toURL.openStream).mkString)

      new Network() with Dns {
        override val name = "Amazon"

        override val ipRanges = {
          for {
            JArray(prefixes) <- json
            JObject(prefix) <- prefixes
            JField("ip_prefix", JString(cidr)) <- prefix
          } yield {
            CidrNotationIpPrefix(cidr)
          }
        }
      }
    }
  }

  override def networks()(implicit executor: ExecutionContext) =
    for {
      ns <- super.networks()
      a <- amazon()
    } yield(ns.+:(a))

}
