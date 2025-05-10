# Budget API

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


### 👤 User
| Method | Endpoint          | Description             |
| ------ | ----------------- | ----------------------- |
| POST   | `/api/auth/register` | Register new user       |
| POST   | `/api/auth/login`    | Login and get JWT token |
| GET    | `/api/auth/users/me`   | Get logged-in user info |
| PUT    | `/api/auth/{userId}/changemypassword`   | Change logged-in user password |
| PUT    | `/api/auth/{userId}/changemyemail`   | Change logged-in user email |
| PUT    | `/api/auth/{userId}/changemyusername`   | Change logged-in user name |
| DELETE | `/api/auth/users/{userId}` | Delete user account     |


### 💰 Budgets

| Method | Endpoint                      | Description                 |
| ------ | ----------------------------- | --------------------------- |
| POST   | `/api/budgets/{userId}`       | Create a new budget         |
| GET    | `/api/budgets/user/{userId}`  | Get all budgets for a user  |
| POST   | `/api/budgets/duplicate/{budgetId}` | Duplicate a specific budget for the next month|
| PUT    | `/api/budgets/{userId}/{budgetId}`     | Update a budget             |
| DELETE | `/api/budgets/{userId}/{budgetId}`     | Delete a budget             |

### BugetEntry

| Method | Endpoint                      | Description                 |
| ------ | ----------------------------- | --------------------------- |
| POST   | `/api/budgets/entries/{budgetId}`       | Create a new budget entry for this budget        |
| GET    | `/api/budgets/entries/{budgetId}`  | Get all budget entries for the budget with that ID |
| PUT    | `/api/budgets/entries/changeDescription/{budgetId}/{budgetEntryId}`     | Change the entry's description           |
| PUT    | `/api/budgets/entries/changeTyp/{budgetId}/{budgetEntryId}`     | Change the entry's typ           |
| PUT    | `/api/budgets/entries/changeName/{budgetId}/{budgetEntryId}`     | Change the entry's name           |
| PUT    | `/api/budgets/entries/changeusedamount/{budgetId}/{budgetEntryId}`     | Change completly the entry's usedAmount           |
| PUT    | `/api/budgets/entries/updateusedamount/{budgetId}/{budgetEntryId}`     | add this a usedamount to this entry's usedAmount           |
| PUT    | `/api/budgets/entries/changeplanedamount/{budgetId}/{budgetEntryId}`     | Change the entry's planedAmount           |
| PUT    | `/api/budgets/entries/changebudgetentry/{budgetId}/{budgetEntryId}`     | Modify all parameter of the entry           |
| DELETE | `/api/budgets/entries/{budgetId}/{budgetEntryId}`     | Delete a budgetEntry             |

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
- change username
```Json
{
  "password": "user_password",
  "newName": "new username"
}
```
- change user email
```Json
{
  "password": "user_password",
  "newEmail": "newemail@mail.com"
}
```
- change user password
```Json
{
  "altPassword": "user_password",
  "newPassword": "newUserPassword"
}
```
- create or update a budget: we choosed to firstly set a empty budget before creating his entries.
```Json
{
  "name": "Budget 1",
  "description": "my budget for May",
  "month": "may"
}
```

- create or update a budget entry (or subbuget) for as type "investment", "income" or "saving" also exist.
```Json
{
  "name": "Food",
  "description": "For My",
  "type": "Spending", 
  "planedAmount": 100.0,
  "usedAmount": 20
}
```
- 📝 Note that for updating a specific budget entry parameter like type, name, etc. the request body will directly have the to change `value` without braces `{}`
  
## Security
This API use Spring Security with JWT. Every protected request must include a token :
Authorization: Bearer `<token>`. That `token` is returned after successful login. 

## 🧑‍💻Author
Made by [cyvaldez](https://github.com/cyvaldez)


