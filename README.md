# Biometric ATM System — Java Backend

A Spring Boot implementation of the Biometric ATM System backend, providing the same API endpoints and functionality as the original Express.js version.

## Features

- **Fingerprint Authentication**: User authentication via fingerprint ID
- **Banking Operations**: Withdraw, deposit, and transfer money
- **Account Management**: View account details and transaction statements
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **CORS Support**: Configured for frontend integration
- **Data Seeding**: Automatic database seeding with sample data

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## API Endpoints

### Health
- `GET /health` - Server health check

### Authentication
- `POST /auth/verify-fingerprint` - Verify user by fingerprint ID

### Dashboard (Protected - requires fingerprintId header)
- `GET /dashboard/account-details` - Get user account details
- `POST /dashboard/account/withdraw` - Withdraw money from account
- `POST /dashboard/account/deposit` - Deposit money to account
- `POST /dashboard/account/transfer` - Transfer money between accounts
- `GET /dashboard/account/statement/{accountNumber}` - Get account statement

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL database

### Database Setup
1. Create a PostgreSQL database named `biometric_atm`
2. Update database credentials in `src/main/resources/application.properties`

### Running the Application

1. Clone and navigate to the project:
```bash
cd java-backend
```

2. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8000`

### Sample Data
The application automatically seeds the database with sample data on startup:

**Users:**
- Abhi (fingerprint: `fingerprint_abhi`)
  - SBI Account: 1001234567 (PIN: 1234, Balance: 50000)
  - HDFC Account: 1001234570 (PIN: 1234, Balance: 30000)

- Rohan (fingerprint: `fingerprint_rohan`)
  - ICICI Account: 1001234568 (PIN: 5678, Balance: 75000)
  - AXIS Account: 1001234571 (PIN: 5678, Balance: 45000)

- Jonny (fingerprint: `fingerprint_jonny`)
  - AXIS Account: 1001234572 (PIN: 9012, Balance: 60000)

## API Usage Examples

### Verify Fingerprint
```bash
curl -X POST http://localhost:8000/auth/verify-fingerprint \
  -H "Content-Type: application/json" \
  -d '{ "fingerprintId": "fingerprint_abhi" }'
```

### Get Account Details
```bash
curl http://localhost:8000/dashboard/account-details \
  -H "fingerprintId: fingerprint_abhi"
```

### Withdraw Money
```bash
curl -X POST http://localhost:8000/dashboard/account/withdraw \
  -H "fingerprintId: fingerprint_abhi" \
  -H "Content-Type: application/json" \
  -d '{ "amt": 5000, "bankName": "SBI", "pin": 1234 }'
```

### Deposit Money
```bash
curl -X POST http://localhost:8000/dashboard/account/deposit \
  -H "fingerprintId: fingerprint_abhi" \
  -H "Content-Type: application/json" \
  -d '{ "amt": 20000, "bankName": "HDFC" }'
```

### Transfer Money
```bash
curl -X POST http://localhost:8000/dashboard/account/transfer \
  -H "fingerprintId: fingerprint_abhi" \
  -H "Content-Type: application/json" \
  -d '{ "senderAccountNO": "1001234567", "receiverAccountNO": "1001234568", "amt": 5000, "pin": 1234 }'
```

### Get Account Statement
```bash
curl http://localhost:8000/dashboard/account/statement/1001234567 \
  -H "fingerprintId: fingerprint_abhi"
```

## Project Structure

```
src/main/java/com/biometricatm/
├── BiometricAtmApplication.java     # Main application class
├── config/
│   ├── DataSeeder.java              # Database seeding
│   └── WebConfig.java               # Web configuration (CORS, interceptors)
├── controller/
│   ├── AuthController.java          # Authentication endpoints
│   ├── DashboardController.java     # Dashboard endpoints
│   └── HealthController.java        # Health check endpoint
├── dto/                             # Data Transfer Objects
├── entity/                          # JPA entities
├── enums/                           # Enum classes
├── middleware/                      # Custom interceptors
└── repository/                      # JPA repositories
```

## Differences from Express.js Version

- **Language**: Java vs TypeScript
- **Framework**: Spring Boot vs Express.js
- **ORM**: Spring Data JPA/Hibernate vs Prisma
- **Validation**: Jakarta Bean Validation vs manual validation
- **Dependency Management**: Maven vs npm/pnpm
- **Database**: PostgreSQL (same as original)

The API endpoints, request/response formats, and business logic remain identical to ensure compatibility with the existing frontend.
