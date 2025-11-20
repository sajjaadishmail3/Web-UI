Monorepo: Book Catalogue — Web-UI and Management-BE

This repository is a Maven multi-module project (monorepo) with two Spring Boot apps:

- Web-UI — Spring Boot 2 + Thymeleaf web application
- Management-BE — Spring Boot 2 REST API with H2 in-memory database

Structure
- pom.xml (parent aggregator: packaging=pom)
- Web-UI/ … (UI module)
- Management-BE/ … (Backend API module)

Requirements
- Java 8 (1.8)
- Maven 3.6+

Get the code
- Clone (HTTPS):
```
git clone https://github.com/<your-org-or-user>/<your-repo>.git
cd <your-repo>
```
- Or clone (SSH):
```
git clone git@github.com:<your-org-or-user>/<your-repo>.git
cd <your-repo>
```

Pull the latest changes
```
git pull
```

Build all modules
```
mvn clean package
```

Build one module (and its dependencies)
```
mvn -pl Web-UI -am clean package
mvn -pl Management-BE -am clean package
```

Run everything locally (recommended order)
1) Start Management-BE (API on port 8081)
- Windows PowerShell:
```
cd Management-BE
./run-windows.ps1
```
- Linux/macOS:
```
cd Management-BE
chmod +x run-linux.sh
./run-linux.sh
```

2) Start Web-UI (UI on port 8080)
- Windows PowerShell:
```
cd Web-UI
./run-windows.ps1
```
- Linux/macOS:
```
cd Web-UI
chmod +x run-linux.sh
./run-linux.sh
```

Defaults and configuration
- API base URL: http://localhost:8081/api
- Web-UI calls the API above by default. You can override via env var `MANAGEMENT_API_BASE_URL` or property `management.api.base-url`.

Quick test
- After both services start:
  - Open the UI: http://localhost:8080/books
  - API health (example): http://localhost:8081/api/books

Troubleshooting
- Ensure the correct JDK is used: `java -version` should report 1.8.
- Ports 8080 (UI) and 8081 (API) must be free. Change ports via `application.properties` if needed.
- If your IDE doesn’t detect both modules, re-import the Maven project from the root `pom.xml`.

Module READMEs
- See `Web-UI/README.md` for UI details.
- See `Management-BE/README.md` for API details.
