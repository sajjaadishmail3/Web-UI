### Management-BE — Book Management Service (Spring Boot 2, Java 8)

This service exposes a REST API to manage a catalogue of books backed by an in-memory H2 database. It supports basic CRUD operations: list, add, update, delete.

Requirements satisfied:
- Spring Boot 2.x application (2.7.x)
- Java 1.8
- Maven project producing an executable fat jar
- In-memory DB (H2) — not an in-memory list
- REST API endpoints for List/Add/Update/Delete
- Minimum fields: Name (title), ISBN, Publish date (dd/MM/yyyy), Price (ZAR), Book Type

#### Get the code / Pull latest
- From the repo root:
```
git pull
cd Management-BE
```
- Or, if you don’t have the repo yet (see root `README.md` for full clone options):
```
git clone https://github.com/<your-org-or-user>/<your-repo>.git
cd <your-repo>/Management-BE
```

#### Build and Run

Prerequisites:
- Java 8 (JDK 1.8)
- Maven 3.6+

Windows PowerShell:
```
cd Management-BE
./run-windows.ps1              # builds and runs
# or skip build if you already built
./run-windows.ps1 -SkipBuild
```

Linux/macOS:
```
cd Management-BE
chmod +x ./run-linux.sh
./run-linux.sh                 # builds and runs
# or skip build
./run-linux.sh --skip-build
```

By default the service starts on port 8081 with base path `/api`.

H2 Console (dev only):
- URL: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:booksdb`
- User: `sa`, Password: empty

#### API Overview

Base URL: `http://localhost:8081/api`

- GET `/books` — list all books
- GET `/books/{id}` — get one book
- POST `/books` — create a book
- PUT `/books/{id}` — update a book
- DELETE `/books/{id}` — delete a book

Model `Book` (JSON):
```
{
  "id": 1,                       // generated
  "title": "The Hobbit",        // name
  "author": "J.R.R. Tolkien",
  "isbn": "9780261103344",
  "publishedDate": "21/09/1937", // dd/MM/yyyy
  "price": 299.99,               // ZAR
  "type": "HARD_COVER"          // HARD_COVER | SOFT_COVER | EBOOK | AUDIOBOOK
}
```

Example curl requests:
```
# List
curl http://localhost:8081/api/books

# Create
curl -X POST http://localhost:8081/api/books \
  -H 'Content-Type: application/json' \
  -d '{
        "title":"The Pragmatic Programmer",
        "author":"Andrew Hunt, David Thomas",
        "isbn":"9780201616224",
        "publishedDate":"30/10/1999",
        "price": 550.00,
        "type":"SOFT_COVER"
      }'

# Update
curl -X PUT http://localhost:8081/api/books/1 \
  -H 'Content-Type: application/json' \
  -d '{
        "title":"The Pragmatic Programmer (20th Anniversary)",
        "author":"Andrew Hunt, David Thomas",
        "isbn":"9780135957059",
        "publishedDate":"13/09/2019",
        "price": 650.00,
        "type":"SOFT_COVER"
      }'

# Delete
curl -X DELETE http://localhost:8081/api/books/1
```

Validation and errors:
- 400 with details for invalid payloads (missing fields, invalid date/price)
- 404 for not found IDs

#### Integration note (Web-UI)
The Web-UI client is configured to call `http://localhost:8081/api/books`. Ensure the UI sends/accepts the date in `dd/MM/yyyy` format and includes the `price` and `type` fields as required by the Management service.
