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
| GET    |/accounts  | list all accounts |
| GET    |/account/{id}  | get account by id |
| POST    |/accounts  | create account |
| GET    |/transfers  | list all transfers(deposits and withdrawals are part of transfer) |
| POST    |/transfers  | create transfer |
| POST    |/deposits  | create deposit |
| POST    |/withdrawals  | create withdrawal |
