# Requests

## Installation

```bash
cd <install-location>
git submodule add git@github.com:ModularEnigma/Requests.git api
```

Ensure the source files package (`package com.modularenigma.zander.proxy.api;`) matches the file structure.

### Maven

Ensure this is added to your `pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>com.googlecode.json-simple</groupId>
        <artifactId>json-simple</artifactId>
        <version>1.1.1</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

### Gradle

Ensure this is added to your `build.gradle`

```gradle
dependencies {
    // https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
    implementation group: 'com.googlecode.json-simple', name: 'json-simple', version: '1.1.1'
}
```
