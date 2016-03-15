// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

case class NetworkLookup(host: String, network: Option[String] = None, hosted: Boolean = false, validation: Option[InvalidInput] = None)
