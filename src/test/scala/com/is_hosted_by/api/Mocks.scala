// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Affero General Public License.
// See the LICENSE file for more information.

package com.is_hosted_by.api

import org.specs2.mock.Mockito
import net.Dns

trait Mocks extends Mockito {

  trait MockDns extends Dns {
    override val resolve = mock[Resolver]
  }

}
