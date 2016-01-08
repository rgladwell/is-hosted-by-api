// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.futures

import org.specs2.mutable.Specification
import scala.concurrent._
import org.specs2.matcher.FutureMatchers
import me.gladwell.aws.test.Eventually

object FuturesSpec extends Specification with FutureMatchers with Eventually {

  "Futures" should {

    "find result" in {
      val futures = Seq(Future(1))
      futures.findWithFailure(_ == 1) must beSome(1).await
    }

    "find result in many results" in {
      val futures = Seq(Future(3), Future(10), Future(1), Future(9))
      futures.findWithFailure(_ == 1) must beSome(1).await
    }

    "find result with failed results" in {
      val futures = Seq(Future(throw new RuntimeException), Future(1))
      futures.findWithFailure(_ == 1) must beSome(1).await
    }

    "not find result" in {
      val futures = Seq(Future(2))
      futures.findWithFailure(_ == 1) must beNone.await
    }

    "fail result" in {
      val futures = Seq(Future[Int](throw new RuntimeException))
      futures.findWithFailure(_ == 1).eventually must throwA[Exception]
    }


  }

}
