// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package me.gladwell.aws

import org.specs2.mock.Mockito
import me.gladwell.aws.net.Dns

trait Mocks extends Mockito {

  trait MockDns extends Dns {
    override val resolve = mock[Resolver]
  }

}
