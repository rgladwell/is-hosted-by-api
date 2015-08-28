// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.response.ResponseFunction
import io.netty.handler.codec.http.HttpResponse

trait Views {

  type View = ResponseFunction[HttpResponse]

  def index(): View
  def resultView(result: NetworkLookup): View
  def errorView(error: Throwable): View

}
