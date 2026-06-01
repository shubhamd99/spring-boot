# Spring Boot

Spring is a popular Java framework used to build enterprise applications. It provides core features such as dependency injection, web application support, database integration, transaction management, and security. Spring helps developers organize application code and manage common backend tasks more easily.

Spring Boot is a Java framework that makes it easier to build production-ready Spring applications. It removes much of the manual configuration normally needed in Spring by providing sensible defaults, auto-configuration, and embedded servers.

With Spring Boot, you can quickly create standalone web applications, REST APIs, microservices, and backend services that are easy to run and deploy.

## IoC and DI

IoC stands for Inversion of Control. It means the framework controls how objects are created, connected, and managed instead of the developer manually creating every object with `new`.

DI stands for Dependency Injection. It is a way to provide an object with the other objects it needs. In Spring, dependencies are usually injected through constructors, setter methods, or fields. Constructor injection is usually preferred because it makes required dependencies clear and helps with testing.

For example, instead of a service class creating its own repository object, Spring can create the repository and inject it into the service. This makes code easier to test, reuse, and maintain.

## History

Spring Boot was created by the Spring team at Pivotal, with Phil Webb and Dave Syer commonly credited as co-creators. The first milestone release was announced on August 6, 2013, and Spring Boot 1.0 GA was released on April 1, 2014.

It was created to make Spring application development faster and simpler by reducing manual configuration, improving project startup time, and making it easier to build standalone production-ready applications.

## Key Features

- Auto-configuration for common application setup
- Embedded web servers such as Tomcat or Jetty for servlet applications, and Reactor Netty for reactive applications
- Starter dependencies for web, data, security, testing, and more
- Production-ready tools like health checks, metrics, and externalized configuration
- Simple application startup using a standard `main` method

## Common Spring Boot Layers

Most Spring Boot backend applications are organized into layers. Each layer has a clear responsibility.

### Controller

The controller handles incoming HTTP requests from the client. It receives request data, calls the service layer, and returns a response.

Common annotations:

- `@RestController`
- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`

### Service

The service layer contains business logic. It decides what the application should do, validates rules, and coordinates work between controllers and repositories.

Common annotation:

- `@Service`

### Repository

The repository layer handles database operations. It is used to save, read, update, and delete data.

Common annotations and interfaces:

- `@Repository`
- `JpaRepository`
- `CrudRepository`

### Entity

An entity represents a database table in Java code. Each object of an entity class usually represents one row in the table.

Common annotations:

- `@Entity`
- `@Id`
- `@GeneratedValue`

### DTO

DTO stands for Data Transfer Object. It is used to send only the required data between layers or between the backend and frontend.

### Basic Flow

```text
Client -> Controller -> Service -> Repository -> Database
```

## gRPC in Spring Boot

gRPC is a high-performance communication framework used for service-to-service communication. It usually uses Protocol Buffers, also called protobuf, instead of JSON.

In Spring Boot, gRPC is useful for fast internal APIs, strongly typed contracts, and streaming. REST is usually easier for beginners and better for public APIs.

Learn the basics of `.proto` files, gRPC services, unary calls, streaming, clients, servers, error handling, security, and testing.

## GraphQL in Spring Boot

GraphQL is an API query language that lets clients ask for exactly the data they need. Unlike REST, where different endpoints often return fixed response structures, GraphQL usually uses a single endpoint and a schema that defines available data and operations.

Spring Boot can be used with GraphQL to build flexible APIs for web and mobile applications. Learn schemas, queries, mutations, subscriptions, resolvers, input types, Spring for GraphQL, validation, security, and testing.

## Running Spring Boot Applications

Spring Boot applications can run locally during development and in production after being packaged.

### Local Development

During development, you usually run the application from the IDE or terminal:

```bash
mvn spring-boot:run
```

You can also build and run the JAR:

```bash
mvn clean package
java -jar target/app-name.jar
```

By default, a Spring Boot web application starts at `http://localhost:8080`.

### Production

In production, the application is usually packaged as a JAR and run on a server, Docker container, or cloud platform:

```bash
java -jar app-name.jar
```

Production apps should use environment variables, proper logging, security, monitoring, health checks, and profiles such as `dev`, `test`, and `prod`.

### Embedded Tomcat vs External Tomcat

Spring Boot web applications usually include an embedded web server through the web starter dependency. For servlet web applications, this is usually embedded Tomcat, so you usually do not need to install Tomcat separately. Spring Boot can also support WAR deployment to an external Tomcat server, but standalone JAR deployment is simpler and more common.

## Why Use Spring Boot?

Spring Boot helps developers focus on business logic instead of boilerplate setup. It is widely used for building scalable Java applications and is a popular choice for REST APIs, enterprise services, and cloud-native applications.

## What to Learn

To learn Spring Boot properly, focus on these topics:

- Java basics and object-oriented programming
- Maven project structure and `pom.xml`
- Spring core concepts: IoC, DI, beans, and application context
- Spring Boot project structure
- `@SpringBootApplication`
- Beans and components: `@Component`, `@Service`, `@Repository`, and `@Controller`
- REST API development
- Controllers, services, repositories, entities, and DTOs
- HTTP methods: GET, POST, PUT, DELETE
- Request and response handling
- Validation using annotations like `@Valid`, `@NotNull`, and `@Size`
- Exception handling using `@ControllerAdvice`
- Database integration with Spring Data JPA
- MySQL or PostgreSQL basics
- Configuration using `application.properties` or `application.yml`
- Profiles such as `dev`, `test`, and `prod`
- Spring Boot Actuator for health checks and metrics
- Database migrations using Flyway or Liquibase
- API documentation using OpenAPI or Swagger
- Logging basics
- Testing with JUnit and Mockito
- Spring Security basics
- Building and running the application with Maven
- Docker basics for packaging and deployment
- API testing using Postman or curl
- gRPC basics for service-to-service communication
- GraphQL basics for flexible API queries

## Maven vs Gradle

Spring Boot projects can be built using either Maven or Gradle.

Maven is a good choice for beginners because it is simple, widely used in tutorials, and uses a clear `pom.xml` file to manage dependencies and build settings.

Gradle is more flexible and often faster for large projects, but it can be harder to learn at first because its build files use a scripting style.

If you are learning Spring Boot for the first time, start with Maven. After you understand the basics of Spring Boot, dependencies, and project structure, you can try Gradle.

## Official Links

- [Spring Projects](https://spring.io/projects)
- [Spring Boot](https://spring.io/projects/spring-boot)
