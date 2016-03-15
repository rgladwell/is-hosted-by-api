// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import io.netty.handler.codec.http.cors.CorsHandler
import io.netty.handler.codec.http.cors.CorsConfig.withAnyOrigin
import io.netty.channel.ChannelHandler.Sharable

trait Cors {

  @Sharable class ApiCorsHandler extends CorsHandler(withAnyOrigin().build())

  val cors = new ApiCorsHandler

}
