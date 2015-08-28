// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import java.net.InetAddress
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait Dns {

  type Resolver = (String) => Future[InetAddress]

  def resolve: Resolver = { name => Future { InetAddress.getByName(name) } }

}
