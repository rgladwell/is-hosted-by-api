package me.gladwell.aws

import java.net.URI

package object net {
  implicit def javaUriToUri(uri: URI) = new Uri(uri)
}
