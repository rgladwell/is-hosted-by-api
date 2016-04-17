# Is Hosted By API [![Build Status](https://travis-ci.org/is-hosted-by/is-hosted-by-api.svg?branch=master)](https://travis-ci.org/is-hosted-by/is-hosted-by-api) [![Codacy Badge](https://www.codacy.com/project/badge/431145a2eba44687b04d6b9ce2b1328c)](https://www.codacy.com/app/ricardo_3/is-hosted-by-api)

RESTful Hypermedia API that tells you which cloud service/hosting provider a given server's address/name
is hosted (e.g. Amazon, CloudFlare, etc.)

Requirements:

  * [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html).

Before you start, you should configure some or all of the following environment
variables:

| Environment Variable     | Description
|--------------------------|-------------
| `PORT`                   | Port number for API to host off (defaults `8080`). |
| `AWS_IPRANGE_LOCATION`   | Required URL for the AWS IP ranges (typically https://ip-ranges.amazonaws.com/ip-ranges.json). |
| `IS_HOSTED_BY_ASSETS_LOCATION` | Required URL for the HTML assets (e.g. CSS, scripts etc). |
| `IPRANGES_LOCATION`      | Required URL for the general IP ranges (typically https://ip-ranges.is-hosted-by.com/). |

## Building

To build and run locally execute:

``` sh
git clone https://github.com/is-hosted-by/is-hosted-by-api.git
cd is-hosted-by-api
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

To deploy on a [Dokku](https://github.com/dokku/dokku) instance execute the following:

```sh
git remote add dokku dokku@<ADDRESS>:is-hosted-by-api
git push dokku master
ssh dokku@<ADDRESS> 'config:set is-hosted-by-api AWS_IPRANGE_LOCATION=https://ip-ranges.amazonaws.com/ip-ranges.json'
ssh dokku@<ADDRESS> 'config:set is-hosted-by-api IS_HOSTED_BY_ASSETS_LOCATION=https://is-hosted-by-assets.firebaseapp.com/'
ssh dokku@<ADDRESS> 'config:set is-hosted-by-api  IPRANGES_LOCATION=https://ip-ranges.is-hosted-by.com/'
ssh dokku@46.101.18.84 'domains:add is-hosted-by-api is-hosted-by.com'
```

Where `ADDRESS` is the address of hosting your Dokku instance.
