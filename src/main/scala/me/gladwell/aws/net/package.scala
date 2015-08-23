package me.gladwell.aws

import java.net.URI
import scala.language.implicitConversions

package object net {
  implicit def javaUriToUri(uri: URI) = new Uri(uri)
}
