# Is This Hosted On AWS API [![Build Status](https://travis-ci.org/rgladwell/is-aws-api.svg?branch=master)](https://travis-ci.org/rgladwell/is-aws-api)

RESTful API that determines if a given server's address/name is hosted on
Amazon or Amazon Web Services.

Requirements:

  * [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html).

Before you start, you should configure some or all of the following environment
variables:

| Environment Variable    | Description
|-------------------------|-------------
| `PORT`                  | Port number for API to host off (defaults `8080`). |
| `AWS_IPRANGE_LOCATION`  | Required URL for the AWS IP ranges (typically https://ip-ranges.amazonaws.com/ip-ranges.json). |

To build and run locally execute:

``` sh
git clone https://github.com/rgladwell/is-aws-api.git
cd is-aws-api
sbt run
```

To deploy on a [Dokku](https://github.com/progrium/dokku) instance execute the following:

```sh
git remote add dokku dokku@<ADDRESS>:is-aws-api
git push dokku master
ssh dokku@<ADDRESS> 'config:set is-aws-api AWS_IPRANGE_LOCATION=https://ip-ranges.amazonaws.com/ip-ranges.json'
```

Where `ADDRESS` is the address of hosting your Dokku instance.
