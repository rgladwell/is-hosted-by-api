// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import java.net.InetAddress

trait Dns {

  case class Domain(name: String) {
    def resolve() = InetAddress.getByName(name)
  }

}
