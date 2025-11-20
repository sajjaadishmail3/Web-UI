Book Catalogue Web UI (Spring Boot 2 + Thymeleaf)

This project is the Web UI layer for a multi-tier book catalogue solution. It provides a browser-based interface to list, add, edit, and delete books. The UI integrates with a separate Management service via a JAX-RS (Jersey) client.

Requirements
- Java 8 (1.8)
- Maven 3.6+
- A running Management service exposing a REST API at ${MANAGEMENT_API_BASE_URL} (default http://localhost:8081/api).
  - Expected endpoints:
    - GET    /api/books -> list of books
    - GET    /api/books/{id} -> single book
    - POST   /api/books -> create book
    - PUT    /api/books/{id} -> update book
    - DELETE /api/books/{id} -> delete book

Tech Stack
- Spring Boot 2 (Web, Validation, Thymeleaf)
- Thymeleaf templates for server-side HTML rendering
- JAX-RS Client using Jersey + Jackson for JSON
- Packaged as an executable jar with dependencies

Configure
You can configure the Management API base URL via either environment variable or application properties:
- Environment variable: MANAGEMENT_API_BASE_URL (recommended)
- Property in src/main/resources/application.properties: management.api.base-url

Defaults to http://localhost:8081/api if not set.

Build
1. mvn clean package

The jar will be created at target/book-ui.jar.

Run
- Linux/macOS:
  - ./run-linux.sh
- Windows (PowerShell):
  - ./run-windows.ps1

You may pass the Management API URL as an argument, for example:
- Linux/macOS: ./run-linux.sh http://localhost:8081/api
- Windows PowerShell: ./run-windows.ps1 http://localhost:8081/api

Alternatively, set the environment variable:
- Linux/macOS
  - export MANAGEMENT_API_BASE_URL=http://localhost:8081/api
  - java -jar target/book-ui.jar
- Windows PowerShell
  - $env:MANAGEMENT_API_BASE_URL = "http://localhost:8081/api"
  - java -jar target/book-ui.jar

The application starts on http://localhost:8080. Navigate to /books.

UI Features
- List all books
- Add a new book
- Edit an existing book
- Delete a book

Notes
- The Book fields used by the UI: id, title, author, isbn, publishedDate (yyyy-MM-dd). Ensure the Management service uses compatible JSON for LocalDate (ISO-8601).
- If your Management service differs in field names or paths, adjust Book and BookManagementClient accordingly.

Packaging as Executable Jar
This project uses spring-boot-maven-plugin. Run mvn package to produce a fat jar at target/book-ui.jar that can be executed with java -jar.

Troubleshooting
- 404/500 errors when loading books usually indicate the Management service is not reachable or has different endpoints. Verify MANAGEMENT_API_BASE_URL and that the service is running.
- Template errors on date inputs: ensure publishedDate serializes/deserializes as ISO date (yyyy-MM-dd). This project configures Jersey Jackson with JavaTimeModule.

License
MIT (or adjust as needed)
