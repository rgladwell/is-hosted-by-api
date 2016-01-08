// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mutable.Specification
import org.specs2.matcher.FutureMatchers
import me.gladwell.aws.net.CidrNotationIpPrefix
import me.gladwell.aws.net.IpRangePrefix
import java.io.ByteArrayInputStream
import java.net.InetAddress.getByName

object MicrodataNetworkParserSpec extends Specification with MicrodataNetworkParser {

  "NetworkMicrodataParser should" >> {

    "parse wellformed network microdata document and" >> {

      val microdata = """
        <main role="main">

          <article itemscope itemtype="https://ip-ranges.is-hosted-by.com/schemas/network.html" id="digitalocean">
            <h2 itemprop="name">Digital Ocean</h2>

            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>IP Range</th>
                </tr>
              </thead>
              <tbody>
                <tr itemprop="ranges" itemscope>
                  <td itemprop="id">DIGITALOCEAN-5</td>
                  <td itemprop="cidr">198.199.64.0/18</td>
                </tr>
              </tbody>
            </table>

            <p>Regions:</p>

            <ul>
              <li><a href="#digitalocean-usa">New York/San Francisco</a></li>
            </ul>

            <section itemprop="sub-networks" itemscope id="digitalocean-usa">

              <h3 itemprop="name">New York/San Francisco</h3>

              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>IP Range</th>
                    <th>IP From</th>
                    <th>IP To</th>
                  </tr>
                </thead>
                <tbody>
                  <tr itemprop="ranges" itemscope>
                    <td itemprop="id">DIGITALOCEAN-1</td>
                    <td itemprop="cidr">208.68.36.0/22</td>
                  </tr>
                  <tr itemprop="ranges" itemscope>
                    <td itemprop="id">DIGITALOCEAN-2</td>
                    <td></td>
                    <td itemprop="from">208.68.36.1</td>
                    <td itemprop="to">208.68.36.2</td>
                  </tr>
                  <tr itemprop="ranges" itemscope>
                    <td itemprop="id">DIGITALOCEAN-3</td>
                    <td itemprop="cidr">208.68.36.0/23</td>
                  </tr>
                  <tr itemprop="ranges" itemscope>
                    <td itemprop="id">DIGITALOCEAN-4</td>
                    <td></td>
                    <td itemprop="from">208.68.36.3</td>
                    <td itemprop="to">208.68.36.4</td>
                  </tr>
                </tbody>
              </table>

              <section itemscope itemprop="sub-networks" id="digitalocean-usa-west">
  
                <h4 itemprop="name">New York</h4>
  
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>IP Range</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr itemprop="ranges" itemscope>
                      <td itemprop="id">DIGITALOCEAN-6</td>
                      <td itemprop="cidr">104.236.0.0/16</td>
                    </tr>
                  </tbody>
                </table>

              </section>

              <footer>
                <a itemprop="source" href="http://whois.arin.net/rest/org/DO-13/nets">Source</a>
              </footer>

            </section>
          </article>
        </main>"""

      "return success" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight
      }

      "parse name" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.name must_== "Digital Ocean"
        })
      }

      "parse CIDR range" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must contain(CidrNotationIpPrefix("208.68.36.0/22"))
        })
      }

      "parse IP range" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must contain(IpRangePrefix("208.68.36.1", "208.68.36.2"))
        })
      }

      "parse multiple CIDR ranges" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must contain(CidrNotationIpPrefix("208.68.36.0/23"))
        })
      }

      "parse multiple IP ranges" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must contain(IpRangePrefix("208.68.36.3", "208.68.36.4"))
        })
      }

      "not mix up to and from address in IP ranges" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must not contain(IpRangePrefix("208.68.36.1", "208.68.36.4"))
        })
      }

      "parse root CIDR range" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must contain(CidrNotationIpPrefix("198.199.64.0/18"))
        })
      }

      "parse deeply nested CIDR range" >> {
        parseNetwork(new ByteArrayInputStream(microdata.getBytes)) must beRight(contain{ (network: Network) =>
          network.ipRanges must contain(CidrNotationIpPrefix("104.236.0.0/16"))
        })
      }
    }
  }

}
