# wolframalpha-client
Java client for wolframalpha API

#### Build status

[![][travis img]][travis] [![][codecovbadge img]][codecovbadge] [![][dependencies img]][dependencies]

### Known issues
Saw incorrect SSL configuration of some wolfram servers during testing causing async pod download to fail with SSL 
handshake exception. Seems to be fixed now, but if it would appear again - would need to implement workaround as it
is described in [related issues](https://github.com/lightbody/browsermob-proxy/issues/117)

### Overview
This library provides base RESTful binding to a WolframAlpha API 
to all endpoints that have API documentation at this moment.

Supported APIs:

* Simple API<br/>
Documentation : [reference](http://products.wolframalpha.com/simple-api/documentation/)<br/>
```java
byte[] query(@Param("literal") String literal,
                 @Param("appid") String appId,
                 @Param("layout") Layout layout,
                 @Param("background") String background,
                 @Param("foreground") String foreground,
                 @Param("fontsize") int fontsize,
                 @Param("width") int width,
                 @Param("units") Units units,
                 @Param("timeout") int timeout);
```
Returns image from API response as raw binary array.

<hr/>

* Short answer API<br/>
Documentation : [reference](http://products.wolframalpha.com/short-answers-api/documentation/)<br/>
```java
String getShortAnswer(@Param("literal") String literal,
                          @Param("appid") String appId,
                          @Param("units") Units units,
                          @Param("timeout") int timeout)
```
Returns string from API response.

<hr/>

* Spoken results API<br/>
Documentation : [reference](http://products.wolframalpha.com/spoken-results-api/documentation/)<br/>

```java
String getSpokenResults(@Param("literal") String literal,
                        @Param("appid") String appId,
                        @Param("units") Units units,
                        @Param("timeout") int timeout);
```
Returns string from API response.

<hr/>

* Full results API - __WIP__<br/>
Documentation : [reference](http://products.wolframalpha.com/api/documentation/)<br/>
Supported features:<br/>
    * Result format selection: IMAGE, IMAGEMAP, MINPUT, MOUTPUT, PLAINTEXT, CELL, MATHML, SOUND, WAV
    * ImageMap bindings
    * Basic MathMl bindings - raw xml object
    * Pods/Subpods with attributes
    * Assumptions
    * Cells
    * Location selection
    * Pod states
    * Async requests
    
#### Credits
https://github.com/aaronsw/html2text - nice workaround for TravisCI to see test report
    
[travis]:https://travis-ci.org/nginate/wolframalpha-client
[travis img]:https://travis-ci.org/nginate/wolframalpha-client.svg?branch=master

[codecovbadge]:https://codecov.io/gh/nginate/wolframalpha-client
[codecovbadge img]:https://codecov.io/gh/nginate/wolframalpha-client/branch/master/graph/badge.svg

[dependencies]:https://www.versioneye.com/user/projects/59b850876725bd004a5e5921
[dependencies img]:https://www.versioneye.com/user/projects/59b850876725bd004a5e5921/badge.svg?style=flat