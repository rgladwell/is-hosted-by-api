// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import org.slf4s.Logging

trait MicrodataNetworks extends Networks with Logging {
  this: Configuration with NetworkParser =>

  private def microdataNetworks()(implicit executor: ExecutionContext): Future[Seq[Network]] = {
    log.info(s"downloading microdata IP ranges from url=[$ipRangeLocation]")
    val connection = ipRangeLocation.toURL.openConnection
    connection.addRequestProperty("User-Agent", "is-hosted-by/1.0")
    parseNetwork(connection.getInputStream)
  }

  override def networks()(implicit executor: ExecutionContext) =
    for {
      ns <- super.networks()
      ms <- microdataNetworks()
    } yield(ns ++ ms)

}
