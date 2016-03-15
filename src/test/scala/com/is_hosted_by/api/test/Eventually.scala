// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api.test

import scala.language.implicitConversions
import scala.concurrent._
import scala.concurrent.duration._

trait Eventually {

  class EventualFuture[+T](val future: Future[T]) {
    def eventually: T = Await.result(future, 2.seconds)
  }

  implicit def futureToEventualFuture[T](future: Future[T]): EventualFuture[T] = new EventualFuture(future)

}
