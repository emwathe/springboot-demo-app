# Spring Boot Demo Application

This is a simple Spring Boot web application demonstrating:
- REST API with CRUD operations (using MySQL)
- Two entities: User and Product
- Basic web interface (Thymeleaf + Bootstrap)
- Logging to an external file
- Ready for deployment on a Windows server

## Prerequisites
- Java 17 or later
- Maven 3.6+
- MySQL Server

## Setup
1. Clone or copy the project to your Windows server.
2. Configure your MySQL database and update `src/main/resources/application.properties` with your DB credentials.
3. Build the project:
   ```sh
   mvn clean package
   ```
4. Run the application:
   ```sh
   java -jar target/springboot-demo-app-0.0.1-SNAPSHOT.jar
   ```

## Endpoints
- REST API: `/api/users`, `/api/products`
- Web UI: `/users`, `/products`

## Logging
Logs are written to `logs/app.log`.

---

Feel free to modify the entities or extend the functionality as needed!
