// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import unfiltered.response.Html5
import scala.xml.NodeSeq
import urimplicit._
import net._

trait HtmlViews extends Views {
  this: Configuration =>

  val webcomponentsScript   = assetsLocation + "/vendor/webcomponentsjs/webcomponents-lite.min.js"
  val components            = assetsLocation + "/assets.html"
  val favicon               = assetsLocation + "/images/icon-fav.png"
  val stylesheet            = assetsLocation + "/style.min.css"

  private def html5Template(title: String, body: NodeSeq) = Html5 {
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
          <div>
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
        <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" value="" />
      </form>
    )
  }

  private def addresssForm: NodeSeq = {
    <form method="get" action="./" data-rel="next">
      <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" />
    </form>
  }

  override def resultView(query: String, result: NetworkLookup) = {

    val description = result.network match {
      case Some(network) => s"${result.host} is on $network"
      case _ => s"${result.host} is not hosted any known provider"
    }

    val not = if (!result.hosted) "not" else ""

    html5Template(
      title = description,
      <div itemscope="" itemtype="http://schema.org/SearchAction">
        <header itemscope="" itemprop="result" itemtype="http://schema.org/Thing">
          <h1 itemprop="description">
            <span itemprop="host">{result.host}</span>
            is {not} hosted by <span itemprop="network">{result.network.getOrElse("any known provider")}</span>
          </h1>
        </header>
      </div>
      ++ {validationMessage(result.validation)} ++
      <form method="get" action="./" data-rel="next">
        <input type="text" name="address" placeholder="Address, host or URL (i.e. cnn.com)" value={query} />
      </form>
    )
  }

  override def errorView(error: Exception) = {
    html5Template(
      title = "Is this hosted on Amazon? - Error",
      <div itemscope="" itemtype="http://schema.org/SearchAction">
        <header itemscope="" itemprop="error" itemtype="http://schema.org/Thing">
          <h1 itemprop="description">
            There was a problem looking up your address, please try again later..
          </h1>
        </header>
      </div>
    )
  }

  private def validationMessage(validation: Option[InvalidInput]): NodeSeq = {
    validation match {
      case Some(v)  => <h2 class="p-invalid-input" title={v.id}>{v.message}</h2>
      case None     => NodeSeq.Empty
    }
    
  }

}
