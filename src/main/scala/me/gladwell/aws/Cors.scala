// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

trait Cors {

  import io.mth.unfiltered.cors._

  val cors = Cors(
    CorsConfig(
      (_: String) => true,
      (_: String) => true,
      (_: List[String]) => true,
      true,
      Some(120),
      Nil
    )
  )

}
