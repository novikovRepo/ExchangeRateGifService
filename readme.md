Task
---
```
Create a service that calls the exchange rate service and returns a gif in response:

if the exchange rate against the ruble for today has become higher than yesterday,
then we give random from here https://giphy.com/search/rich
if lower - from here https://giphy.com/search/broke
  
Links

Exchange rates REST API - https://docs.openexchangerates.org/
GIF REST API - https://developers.giphy.com/docs/api#quick-start-guide
  
must have
  
Service on Spring Boot 2 + Java / Kotlin
Requests come to the HTTP endpoint, the currency code is passed there
Feign is used to interact with external services
All parameters (currency in relation to which the rate is viewed,
addresses of external services, etc.) are moved to the settings
Tests written for the service
(for mocking external services you can use @mockbean or WireMock)
Gradle must be used to build
The result of the execution should be a repo on GitHub with instructions for launching
Nice to have
Building and running a Docker container with this service
```  
Creating an Image and Running a Container
---
The project is configured to automate image creation and container creation using plugins
com.palantir.docker and com.palantir.docker-run so all you need to do is
is to pull the project and run the following two commands in sequence from the root directory
project:
```
./gradlew docker
./gradlew dockerRun
```
Application
---
After starting the container, you need to use the following link:
```
http://localhost:8030/api/getgif/{currency code}
```
