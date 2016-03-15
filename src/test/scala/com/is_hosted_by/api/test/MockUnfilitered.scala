// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api.test

import org.specs2.mock.Mockito
import unfiltered.response.HttpResponse
import java.nio.charset.StandardCharsets
import java.io.ByteArrayOutputStream

trait MockUnfilitered extends Mockito {

  def mockResponse() = {
    val response = mock[HttpResponse[Nothing]]
    val charset = StandardCharsets.UTF_8
    val buffer = new ByteArrayOutputStream

    response.outputStream returns buffer
    response.charset returns charset

    response
  }

}
