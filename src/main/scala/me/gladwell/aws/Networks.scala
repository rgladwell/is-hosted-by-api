// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import me.gladwell.aws.net.Dns

trait Networks {

  def networks()(implicit executor: ExecutionContext): Future[Seq[Network]] = Future(Seq())

}
