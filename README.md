# Personal Finance Control Center

## 1. Project Description
Personal finance backend for managing users, accounts, categories, transactions, and financial summary reports. The project includes authentication and authorization with JWT.

## 2. Technologies Used
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Lombok
- springdoc OpenAPI

## 3. How to Run Backend
1. Create a `.env` file in the project root.
2. Add the required environment variables.
3. Run:

```bash
mvn spring-boot:run
```

## 4. How to Run Frontend
Frontend is not implemented yet.

## 5. How to Run Tests
```bash
mvn test
```

## 6. Environment Variables Example
```env
DB_URL=jdbc:h2:file:./data/finance-core-db
DB_USERNAME=sa
DB_PASSWORD=
JWT_SECRET=super-secret-jwt-security-super-code
```

## 7. Main API Endpoints
### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/logout`

### Users
- `GET /api/users/me`
- `PATCH /api/users/me/profile`
- `GET /api/users`

### Accounts
- `POST /api/accounts`
- `GET /api/accounts`
- `GET /api/accounts/{id}`
- `PUT /api/accounts/{id}`
- `DELETE /api/accounts/{id}`

### Categories
- `GET /api/categories`
- `GET /api/categories/{id}`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Transactions
- `POST /api/transactions`
- `GET /api/transactions/{id}`
- `PUT /api/transactions/{id}`
- `DELETE /api/transactions/{id}`
- `GET /api/transactions/search`

### Reports
- `GET /api/reports/summary`

## 8. Database Tables and Relationships
### Tables
- `app_users`
- `app_user_roles`
- `accounts`
- `categories`
- `transactions`

### Relationships
- One user has many accounts.
- One account belongs to one user.
- One transaction belongs to one account.
- One transaction belongs to one category.
- User roles are stored in `app_user_roles`.

## 9. Demo Login Credentials
### Admin
- Email: `admin@admin.com`
- Password: `admin123`

### User
- Email: `user@user.com`
- Password: `password`
