

# Currency Conversion APIs

**Spring Boot project for retrieving currency exchange rates by integrating with the third-party API [openexchangerates.org](https://openexchangerates.org/).**

---

## 🔗 List of API Endpoints

### 1. Get Supported Currencies
GET http://localhost:8080/api/v1/currencies/list
- Returns the list of all supported currencies.

### 2. Get Exchange Rates for a Base Currency
GET http://localhost:8080/api/v1/currencies/rates?base=USD
- Returns exchange rates based on the specified base currency.
- Replace `USD` with any valid currency code (e.g., `EGP`, `EUR`).

---

## 🧪 Swagger UI

For testing and exploring all available APIs interactively, open:

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## 🚀 Steps to Run the Project

### 1. Prerequisites

Ensure you have the following installed:
- [Java 17+](https://adoptium.net/)
- [Docker & Docker Compose](https://docs.docker.com/get-docker/)
- [Maven](https://maven.apache.org/) (or use the wrapper `./mvnw`)
- (Optional) [IntelliJ IDEA](https://www.jetbrains.com/idea/) or any preferred IDE

---

### 2. Clone the Repository

```bash
git clone https://github.com/KhaledEl-Saeed/CurrencyConverter.git
cd CurrencyConverter
```
### 3. Configure Environment Variables
update application.yml to include your Open Exchange Rates API key:

app-id: YOUR_APP_ID_HERE

---
### 4. Start Redis & PostgreSQL with Docker
```bash
docker-compose up -d
```
---
### 5. Run the Spring Boot Application
- Option 1: Using Maven (Terminal)
```bash
./mvnw spring-boot:run
```
- Option 2: Using an IDE
  - Open the project in IntelliJ IDEA or any IDE.
  - Run the CurrencyConverterApplication main class.

---
### 6. Verify the Application
Visit:

    http://localhost:8080/api/v1/currencies/list

    Or use the Swagger UI : http://localhost:8080/swagger-ui/index.html#/
---
### 7. 🗃️ Technologies Used
- Spring Boot

- Spring Data JPA

- Redis (for caching)

- PostgreSQL (for storing historical data)

- Flyway (for DB migrations)

- Docker (for service orchestration)

- Swagger / OpenAPI







