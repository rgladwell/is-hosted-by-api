# Is This Hosted On AWS API [![Build Status](https://travis-ci.org/rgladwell/is-aws-api.svg?branch=master)](https://travis-ci.org/rgladwell/is-aws-api)

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy) 

RESTful API that determines if a given server's address/name is hosted on
Amazon or Amazon Web Services.

To build and run locally execute:

``` sh
git clone https://github.com/rgladwell/is-aws-api.git
cd is-aws-api
sbt run
```

To deploy this application to Heroku, run:

``` sh
heroku create
git push heroku master
```
