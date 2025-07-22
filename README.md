# Spring Boot API Docker Setup

## Prerequisites
- Docker installed
- Docker Compose installed
- MySQL server (either locally or via Docker)


## Project Structure

### Dockerfile
The ```Dockerfile``` uses Eclipse Temurin JDK 21 and packages your Spring Boot application:

```dockerfile
FROM eclipse-temurin:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Key points:

- Uses Java 21 (Eclipse Temurin distribution)
- Expects built JAR file in target/ directory
- Runs the application with default Java options


## Quick Start

1. Build the Docker image:

```text 
docker build -t <your-dockerhub-username>/golfnode_api:latest .
```

2. Run with Docker Compose:

```text
docker compose up
```

## Configuration

### Docker Image
Edit the ```Dockerfile``` if you need to customize the Java version or build process.

### Database Configuration
Edit the ```docker-compose.yml``` file with your specific values:
```yaml
version: '3'
services:
  golfnode-api:
    image: <your-dockerhub-username>/golfnode_api:latest
    ports:
      - 8080:8080
    environment:
      - spring.datasource.url=jdbc:mysql://host.docker.internal:3306/golfnode
      - spring.datasource.username=<your_db_username>
      - spring.datasource.password=<your_db_password>
```

### Replace these values:

```<your-dockerhub-username>```: Your Docker Hub username

```<your_db_username>```: MySQL username

```<your_db_password>```: MySQL password

```golfnode```: Your MySQL database schema name

## API Access

Once running, access the API at:

```text
http://localhost:8080
```

## API Documentation

Check ```Golfnode API Doc.pdf``` file
