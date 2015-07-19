// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import java.net.InetAddress
import scala.util.Try

trait Dns {

  type Resolver = (String) => Try[InetAddress]

  def resolve: Resolver = { name => Try { InetAddress.getByName(name) } }

}
