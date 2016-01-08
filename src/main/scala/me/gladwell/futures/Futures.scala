// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.futures

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

class Futures[T](futures: Seq[Future[T]]) {
  
  def findWithFailure(p: (T) => Boolean)(implicit context: ExecutionContext): Future[Option[T]] = {
    val tries = futures.map{ _.map(Success(_)).recover{ case t: Throwable => Failure(t) } }

    val sequence = Future.sequence(tries)

    val positive = sequence.map{ _.find{ case Success(r) => p(r); case _ => false } }
    val failure = sequence.map{ _.find{ case Failure(_) => true; case _ => false } }

    val futureTry =
      for {
        p <- positive
        f <- failure
      } yield( p.orElse(f) )

    futureTry.map { _.map { t => t.get } }
  }

}
