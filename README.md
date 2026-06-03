# Spring Boot

Spring is a popular Java framework used to build enterprise applications. It provides core features such as dependency injection, web application support, database integration, transaction management, and security. Spring helps developers organize application code and manage common backend tasks more easily.

Spring Boot is a Java framework that makes it easier to build production-ready Spring applications. It removes much of the manual configuration normally needed in Spring by providing sensible defaults, auto-configuration, and embedded servers.

With Spring Boot, you can quickly create standalone web applications, REST APIs, microservices, and backend services that are easy to run and deploy.

## IoC, DI, and the IoC Container

- **IoC (Inversion of Control)**: A design principle where the control of object creation and lifecycle management is transferred from the developer to the framework. Instead of manually instantiating classes with the `new` operator, the framework takes care of creating and managing them.
- **DI (Dependency Injection)**: A pattern used to implement IoC. It is the process of supplying a resource (dependency) that a class needs. In Spring, dependencies are typically injected using Constructor Injection, Setter Injection, or Field Injection. Constructor injection is recommended because it ensures required dependencies are not null and supports immutability.
- **IoC Container**: The core component of the Spring framework that implements IoC. It is responsible for instantiating, configuring, and assembling application objects (referred to as **Beans**) by reading configuration metadata. It is represented by two main interfaces:
  - **BeanFactory**: The basic container that provides configuration management and lazy-loading of beans (instantiates them only when requested).
  - **Spring Context (ApplicationContext)**: The advanced container built on top of `BeanFactory`. It adds enterprise features such as event publishing, internationalization, and eager-loading of beans (instantiates singletons on startup).

For example, if you manually create an object using the `new` keyword (e.g., `MyService service = new MyService()`), that object is created in the JVM heap but exists **outside** the Spring IoC container. As a result, it does not benefit from Spring's features—managing its dependencies, configuration, and lifecycle becomes your manual responsibility.

This is precisely why Spring is used: the Spring IoC container takes over this responsibility, automatically creating and wiring the objects (beans) so you can focus on business logic. Instead of manually creating a service object, the container creates it and injects it into your controller, decoupling the classes and making the code easier to test, maintain, and reuse.

### Spring Bean Lifecycle

The Spring IoC container manages the complete lifecycle of a bean from its creation to its destruction. Understanding this lifecycle helps in executing custom logic at specific stages.

The typical phases of a Spring Bean lifecycle are:

1. **Instantiation**: The container finds the bean definition and instantiates the bean (creates the Java object).
2. **Populate Properties**: The container injects the bean's dependencies (Dependency Injection).
3. **Aware Interfaces**: If the bean implements any `*Aware` interfaces (e.g., `BeanNameAware`, `BeanFactoryAware`, `ApplicationContextAware`), the container passes the corresponding resources to the bean.
4. **Bean Post-Processing (Before Initialization)**: The container executes `postProcessBeforeInitialization` methods of any registered `BeanPostProcessor` beans (this is where annotations like `@PostConstruct` are processed).
5. **Initialization**:
   - If the bean implements `InitializingBean`, its `afterPropertiesSet()` method is called.
   - If a custom `init-method` is configured, it is called.
6. **Bean Post-Processing (After Initialization)**: The container executes `postProcessAfterInitialization` methods of any registered `BeanPostProcessor` beans. At this stage, the bean is fully initialized and ready for use.
7. **Destruction**: When the application context is closed (e.g., application shutdown):
   - Methods annotated with `@PreDestroy` are called.
   - If the bean implements `DisposableBean`, its `destroy()` method is called.
   - If a custom `destroy-method` is configured, it is called.

## Dependency Injection & Key Annotations

### Key Annotations

- **`@Component`**: Marks a class as a Spring-managed bean, meaning the IoC container will control its lifecycle and instantiation.
  ```java
  @Component
  public class EmailService {}
  ```
- **`@Autowired`**: Instructs Spring to inject the required dependency automatically. By default, it resolves dependencies **by type**.
  ```java
  @Autowired
  private EmailService emailService;
  ```
- **`@Primary`**: Prioritizes a bean when multiple beans of the same type exist.
  ```java
  @Component
  @Primary
  public class HighSpeedEmailService implements NotificationService {}
  ```
- **`@Qualifier`**: Used alongside `@Autowired` to specify the exact name of the bean to inject when multiple beans of the same type exist.
  ```java
  @Autowired
  @Qualifier("slowEmailService")
  private NotificationService service;
  ```

### Dependency Injection (DI) Styles

There are three ways to inject dependencies into a class:

#### 1. Constructor Injection (Recommended)

Dependencies are provided through the constructor. It supports immutability (using `final` variables) and ensures the class cannot be created without its dependencies.

```java
@Component
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { // @Autowired is optional here
        this.userService = userService;
    }
}
```

#### 2. Setter Injection

Dependencies are provided via setter methods. Good for optional or changeable dependencies.

```java
@Component
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

#### 3. Field Injection (Instance Variable Injection)

Dependencies are injected directly into instance variables/fields using reflection. Easy to write but discouraged because it makes testing harder and bypasses constructor validation.

```java
@Component
public class UserController {
    @Autowired
    private UserService userService; // Injected directly into the instance variable
}
```

## XML-Based Spring Configuration

While modern Spring Boot uses Java annotations and configuration classes, traditional Spring applications (or non-Boot projects) configure the IoC Container using XML.

- **`spring.xml`**: The standard filename used for the XML configuration file containing bean definitions and wiring instructions.
- **`<beans>`**: The root XML element encapsulating all bean declarations.
- **`<bean>`**: The XML element defining a single bean, specifying its unique `id` and fully qualified `class`.

_Example `spring.xml`:_

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="dev" class="org.shubham.Dev" />
</beans>
```

## History

Spring Boot was created by the Spring team at Pivotal, with Phil Webb and Dave Syer commonly credited as co-creators. The first milestone release was announced on August 6, 2013, and Spring Boot 1.0 GA was released on April 1, 2014.

It was created to make Spring application development faster and simpler by reducing manual configuration, improving project startup time, and making it easier to build standalone production-ready applications.

## JVM (Java Virtual Machine)

The Java Virtual Machine (JVM) is the engine that drives Java applications. It provides the runtime environment to execute Java bytecode. Because Spring Boot is a Java-based framework, its applications run on the JVM.

Key concepts of the JVM in the context of Spring Boot:

- **Platform Independence**: Java code compiles into platform-independent bytecode (`.class` files), which the JVM translates into machine code for the host operating system.
- **Memory Management & Garbage Collection**: The JVM automatically manages memory allocation and deallocation (Garbage Collection) for Spring beans and other Java objects, helping to prevent memory leaks.
- **Just-In-Time (JIT) Compiler**: The JVM compiles frequently executed bytecode into native machine code at runtime to optimize the performance of the Spring Boot application.
- **JVM Tuning**: In production, developers configure JVM flags (e.g., `-Xms` for initial heap size and `-Xmx` for maximum heap size) to optimize the performance and memory footprint of the Spring Boot process.

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

## What is a POM File in Spring Boot?

In Spring Boot projects built with Maven, the `pom.xml` (Project Object Model) file is the central configuration file located in the root directory. It contains configuration details used by Maven to build the project.

Key components of a `pom.xml` file in a Spring Boot application include:

- **Project Metadata**: Basic info such as `groupId` (group/org ID), `artifactId` (project name), and `version`.
- **Parent POM (`<parent>`)**: Typically inherits from `spring-boot-starter-parent` to provide default configurations and version management for starter dependencies.
- **Properties (`<properties>`)**: Defines configuration variables, such as the Java version (e.g., `<java.version>17</java.version>`).
- **Dependencies (`<dependencies>`)**: Lists the libraries (e.g., `spring-boot-starter-web` for web APIs, `spring-boot-starter-test` for testing) that the project requires.
- **Plugins (`<build><plugins>`)**: Configures build plugins, such as `spring-boot-maven-plugin` which repackages the application into an executable JAR.

### Maven Archetype

A **Maven Archetype** is a project templating toolkit. It provides a standardized blueprint/template to generate new Maven projects quickly with a predefined directory structure, dependencies, and configuration.

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
