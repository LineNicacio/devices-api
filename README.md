# 📱 Devices API

This is a RESTful API developed for managing devices. It allows to create, list, update, and retrieve devices from a
database.

---

## 📦 JSON Format

The API works with the following JSON structure for devices:

```json
{
  "id": "68c7be82f42d3ea0249b030e",
  "name": "nameDevice",
  "brand": "brandDevice",
  "state": "AVAILABLE",
  "createdAt": "2025-09-15T07:21:38.309Z"
}
```

## Technologies

- Java 21
- Spring Boot 3.5.5
- MongoDB
- Redis
- JUnit + JaCoCo (80%+ test coverage)
- OpenAPI / Swagger

## Architecture

This project follows the **Clean Architecture** principles to ensure separation of concerns, maintainability, and
testability.

## How to Run the Application

### Running Locally (Using IDE)

1. Make sure MongoDB and Redis are running locally on their default ports.
2. In your IDE (e.g., IntelliJ), set the active Spring profile to `local`.

    - If running from the IDE, add this VM option in your run configuration:

      ```
      -Dspring.profiles.active=local
      ```

    - Or run via terminal using Maven:

      ```bash
      ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
      ```

3. Start the application. It will connect to your local MongoDB and Redis instances.

---

### Running with Docker

1. Make sure you have **Docker** and **Docker Compose** installed.
2. Give execute permission to the run script:

   ```bash
   chmod +x run.sh
   ```

3. Run the script to build the project and start all containers (MongoDB, Redis, and the API):
   ```bash
   ./run.sh
   ```

4. The API will be accessible at:  
   [http://localhost:8080](http://localhost:8080)

5. The Swagger UI documentation is available at:  
   [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)


