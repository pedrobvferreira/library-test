# Library BackEnd - Library API

Library Service is a Spring Boot application that provides a RESTful API for managing users, books, and rentals in a library. The application uses H2 as an in-memory database, Liquibase for database version control, and Springdoc OpenAPI for API documentation.

## Prerequisites
- Java(21)
- Springframework
- Maven
- Angular
- SQL(In-memory: H2 or other)**

## How to Run
```bash
cd library-test/libraryservice
mvn clean install
mvn spring-boot:run
```
## Access the application:
H2 Console: http://localhost:9091/h2-console

API Documentation: http://localhost:9091/swagger-ui.html

## Access the h2 database:
    username=sa
    password=password

## API Endpoints
User

    GET /api/users - Returns all users
    GET /api/users/{id} - Returns a user by ID
    POST /api/users - Creates a new user
    PUT /api/users/{id} - Updates an existing user
    PUT /api/users/deactivate/{id} - Deactivate a user

Book

    GET /api/books - Returns all books
    GET /api/books/{id} - Returns a book by ID
    POST /api/books - Creates a new book
    PUT /api/books/{id} - Updates an existing book
    DELETE /api/books/{id} - Deletes a book

Rental

    POST /api/rentals/rent - Rent a book
    POST /api/rentals/return/{id} - Return a rental

------------
------------
# Library Frontend

This is the frontend of a library management application, built using Angular. This application allows you to manage users, books and book rentals.

## Prerequisites
- [Angular CLI](https://github.com/angular/angular-cli) version 18.0.4

## Build

Run `ng build` to build the project
```bash
cd library-test/library-frontend
ng build
```
The build artifacts will be stored in the `dist/` directory.

## Development server

Run `ng serve` for a dev server
```bash
ng serve
```
Navigate to http://localhost:4200/.
The application will automatically reload if you change any of the source files.

## Running unit tests

Run `ng test` to execute the unit tests
```bash
ng test
```
Unit tests via [Karma](https://karma-runner.github.io).


