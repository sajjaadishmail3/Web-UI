### Web-UI — Book Catalogue (Spring Boot 2 + Thymeleaf)

This module is the Web UI for the Book Catalogue. It calls the `Management-BE` REST API via a JAX-RS (Jersey) client.

#### Get the code / Pull latest
- From the repo root:
```
git pull
cd Web-UI
```
- Or, if you don’t have the repo yet (see root `README.md` for full clone options):
```
git clone https://github.com/<your-org-or-user>/<your-repo>.git
cd <your-repo>/Web-UI
```

#### Requirements
- Java 8 (1.8)
- Maven 3.6+

#### Configure
- Management API base URL (defaults to `http://localhost:8081/api`):
  - Env var: `MANAGEMENT_API_BASE_URL`
  - or property: `management.api.base-url`

#### Build and Run
- Ensure the backend `Management-BE` is running on http://localhost:8081 (see its README) before starting the UI.
- Windows (PowerShell):
```
./run-windows.ps1           # builds this module and runs on port 8080
```
- Linux/macOS:
```
chmod +x ./run-linux.sh
./run-linux.sh              # builds this module and runs on port 8080
```

Open http://localhost:8080/books in your browser.

Troubleshooting
- If the API runs on a different host/port, set `MANAGEMENT_API_BASE_URL` before starting, e.g. on PowerShell:
```
$env:MANAGEMENT_API_BASE_URL = "http://localhost:8081/api"; ./run-windows.ps1
```
- Verify Java version is 1.8 and that port 8080 is free.

#### Tech
- Spring Boot 2.7.x (Web, Validation, Thymeleaf)
- Jersey Client + Jackson JSR-310
