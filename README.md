# Online-Shopping-System-EAD_Project-

# User and Payment Management System

A scalable and secure **User Management** and **Payment Management System** developed using **Spring Boot** in a microservices architecture. This project demonstrates efficient handling of authentication, user profile management, and payment processing functionalities.

---

## **Table of Contents**

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)

---

## **Features**

### **User Management**
- **JWT Authentication** for secure user login and authorization.
- **Role-Based Access Control (RBAC):**
  - Admins can manage users and roles.
  - Users can access or modify their own data only.
- **Email Verification** for user account activation.
- **Password Encryption** for secure storage of user credentials.
- **Profile Management** with support for image uploads.

### **Payment Management**
- **Process Payments** for orders.
- **Payment Confirmation** with order ID and amount verification.
- **Save Payment Methods** such as credit card details.
- Retrieve **order IDs by user ID** and filter **delivery orders by status**.
- **Error Handling** for invalid payment statuses or inputs.

---

## **Tech Stack**

- **Backend:** Spring Boot
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT)
- **Security:** Spring Security
- **Programming Language:** Java 17+
- **Build Tool:** Maven

---

## **Architecture**

This system follows a **microservices architecture**, with modular services for user management and payment processing. Each service interacts with its own database and communicates with other services using REST APIs.

---

## **Installation**

### **Prerequisites**
- Java 17 or higher
- Maven
- MySQL
- Postman (optional for testing APIs)

### **Setup**
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
