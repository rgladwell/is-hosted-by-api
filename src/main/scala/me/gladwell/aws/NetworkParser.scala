// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.io.InputStream

trait NetworkParser {

  def parseNetwork(input: InputStream): Either[Exception, Seq[Network]]

}
