# Mechanical Workshop Management System - Backend

REST API built with **Java 17**, **Spring Boot 3**, **Spring Data JPA**, **Spring Security + JWT** and **MySQL**, ready to open in **Spring Tool Suite (STS)**.

This version matches the database `dbmechanicalworkshop` (UUID primary keys, English table/column names), the same database you already created in HeidiSQL.

## 1. Project structure

```
src/main/java/com/tallermecanico/api
├── ApiApplication.java
├── config/             -> SecurityConfig (CORS, security beans, filter chain)
├── controller/          -> REST controllers (@RestController)
├── service/             -> Service interfaces
├── service/impl/        -> Service implementations (business logic)
├── repository/          -> JpaRepository interfaces
├── entity/              -> JPA entities (@Entity), mapped to tuser, tcustomer, etc.
├── dto/request/         -> Input DTOs (validated with @Valid)
├── dto/response/        -> Output DTOs (entities are never exposed directly)
├── security/            -> JWT (JwtUtils, filter, UserDetailsService, etc.)
├── exception/           -> Custom exceptions + global handler
├── enums/                -> Role, WorkOrderStatus
└── util/                 -> IdGenerator (UUID generation for primary keys)

dbscript/dbmechanicalworkshop.sql   -> Database creation script (already executed in HeidiSQL)
```

## 2. Important: UUID primary keys

Every table uses `char(36)` as primary key (a UUID string), not an auto-increment number. The application generates the UUID in Java (`IdGenerator.generate()`) before saving each new record — this is why `spring.jpa.hibernate.ddl-auto=none` in `application.properties`: the tables already exist (you created them in HeidiSQL) and Hibernate must not try to alter them.

## 3. Prerequisites

- JDK 17
- Maven 3.8+ (or the one embedded in STS)
- MySQL 8.x running on `localhost:3306`, with the `dbmechanicalworkshop` database already created
- Spring Tool Suite 4 (or any Maven-compatible IDE)
- Postman (for testing)

## 4. Database configuration

`src/main/resources/application.properties` already points to:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dbmechanicalworkshop?...
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=none
```

Adjust username/password if yours are different. If you ever need to recreate the database from scratch, run `dbscript/dbmechanicalworkshop.sql` again in HeidiSQL.

## 5. How to open the project in Spring Tool Suite

1. `File > Import > Maven > Existing Maven Projects`
2. Select the project root folder (where `pom.xml` is located)
3. Wait for STS to download the Maven dependencies
4. Right-click the project > `Run As > Spring Boot App`

## 6. How to run from the command line

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 7. Security: JWT flow

1. The user registers (`POST /api/auth/register`) or logs in (`POST /api/auth/login`).
2. The API returns a JWT `token` (valid for 24 hours) and the user data.
3. For every other endpoint, the client must send the header:

```
Authorization: Bearer <token>
```

4. Without this header, protected routes return `401 Unauthorized`.
5. Create/update/delete actions are restricted by role with `@PreAuthorize` (ADMIN, MECHANIC, RECEPTIONIST). All authenticated roles can read (GET).

## 8. Available endpoints

### Authentication (public, no token required)
| Method | Endpoint              | Description                |
|--------|------------------------|------------------------------|
| POST   | /api/auth/register     | Registers a new user         |
| POST   | /api/auth/login        | Logs in and returns a JWT    |

### Customers
| Method | Endpoint              | Required role                   |
|--------|------------------------|------------------------------------|
| GET    | /api/customers         | Any authenticated user             |
| GET    | /api/customers/{id}    | Any authenticated user             |
| POST   | /api/customers         | ADMIN, RECEPTIONIST                |
| PUT    | /api/customers/{id}    | ADMIN, RECEPTIONIST                |
| DELETE | /api/customers/{id}    | ADMIN                              |

### Vehicles
| Method | Endpoint              | Required role                   |
|--------|------------------------|------------------------------------|
| GET    | /api/vehicles          | Any authenticated user             |
| GET    | /api/vehicles/{id}     | Any authenticated user             |
| POST   | /api/vehicles          | ADMIN, RECEPTIONIST                |
| PUT    | /api/vehicles/{id}     | ADMIN, RECEPTIONIST                |
| DELETE | /api/vehicles/{id}     | ADMIN                              |

### Mechanics
| Method | Endpoint              | Required role |
|--------|------------------------|------------------|
| GET    | /api/mechanics         | Authenticated     |
| GET    | /api/mechanics/{id}    | Authenticated     |
| POST   | /api/mechanics         | ADMIN             |
| PUT    | /api/mechanics/{id}    | ADMIN             |
| DELETE | /api/mechanics/{id}    | ADMIN             |

### Work Orders
| Method | Endpoint               | Required role                       |
|--------|-------------------------|----------------------------------------|
| GET    | /api/work-orders        | Authenticated                          |
| GET    | /api/work-orders/{id}   | Authenticated                          |
| POST   | /api/work-orders        | ADMIN, RECEPTIONIST                    |
| PUT    | /api/work-orders/{id}   | ADMIN, MECHANIC, RECEPTIONIST          |
| DELETE | /api/work-orders/{id}   | ADMIN                                  |

### Services
| Method | Endpoint              | Required role |
|--------|------------------------|------------------|
| GET    | /api/services           | Authenticated     |
| GET    | /api/services/{id}      | Authenticated     |
| POST   | /api/services           | ADMIN             |
| PUT    | /api/services/{id}      | ADMIN             |
| DELETE | /api/services/{id}      | ADMIN             |

### Spare Parts
| Method | Endpoint              | Required role |
|--------|------------------------|------------------|
| GET    | /api/spare-parts        | Authenticated     |
| GET    | /api/spare-parts/{id}   | Authenticated     |
| POST   | /api/spare-parts        | ADMIN             |
| PUT    | /api/spare-parts/{id}   | ADMIN             |
| DELETE | /api/spare-parts/{id}   | ADMIN             |

### Invoices
| Method | Endpoint              | Required role           |
|--------|------------------------|---------------------------|
| GET    | /api/invoices           | Authenticated              |
| GET    | /api/invoices/{id}      | Authenticated              |
| POST   | /api/invoices           | ADMIN, RECEPTIONIST        |
| PUT    | /api/invoices/{id}      | ADMIN, RECEPTIONIST        |
| DELETE | /api/invoices/{id}      | ADMIN                      |

## 9. How to test the API with Postman (step by step)

### Step 1: Register an ADMIN user

`POST http://localhost:8080/api/auth/register`

```json
{
  "firstName": "Admin",
  "surName": "Main",
  "email": "admin@workshop.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

Response (201 Created):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "idUser": "3f1e2a90-....",
    "firstName": "Admin",
    "surName": "Main",
    "email": "admin@workshop.com",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer"
  },
  "timestamp": "2026-06-24T10:00:00"
}
```

Save the `token` value, you'll need it for every other request.

### Step 2: Log in (optional, if you already have a user)

`POST http://localhost:8080/api/auth/login`
```json
{
  "email": "admin@workshop.com",
  "password": "admin123"
}
```

### Step 3: Configure the token in Postman

In each protected request:
1. Go to the **Authorization** tab
2. Type: **Bearer Token**
3. Paste the token obtained in step 1 or 2

### Step 4: Create a customer

`POST http://localhost:8080/api/customers`
Header: `Authorization: Bearer <token>`

```json
{
  "firstName": "John",
  "surName": "Smith",
  "documentNumber": "12345678",
  "phone": "999888777",
  "email": "john.smith@gmail.com",
  "address": "123 El Sol Ave, Cusco"
}
```

Save the `idCustomer` returned in the response.

### Step 5: Create a vehicle for that customer

`POST http://localhost:8080/api/vehicles`

```json
{
  "brand": "Toyota",
  "model": "Yaris",
  "year": 2020,
  "plate": "ABC-123",
  "color": "Red",
  "customerId": "<idCustomer from step 4>"
}
```

### Step 6: Create a mechanic

`POST http://localhost:8080/api/mechanics`

```json
{
  "firstName": "Carlos Quispe",
  "specialty": "Engine",
  "phone": "987654321"
}
```

### Step 7: Create a work order

`POST http://localhost:8080/api/work-orders`

```json
{
  "entryDate": "2026-06-24T09:00:00",
  "problemDescription": "The engine makes a strange noise when accelerating",
  "diagnosis": "Possible spark plug failure",
  "status": "PENDING",
  "vehicleId": "<idVehicle from step 5>",
  "mechanicId": "<idMechanic from step 6>"
}
```

### Step 8: Create a service and a spare part

`POST http://localhost:8080/api/services`
```json
{
  "name": "Spark plug replacement",
  "description": "Replacement of all 4 spark plugs",
  "price": 90.00
}
```

`POST http://localhost:8080/api/spare-parts`
```json
{
  "name": "NGK Spark Plug",
  "brand": "NGK",
  "price": 15.00,
  "stock": 40
}
```

### Step 9: Generate an invoice

`POST http://localhost:8080/api/invoices`

```json
{
  "issueDate": "2026-06-24T10:00:00",
  "customerId": "<idCustomer>",
  "details": [
    {
      "concept": "Spark plug replacement",
      "quantity": 1,
      "serviceId": "<idService>",
      "workOrderId": "<idWorkOrder>"
    },
    {
      "concept": "NGK Spark Plug x4",
      "quantity": 4,
      "sparePartId": "<idSparePart>",
      "workOrderId": "<idWorkOrder>"
    }
  ]
}
```

The API automatically computes `unitPrice`, `subtotal` and the invoice `total`, and decreases the spare part stock used.

### Step 10: Test the remaining methods

- `GET /api/customers` -> should list every customer
- `GET /api/vehicles/{id}` -> should show the vehicle detail
- `PUT /api/work-orders/{id}` -> updates the status to `"IN_REPAIR"` or `"FINISHED"`
- `DELETE /api/spare-parts/{id}` -> requires ADMIN role; if the user is RECEPTIONIST, it should respond `403 Forbidden`

### Step 11: Test error handling

- `GET /api/customers/non-existing-id` -> `404 Not Found` with a clear message
- `POST /api/customers` without the `documentNumber` field -> `400 Bad Request` with per-field validation detail
- Any protected endpoint without the `Authorization` header -> `401 Unauthorized`

## 10. Standard response format

```json
{
  "success": true,
  "message": "Human-readable description of the operation",
  "data": { },
  "timestamp": "2026-06-24T10:00:00"
}
```

On error:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2026-06-24T10:00:00"
}
```

## 11. CORS for Angular

The backend allows requests from `http://localhost:4200` (Angular CLI default port) and any `localhost` port. Configuration in `config/SecurityConfig.java`, method `corsConfigurationSource()`.
