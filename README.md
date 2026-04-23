# Bright_Class_LMS_backend

BrightClass LMS Backend 🚀

BrightClass is an enterprise-grade Learning Management System (LMS) backend built with Spring Boot. It is specifically architected to handle academic degree programs (like BIT) and skill-based courses, focusing on high security, modularity, and a standardized API architecture.

## Table of Contents

- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Response Standard](#api-response-standard)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Roadmap](#roadmap)

## 🌟 Key Features

### 🔐 Advanced Security (Industry Standard)

- **JWT Authentication**: Secure, stateless authentication using JSON Web Tokens.
- **Refresh Token Rotation**: Advanced security layer that prevents token theft by rotating tokens on every refresh request.
- **HttpOnly & Secure Cookies**: All sensitive tokens are stored in HttpOnly cookies to mitigate XSS (Cross-Site Scripting) attacks.
- **Role-Based Access Control (RBAC)**: Fine-grained access management for Admin, Lecturer, and Student roles.
- **Automated Token Cleanup**: A background Spring Scheduler that automatically purges expired tokens to maintain database performance.

### 🛠️ Technical Architecture

- **Standardized API Responses**: Every endpoint returns data in a consistent ApiResponse wrapper (Timestamp, Status, Success, Message, Data).
- **DTO Pattern with MapStruct**: Strict separation between the database layer and API layer for clean, maintainable code.
- **Global Exception Handling**: Centralized error management that provides meaningful feedback to the frontend.
- **CORS Configured**: Securely optimized for communication with the Angular frontend (localhost:4200).

### 📚 Academic & Enrollment

- **Course Management**: Full curriculum handling including semesters and categories.
- **Enrollment Workflow**: Robust enrollment logic with support for manual and gateway-based payment verification.

## 💻 Tech Stack

- **Backend**: Spring Boot 4.x
- **Language**: Java 17+ (LTS)
- **Security**: Spring Security 6 (JWT)
- **Database**: PostgreSQL
- **Mapping**: MapStruct & Lombok
- **Build Tool**: Maven

## 📂 Project Structure

```
src/main/java/com/lms/lms_backend/
├── config/             # Security, CORS, and Application Bean Configs
├── controller/         # REST Controllers (API Endpoints)
├── dto/                # Data Transfer Objects & Response Wrapper
├── entity/             # JPA Entities (Database Models)
├── exception/          # Global Exception Handling Logic
├── mapper/             # MapStruct Interface Definitions
├── repository/         # Spring Data JPA Repositories
├── scheduler/          # Scheduled Background Tasks (Cleanup)
├── security/           # JWT Filters, Util, and Custom Auth Logic
└── service/            # Business Logic Layer (Implementations)
```

## 🚀 Getting Started

### Prerequisites

- **JDK**: 17 or higher
- **Maven**: 3.8.x or higher
- **PostgreSQL**: 14 or higher

### Installation & Setup

1. **Clone the repository**:
```bash
git clone https://github.com/eshanjayawardhana/Bright_Class_LMS_backend.git
cd Bright_Class_LMS_backend
```

2. **Configure Database**:
Update `src/main/resources/application.yml` with your PostgreSQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/lms_db
    username: root
    password: 1234
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

3. **Build and Run**:
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## Configuration

### Environment Variables

Create an `.env` file or configure `application.yml`:

```yaml
spring:
  application:
    name: brightclass-lms-backend
  datasource:
    url: jdbc:postgresql://localhost:5432/lms_db
    username: root
    password: 1234
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080
  servlet:
    context-path: /api

jwt:
  secret: jwt_secret_key
  expiration: 86400000
  refresh-token-expiration: 604800000

cors:
  allowed-origins: http://localhost:4200
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: '*'
```

## 📡 API Response Standard

All API responses follow this consistent structure:

### Success Response

```json
{
  "timestamp": "2026-04-23T10:30:00",
  "status": 200,
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

### Error Response

```json
{
  "timestamp": "2026-04-23T10:30:00",
  "status": 400,
  "success": false,
  "message": "Validation failed",
  "data": null
}
```

## API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | User authentication |
| POST | `/api/auth/register` | User registration |
| POST | `/api/auth/refresh` | Refresh access token |
| POST | `/api/auth/logout` | User logout |

### Course Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/courses` | List all courses |
| POST | `/api/courses` | Create a new course |
| GET | `/api/courses/{id}` | Get course details |
| PUT | `/api/courses/{id}` | Update course |
| DELETE | `/api/courses/{id}` | Delete course |

### Enrollment Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/enrollments` | List enrollments |
| POST | `/api/enrollments` | Create enrollment |
| GET | `/api/enrollments/{id}` | Get enrollment details |
| PUT | `/api/enrollments/{id}` | Update enrollment |

### User Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List all users (Admin only) |
| GET | `/api/users/{id}` | Get user details |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user (Admin only) |

## Security

### JWT Token Flow

1. **Login**: User provides credentials
2. **Token Generation**: Server generates JWT access token and refresh token
3. **Token Storage**: Tokens stored in HttpOnly, Secure cookies
4. **API Requests**: Include token in Authorization header
5. **Token Refresh**: Use refresh token to obtain new access token
6. **Logout**: Tokens are invalidated and cleaned up

### Role-Based Access Control

- **ADMIN**: Full system access, user management
- **LECTURER**: Course management, grading, student management
- **STUDENT**: Course enrollment, assignment submission, grade viewing


## 🏗️ Roadmap

- [x] JWT Authentication & RBAC
- [x] Refresh Token Rotation logic
- [x] HttpOnly Cookie implementation
- [x] API Response Standardization
- [x] Automated Token Cleanup Scheduler
- [ ] PayHere Payment Gateway Integration (Next Phase)
- [ ] AWS S3 / Cloudinary Integration for Course Materials
- [ ] Email Notification Engine
- [ ] Real-time Notifications (WebSocket)
- [ ] Advanced Analytics & Reporting



---

**Developed by**: Eshan Jayawardana

**Status**: Backend 90% Complete (Moving to Frontend Integration)

**Last Updated**: April 23, 2026

**Version**: 1.0.0
