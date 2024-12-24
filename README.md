# TRADER

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.5-brightgreen)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-lightgrey)
![Docker](https://img.shields.io/badge/Docker-Supported-blue)

## Introduction

**TRADER** is a backend application designed for automated cryptocurrency trading using Binance API.

---

## Environment Variable ( Locally )

You can edit application.properties or add your own keys to environment variables

```bash
export BINANCE_API_KEY="your_api_key"
export BINANCE_API_SECRET="your_api_secret"
export DB_URL="jdbc:postgresql://localhost:5432/tradingdb"
export DB_USERNAME="ADMIN"
export DB_PASSWORD="PASS"
```

---

# How to Run the Project

## Prerequisites

- **Docker**: Ensure Docker is installed on your system.

## Running the Application

1. **Navigate to the project root directory**:

   ```bash
   cd <project_root_directory>
   ```

2. **Build and run the application using Docker Compose**:

   ```bash
   docker-compose up --build
   ```

3. **Access the application**:
   - **Backend**: [http://localhost:8080](http://localhost:8080)

To stop the application and remove the containers, run:

```bash
docker-compose down
```

---

## Built By

**Melih Can Aydın**  
Connect with me on:

- [GitHub](https://github.com/melihcanaydin)
- [LinkedIn](https://www.linkedin.com/in/melihcanaydin/)
