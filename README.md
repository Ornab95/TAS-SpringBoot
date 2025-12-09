# Talent Acquisition System - Backend API

A comprehensive REST API for a modern talent acquisition system built with Spring Boot, providing secure authentication, job management, application tracking, and file handling capabilities.

## 🚀 Features

- **JWT Authentication & Authorization** - Secure user authentication with role-based access control
- **User Management** - Complete user lifecycle management with profile picture support
- **Job Management** - CRUD operations for job postings with advanced filtering
- **Application Tracking** - End-to-end application management system
- **File Upload & Serving** - Secure file handling for resumes and profile pictures
- **Password Management** - Secure password change functionality
- **Rate Limiting** - Built-in request rate limiting for API protection
- **CORS Support** - Cross-origin resource sharing configuration
- **Database Integration** - MySQL database with JPA/Hibernate

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Security**: Spring Security with JWT
- **Database**: MySQL 8.0+ with Spring Data JPA
- **Build Tool**: Maven
- **Java Version**: 17+
- **Authentication**: JWT (JSON Web Tokens)
- **Password Encryption**: BCrypt
- **File Storage**: Local file system

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## ⚙️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd TAS/backend
```

### 2. Database Setup
```sql
-- Create database (optional - auto-created by application)
CREATE DATABASE tasdb;

-- Create user (optional)
CREATE USER 'tas_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON tasdb.* TO 'tas_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configuration
Update `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/tasdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Server Configuration
server.port=8080
```

### 4. Build & Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### POST /api/auth/login
Authenticate user and receive JWT token.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CANDIDATE",
  "department": "IT & Engineering",
  "profilePicture": "/api/files/profile-pictures/filename.jpg"
}
```

#### POST /api/auth/register
Register a new user account.

**Request Body:**
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "firstName": "Jane",
  "lastName": "Smith",
  "phone": "+1234567890",
  "role": "CANDIDATE",
  "department": "Marketing"
}
```

### User Management Endpoints

#### PUT /api/users/{userId}
Update user profile information.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "firstName": "Updated Name",
  "lastName": "Updated Last",
  "email": "updated@example.com",
  "phone": "+0987654321",
  "department": "Finance"
}
```

#### PUT /api/users/{userId}/change-password
Change user password.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword456"
}
```

#### POST /api/users/{userId}/profile-picture
Upload user profile picture.

**Headers:** `Authorization: Bearer <token>`

**Request:** Multipart form data with `profilePicture` file

**Response:**
```json
{
  "profilePictureUrl": "/api/files/profile-pictures/uuid-filename.jpg"
}
```

### Job Management Endpoints

#### GET /api/jobs/public
Get all active job postings (public access).

**Response:**
```json
[
  {
    "id": 1,
    "title": "Software Engineer",
    "description": "Join our development team...",
    "department": "IT & Engineering",
    "location": "New York, NY",
    "salaryMin": 70000,
    "salaryMax": 90000,
    "experienceRequired": 2,
    "skillsRequired": "Java, Spring Boot, MySQL",
    "employmentType": "FULL_TIME",
    "status": "ACTIVE",
    "postedAt": "2024-01-15T10:30:00",
    "applicationDeadline": "2024-02-15T23:59:59"
  }
]
```

#### GET /api/jobs/{id}
Get specific job details.

#### POST /api/jobs
Create new job posting.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "title": "Senior Developer",
  "description": "We are looking for...",
  "department": "IT & Engineering",
  "location": "Remote",
  "salaryMin": 80000,
  "salaryMax": 120000,
  "experienceRequired": 5,
  "skillsRequired": "Java, Spring, React, MySQL",
  "employmentType": "FULL_TIME",
  "applicationDeadline": "2024-03-01T23:59:59"
}
```

#### PUT /api/jobs/{id}
Update existing job posting.

#### DELETE /api/jobs/{id}
Delete job posting.

### Application Management Endpoints

#### GET /api/applications
Get all applications (admin/HR access).

#### GET /api/applications/candidate/{candidateId}
Get applications by candidate.

#### POST /api/applications
Submit job application.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "jobId": 1,
  "coverLetter": "I am interested in this position...",
  "portfolioUrl": "https://myportfolio.com"
}
```

### File Serving Endpoints

#### GET /api/files/profile-pictures/{filename}
Serve profile picture files (public access).

#### GET /api/files/resumes/{filename}
Serve resume files (authenticated access).

## 🏗 Project Structure

```
src/main/java/com/tas/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java         # Security & CORS configuration
│   ├── JwtAuthenticationFilter.java # JWT filter
│   ├── JwtUtil.java               # JWT utility methods
│   ├── RateLimitingFilter.java    # Rate limiting
│   └── FileUploadConfig.java      # File upload configuration
├── controller/             # REST Controllers
│   ├── AuthController.java        # Authentication endpoints
│   ├── UserController.java        # User management
│   ├── JobController.java         # Job management
│   ├── ApplicationController.java # Application management
│   ├── FileController.java        # File upload
│   └── FileServeController.java   # File serving
├── dto/                   # Data Transfer Objects
│   ├── AuthResponse.java          # Authentication response
│   └── LoginRequest.java          # Login request
├── entity/                # JPA Entities
│   ├── User.java                  # User entity
│   ├── Job.java                   # Job entity
│   ├── Application.java           # Application entity
│   └── Company.java               # Company entity
├── enums/                 # Enumerations
│   ├── UserRole.java              # User roles
│   ├── ApplicationStatus.java     # Application statuses
│   └── JobStatus.java             # Job statuses
├── repository/            # Data Repositories
│   ├── UserRepository.java        # User data access
│   ├── JobRepository.java         # Job data access
│   └── ApplicationRepository.java # Application data access
├── service/               # Business Logic
│   ├── AuthService.java           # Authentication service
│   ├── JobService.java            # Job management service
│   └── InterviewService.java      # Interview management
└── TasApplication.java    # Main application class
```

## 🔐 Security Features

### JWT Authentication
- Stateless authentication using JSON Web Tokens
- Token expiration and validation
- Role-based access control

### Password Security
- BCrypt password hashing
- Secure password change functionality
- Password strength validation

### Rate Limiting
- Request rate limiting to prevent abuse
- Configurable limits per endpoint

### CORS Configuration
- Cross-origin resource sharing setup
- Secure frontend-backend communication

## 🗄 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    profile_picture_file_name VARCHAR(255),
    profile_picture_file_path VARCHAR(500),
    role ENUM('CANDIDATE', 'HR_ADMIN', 'RECRUITER', 'HIRING_MANAGER', 'SYSTEM_ADMIN'),
    department VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Jobs Table
```sql
CREATE TABLE jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    department VARCHAR(100),
    location VARCHAR(200),
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    experience_required INT,
    skills_required TEXT,
    employment_type ENUM('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP'),
    status ENUM('ACTIVE', 'INACTIVE', 'CLOSED'),
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    application_deadline TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
```

### Applications Table
```sql
CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    cover_letter TEXT,
    resume_file_name VARCHAR(255),
    resume_file_path VARCHAR(500),
    resume_data LONGBLOB,
    portfolio_url VARCHAR(500),
    status ENUM('APPLIED', 'REVIEWED', 'SHORTLISTED', 'REJECTED', 'HIRED'),
    screening_score INT,
    source_channel VARCHAR(100),
    assigned_recruiter BIGINT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (job_id) REFERENCES jobs(id),
    FOREIGN KEY (candidate_id) REFERENCES users(id),
    FOREIGN KEY (assigned_recruiter) REFERENCES users(id)
);
```

## 🚦 API Response Codes

| Code | Description |
|------|-------------|
| 200  | Success |
| 201  | Created |
| 400  | Bad Request |
| 401  | Unauthorized |
| 403  | Forbidden |
| 404  | Not Found |
| 409  | Conflict |
| 500  | Internal Server Error |

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthControllerTest

# Run with coverage
mvn test jacoco:report
```

### Test Categories
- **Unit Tests**: Service layer testing
- **Integration Tests**: Controller and repository testing
- **Security Tests**: Authentication and authorization testing

## 📦 Deployment

### Production Configuration
```properties
# Production database
spring.datasource.url=jdbc:mysql://prod-server:3306/tasdb
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Security
jwt.secret=${JWT_SECRET}
jwt.expiration=3600000

# Logging
logging.level.com.tas=INFO
logging.file.name=logs/tas-backend.log
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/tas-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Arnab Chakraborty**
- Portfolio: [Your Portfolio URL]
- GitHub: [@your-github-username]
- LinkedIn: [Your LinkedIn Profile]
- Email: your.email@example.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- MySQL for reliable database solution
- JWT.io for token implementation guidance
- All contributors and testers

---

*Built with ❤️ using Spring Boot*