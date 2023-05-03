# vat-flat-rate-calculator-frontend

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://api.travis-ci.org/hmrc/vat-flat-rate-calculator-frontend.svg?branch=master)](https://travis-ci.org/hmrc/vat-flat-rate-calculator-frontend)
[![Download](https://api.bintray.com/packages/hmrc/releases/vat-flat-rate-calculator-frontend/images/download.svg)](https://bintray.com/hmrc/releases/vat-flat-rate-calculator-frontend/_latestVersion)

### Vat Flat Rate Calculator Frontend

This is the repository for the Vat Flat Rate Calculator frontend. This service allows enterprises on the Flat Rate Scheme to find out whether they should be using the new 16.5% rate or not.

Requirements
------------

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.


## Run the application


To update from Nexus and start all services from the RELEASE version instead of snapshot

```
sm --start VFR_ALL -r
```


### To run the application locally execute the following:

Stop the frontend running on servive manager 
```
sm --stop VAT_FLAT_RATE_CALC_FRONTEND
``` 
and from the root directory run:
```
sbt 'run 9080'
```


## Test the application

To test the application execute

```
sbt test
```




### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")

