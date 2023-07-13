# Web-Crawler

This is a java web crawler which crawls a URL and returns the URLs visited with all the links on that URL found.

## Teck Stack

The project is build with [Spring](https://spring.io/) and Java 17. It uses [JUnit](https://junit.org/junit5/).
The project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) by utilising the [spotless plugin](https://github.com/diffplug/spotless). It also provides a code coverage report by using [jacoco](https://github.com/jacoco/jacoco).

For CI/CD it uses [Render](https://render.com/) and the application is wrapped with [Docker](https://www.docker.com/)

### Run it locally
1) Clone the project on your local machine.  <br/>
   `$ git clone https://github.com/apavlidi/WebCrawler.git`

2) Navigate to the project folder and install the dependencies with the following command.  <br/>
   `$ mvn install`

3Run the application locally (the application can be accessed from [localhost:8080](http://localhost:8080/)) <br/>
   `$ mvn spring-boot:run`

#### Run tests
You can run the tests by using `$ mvn test`.

#### Generate coverage report
You can produce code coverage report using the jacoco plugin `$ mvn jacoco:report`.


### Documentation
Web-Crawl documentation is available [here](https://github.com/codurance/Retropolis-BE/wiki)

### Project Kanban
Web-Crawl project kanban is available [here](https://github.com/users/apavlidi/projects/1)
