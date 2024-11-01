# Bird Sightings REST API Service

This project is a REST API service for managing bird data and bird sightings. It is built with Java 11, Spring WebFlux, and PostgreSQL for persistence, and it uses Docker and Docker Compose for containerized deployment.

## Project Structure and Requirements

1. **Birds and Sightings Management**: The service manages birds and their sightings, supporting CRUD operations and complex queries.
2. **Persistence**: Uses PostgreSQL to store data persistently.
3. **Dockerized Setup**: Easily deployable using Docker and Docker Compose.
4. **API Documentation**: Automatically generated using Swagger.

### Key Features

- CRUD operations for birds and sightings.
- Query capabilities for birds by name and color, and sightings by bird, location, and time interval.
- Documentation via Swagger.
- Thread-safe and optimized for performance with resource limits on containers.

---

## Prerequisites

- **Docker**: Ensure Docker and Docker Compose are installed on your machine.
- **Java 11**: This project is built with Java 11.
- **Maven**: Required to build the project.

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository_url>
cd <repository_directory>
