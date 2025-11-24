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
