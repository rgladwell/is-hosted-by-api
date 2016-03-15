// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import unfiltered.response.Html5
import scala.xml.NodeSeq
import java.net.URI
import net._

trait HtmlViews extends Views {
  this: Configuration =>

  val webcomponentsScript   = assetsLocation + "/vendor/webcomponentsjs/webcomponents-lite.min.js"
  val components            = assetsLocation + "/assets.html"
  val favicon               = assetsLocation + "/images/icon-fav.png"
  val stylesheet            = assetsLocation + "/style.min.css"

  private def html5Template(title: String, body: NodeSeq, format: String) = Html5 {
    <html lang="en">
      <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1"/>

        <title>{title}</title>

        <script src={webcomponentsScript.toString} async="async" ></script>
        <link rel="stylesheet" href={stylesheet.toString} />
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

  private def addresssForm: NodeSeq = {
    <form method="get" action="./" data-rel="next">
      <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" />
    </form>
  }

  override def resultView(query: String, result: NetworkLookup) = {

    val yesno = if(result.hosted) "Yes" else "No"
    val not = if(result.hosted) "" else " not"

    html5Template(
      title = s"Is ${result.host} hosted on Amazon? $yesno",
      <h1>
        <data class="p-is-hosted" value={result.hosted.toString}>{yesno}</data>,
        <data class="p-host">{result.host}</data>
        is{not} hosted by <data class="p-network">{result.network.getOrElse("any network")}</data>.
      </h1>
      ++ {validationMessage(result.validation)} ++
      <form method="get" action="./" data-rel="next">
        <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" value={query} />
      </form>,
      format = "h-network-lookup"
    )
  }

  override def errorView(error: Exception) = {
    html5Template(
      title = "Is this hosted on Amazon? - Error",
      <h1>There was a problem looking up your address, please try again later.</h1>,
      format = "h-error"
    )
  }

  private def validationMessage(validation: Option[InvalidInput]): NodeSeq = {
    validation match {
      case Some(v)  => <h2 class="p-invalid-input" title={v.id}>{v.message}</h2>
      case None     => NodeSeq.Empty
    }
    
  }

}
