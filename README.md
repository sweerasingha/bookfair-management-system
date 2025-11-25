# Book Fair Management System – Backend

The Book Fair Management System is designed to streamline and automate processes involved in organizing, managing, and operating book fairs. This backend application provides a robust API layer and integrations to enable efficient management of books, events, participants, vendors, ticketing, reporting, and more.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Docker Support](#docker-support)
- [Contributing](#contributing)
- [License](#license)

## Features

- Book, author, and inventory management
- Exhibitor and vendor coordination
- Event and schedule planning
- Attendee registration and ticketing
- Reporting and analytics
- Role-based access controls for administrators, sellers, and attendees
- RESTful API endpoints
- Dockerized for easy deployment

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot (assumed for typical Maven-based backend)
- **Build Tool:** Maven
- **Database:** (Configure in `.env` and `docker-compose.yml`, typically MySQL or PostgreSQL)
- **Containerization:** Docker, Docker Compose

## Getting Started

### Prerequisites

- Java 11 or later
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized setup)
- Git

### Clone the repository

```sh
git clone https://github.com/sweerasingha/bookfair-management-system.git
cd bookfair-management-system
```

### Configure Environment Variables

Copy the example environment file and edit as needed:
```sh
cp .env.example .env
```
Edit `.env` to specify your database, server, and other secrets.

### Build and Run Locally

```sh
# Build the application
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080` by default.

## Environment Variables

The backend uses an environment file to manage sensitive configuration. Refer to `.env.example` for available variables, such as:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `SERVER_PORT`

## Docker Support

You can run the backend and any dependencies easily using Docker Compose:

```sh
docker-compose up --build
```

This uses the provided `Dockerfile` for the service, and spins up the full environment as detailed in `docker-compose.yml`.

## Project Structure

```
.
├── .env.example          # Sample environment variables
├── Dockerfile            # Application Docker configuration
├── docker-compose.yml    # Multi-container orchestration
├── pom.xml               # Maven dependencies and configuration
├── src/
│   ├── main/
│   │   └── java/...      # Java source code
│   └── test/...          # Unit/integration tests
├── logs/                 # Application logs
└── ...
```

## Usage

API documentation is usually available via [Swagger UI](http://localhost:8080/swagger-ui/index.html) (if enabled in your Spring configuration). Refer to the `/src/main/java/.../controllers` directory for available endpoints.

## Contributing

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a pull request

*For questions and support, please file an issue in the repo.*