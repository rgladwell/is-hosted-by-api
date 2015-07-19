// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.response.ResponseFunction
import javax.servlet.http.HttpServletResponse

trait Views {

  type View = ResponseFunction[HttpServletResponse]

  def index(): View
  def resultView(result: NetworkLookup): View
  def errorView(error: Throwable): View

}
