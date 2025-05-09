# Budget API- API to manage Budgets

## Description
This API may help users to plan and follow their budgets with regart to their income, invesments, saving and spendings.

## Used Technologies
- Java 21
- Spring Boot
- Maven
- H2 / MySQL

## For Local Installation
1. Clone the project :
   git clone https://github.com/ton-utilisateur/budgetapi.git

2. Go to the repository :
   cd budgetapi

3. and run :
   ./mvnw spring-boot:run

## Request configuration
### 6. **Endpoints principaux**
Présente les chemins d’accès organisés par catégorie, par exemple :

### 🔐 Authentification
| Method | Endpoint             | Description             |
| ------ | -------------------- | ----------------------- |
| POST   | `/api/auth/register` | Register new user       |
| POST   | `/api/auth/login`    | Login and get JWT token |


### 👤 User
| Method | Endpoint          | Description             |
| ------ | ----------------- | ----------------------- |
| GET    | `/api/users/me`   | Get logged-in user info |
| DELETE | `/api/users/{id}` | Delete user account     |


### 💰 Budgets

| Method | Endpoint                      | Description                 |
| ------ | ----------------------------- | --------------------------- |
| POST   | `/api/budgets/{userId}`       | Create a new budget         |
| GET    | `/api/budgets/user/{userId}`  | Get all budgets for a user  |
| POST   | `/api/budgets/duplicate/{id}` | Duplicate a specific budget |
| PUT    | `/api/budgets/{budgetId}`     | Update a budget             |
| DELETE | `/api/budgets/{budgetId}`     | Delete a budget             |

### BugetEntry

| Method | Endpoint                      | Description                 |
| ------ | ----------------------------- | --------------------------- |
| POST   | `/api/budgets/entries/{budgetId}`       | Create a new budget         |
| GET    | `/api/budgets/entries/{budgetId}`  | Get all budgets for a user  |
| POST   | `/api/budgets/entries/{budgetId}` | Duplicate a specific budget |
| PUT    | `/api/budgets/entries/{budgetId}`     | Update a budget             |
| DELETE | `/api/budgets/entries/{budgetId}`     | Delete a budget             |

## 📥 Sample Request (JSON)
- to register a user
```Json
{
  "email": "user@email.com",
  "name": "username",
  "password": "user_password"
}
```
- user login
```Json
{
  "email": "user@email.com",
  "password": "user_password"
}
```
- create a budget: we choosed to firstly set a empty budget before creating his entries.
```Json
{
  "name": "Budget 1",
  "description": "my budget for May",
  "month": "may"
}
```

- create an budget entry (or subbuget) for as type "investment", "income" or "spending" also exist.
```Json
{
  "name": "Budget 1",
  "description": "my budget for May",
  "type": "saving", 
  "planedAmount": 12.0,
  "usedAmount": 4
}
```
## Security
This API use Spring Security with JWT. Every protected request must include a token :
Authorization: Bearer `<token>`. That `token` is returned after successful login. 


