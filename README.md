# Monitoring Dashboard for Docker Containers

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.2.0-green)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-enabled-blue)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](LICENSE)

A **Spring Boot application** to monitor Docker containers running on your system. It collects container resource metrics (CPU, memory, network) and exposes them to **Prometheus** for visualization in **Grafana**.

---
## Project Structure
                +------------------+
                |   Web Dashboard  |
                |  (React / Vue)   |
                +--------+---------+
                         |
                         | REST API
                         |
               +---------v---------+
               |   Spring Boot API |
               | Monitoring Service|
               +---------+---------+
                         |
         +---------------+----------------+
         |                                |
+--------v--------+              +--------v--------+
|   Prometheus    |              |     Docker      |
| Metrics Storage |              | Containers Info |
+--------+--------+              +--------+--------+
         |                                |
         +--------------+-----------------+
                        |
                +-------v-------+
                |    Grafana    |
                |  Visualization|
                +---------------+
---
## Features

- ✅ List all Docker containers with basic info: ID, Name, Image, Status
- ✅ Collect real-time container resource metrics:
  - CPU usage (%)
  - Memory usage & memory limit
  - Network I/O (Rx & Tx)
- ✅ Expose metrics to Prometheus via **Spring Boot Actuator**
- ✅ Visualize metrics with Grafana dashboards
- ✅ Easy setup using Docker, Prometheus, and Grafana

---

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Docker Java API** (`docker-java`)
- **Micrometer** for metrics
- **Prometheus** for monitoring
- **Grafana** for dashboard visualization
- **Docker** (for Prometheus & Grafana)

---
## Structure
monitoring-dashboard/
│
├─ src/main/java/com/example/devops/monitoring_dashboard/
│ ├─ controller/ # REST endpoints for containers & metrics
│ ├─ model/ # ContainerInfo & ContainerMetrics classes
│ ├─ service/ # DockerService, ContainerMetricsPublisher
│ └─ MonitoringDashboardApplication.java
│
├─ src/main/resources/
│ └─ application.yml # Spring Boot configuration
│
└─ pom.xml # Maven dependencies

---

## REST Endpoints

| Endpoint                           | Description                                      |
|-----------------------------------|--------------------------------------------------|
| `GET /api/containers/info`         | List all containers (ID, Name, Image, Status)   |
| `GET /api/containers/metrics`     | Container resource metrics (CPU, Memory, Network) |
| `GET /actuator/prometheus`         | Prometheus metrics endpoint                      |
| `GET /api/metrics/system`          | System metrics (CPU, Memory)                     |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker (with TCP API enabled at `tcp://localhost:2375`)
- Prometheus
- Grafana

---

### Run Spring Boot Application

```bash
git clone https://github.com/OmariMohammedmounir/DevOps-Monitoring-Dashboard.git
cd monitoring-dashboard
mvn clean install
mvn spring-boot:run
docker run -p 9090:9090 \
  -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml \prom/prometheus
docker run -d -p 3000:3000 grafana/grafana



