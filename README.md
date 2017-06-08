**News Aggregator System**

This _Spring Boot_ Application can be launched using Maven by:

_a)_ Navigating to the root directory:  `/.../spring.assignment`  
_b)_ Run the following command: `mvn spring-boot:run`


**Packages**

The packages are organised as follows:

_aspect_: The functionality for any cross-cutting concerns handled through AOP. 

_batch_: The functionality and configuration for Batch Jobs.

_cache_: The Cache Configuration.

_controller_: The REST controllers containing the API exposed by the application.
  
_dao_: The persistent Entities.

_documentation_: The API documentation configuration using Swagger.

_hacker.news.api_: The service layer handling remote calls to the Hacker News API.

_model_: Contains in-memory request and response components.

_repository_: The persistence layer serving to perform CRUD operations on Entities. 

_scheduler_: The functionality and configuration for Scheduled Tasks.

_service_: The business logic of the application.

**API**

The API can be viewed and executed by starting the server and:
 
a) Importing the Postman Collection found in:
`/.../spring.assignment/src/main/resources/news_aggregator_system.postman_collection`

b) Viewing the API using Swagger:
`http://localhost:8080/swagger-ui.html`