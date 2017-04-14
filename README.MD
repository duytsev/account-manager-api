# Account Manager API


Run app on port 8080

As packaged application:
```
java -jar target/account-manager-api-0.0.1-SNAPSHOT.jar
```

Using the Maven plugin:
```
mvn spring-boot:run
```

### Endpoints

| Method | Url | Decription |
| ------ | --- | ---------- |
| GET    |/api/accounts  | list all accounts |
| GET    |/api/accounts/{id}  | get account by id |
| POST    |/api/accounts  | create account |
| GET    |/api/transfers  | list all transfers(deposits and withdrawals are part of transfer) |
| POST    |/api/transfers  | create transfer |
| POST    |/api/deposits  | create deposit |
| POST    |/api/withdrawals  | create withdrawal |


### Examples

```javascript
POST api/accounts
{  
  "balance": 500
}

POST api/transfers
{    
  "fromAccountId": 1,  
  "toAccountId": 2,
  "amount": 100.1
}

POST api/deposits
POST api/withdrawals
{      
  "accountId": 3,    
  "amount": 200.12
}
```
