// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell

import scala.concurrent.Future
import scala.language.implicitConversions

package object futures {

  implicit def augmentFutures[T](futures: Seq[Future[T]]) = new Futures(futures)

}
