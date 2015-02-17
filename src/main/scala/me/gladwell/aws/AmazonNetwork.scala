// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.InetAddress
import scala.io.Source.fromInputStream
import org.apache.commons.net.util.SubnetUtils
import org.slf4s.Logging

trait AmazonNetwork extends Network with Logging {
  this: Configuration =>

  case class CidrNotationIpPrefix(notation: String) extends IpPrefix {
    private val subnet = new SubnetUtils(notation)
    override def inRange(ip: InetAddress) = subnet.getInfo.isInRange(ip.getHostAddress)
  }

  override val ipRanges = { () =>
    log.info(s"downloading Amazon IP ranges from url=[$awsIpRangeLocation]")

    import org.json4s._
    import org.json4s.native.JsonMethods._

    // TODO use caching HTTP client to load IP ranges
    val json = parse(fromInputStream(awsIpRangeLocation.toURL.openStream).mkString)

    for {
      JArray(prefixes) <- json
      JObject(prefix) <- prefixes
      JField("ip_prefix", JString(cidr)) <- prefix
    } yield {
      CidrNotationIpPrefix(cidr)
    }
  }

}
