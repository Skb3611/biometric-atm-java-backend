# 🚀 Java Biometric ATM Backend Guide

## 📋 Overview
This Spring Boot backend replicates the functionality of the Express.js biometric ATM system with the same API endpoints and data models.

---

## 🛠️ Prerequisites

### Required Software
- **Java 11+** (Recommended: Java 11 for stability)
- **Maven 3.6+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Environment Setup
```bash
# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-11
set PATH=%JAVA_HOME%\bin;%PATH%

# Or for Java 25
set JAVA_HOME=C:\Program Files\Java\jdk-25
set PATH=%JAVA_HOME%\bin;%PATH%
```

---

## 🚀 Start Command

### Development Mode
```bash
cd java-backend
mvn spring-boot:run
```

### Production Mode
```bash
mvn clean package
java -jar target/biometric-atm-backend-1.0.0.jar
```

---

## 📡 API Endpoints

### 🔓 Authentication Endpoints

#### POST `/auth/verify-fingerprint`
**Description**: Verify fingerprint and get user details
**Headers**: None
**Body**:
```json
{
  "fingerprintId": "fingerprint_jonny"
}
```
**Response**:
```json
{
  "message": "Fingerprint verified",
  "user": {
    "id": 3,
    "name": "Jonny",
    "fingerprintId": "fingerprint_jonny",
    "accounts": [...],
    "transactions": [...]
  }
}
```

### 🏥 Health Check

#### GET `/health`
**Description**: Check if server is running
**Headers**: None
**Response**:
```json
{
  "status": "UP",
  "timestamp": "2026-01-04T14:22:52.837946"
}
```

### 🏦 Dashboard Endpoints (Protected)

**🔐 All dashboard endpoints require `fingerprintId` header**

#### GET `/dashboard/account-details`
**Description**: Get user's account details and transactions
**Headers**: 
```
fingerprintId: fingerprint_jonny
```
**Response**:
```json
{
  "message": "User found",
  "user": {
    "id": 3,
    "name": "Jonny",
    "fingerprintId": "fingerprint_jonny",
    "accounts": [...],
    "transactions": [...]
  }
}
```

#### POST `/dashboard/account/withdraw`
**Description**: Withdraw money from account
**Headers**: `fingerprintId: fingerprint_jonny`
**Body**:
```json
{
  "accountNumber": "1001234572",
  "amt": 1000,
  "pin": 9012
}
```

#### POST `/dashboard/account/deposit`
**Description**: Deposit money to account
**Headers**: `fingerprintId: fingerprint_jonny`
**Body**:
```json
{
  "accountNumber": "1001234572",
  "amt": 1000,
  "pin": 9012
}
```

#### POST `/dashboard/account/transfer`
**Description**: Transfer money between accounts
**Body**:
```json
{
  "senderAccountNO": "1001234572",
  "receiverAccountNO": "1001234571",
  "amt": 1000,
  "pin": 9012
}
```

#### GET `/dashboard/account/statement/{accountNumber}`
**Description**: Get account statement
**Headers**: `fingerprintId: fingerprint_jonny`
**Path Variable**: `accountNumber`

---

## 🗄️ Database Configuration

### Current Setup (H2 In-Memory)
```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Access H2 Console
- **URL**: `http://localhost:8000/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (empty)

### Switch to PostgreSQL
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/biometric_atm
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 🌐 CORS Configuration

### Current Development Origins
- `http://localhost:8080` (Vue.js)
- `http://localhost:3000` (React)
- `http://localhost:5173` (Vite)
- `http://127.0.0.1:8080`
- `http://127.0.0.1:3000`
- `http://127.0.0.1:5173`

### Add Production Origins
```properties
spring.web.cors.allowed-origins=https://yourdomain.com,https://www.yourdomain.com
```

---

## 📊 Data Models

### User Entity
```java
{
  "id": 3,
  "name": "Jonny",
  "fingerprintId": "fingerprint_jonny",
  "createdAt": "2026-01-04T14:22:52.837946",
  "updatedAt": "2026-01-04T14:22:52.837946"
}
```

### Account Entity
```java
{
  "id": 5,
  "accountNumber": "1001234572",
  "pin": 9012,
  "balance": 60000,
  "bankName": "AXIS",
  "userId": 3,
  "createdAt": "2026-01-04T14:22:52.837946",
  "updatedAt": "2026-01-04T14:22:52.837946"
}
```

### Transaction Entity
```java
{
  "id": 1,
  "amount": 1000,
  "fromAccountNumber": "1001234572",
  "toAccountNumber": "1001234572",
  "type": "withdraw",
  "userId": 3,
  "createdAt": "2026-01-04T14:22:52.837946"
}
```

---

## 🔧 Configuration Files

### `application.properties`
- Server port: 8000
- Database: H2 in-memory
- CORS: Multiple origins
- Logging: DEBUG level

### `pom.xml`
- Java version: 11
- Spring Boot: 3.3.0
- Dependencies: Web, JPA, H2, PostgreSQL, Validation

---

## 📝 Sample Data

### Seeded Users
1. **Abhi** - `fingerprint_abhi`
2. **Rohan** - `fingerprint_rohan`
3. **Jonny** - `fingerprint_jonny`

### Sample Accounts
- **SBI**: Account numbers starting with `100123456`
- **HDFC**: Account numbers starting with `100123457`
- **ICICI**: Account numbers starting with `100123458`
- **AXIS**: Account numbers starting with `100123459`

### Sample Transactions
- Withdrawals, deposits, and transfers
- Amounts ranging from 1000 to 50000

---

## 🐛 Debugging & Logging

### View Logs
```bash
# Real-time logs in terminal
mvn spring-boot:run

# Check specific command output
command_status <command-id>
```

### Logging Levels
```properties
logging.level.com.biometricatm=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Common Issues
1. **CORS Errors**: Check allowed origins in application.properties
2. **Database Connection**: Verify H2 console or PostgreSQL settings
3. **Authentication**: Ensure `fingerprintId` header is included
4. **Java Version**: Use Java 11 for maximum compatibility

---

## 🔒 Security Features

### Authentication
- Fingerprint-based authentication
- Required for all dashboard endpoints
- Header: `fingerprintId`

### Input Validation
- All request bodies validated
- Required fields checked
- PIN verification for financial operations

---

## 📦 Build & Deployment

### Build JAR
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Docker Support (Future)
```dockerfile
FROM openjdk:11-jre-slim
COPY target/biometric-atm-backend-1.0.0.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## 🚨 Troubleshooting

### Port Already in Use
```bash
# Find process on port 8000
netstat -an | findstr :8000

# Kill Java processes
taskkill /F /IM java.exe
```

### Database Issues
- Check H2 console: `http://localhost:8000/h2-console`
- Verify PostgreSQL connection if switched
- Check SQL logs in console

### CORS Issues
- Verify frontend origin is in allowed list
- Check preflight OPTIONS requests
- Ensure credentials are allowed

---

## 📞 Support

### Frontend Integration
- Base URL: `http://localhost:8000`
- All endpoints return JSON
- Authentication via `fingerprintId` header

### Testing
- Use Postman or curl for API testing
- Sample fingerprint IDs available in seeded data
- H2 console for database inspection

---

**🎉 Your Java Biometric ATM Backend is ready!**
