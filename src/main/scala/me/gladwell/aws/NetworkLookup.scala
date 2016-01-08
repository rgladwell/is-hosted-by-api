// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

case class NetworkLookup(host: String, network: Option[String] = None, hosted: Boolean = false, validation: Option[InvalidInput] = None)
