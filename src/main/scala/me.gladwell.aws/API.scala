package me.gladwell.aws

import unfiltered.request._
import unfiltered.response._

import unfiltered.directives._, Directives._

class API extends unfiltered.filter.Plan {

  def intent = Directive.Intent {
    case GET(_) => success(Ok ~> index())
  }

  def index() = {
    Html5(
     <html>
      <head>
        <title>is-aws-api</title>
      </head>
      <body>
       <form method="GET">
         <input type="submit" />
       </form>
     </body>
    </html>
   )
  }
}

object API {
  def main(args: Array[String]) {
    unfiltered.jetty.Server.local(8080).plan(new API).run()
  }
}
