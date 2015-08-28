package me.gladwell.aws.test

import scala.language.implicitConversions
import scala.concurrent._
import scala.concurrent.duration._

trait Eventually {

  class EventualFuture[+T](val future: Future[T]) {
    def eventually: T = Await.result(future, 2.seconds)
  }

  implicit def futureToEventualFuture[T](future: Future[T]): EventualFuture[T] = new EventualFuture(future)

}
