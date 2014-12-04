# Instagram API wrapper for Scala

## Installation

The current release is distributed for Scala 2.11.4 or later. Add scalagram as a dependency in sbt:

### sbt

Add the scalagram dependency:

```scala
val scalagram = "com.rydgel" %% "scalagram" % "0.1.0"
```

## Run the test suite

You first need to copy the token.txt.default from the resources and create a token.txt file with a valid
access_token in it.

```bash
sbt test
```

## Documentation

### Examples

```scala
import com.rydgel.scalagram._
import com.rydgel.scalagram.responses._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

val clientId = "client-id"
val clientSecret = "client-secret"
val redirectURI = "redirect-URI"

// Server-Side login
// Step 1: Get a URL to call. This URL will return the CODE to use in step 2
val codeUrl = Authentication.codeURL(clientId, redirectURI)

// Step 2: Use the code to get an AccessToken
val accessTokenFuture = Authentication.requestToken(clientId, clientSecret, redirectURI, code = "the-code-from-step-1")
val accessToken = accessTokenFuture onComplete {
    case Success(Response(Some(token: AccessToken)) => token
    case Failure(t) => println("An error has occured: " + t.getMessage)
}

// Making an authenticated call
val auth = AccessToken("an-access-token")
// The library is asynchronous by default and returns a promise.
val future = Scalagram.userFeed(auth)
future onComplete {
  case Success(Response(data, pagination, meta, headers)) => println(data) // do stuff
  case Failure(t) => println("An error has occured: " + t.getMessage)
}

// You are still able to perform a synchronous call for quick and dirty stuff
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

val response: Response[List[Media]] = Await.result(Scalagram.userFeed(auth), 10 seconds)

// Enforce signed headers
// You can activate this option for some calls
// (please read the documentation here http://instagram.com/developer/restrict-api-requests/)
val headers = Authentication.createSignedHeader(clientSecret, Some(List("127.0.0.1")))

```