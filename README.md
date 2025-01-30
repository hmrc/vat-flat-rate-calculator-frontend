# vat-flat-rate-calculator-frontend

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

### Vat Flat Rate Calculator Frontend

This is the repository for the Vat Flat Rate Calculator frontend. This service allows enterprises on the Flat Rate Scheme to find out whether they should be using the new 16.5% rate or not.

Requirements
------------

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.


## Run the application


To update from Nexus and start all services from the RELEASE version instead of snapshot

```
sm2 --start VFR_ALL
```


### To run the application locally execute the following:

Stop the frontend running on servive manager 
```
sm2 --stop VAT_FLAT_RATE_CALC_FRONTEND
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

