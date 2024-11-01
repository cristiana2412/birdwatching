# Bird Sightings REST API Service

This project is a REST API service for managing bird data and bird sightings and it is built using:
    - **Java 11**; 
    - **Spring WebFlux**; 
    - **PostgreSQL**; and
    - **Docker** along with **Docker Compose**.

---

### Key Features

- CRUD operations for birds and sightings.
- Persistence of data using the features of the selected database.
- Query capabilities for birds by name and color, and sightings by bird, location, and time interval.
- Documentation via Swagger.
- Easy deployable setup via containerization.
- Thread-safe and optimized for performance with resource limits on containers.

---

## Prerequisites

- **Docker**: Ensure Docker and Docker Compose are installed on your machine.
- **Java 11**: This project is built with Java 11.
- **Maven**: Required to build the project.

---

## Getting Started

### 1. Clone the Repository

Run the following commands in your terminal:
    
    git clone https://github.com/cristiana2412/birdwatching.git
    cd /path/to/repo/birdwatching

### 2. Build the Project

Build the birdwatching application using Maven either manually*, via the 'mvn clean package -DskipTests' command, or as it is currently intended, via Docker-Compose commands.

*This step is helpful for local development and testing.

### 3. Start the Application with Docker Compose

To start both the application and PostgreSQL containers, simply run:
    
    docker-compose up

This command will:
- Build and start the Java+Spring WebFlux application (exposed on port 8080).
- Start a PostgreSQL instance with the necessary database schema and initial data loaded via init.sql.
- Start an Adminer instance (accessible at http://localhost:8081) for managing the PostgreSQL database.
   
To stop the containers:

    docker-compose down

---

### API Documentation

The API documentation is generated automatically using Swagger. After starting the application, access it at:
- Swagger UI: http://localhost:8080/swagger-ui.html

---

### Database Management with Adminer
Adminer is included in this setup for managing and inspecting the PostgreSQL database. 

After starting the Docker Compose setup, you can access Adminer at: http://localhost:8081

To log in to Adminer, use the following credentials:
- System: PostgreSQL
- Server: db
- Username: bird_user
- Password: bird_password
- Database: birdsdb

- Adminer provides a graphical interface for viewing, querying, and managing the database.

---

### Environment Variables

Environment variables used in docker-compose.yml:
- DB_HOST: Hostname for PostgreSQL (set to db in Docker Compose).
- DB_PORT: Port for PostgreSQL (5432 by default).
- DB_NAME: Database name (birdsdb).
- DB_USER: Database user (bird_user).
- DB_PASSWORD: Database password (bird_password).

These can be customized in docker-compose.yml if needed.

---

### Database Access

PostgreSQL is exposed on localhost:5432 and can be accessed with:
- Username: bird_user
- Password: bird_password
- Database: birdsdb

In addition to Adminer, you can connect using a tool like pgAdmin or DBeaver for direct access if desired.

---

### File Structure

- Dockerfile: Configuration for building the Java application container.
- docker-compose.yml: Manages the application and PostgreSQL containers.
- db_init/init.sql: SQL script to initialize the database schema and load initial data.
- src/: Java source code for the Spring WebFlux application.

---

### Data Schema and Initialization
The database is initialized with a schema for birds and sightings, created through the init.sql script. 

This script also creates indexes for optimized querying and inserts sample data for testing.

init.sql Highlights:
- Tables: birds and sightings with necessary fields and foreign key constraints.
- Indexes: Created for optimal querying of bird name, color, sighting location, and date.
- Sample Data: Includes sample birds and sightings to validate the application’s functionality immediately after deployment.

---

### Exception Handling and Validation
The project includes a GlobalExceptionHandler for centralized exception management, including:
- ResourceNotFoundException: Thrown when a requested resource is not found.
- ConflictException: Handles optimistic locking conflicts.
- Input Validation: Validates incoming requests to ensure proper data integrity.

These components ensure robust error handling and make the service more resilient to common input and concurrency issues.

---

### Testing
The project includes configurations for unit and integration testing:
- Reactive Components: Uses ReactorTest for testing asynchronous components.
- Mock and Data Testing: Uses Spring Boot’s testing tools to validate services, repositories, and controllers.
- Error Scenarios: Includes tests for validation errors and data conflicts.

To run tests, use the following command in the terminal:

    mvn test

---

### Troubleshooting

Reinitialize Database: 
If you need to reset the database, stop the containers and remove the volume by running:
    
    docker-compose down -v

followed by:

    docker-compose up 

If you need to view logs for troubleshooting, run the following command based on your needs:

1. For logs from docker-compose:

       docker-compose logs -f

2. For logs from the app:

       docker logs app 

3. For logs from the database:

       docker logs db 

---

### Additional Considerations
- Thread Safety: The application leverages Spring WebFlux's non-blocking, reactive design for thread-safe operations.
- Error Handling: Includes validation for request parameters and meaningful HTTP status codes for error handling.
- Performance: Configured with sensible memory and CPU limits for efficient local use.
- Swagger Integration: Automatically documents all endpoints and models, providing developers with a clear view of API capabilities.