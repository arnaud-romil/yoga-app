# Yoga Studio App

## Description

This full-stack application allows the management of a yoga studio with the following features:

* An administrator can create and modify yoga sessions.
* Clients can register on the application.
* Clients can log in and book their participation in yoga sessions.
* Clients can cancel their participation in a session.
* Clients can delete their accounts.

The application consists of:

* **Backend**: REST API developed with Spring Boot 2.6.1 (Java 8), secured with JWT, and connected to a MySQL database.
* **Frontend**: Angular application developed with Angular 14.

This project was developed by **NumDev**, an agency specializing in enterprise management solutions.

## Installation and Configuration

### Database Installation

1. Ensure MySQL is installed on your machine.

2. Create a database :

```
CREATE DATABASE yoga_db;
```

3. Create a user with the necessary privileges:

```
CREATE USER 'yoga_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON yoga_db.* TO 'yoga_user'@'localhost';
FLUSH PRIVILEGES;
```

4. Run the ```script.sql``` script located at ```yoga-app/ressources/sql/```.

5. Verify that the connection details in application.properties of the backend are correct, for example:

```
spring.datasource.url=jdbc:mysql://localhost:3306/yoga_db
spring.datasource.username=yoga_user
spring.datasource.password=password
``` 

### Application Installation

**Backend (Spring Boot)**

1. Clone the project:

```
git clone https://github.com/arnaud-romil/yoga-app.git
cd yoga-app/back
```

2. Install Maven dependencies:

```
mvn clean install
```

**Frontend (Angular)**

1. Navigate to the frontend folder:

```
cd ../front
```

2. Install npm dependencies:

```
npm install
```

### Running the Application

1. Start the Backend: Ensure MySQL is running and start the Spring Boot Server:

```
mvn spring-boot:run
```

The API will be available at ```http://localhost:8080```

2. Start the Frontend

In the ```front``` folder, run:

```
ng serve
```

The application will be accessible at: ```http://localhost:4200```

### Running Tests

**Backend Tests (Spring Boot)**

- Run unit and integration tests with Maven:

```
mvn clean test
```

The coverage report will be available at: ```yoga-app/back/target/site/jacoco/index.html```.

**Frontend Tests (Angular)** 

- Run unit and integration tests with Jest:

```
npm run test
```

The coverage report will be available at: ```yoga-app/front/coverage/jest/lcov-report/index.html```

- Run e2e tests with Cypress:

1. ``` npm run e2e ```

2. ``` npm run cypress:open ``` (in another terminal)

3. Select "E2E Testing" and run "all.cy.ts"

The coverage report will be available at: ```yoga-app/front/coverage/lcov-report/index.html```

## Licence

This project is the property of **NumDev**.







