# 💱 FX Exchange Service

FX Exchange Service is a Spring Boot application that provides real-time foreign exchange rates, currency conversion operations, and historical tracking of conversions.

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/your-username/fx.git
cd fx

./mvnw spring-boot:run

🔗 Access Points
📘 Swagger UI (API Documentation)
The API documentation is available through Swagger UI at:

http://localhost:8080/swagger-ui.html

http://localhost:8080/swagger-ui/index.html

💾 H2 In-Memory Database Console
You can access the H2 console at:

http://localhost:8080/h2-console

Connection settings:

Property	Value
JDBC URL	jdbc:h2:mem:testdb
Username	sa
Password	(leave blank)

⚙️ Tech Stack
Java 17

Spring Boot 3.5.0

Spring Web & WebFlux

Spring Data JPA

H2 In-Memory Database

MapStruct

Lombok

Swagger (Springdoc OpenAPI)

🧪 Running Tests
To execute unit and integration tests:

bash
./mvnw test
📂 Project Structure
src
└── main
    ├── java
    │   └── com.fxexchange.fx
    │       ├── controller     # REST endpoints
    │       ├── service        # Business logic
    │       ├── dto            # Data Transfer Objects
    │       ├── model          # JPA entities
    │       └── repository     # Database access layer
    └── resources
        └── application.yml    # Configuration (port, DB, etc.)
📌 Notes
Ensure that Lombok is enabled in your IDE.

IntelliJ: Preferences > Plugins > Lombok → Install and restart.

Make sure annotation processing is enabled for MapStruct and Lombok.

👤 Author
Burak Akgün
Software Engineer | Banking & Finance Tech
LinkedIn