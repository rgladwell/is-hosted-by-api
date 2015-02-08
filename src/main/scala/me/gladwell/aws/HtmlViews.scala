package me.gladwell.aws

import unfiltered.response.Html5

trait HtmlViews extends Views {

  override val index = Html5 {
    <head>
      <title>is-aws-api</title>
    </head>
    <body>
     <form method="GET">
       <label for="address">Address</label>
       <input type="text" name="address" id="address" />
     </form>
   </body>
  }

  override def resultView(result: Boolean) = Html5 {
    <head>
      <title>is-aws-api</title>
    </head>
    <body>
      <div id="result">
        <p><span id="is-aws">{result}</span></p>
      </div>
    </body>
  }

  def errorView(error: Throwable) = Html5 {
    <head>
      <title>is-aws-api</title>
    </head>
    <body>
      <div id="error">
        <p>{error.getMessage}</p>
      </div>
    </body>
  }

}
