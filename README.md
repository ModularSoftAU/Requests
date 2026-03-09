# Requests

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ModularEnigma/Requests.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.ModularEnigma%22%20AND%20a:%22Requests%22)

A lightweight Java library for building and executing HTTP requests. Wraps `java.net.http.HttpClient` behind a fluent builder API.

## Requirements

- Java 17+

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.ModularEnigma</groupId>
    <artifactId>Requests</artifactId>
    <version>x.y.z</version>
</dependency>
```

Replace `x.y.z` with the version shown in the badge above. For other build tools (Gradle, sbt, etc.) see the [Maven Central Repository](https://search.maven.org/artifact/io.github.ModularEnigma/Requests).

## Usage

### GET request

```java
Response response = Request.builder()
        .setURL("https://api.example.com/users/1")
        .setMethod(Request.Method.GET)
        .build()
        .execute();

int status = response.getStatusCode(); // e.g. 200
String body = response.getBody();      // raw response body
```

### POST request

```java
Response response = Request.builder()
        .setURL("https://api.example.com/users")
        .setMethod(Request.Method.POST)
        .setRequestBody("{\"name\": \"Alice\"}")
        .build()
        .execute();
```

### Custom headers

```java
Response response = Request.builder()
        .setURL("https://api.example.com/protected")
        .setMethod(Request.Method.GET)
        .addHeader("Authorization", "Bearer my-token")
        .addHeader("X-Request-ID", "abc123")
        .build()
        .execute();
```

`Accept: application/json` and `Content-Type: application/json` are set automatically. Headers added via `addHeader` are appended using `HttpRequest.Builder.header(...)`, which adds an additional header value rather than replacing any existing one. This means adding `Accept` or `Content-Type` via `addHeader` will result in duplicate header values, not an override; avoid re-adding those headers if you intend to change the defaults.

### Timeout

```java
import java.time.Duration;

Response response = Request.builder()
        .setURL("https://api.example.com/heartbeat")
        .setMethod(Request.Method.GET)
        .setTimeout(Duration.ofSeconds(10))
        .build()
        .execute();
```

When a timeout is set and the server does not respond in time, `execute()` throws a `RuntimeException` wrapping a `java.net.http.HttpTimeoutException`. Without a timeout, the request blocks until the OS-level socket timeout fires (which can be several minutes).

### Reading the response

```java
Response response = /* ... */;

response.getStatusCode();  // int — HTTP status code (200, 404, 500, …)
response.getBody();        // String — raw response body, may be empty
response.getHeaders();     // Map<String, List<String>> — all response headers
```

## API reference

### `RequestBuilder`

Obtain an instance via `Request.builder()`.

| Method | Required | Description |
|---|---|---|
| `setURL(String url)` | Yes | Target URL. Must be a valid RFC 2396 URI. |
| `setMethod(Request.Method method)` | Yes | `GET` or `POST`. |
| `setRequestBody(String body)` | No | Request body string (typically JSON). Relevant for `POST`. |
| `addHeader(String header, String value)` | No | Add a custom request header. May be called multiple times. |
| `setTimeout(Duration timeout)` | No | Per-request response timeout. Must be a positive `Duration`. |
| `build()` | — | Constructs and returns a `Request`. Throws `IllegalStateException` if `url` or `method` is missing. |

### `Request`

| Method | Description |
|---|---|
| `execute()` | Sends the request synchronously and returns a `Response`. Throws `RuntimeException` on I/O error, interruption, or timeout. |

### `Response`

| Method | Return type | Description |
|---|---|---|
| `getStatusCode()` | `int` | HTTP status code. |
| `getBody()` | `String` | Response body. May be empty. |
| `getHeaders()` | `Map<String, List<String>>` | Response headers. |

## Publishing a new version

### Prerequisites

- Write access to the repository
- A GPG key configured and known to Maven (used to sign artifacts)
- Sonatype OSSRH credentials in `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-sonatype-username</username>
      <password>your-sonatype-password</password>
    </server>
  </servers>
</settings>
```

### Steps

1. **Bump the version** in `pom.xml`:

   ```xml
   <version>1.0.5</version>
   ```

2. **Commit the version bump**:

   ```bash
   git add pom.xml
   git commit -m "Release 1.0.5"
   git tag v1.0.5
   git push origin master --tags
   ```

3. **Deploy to OSSRH staging**:

   ```bash
   mvn clean deploy
   ```

   This compiles, runs tests, attaches sources and Javadoc jars, signs all artifacts with GPG, and uploads everything to the OSSRH staging repository.

4. **Release from staging to Maven Central**:

   - Log in to [https://s01.oss.sonatype.org](https://s01.oss.sonatype.org)
   - Navigate to **Staging Repositories**
   - Select the repository, click **Close**, then **Release**
   - Propagation to Maven Central typically takes 10–30 minutes

   Alternatively, add the `nexus-staging-maven-plugin` to `pom.xml` and run:

   ```bash
   mvn nexus-staging:release
   ```

5. **Update the README** if the feature set changed, then push.
