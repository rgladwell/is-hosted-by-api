// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.InetAddress
import scala.io.Source.fromInputStream
import org.apache.commons.net.util.SubnetUtils

trait AmazonNetwork extends Network {
  this: Configuration =>

  case class CidrNotationIpPrefix(notation: String) extends IpPrefix {
    private val subnet = new SubnetUtils(notation)
    override def inRange(ip: InetAddress) = subnet.getInfo.isInRange(ip.getHostAddress)
  }

  override lazy val ipRanges = {
    import org.json4s._
    import org.json4s.native.JsonMethods._

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