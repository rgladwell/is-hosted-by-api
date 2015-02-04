// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

trait Amazon {

  val aws: Aws = new Aws;

  class Aws {
    def isHosted(address: String): Boolean = false
  }

}
