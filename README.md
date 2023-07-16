# Web-Crawler [![CircleCI](https://dl.circleci.com/status-badge/img/gh/apavlidi/WebCrawler/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/apavlidi/WebCrawler/tree/main) [![codecov](https://codecov.io/gh/apavlidi/WebCrawler/branch/main/graph/badge.svg?token=T7ACUBVJDX)](https://codecov.io/gh/apavlidi/WebCrawler) [![Known Vulnerabilities](https://snyk.io/test/github/apavlidi/WebCrawler/badge.svg)](https://snyk.io/test/github/apavlidi/WebCrawler)

This is a java web crawler which crawls a URL and returns the URLs visited with all the links on that URL found. You can find here the released version of this application: [http://web-crawl-env.eba-upp2ihyt.eu-west-2.elasticbeanstalk.com/](http://web-crawl-env.eba-upp2ihyt.eu-west-2.elasticbeanstalk.com/)

## Teck Stack

The project is build with [Spring](https://spring.io/) and Java 17. It uses [JUnit](https://junit.org/junit5/).
The project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) by utilising the [spotless plugin](https://github.com/diffplug/spotless) which is configured with [google style guide](https://google.github.io/styleguide/javaguide.html). It also provides a code coverage report by using [jacoco](https://github.com/jacoco/jacoco). 

For CI/CD it uses [CircleCI](https://app.circleci.com/), for deployment [AWS Elastic Beanstalk](https://aws.amazon.com/elasticloadbalancing/) and the application is wrapped with [Docker](https://www.docker.com/).

It also integrated with [Snyk](https://snyk.io/) for security vulnerabilities.

The CI/CD includes the following steps:
1. Check codestyle
2. Run tests
3. Create Code Coverage report and publish it to Codecov
4. Deploys application to ELB

### Run it locally
1) Clone the project on your local machine.  <br/>
   `$ git clone https://github.com/apavlidi/WebCrawler.git`

2) Navigate to the project folder and install the dependencies with the following command.  <br/>
   `$ mvn install`

3) Run the application locally (the application can be accessed from [localhost:8080](http://localhost:8080/)) <br/>
   `$ mvn spring-boot:run`

#### Docker
You can also run the application using docker:

1) `$ docker build -t app .`

2) `$ docker run -p 8080:8080 app`

#### Run tests ✅
You can run the tests by using `$ mvn test`.

#### Generate coverage report 📊
You can produce code coverage report using the jacoco plugin `$ mvn jacoco:report`.
The code coverage report has been deployed to [Codecov](https://app.codecov.io/).

#### Lint code 💅
You can format the code by using the spotless plugin `$ mvn spotless:apply`. Spotless has been configured to use [google style code](https://google.github.io/styleguide/javaguide.html).

### Documentation 📕
Web-Crawl documentation is available [here](https://github.com/apavlidi/WebCrawler/wiki).
The API is also exposed via [OpenAPI](https://www.openapis.org/), and it's accessible here: [/v3/api-docs](http://web-crawl-env.eba-upp2ihyt.eu-west-2.elasticbeanstalk.com/v3/api-docs)

### Project Kanban 👨‍🏫
Web-Crawl project kanban is available [here](https://github.com/users/apavlidi/projects/1).
