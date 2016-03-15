// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

sealed class InvalidInput(val id: String, val message: String)

case object NoSuchDomainName extends InvalidInput(
  id        = "dns-does-not-exist", 
  message   = "We could not find information on the domain."
)
