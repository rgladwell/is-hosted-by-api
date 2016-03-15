package com.is_hosted_by.api

import scala.language.postfixOps
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PerformanceTest extends Simulation {

  def domainNames = csv("domains.txt").random

  val browser = http
    .baseURL("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

  val networkLookup = scenario("PerformanceTest")
    .exec(
      http("index")
      .get("/")
    )
    .pause(2)
    .feed(domainNames)
    .exec(
      http("network-lookup: ${domain}")
      .get("/?address=${domain}")
    )

  setUp(
    networkLookup.inject(
      rampUsers(60) over(60 seconds)
    )
  )
  .protocols(browser)

}
