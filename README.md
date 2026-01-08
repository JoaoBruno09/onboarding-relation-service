## 🧩 Relation Service

The Relation Service is a specialized microservice responsible for managing relationships between customers within the banking account onboarding system. These relationships define how customers are connected to one another in the context of an account (e.g., legal representative, spouse, guardian, or other business-defined relations).

Built following Domain-Driven Design (DDD) and microservices architecture (MSA) principles, this service owns the relationship domain exclusively and communicates with other services through asynchronous events to ensure consistency without tight coupling.

## 🔍 Key Features

- Deletion of relationships between customers
- Validation of relationship constraints within an account context
- Event-driven synchronization with Customer services
- Independent persistence using the Database per Service pattern

## 🔗 API Endpoints
- DELETE /relations/{relationId} - Delete customers relationships in the account.
  
## 👨‍💻 Technologies

<div style="display: inline_block"><br>
<img align="center" alt="Java" height="40" width="40" src="https://github.com/devicons/devicon/blob/master/icons/java/java-original.svg">
<img align="center" alt="Spring" height="40" width="40" src="https://github.com/devicons/devicon/blob/master/icons/spring/spring-original.svg">
<img align="center" alt="Docker" height="40" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg" />
<img align="center" alt="PostgreSQL" height="40" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postgresql/postgresql-original.svg" />
</div>

## 📂 Repository Structure

The repository is organized as follows:

- `boot`: Module that includes the application startup.
- `services/src/main/java/com/bank/onboarding/accountservice/services`: Contains services and their implementation.
- `web/src/main/java/com/bank/onboarding/accountservice/controllers`: Contains all the controllers of the application.

## 📋 Prerequisites

- Java 17+
- Maven
- Docker
- PostgreSQL database instance (local or containerized)

## 🌟 Additional Resources

- [Master's dissertation](http://hdl.handle.net/10400.22/26586)
