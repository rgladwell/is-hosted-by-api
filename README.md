# Is This Hosted On AWS API [![Build Status](https://travis-ci.org/rgladwell/is-aws-api.svg?branch=master)](https://travis-ci.org/rgladwell/is-aws-api)

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy) 

RESTful API that determines if a given server's address/name is hosted on
Amazon or Amazon Web Services.

Before you start, you should configure some or all of the following environment
variables:

| Environment Variable    | Description
|-------------------------|-------------
| `PORT`                  | Port number for API to host off (defaults `8080`) |
| `AWS_IPRANGE_LOCATION`  | Required URL for the AWS IP ranges (typically https://ip-ranges.amazonaws.com/ip-ranges.json) |

To build and run locally execute:

``` sh
git clone https://github.com/rgladwell/is-aws-api.git
cd is-aws-api
sbt run
```

To deploy this application to Heroku, run:

``` sh
heroku create -s cedar --buildpack https://github.com/heroku/heroku-buildpack-scala.git
heroku config:set AWS_IPRANGE_LOCATION=https://ip-ranges.amazonaws.com/ip-ranges.json
git push heroku master
```
