# Budget Management System 🚀

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-orange.svg)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-white.svg)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4-lightblue.svg)
![JWT](https://img.shields.io/badge/JWT-0.11.5-red.svg)
![MapStruct](https://img.shields.io/badge/MapStruct-1.6.3-purple.svg)
![Maven](https://img.shields.io/badge/Maven-3.8.1-blue.svg)


Full-stack budget management system with secure transaction handling and comprehensive financial reporting, built with Spring Boot and Thymeleaf.

## Features ✨

- **Advanced Security** 🔐
  - JWT authentication with HTTP-only cookies
  - CSRF protection
  - Secure password hashing
  - Session management

- **Bank Account Management** 🏦
  - Create/manage multiple bank accounts
  - Track account balances
  - Transaction history tracking

- **Transaction Management** 💳
  - Add/edit/delete transactions
  - Categorize transactions (income/expense)
  - Filter transactions by:
    - Date range (month/year)
    - Category
    - Amount range
    - Payment method

- **Financial Reporting** 📈
  - Monthly financial summaries
  - Category-based spending analysis
  - Income vs Expense comparisons

- **User Dashboard** 📊
  - Real-time financial overview
  - Account balance visualization
  - Budget progress tracking

## Technologies Used 🛠️

**Backend:**
- Spring Boot 3.4.2
- Spring Security with JWT
- Spring Data JPA

**Frontend:**
- Thymeleaf templates
- Chart.js for visualizations
- Bootstrap 5
- Vanilla JavaScript

**Database:**
- PostgreSQL 15
- Hibernate ORM

**Tools:**
- Maven
- MapStruct
- JWT (jjwt)

## Installation & Setup 🚀

1.  **Clone Repository**
   ```bash
   git clone https://github.com/Parias0/Budget-Management-Java-SpringBoot.git
   cd Budget-Management-Java-SpringBoot
```
2.  **Database setup**

```sql
  CREATE DATABASE budget_management;
```

3.  **Update application.properties**

```
#Database config
spring.datasource.url=jdbc:postgresql://localhost:5432/budgetmanagement
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=org.postgresql.Driver

#JPA/Hibernate config
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

#JWT config
jwt.secret=
jwt.expiration=3600000

#Admin account
app.init.create-admin=true
app.init.admin.username=
app.init.admin.password=
```

**API Endpoints Overview** 🌐

- Authentication	/api/auth/**	POST
- Accounts	/api/accounts/**	GET, POST, PUT, DELETE
- Transactions	/api/transactions/**	GET, POST, PUT, DELETE
- Categories	/api/categories/**	GET, POST, PUT, DELETE
- Summary	/api/summary/**	GET

![image](https://github.com/user-attachments/assets/9583d0f5-ff27-40dd-812a-1004c7cb4910)

![image](https://github.com/user-attachments/assets/7af0ed9a-d1b0-46f2-88d8-5965cd1629b1)

![image](https://github.com/user-attachments/assets/b9178a3e-845d-4fda-a16e-19e039358d5f)

![image](https://github.com/user-attachments/assets/9e3e297e-ae51-4661-9349-e5259e2659e5)

![image](https://github.com/user-attachments/assets/cc8f1189-4087-4b3f-b394-554a3720c00f)

![image](https://github.com/user-attachments/assets/4c6f70c7-3f81-40df-8dff-0798911e6c02)






