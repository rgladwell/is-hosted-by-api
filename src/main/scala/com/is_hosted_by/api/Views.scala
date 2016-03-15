// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import unfiltered.response.ResponseFunction
import io.netty.handler.codec.http.HttpResponse

trait Views {

  type View = ResponseFunction[HttpResponse]

  def index(): View
  def resultView(query: String, result: NetworkLookup): View
  def errorView(error: Exception): View

}
