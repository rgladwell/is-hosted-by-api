// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import org.slf4s.Logging

trait MicrodataNetworks extends Networks with Logging {
  this: Configuration with NetworkParser =>

  private def microdataNetworks()(implicit executor: ExecutionContext): Future[Seq[Network]] = {
    log.info(s"downloading microdata IP ranges from url=[$ipRangeLocation]")
    parseNetwork(ipRangeLocation.toURL.openStream)
  }

  override def networks()(implicit executor: ExecutionContext) =
    for {
      ns <- super.networks()
      ms <- microdataNetworks()
    } yield(ns ++ ms)

}
