one.irradia.neutrino
===

[![Build Status](https://img.shields.io/travis/irradia/one.irradia.neutrino.svg?style=flat-square)](https://travis-ci.org/irradia/one.irradia.neutrino)
[![Maven Central](https://img.shields.io/maven-central/v/one.irradia.neutrino/one.irradia.neutrino.api.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22one.irradia.neutrino%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/one.irradia.neutrino/one.irradia.neutrino.api.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/one.irradia.neutrino/)
[![Codacy Badge](https://img.shields.io/codacy/grade/CODACY_TOKEN.svg?style=flat-square)](https://www.codacy.com/app/github_79/one.irradia.neutrino?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=irradia/one.irradia.neutrino&amp;utm_campaign=Badge_Grade)
[![Codecov](https://img.shields.io/codecov/c/github/irradia/one.irradia.neutrino.svg?style=flat-square)](https://codecov.io/gh/irradia/one.irradia.neutrino)
[![Gitter](https://badges.gitter.im/irradia-org/community.svg)](https://gitter.im/irradia-org/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

![neutrino](./src/site/resources/neutrino.jpg?raw=true)

## Features

* ISC license
* High coverage automated test suite

## Building

Install the Android SDK.

```
$ ./gradlew clean assembleDebug test
```

If the above fails, it's a bug. Report it!

## Modules

|Module|Description|
|------|-----------|

## Publishing Releases

Releases are published to Maven Central with the following invocation:

```
$ ./gradlew clean assembleDebug publish closeAndReleaseRepository
```

Consult the documentation for the [Gradle Signing plugin](https://docs.gradle.org/current/userguide/signing_plugin.html)
and the [Gradle Nexus staging plugin](https://github.com/Codearte/gradle-nexus-staging-plugin/) for
details on what needs to go into your `~/.gradle/gradle.properties` file to do the appropriate
PGP signing of artifacts and uploads to Maven Central.

## Semantic Versioning

All [irradia.one](https://www.irradia.one) packages obey [Semantic Versioning](https://www.semver.org)
once they reach version `1.0.0`.
