# Spring Boot

Spring is a popular Java framework used to build enterprise applications. It provides core features such as dependency injection, web application support, database integration, transaction management, and security. Spring helps developers organize application code and manage common backend tasks more easily.

Spring Boot is a Java framework that makes it easier to build production-ready Spring applications. It removes much of the manual configuration normally needed in Spring by providing sensible defaults, auto-configuration, and embedded servers.

With Spring Boot, you can quickly create standalone web applications, REST APIs, microservices, and backend services that are easy to run and deploy.

## Projects in this Workspace

This repository contains several sample projects to demonstrate Spring and Spring Boot concepts:

- **[DemoSpringWithoutBoot](./DemoSpringWithoutBoot/)**: A demonstration of core Spring concepts (IoC, DI) without the auto-configuration magic of Spring Boot.
- **[myApp](./myApp/)**: A basic introductory Spring Boot application.
- **[ecom-proj](./ecom-proj/)**: A comprehensive E-Commerce REST API demonstrating CRUD operations, Spring Data JPA, H2 database integration, and file handling.
- **[service-registry](./service-registry/)**: A demonstration of Netflix Eureka Server acting as a Service Discovery Registry for microservices.

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
- **`@Value`**: Injects values from properties files (`application.properties` or `application.yml`) directly into variables.
  ```java
  @Value("${app.max-users:100}")
  private int maxUsers;
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

## Java-Based Configuration

In modern Spring Boot applications, Java-based configuration is preferred over XML. This is done using `@Configuration` and `@Bean`.

- **`@Configuration`**: Marks a class as a source of bean definitions. It tells Spring that this class contains methods that will instantiate and configure beans.
- **`@Bean`**: Used on methods within a `@Configuration` class. It tells Spring that the method returns an object that should be registered as a bean in the Spring application context.

```java
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // The returned object is managed by Spring
    }
}
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
- **Spring Boot Actuator**: Provides built-in production-ready REST endpoints (like `/actuator/health` and `/actuator/metrics`) to monitor and manage your application's health, metrics, environment, and configuration without having to write code for it.

## Other Essential Concepts & Tools

### AOP (Aspect-Oriented Programming)

AOP is a programming paradigm used to modularize cross-cutting concerns—functions that span multiple points of an application, such as logging, security, or transaction management. Instead of duplicating logging code in every service method, you write an **Aspect** that automatically intercepts method calls and applies the logging logic.

### Lombok

Project Lombok is a popular Java library that automatically plugs into your editor and build tools to drastically reduce boilerplate code. 

**Why is it used?**
In standard Java, creating a simple Data class (like a User or Product) requires writing dozens of lines of repetitive code: Getters, Setters, Constructors, `equals()`, `hashCode()`, and `toString()`. Lombok generates all of this automatically at compile time using simple annotations.

**Without Lombok:**
```java
public class User {
    private String name;
    
    public User() {}
    public User(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ... equals, hashCode, toString
}
```

**With Lombok:**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
}
```
Using annotations like `@Data` (which combines getters, setters, toString, equals, and hashCode), `@Getter`, `@Setter`, `@NoArgsConstructor`, and `@AllArgsConstructor`, you keep your codebase incredibly clean and readable.

### Spring Security

Spring Security is a powerful and customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.

- **Authentication**: Verifies who the user is (e.g., checking username and password).
- **Authorization**: Verifies what the user is allowed to do (e.g., checking if the user has an `ADMIN` role).
- It operates using a chain of **Security Filters** that intercept incoming requests to enforce security rules before the request ever reaches your Controller.

### Service Discovery & Eureka

In a microservices architecture, dozens of independent services need to communicate with one another. Instead of hardcoding IP addresses and ports, they use **Service Discovery**.

- A **Service Registry** acts as a "phone book" where all microservices register themselves on startup.
- **Eureka** (built by Netflix and integrated via Spring Cloud) is one of the most popular service registries. It consists of a **Eureka Server** (the phone book) and **Eureka Clients** (the microservices that register themselves and look up other services).

## Spring Web and Spring MVC

### Spring Web

**Spring Web** (`spring-web`) provides the core web integration features of the Spring Framework, serving as the foundation for web application development.

- **Web Integration**: Contains basic web-oriented integration features like multipart file upload, HTTP client utilities (`RestTemplate`, `WebClient`), and serialization/deserialization helpers.
- **Application Context**: Extends the core Spring IoC container with a web-aware application context (`WebApplicationContext`).
- **Servlet Integration**: Defines servlet listeners and filters needed to initialize Spring contexts in standard servlet containers.

### Spring MVC (Model-View-Controller)

**Spring MVC** (`spring-webmvc`) is a servlet-based web framework built on the Model-View-Controller design pattern. It uses the DispatcherServlet to orchestrate the lifecycle of incoming web requests.

Key architectural components include:

- **`DispatcherServlet` (Front Controller)**: The central dispatcher that receives all HTTP requests, processes them, and routes them to the appropriate controllers.
- **`HandlerMapping`**: Maps incoming HTTP requests to specific handler methods in `@Controller` or `@RestController` beans.
- **`HandlerAdapter`**: Executes the handler method found by `HandlerMapping`.
- **Controllers**: Contain request-handling logic, process user inputs, and return view names or data directly.
- **`ViewResolver`**: Resolves logical view names (e.g., `"home"`) returned by controllers to actual rendering technologies (e.g., JSP, Thymeleaf templates).
- **`ModelAndView` / Model**: Carries model data to be rendered by the View. For REST APIs, this step is bypassed using `@ResponseBody`, returning serialized JSON/XML data directly.

#### Spring MVC Request Lifecycle Flow:

```text
Client Request
      │
      ▼
┌────────────────────────────────────────────────────────────────┐
│                   DispatcherServlet (Front Controller)          │
└────────┬─────────────────────────▲────────────────────┬────────┘
         │ 1. Get Handler          │ 4. Return View     │ 6. Send
         ▼                         │    Name & Model    │    Response
┌─────────────────┐       ┌────────┴────────┐           ▼
│ HandlerMapping  │       │   Controller    │      ┌──────────┐
└─────────────────┘       └────────▲────────┘      │  Client  │
                           2. Dispatch Request     └──────────┘
                           3. Execute Handler
```

1. **Client Request**: The client sends an HTTP request (e.g., `GET /users`).
2. **Front Controller Dispatch**: The `DispatcherServlet` intercepts the request and asks `HandlerMapping` to locate the appropriate controller.
3. **Execution**: The `DispatcherServlet` invokes the controller method (via the handler adapter).
4. **Business & Data Processing**: The controller processes input parameters, calls the service layer to perform business logic, and prepares the response.
5. **Response Resolution**:
   - **For Web Pages (MVC)**: The controller returns a logical view name and model data. `DispatcherServlet` resolves the view using a `ViewResolver`, renders it with model data, and returns HTML.
   - **For REST APIs (Spring Web)**: The controller is annotated with `@RestController` (or `@ResponseBody`). The response object is serialized directly to JSON/XML using message converters (like Jackson) and written straight to the HTTP response body.

---

## Common Spring Boot Layers

Most Spring Boot backend applications are organized into a layered architecture to achieve **Separation of Concerns**. Each layer has a clear, isolated responsibility.

### 1. Presentation Layer (Controller)

The controller handles incoming HTTP requests from the client. It receives request data (path variables, request parameters, request body), validates inputs, calls the service layer, and returns the response.

Common annotations:
- `@RestController`: Marks the class as a REST controller where methods return JSON/XML responses directly.
- `@RequestMapping`, `@GetMapping`, `@PostMapping`: Map specific HTTP request methods and paths to Java methods.

**Example Syntax**:
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
```

### 2. Business Logic Layer (Service)

The service layer contains the business rules and logic of the application. It handles transaction management, performs validations, and coordinates business processes.

Common annotation:
- `@Service`: Registers the class as a service bean in the IoC container.

**Example Syntax**:
```java
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        // Map the Entity to a DTO before returning
        return new UserDTO(user.getName(), user.getEmail());
    }
}
```

### 3. Data Access Layer (Repository / DAO)

The repository layer handles database operations and queries, abstracting the underlying SQL/NoSQL operations.

Common annotations/interfaces:
- `@Repository`: Registers the class as a repository bean.
- `JpaRepository`: Interface provided by Spring Data for out-of-the-box CRUD operations.

### 4. Database Layer (Entity)

An entity represents a database table structure as a Java class using JPA.

Common annotations:
- `@Entity`: Defines that the class is mapped to a database table.
- `@Id`: Specifies the primary key of the entity.

**Example Syntax**:
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String password; // Sensitive data, should not be returned to client
    
    // Getters and Setters...
}
```

### 5. DTO (Data Transfer Object) & Mapper Layer

- **DTO**: Used to send only the required/safe data between the client and the server. It prevents exposing internal database structures (like passwords in Entities) directly to the API client.
- **Mapper**: Used to convert Entities to DTOs and vice-versa.

**Example Syntax**:
```java
// A simple DTO exposing only safe fields to the client
public class UserDTO {
    private String name;
    private String email;
    
    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
    // Getters and Setters...
}
```
*(Note: In modern Java, `record UserDTO(String name, String email) {}` is often used for concise DTOs).*

### 6. Exception Handling Layer

A cross-cutting layer used to handle application-wide exceptions globally, formatting clean and standard error responses for clients.

Common annotations:
- `@RestControllerAdvice`: Intercepts exceptions thrown by controllers globally.
- `@ExceptionHandler`: Defines methods to handle specific exceptions.

### Basic Request Flow:

```text
Client  ──►  Controller  ──►  Service  ──►  Repository  ──►  Database
  ▲               │              │              │               │
  │               ▼              ▼              ▼               ▼
Response ◄─── (DTOs) ◄────── (Entities) ◄──── (Rows) ◄──────────┘
```

---

## Spring Data JPA and H2 Database

### Spring Data JPA

**Spring Data JPA** is part of the larger Spring Data family that makes it easy to implement JPA (Java Persistence API) based repositories. It reduces the boilerplate code required to implement data access layers.

- **ORM (Object-Relational Mapping)**: It allows Java developers to map Java objects (Entities) to relational database tables and vice-versa, avoiding manual SQL queries.
- **Hibernate**: Spring Data JPA uses **Hibernate** by default as its primary JPA Provider (implementation of the JPA specification).

### JPA Repository (`JpaRepository`)

`JpaRepository` is a Spring Data JPA interface that extends `PagingAndSortingRepository` and `CrudRepository`. By extending `JpaRepository`, your repository inherits standard CRUD (Create, Read, Update, Delete) methods without writing any implementation code.

#### Inherited Features:

1. **Out-of-the-box CRUD**: Standard operations like `.save()`, `.findById()`, `.findAll()`, `.deleteById()`, and `.count()`.
2. **Paging and Sorting**: Fetching data in chunks and sorting by specified fields (e.g., `.findAll(Pageable pageable)`).
3. **Derived Query Methods**: Generating queries automatically based on method names.

   ```java
   public interface UserRepository extends JpaRepository<User, Long> {
       // Translates to: SELECT * FROM users WHERE email = ?
       Optional<User> findByEmail(String email);

       // Translates to: SELECT * FROM users WHERE first_name = ? AND last_name = ?
       List<User> findByFirstNameAndLastName(String firstName, String lastName);
   }
   ```

4. **Custom Queries**: Using `@Query` to write custom JPQL (Java Persistence Query Language) or native SQL queries.

   ```java
   @Query("SELECT u FROM User u WHERE u.status = :status")
   List<User> findUsersByStatus(@Param("status") String status);

   @Query(value = "SELECT * FROM users WHERE registration_date > :date", nativeQuery = true)
   List<User> findNewUsers(@Param("date") LocalDate date);
   ```

### H2 Database

**H2** is a lightweight, open-source relational database written in Java. It can be run in **Embedded Mode** (stored in memory or a local file) or **Server Mode**.

- **In-Memory Mode**: The database is created in the JVM's memory when the application starts, and is completely destroyed when the application stops. This makes it ideal for local development, rapid prototyping, and running unit tests.
- **H2 Console**: H2 provides a built-in web-based console to inspect and query database tables.

#### Common H2 Configuration (`application.properties`):

```properties
# Enable H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Database Connection URL (In-Memory)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# Hibernate DDL Auto (creates/updates schema automatically based on @Entity classes)
spring.jpa.hibernate.ddl-auto=update
```

#### Accessing H2 Console:

Once the application is running, navigate to `http://localhost:8080/h2-console` in your browser. Make sure the JDBC URL matches the configured one (e.g., `jdbc:h2:mem:testdb`) to connect to the active in-memory database.

---

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
