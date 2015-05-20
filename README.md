# Simple Java Web Project

This is test project inspired by this [video, russian](http://jugru.org/meetings/107) and discussion with Pavel Shabalin

I am trying to implement Java Web Service Application and archive goal below:
  * Simple development environment, reduce needs to configure or install something for developers who just joined the project
  * Fast development process, reduce "build and redeploy" time
  * Keep noise level in code and configuration files at minimal possible level. Good example of noise is maven's pom files or java's getters/setters
  * Follow good development practicess - KISS, YAGNI, DRY, TDD, etc :)

The client part is not in scope of this projects. *TODO* Create Web UI module

## Let Get Started. Pre-requirements:

  * GIT
  * Java 8
  * Gradle 2.4
  * IDE

Checkout the project. Directories and files:
  * dist - configuration and other runtime files
  * sql - flywaydb's scripts
  * src and test - Java files
  * build.gradle - build files
  * REAMDE.md - this document

Because maven is not an option due to extra high level of noise in pom files I am using gradle (initial version uses
Apache Ant + Apache Ivy) to resolve dependencies and build the project. The last version is faster the previous, but
the general idea is that I ain't need to run a build manually anyway.

Next step - prepare project to be imported to your IDE. Execute *gradle configure*, it will create extra directories and
resolve dependencies. New directories are:
  * config - App runtime configuration files. Those files are copied only once, a developer may change them to fine tune
    local environment, for instance change log level.
    * application.properties
    * logback.xml
  * data - H2 database file(s)
  * build - build directory
  * .gradle  - gradle's stuff

Now you can import the project into your IDE, IDEA has builtin gradle support, but because of non-standart source files
location it may have trouble to resolve them correctly, please check it after import. Source code located in src and test
folders. If everything resolved correctly you can run Main class. Open
[http://localhost:8080](http://localhost:8080) in browser. This is swagger JSON for API

Test may be run from IDE or from console using *gradle test*. *TODO* think how to implement/run integration tests

## Build the application

You aren't gonna need it, unless you are a build server.
Build command is *gradle build*. The tar file in the *build* directory is a final app, to run it untar
and execute *run.sh* from within the simple-1.0 directory. *TODO* Make run.sh compatible with Linux/Unix initialization
scripts

The application has the same folder structure - config, data, etc, plus 2 new folders - libs and logs. Folder names
speak for themselves. Note about config folder - files provided by default are just templates, but they allow to run the
app with default parameters (assume it does not need any third party resources - database, etc). In real life they are
going to be replaced to environment specific files during deployment process. Deployment stuff it out of scope of this
project.

## Implementation Details

Main server side components are:
  * Embedded Jetty
  * Try to keep app start time low
  * Spring Framework - dependency injection,
  * Spring MVC + Jackson - JSON web services
  * Swagger + springfox for API description
  * H2 database - embedded database, good for small projects/prototypes, some brave folks use it in production
  * flywaydb - DB schema management
  * slf4j + logback, google guava - utility libraries
  * testng + mockito for unit testing
  * DAO framework is still to be selected

Coding styles:
  * Java and annotation based configuration
  * No JSP or other server-side templates
  * Use public variables for properties, avoid getters/setters
  * Use no-modifier variables for dependencies, useful in unit tests


## Swager and Springfox

I have discovered those 2 cool products recently. And I think it is cool to have the service description mapped to the
root of the server. The only issue is the document generation slow down server startup a bit. It may be even slower when
the project grows. No a big deal for prod/qa environment, but may be annoying on local. So I have added an option to
disable it. Configuration for this feature is included into *default* profile. To skip it add any Spring's profile into
IDE run configuration, add *-Dspring.profiles.active="noswagger"* into JVM options. Profile name does not matter.

I have created 2 run configuration for myself - with and without swagger documentation, just in case I need to test it.
There also other features like build the documentation during the build or even contract first development. The last is
out of scope of this project, the first will complicate the build and IDE integration.