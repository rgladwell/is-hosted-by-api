# Is This Hosted On AWS API [![Build Status](https://travis-ci.org/is-aws/is-aws-api.svg?branch=master)](https://travis-ci.org/is-aws/is-aws-api) [![Codacy Badge](https://www.codacy.com/project/badge/431145a2eba44687b04d6b9ce2b1328c)](https://www.codacy.com/app/ricardo_3/is-aws-api)

RESTful Hypermedia API that determines if a given server's address/name is hosted on
Amazon or Amazon Web Services.

Requirements:

  * [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html).

Before you start, you should configure some or all of the following environment
variables:

| Environment Variable     | Description
|--------------------------|-------------
| `PORT`                   | Port number for API to host off (defaults `8080`). |
| `AWS_IPRANGE_LOCATION`   | Required URL for the AWS IP ranges (typically https://ip-ranges.amazonaws.com/ip-ranges.json). |
| `IS_AWS_ASSETS_LOCATION` | Required URL for the HTML assets (e.g. CSS, scripts etc). |
| `IPRANGES_LOCATION`      | Required URL for the general IP ranges (typically https://ip-ranges.is-hosted-by.com/). |

## Building

To build and run locally execute:

``` sh
git clone https://github.com/rgladwell/is-aws-api.git
cd is-aws-api
sbt run
```

## Testing

To run the unit tests:

``` sh
sbt test
```

To run the load tests:

``` sh
cd load-test
sbt test
```

## Deployment

To deploy on a [Dokku](https://github.com/progrium/dokku) instance execute the following:

```sh
git remote add dokku dokku@<ADDRESS>:is-aws-api
git push dokku master
ssh dokku@<ADDRESS> 'config:set is-aws-api AWS_IPRANGE_LOCATION=https://ip-ranges.amazonaws.com/ip-ranges.json'
ssh dokku@<ADDRESS> 'config:set is-aws-api IS_AWS_ASSETS_LOCATION=http://is-aws-assets.divshot.io'
ssh dokku@<ADDRESS> 'config:set is-aws-api IPRANGES_LOCATION=https://ip-ranges.is-hosted-by.com/'
ssh dokku@46.101.18.84 'domains:add is-aws-api test.is-aws.com'
```

Where `ADDRESS` is the address of hosting your Dokku instance.
