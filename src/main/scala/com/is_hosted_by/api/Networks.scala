// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait Networks {

  def networks()(implicit executor: ExecutionContext): Future[Seq[Network]] = Future(Seq())

}
