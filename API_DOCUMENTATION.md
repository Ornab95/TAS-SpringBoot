# TAS Backend - Detailed API Documentation

## 🔗 Base URL
```
http://localhost:8080
```

## 🔑 Authentication
All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📋 Complete API Reference

### 🔐 Authentication Endpoints

#### 1. User Login
**POST** `/api/auth/login`

**Description:** Authenticate user and receive JWT token with user details.

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Success Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTYzOTU2NzIwMCwiZXhwIjoxNjM5NjUzNjAwfQ.signature",
  "id": 1,
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CANDIDATE",
  "department": "IT & Engineering",
  "profilePicture": "/api/files/profile-pictures/uuid-filename.jpg"
}
```

**Error Responses:**
```json
// 401 Unauthorized
{
  "error": "Invalid credentials"
}

// 400 Bad Request
{
  "error": "Email and password are required"
}
```

#### 2. User Registration
**POST** `/api/auth/register`

**Description:** Register a new user account.

**Request Body:**
```json
{
  "email": "jane.smith@example.com",
  "password": "strongPassword456",
  "firstName": "Jane",
  "lastName": "Smith",
  "phone": "+1-555-0123",
  "role": "CANDIDATE",
  "department": "Marketing"
}
```

**Success Response (200):**
```json
{
  "id": 2,
  "email": "jane.smith@example.com",
  "firstName": "Jane",
  "lastName": "Smith",
  "phone": "+1-555-0123",
  "role": "CANDIDATE",
  "department": "Marketing",
  "active": true,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### 👤 User Management Endpoints

#### 3. Update User Profile
**PUT** `/api/users/{userId}`

**Description:** Update user profile information.

**Path Parameters:**
- `userId` (Long): User ID

**Request Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John Updated",
  "lastName": "Doe Updated",
  "email": "john.updated@example.com",
  "phone": "+1-555-9999",
  "department": "Finance"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "email": "john.updated@example.com",
  "firstName": "John Updated",
  "lastName": "Doe Updated",
  "phone": "+1-555-9999",
  "role": "CANDIDATE",
  "department": "Finance",
  "active": true,
  "updatedAt": "2024-01-15T11:45:00Z"
}
```

#### 4. Change Password
**PUT** `/api/users/{userId}/change-password`

**Description:** Change user password with current password verification.

**Request Body:**
```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "newSecurePassword456"
}
```

**Success Response (200):**
```json
{
  "message": "Password changed successfully"
}
```

**Error Responses:**
```json
// 400 Bad Request - Wrong current password
{
  "error": "Current password is incorrect"
}

// 400 Bad Request - Missing fields
{
  "error": "Current password and new password are required"
}
```

#### 5. Upload Profile Picture
**POST** `/api/users/{userId}/profile-picture`

**Description:** Upload and set user profile picture.

**Request Headers:**
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**Request Body:**
```
Form Data:
- profilePicture: [image file]
```

**Success Response (200):**
```json
{
  "profilePictureUrl": "/api/files/profile-pictures/a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg"
}
```

### 💼 Job Management Endpoints

#### 6. Get All Jobs (Public)
**GET** `/api/jobs/public`

**Description:** Retrieve all active job postings. No authentication required.

**Query Parameters:**
- `department` (String, optional): Filter by department
- `location` (String, optional): Filter by location
- `minSalary` (Integer, optional): Minimum salary filter
- `maxSalary` (Integer, optional): Maximum salary filter

**Example Request:**
```
GET /api/jobs/public?department=IT%20%26%20Engineering&minSalary=50000
```

**Success Response (200):**
```json
[
  {
    "id": 1,
    "title": "Senior Software Engineer",
    "description": "We are seeking a talented Senior Software Engineer to join our dynamic development team. The ideal candidate will have extensive experience in Java, Spring Boot, and modern web technologies.",
    "department": "IT & Engineering",
    "location": "New York, NY",
    "salaryMin": 80000,
    "salaryMax": 120000,
    "experienceRequired": 5,
    "skillsRequired": "Java, Spring Boot, React, MySQL, Docker",
    "employmentType": "FULL_TIME",
    "status": "ACTIVE",
    "postedAt": "2024-01-10T09:00:00Z",
    "applicationDeadline": "2024-02-10T23:59:59Z",
    "companyName": "TechCorp Solutions"
  },
  {
    "id": 2,
    "title": "Marketing Specialist",
    "description": "Join our marketing team to drive brand awareness and customer engagement through innovative campaigns.",
    "department": "Marketing",
    "location": "Remote",
    "salaryMin": 45000,
    "salaryMax": 65000,
    "experienceRequired": 2,
    "skillsRequired": "Digital Marketing, SEO, Content Creation, Analytics",
    "employmentType": "FULL_TIME",
    "status": "ACTIVE",
    "postedAt": "2024-01-12T14:30:00Z",
    "applicationDeadline": "2024-02-12T23:59:59Z",
    "companyName": "TechCorp Solutions"
  }
]
```

#### 7. Get Job by ID
**GET** `/api/jobs/{id}`

**Description:** Retrieve specific job details by ID.

**Path Parameters:**
- `id` (Long): Job ID

**Success Response (200):**
```json
{
  "id": 1,
  "title": "Senior Software Engineer",
  "description": "Detailed job description here...",
  "department": "IT & Engineering",
  "location": "New York, NY",
  "salaryMin": 80000,
  "salaryMax": 120000,
  "experienceRequired": 5,
  "skillsRequired": "Java, Spring Boot, React, MySQL, Docker",
  "employmentType": "FULL_TIME",
  "status": "ACTIVE",
  "postedAt": "2024-01-10T09:00:00Z",
  "applicationDeadline": "2024-02-10T23:59:59Z",
  "companyName": "TechCorp Solutions",
  "benefits": "Health insurance, 401k, flexible hours, remote work options",
  "requirements": "Bachelor's degree in Computer Science or related field"
}
```

#### 8. Create Job Posting
**POST** `/api/jobs`

**Description:** Create a new job posting. Requires HR/Admin role.

**Request Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Frontend Developer",
  "description": "We are looking for a skilled Frontend Developer to join our team and help build amazing user experiences.",
  "department": "IT & Engineering",
  "location": "San Francisco, CA",
  "salaryMin": 70000,
  "salaryMax": 95000,
  "experienceRequired": 3,
  "skillsRequired": "React, TypeScript, CSS, HTML, JavaScript",
  "employmentType": "FULL_TIME",
  "applicationDeadline": "2024-03-01T23:59:59Z",
  "benefits": "Health insurance, dental, vision, 401k matching",
  "requirements": "3+ years of frontend development experience"
}
```

**Success Response (201):**
```json
{
  "id": 3,
  "title": "Frontend Developer",
  "description": "We are looking for a skilled Frontend Developer...",
  "department": "IT & Engineering",
  "location": "San Francisco, CA",
  "salaryMin": 70000,
  "salaryMax": 95000,
  "experienceRequired": 3,
  "skillsRequired": "React, TypeScript, CSS, HTML, JavaScript",
  "employmentType": "FULL_TIME",
  "status": "ACTIVE",
  "postedAt": "2024-01-15T16:20:00Z",
  "applicationDeadline": "2024-03-01T23:59:59Z",
  "createdBy": 5
}
```

#### 9. Update Job Posting
**PUT** `/api/jobs/{id}`

**Description:** Update existing job posting. Requires HR/Admin role.

**Request Body:** Same as Create Job Posting

#### 10. Delete Job Posting
**DELETE** `/api/jobs/{id}`

**Description:** Delete job posting. Requires HR/Admin role.

**Success Response (200):**
```json
{
  "message": "Job deleted successfully"
}
```

### 📄 Application Management Endpoints

#### 11. Get All Applications
**GET** `/api/applications`

**Description:** Retrieve all applications. Requires HR/Admin role.

**Query Parameters:**
- `status` (String, optional): Filter by application status
- `jobId` (Long, optional): Filter by job ID
- `candidateId` (Long, optional): Filter by candidate ID

**Success Response (200):**
```json
[
  {
    "id": 1,
    "job": {
      "id": 1,
      "title": "Senior Software Engineer",
      "department": "IT & Engineering"
    },
    "candidate": {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "email": "jane.smith@example.com"
    },
    "coverLetter": "I am very interested in this position because...",
    "resumeFileName": "jane_smith_resume.pdf",
    "portfolioUrl": "https://janesmith.dev",
    "status": "APPLIED",
    "appliedAt": "2024-01-14T10:15:00Z",
    "lastUpdated": "2024-01-14T10:15:00Z",
    "screeningScore": null,
    "notes": null
  }
]
```

#### 12. Get Applications by Candidate
**GET** `/api/applications/candidate/{candidateId}`

**Description:** Retrieve all applications submitted by a specific candidate.

**Path Parameters:**
- `candidateId` (Long): Candidate user ID

**Success Response (200):**
```json
[
  {
    "id": 1,
    "job": {
      "id": 1,
      "title": "Senior Software Engineer",
      "department": "IT & Engineering",
      "location": "New York, NY",
      "salaryMin": 80000,
      "salaryMax": 120000
    },
    "coverLetter": "I am excited to apply for this position...",
    "resumeFileName": "my_resume.pdf",
    "portfolioUrl": "https://myportfolio.com",
    "status": "REVIEWED",
    "appliedAt": "2024-01-14T10:15:00Z",
    "lastUpdated": "2024-01-16T14:30:00Z",
    "screeningScore": 85,
    "notes": "Strong technical background, good communication skills"
  }
]
```

#### 13. Submit Job Application
**POST** `/api/applications`

**Description:** Submit a new job application.

**Request Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "jobId": 1,
  "coverLetter": "Dear Hiring Manager, I am writing to express my strong interest in the Senior Software Engineer position. With over 5 years of experience in Java and Spring Boot development, I believe I would be a valuable addition to your team...",
  "portfolioUrl": "https://johndoe.dev"
}
```

**Success Response (201):**
```json
{
  "id": 2,
  "job": {
    "id": 1,
    "title": "Senior Software Engineer"
  },
  "candidate": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  },
  "coverLetter": "Dear Hiring Manager, I am writing to express...",
  "portfolioUrl": "https://johndoe.dev",
  "status": "APPLIED",
  "appliedAt": "2024-01-15T11:20:00Z",
  "lastUpdated": "2024-01-15T11:20:00Z"
}
```

### 📁 File Management Endpoints

#### 14. Serve Profile Pictures
**GET** `/api/files/profile-pictures/{filename}`

**Description:** Serve profile picture files. Public access.

**Path Parameters:**
- `filename` (String): Profile picture filename

**Response:** Image file (JPEG/PNG)

**Example:**
```
GET /api/files/profile-pictures/a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg
```

#### 15. Serve Resume Files
**GET** `/api/files/resumes/{filename}`

**Description:** Serve resume files. Requires authentication.

**Request Headers:**
```
Authorization: Bearer <token>
```

**Path Parameters:**
- `filename` (String): Resume filename

**Response:** PDF file

## 🔄 Application Status Flow

```
APPLIED → REVIEWED → SHORTLISTED → HIRED
    ↓         ↓           ↓
  REJECTED  REJECTED   REJECTED
```

**Status Descriptions:**
- `APPLIED`: Initial application submitted
- `REVIEWED`: Application has been reviewed by HR
- `SHORTLISTED`: Candidate selected for interview
- `REJECTED`: Application rejected at any stage
- `HIRED`: Candidate successfully hired

## 🎯 User Roles & Permissions

### CANDIDATE
- View public job postings
- Submit applications
- View own applications
- Update own profile
- Change own password

### HR_ADMIN / RECRUITER / HIRING_MANAGER
- All CANDIDATE permissions
- Create, update, delete job postings
- View all applications
- Update application status
- Access candidate information

### SYSTEM_ADMIN
- All permissions
- User management
- System configuration
- Access to all data

## 🚨 Error Handling

### Common Error Responses

#### 400 Bad Request
```json
{
  "error": "Validation failed",
  "details": [
    "Email is required",
    "Password must be at least 6 characters"
  ]
}
```

#### 401 Unauthorized
```json
{
  "error": "Authentication required",
  "message": "Please provide a valid JWT token"
}
```

#### 403 Forbidden
```json
{
  "error": "Access denied",
  "message": "Insufficient permissions for this operation"
}
```

#### 404 Not Found
```json
{
  "error": "Resource not found",
  "message": "Job with ID 999 not found"
}
```

#### 409 Conflict
```json
{
  "error": "Resource conflict",
  "message": "Email already exists"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "message": "An unexpected error occurred"
}
```

## 📊 Rate Limiting

API endpoints are rate-limited to prevent abuse:

- **Authentication endpoints**: 5 requests per minute per IP
- **File upload endpoints**: 10 requests per minute per user
- **General API endpoints**: 100 requests per minute per user

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1640995200
```

## 🔍 Query Examples

### Filter Jobs by Multiple Criteria
```
GET /api/jobs/public?department=IT%20%26%20Engineering&location=Remote&minSalary=70000&maxSalary=100000
```

### Get Applications with Specific Status
```
GET /api/applications?status=SHORTLISTED&jobId=1
```

### Search Jobs by Title (if implemented)
```
GET /api/jobs/public?search=developer&department=IT
```

## 🧪 Testing with cURL

### Login Example
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### Get Jobs with Authentication
```bash
curl -X GET http://localhost:8080/api/jobs/public \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Upload Profile Picture
```bash
curl -X POST http://localhost:8080/api/users/1/profile-picture \
  -H "Authorization: Bearer <token>" \
  -F "profilePicture=@/path/to/image.jpg"
```

---

*This documentation covers all available endpoints in the TAS Backend API. For additional support or questions, please refer to the main README.md file.*