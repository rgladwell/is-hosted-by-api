// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import unfiltered.response.Html5
import scala.xml.NodeSeq
import java.net.URI
import me.gladwell.aws.net._

trait HtmlViews extends Views {
  this: Configuration =>

  val webcomponentsScript   = assetsLocation + "/vendor/webcomponentsjs/webcomponents-lite.min.js"
  val components            = assetsLocation + "/assets.html"
  val favicon               = assetsLocation + "/images/icon-fav.png"

  private def html5Template(title: String, body : NodeSeq, format: String) = Html5 {
    <html lang="en">
      <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1"/>

        <title>{title}</title>

        <script src={webcomponentsScript.toString} async="async" ></script>
        <link rel="import" href={components.toString} />
        <link rel="shortcut icon" href={favicon.toString} />
      </head>

      <body>

        <assets-header></assets-header>

        <main role="main">
          <div class={format}>
          {body}
          </div>
        </main>

        <assets-footer></assets-footer>

      </body>
    </html>
  }

  override val index = {
    html5Template(
      title = "Is this hosted on Amazon?",
      <header>
        <h1>Enter address:</h1>
        <p>Enter the address, URL or host to check if it's hosted by Amazon:</p>
      </header>

      <form method="get" action="./" data-rel="next">
        <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" />
      </form>,
      format = "h-index"
    )
  }

  override def resultView(result: NetworkLookup) = {

    val yesno = if(result.hosted) "Yes" else "No"
    val not = if(result.hosted) "" else " not"

    html5Template(
      title = s"Is ${result.host} hosted on Amazon? $yesno",
      <h1>
        <data class="p-is-hosted" value={result.hosted.toString}>{yesno}</data>,
        <data class="p-host">{result.host}</data>
        is{not} hosted by Amazon.
      </h1>

      <form method="get" action="./" data-rel="next">
        <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" value={result.query} />
      </form>,
      format = "h-network-lookup"
    )
  }

  override def errorView(error: Throwable) = {
    html5Template(
      title = "Is this hosted on Amazon? - Error",
      <h1>{error.getMessage}</h1>,
      format = "h-error"
    )
  }

}
