// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.io.InputStream
import scala.concurrent.{ExecutionContext, Future}

trait NetworkParser {

  def parseNetwork(input: InputStream)(implicit context: ExecutionContext): Future[Seq[Network]]

}
