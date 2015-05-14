# Simple Java Web Project

This is test project inspired by this [video, russian](http://jugru.org/meetings/107).

I am trying to implement Java Web Application and archive goal below:
  * Simple development environment, reduce needs to configure or install something for developers who just joined the project
  * Fast development process, reduce "build and redeploy" time
  * Keep noise level in code and configuration files at minimal possible level. Good example of noise is maven's pom files or java's getters/setters
  * Follow good development practicess - KISS, YAGNI, DRY, TDD, etc :)

## Let Get Started. Pre-requirements:
  * GIT
  * Java 8
  * Ant 1.9
  * IDE

Checkout the project. Directories and files:
  * runtime - configuration and run files templates
  * sql - flywaydb's scripts
  * src and test - Java files
  * webapp - web assets - CSS, JS, HTML, etc
  * build.properties, ivy.xml and build.xml - build files
  * REAMDE.md - this document

Because maven is not an option due to extra high level of noise in pom files I am using Apache Ant + Apache Ivy to
resolve dependencies and build the project. As a minor bonus it is very fast compare to maven, it takes only couple
seconds to build and pack the project if it does not need to resolve dependencies. The drawback of Ant if there is no
task for something than you got a trouble. *TODO* Try gradle

Next step - prepare project to be imported to your IDE. Execute *ant configure*, it will create extra directories and
resolve dependencies. New directories are:
  * config - App runtime configuration files. Those files are copied only once, a developer may change them to fine tune
    local environment, for instance change log level.
    * application.properties
    * logback.xml
  * data - H2 database file(s)
  * ivy - ivy executable and dependencies

Now you can import the project into your IDE. Source code located in src and test folders, libraries in ivy/lib/main and
ivy/lib/test. If everything resolved correctly you can run Main class. Open
[http://localhost:8080](http://localhost:8080) in browser.

Test may be run from IDE or from console using *ant test*. *TODO* think how to implement/run integration tests

## Build the application
You aren't gonna need it, unless you are a build server.
Build command is *ant build*, it creates build directory. The zip file in the directory is a final app, to run it unzip
and execute *run.sh*. *TODO* Make run.sh compatible with Linux/Unix initialization scripts

The application has the same folder structure - config, data, etc, plus 2 new folders - lib and logs. Folder names
speak for themselves. Note about config folder - files provided by default are just templates, but they allow to run the
app with default parameters (assume it does not need any third party resources - database, etc). In real life they are
going to be replaced to environment specific files during deployment process. Deployment stuff it out of scope of this
project.

## Server Side

Main server side components are:
  * Embedded Jetty. *TODO* Run in HTTPS mode, configure keystore with certificate
  * Try to keep app start time low
  * Spring Framework - dependency injection,
  * Spring MVC + Jackson - JSON web services
  * H2 database - embedded database, good for small projects/prototypes, some brave folks use it in production
  * flywaydb - DB schema management
  * slf4j + logback, google guava - utility libraries
  * testng + mockito for unit testing
  * Hibernate for data access (row mappers sucks)

Coding styles:
  * Java and annotation based configuration
  * No JSP or other server-side templates
  * Use public variables for properties, avoid getters/setters
  * Use no-modifier variables for dependencies, useful in unit tests


## Client side (not implemented yet)

The work is still in progress. I would use Angular + Bootstrap on the client side, but really interesting question is
what is the best way to integrate Java build tool with Web build tools - npm/bower/grunt. What is the best structure
for complex multi-module Java/Web applications?