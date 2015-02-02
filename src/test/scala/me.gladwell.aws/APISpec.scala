package me.gladwell.aws

import org.specs2.mutable.Specification

import dispatch.classic._

object APISpec extends Specification with unfiltered.specs2.jetty.Served {

  import dispatch._

  def setup = { _.filter(new API) }

  val http = new Http

  "The HTTP API" should {
    "return OK response for index request" in {
      val status = http x (host as_str) {
        case (code, _, _, _) => code
      }
      status must_== 200
    }
  }
}
