// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.URI
import java.net.URISyntaxException
import org.slf4s.Logging

object Uri extends Logging {

  def unapply(uri: String) = {
    try {
      Option(new URI(uri).getHost)
    } catch {
      case e : URISyntaxException => None
    }
  }

}
