// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws.net

import java.net.URI
import java.net.URISyntaxException
import org.slf4s.Logging

private[net] class Uri(uri: URI) {

  def + (uri2: URI): URI = uri.resolve(uri2)
  def + (uri2: String): URI = this + new URI(uri2)

}

object Uri extends Logging {

  def unapply(uri: String) = {
    try {
      Option(new URI(uri).getHost)
    } catch {
      case e : URISyntaxException => None
    }
  }

}
