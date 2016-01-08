package me.gladwell.aws.net

import java.net.InetAddress
import net.ripe.commons.ip._

case class CidrNotationIpPrefix(notation: String) extends IpPrefix {

  val prefix =
    try {
      new Ipv4RangePrefix(Ipv4Range.parse(notation))
    } catch {
        case e: Exception => try {
          new Ipv6RangePrefix(Ipv6Range.parse(notation))
        } catch {
          case _: Throwable => throw e
        }
    }

  override def inRange(ip: InetAddress) = prefix.inRange(ip)

}
