# Blog API

This is a simple Blog API built with Spring Boot. It allows users to create, read, update, and delete blog posts.

## Technologies Used

*   Java 24
*   Spring Boot 3.5.3
*   Spring Data JPA
*   Spring Web
*   MySQL Connector
*   Lombok
*   ModelMapper
*   Maven

## API Endpoints

The base URL for the API is `/api/v1/posts`.

| Method | Endpoint          | Description                                |
| ------ | ----------------- | ------------------------------------------ |
| POST   | /                 | Creates a new blog post.                   |
| PUT    | /{id}             | Updates an existing blog post.             |
| DELETE | /{id}             | Deletes a blog post.                       |
| GET    | /{blogId}         | Retrieves a single blog post by its ID.    |
| GET    | /                 | Retrieves all blog posts.                  |
| GET    | /search?term={term} | Searches for blog posts by a search term. |

## How to Run

1.  Clone the repository.
2.  Make sure you have Java 24 and Maven installed.
3.  Configure the database connection in `src/main/resources/application.properties`.
4.  Run the application using the following command:

    ```bash
    ./mvnw spring-boot:run
    ```

## How to Test

To run the tests, use the following command:

```bash
./mvnw test
```
