## Public Library Management System

### Introduction
This project is a Public Library Management System implemented using Spring Boot, Hibernate, MapStruct, H2 Database, and Swagger. It provides a RESTful API for managing books and libraries in a public library system.

### Features
- **Book Management**: CRUD operations for managing books including creation, retrieval, updating, and deletion.
- **Library Management**: CRUD operations for managing libraries including creation, retrieval, updating, and deletion.
- **Assigning and Removing Books**: Assign books to libraries and remove books from libraries.
- **Caching**: Caching of book data for improved performance.
- **Swagger Documentation**: API documentation using Swagger UI.

### Technologies Used
- **Spring Boot**: For building RESTful web services.
- **Hibernate**: Object-relational mapping library for data persistence.
- **MapStruct**: For object mapping between DTOs and entities.
- **H2 Database**: In-memory database for data storage during development and testing.
- **Swagger**: For API documentation and testing.

### How to Run
1. Clone the repository: `git clone https://github.com/sonwabo/public-library.git
2. Navigate to the project directory: `cd public-library`
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

### API Endpoints
- **Books**:
    - `GET /api/books/{id}`: Retrieve book by ID.
    - `POST /api/books`: Create a new book.
    - `PUT /api/books/{id}`: Update an existing book.
    - `POST /api/books/assign/{libraryId}/{bookId}`: Assign a book to a library.
    - `POST /api/books/remove/{libraryId}/{bookId}`: Remove a book from a library.
    - `DELETE /api/books/{id}`: Delete a book by ID.

- **Libraries**:
    - `GET /api/libraries`: Retrieve all libraries.
    - `GET /api/libraries/{id}`: Retrieve library by ID.
    - `POST /api/libraries`: Create a new library.
    - `PUT /api/libraries`: Update an existing library.
    - `DELETE /api/libraries/{id}`: Delete a library by ID.

### Swagger UI
Access the Swagger UI to view and interact with the API documentation:
- Swagger UI URL: `http://localhost:8080/swagger-ui/index.html`

### Testing
- Unit tests are provided for service classes.
- Integration tests are provided for REST controllers.

### Caching
- Caching is implemented for library and book data to improve performance.

### Dependencies
- Java 17
- Maven
- Spring Boot
- Hibernate
- MapStruct
- H2 Database
- Swagger

### Contributors
- [Sonwabo Singatha]

### License
This project is licensed under the [MIT License](LICENSE).
