# Ledgers - A Ledger System Visualization and Simulation Tool

## Overview

Ledgers is a Kotlin multiplatform application designed to model, visualize, and simulate ledger-based systems. The application allows users to create "stories" that represent scenarios in distributed ledger environments, with organizations, ledgers, assets, and the relationships between them. Stories unfold through chapters that demonstrate how ledger states change over time.

## Project Structure

The project is organized into several modules:

- **core**: Contains the domain model and business logic
  - Implements the core domain entities (Story, Ledger, Organization, Asset)
  - Defines use cases and repositories
  - Configured as a Kotlin multiplatform module (JVM + JS)

- **components**: Contains the web frontend
  - Built with Angular
  - Consumes the JS artifacts from the core module
  - Provides the user interface for creating and visualizing stories

- **application**: Spring Boot application that serves the web frontend
  - Implements REST controllers
  - Provides persistence through the repository implementations
  - Integrates the core domain with infrastructure concerns

## Key Domain Concepts

The key domain objects are immutable. Methods modifying the story always return a copy of the original story with the changes applied, while the original story remains untouched.

### Story

The central entity in the system. A Story represents a scenario with a specific ledger architecture and storyline.

### Architecture

Defines the structure of the ledger system, including:
- Organizations: Entities that own ledgers
- Ledgers: The actual ledger systems that maintain records
- Assets: Items that can be recorded in ledgers
- Links: Connections between ledgers

### Storyline

A sequence of chapters that illustrate how the ledger system evolves over time. The storyline is assumed to have infinite chapters with the boundary automatically extended as changes are applied.

### Chapter

A specific point in the storyline with a defined stage (state of the ledger system).

### Stage

The state of the ledger system at a particular chapter, including which components are visible and their properties.

## Getting Started

### Prerequisites

- JDK 21
- Node.js 20.17.0 or later
- npm 10.8.2 or later

### Building the Project

```bash
./gradlew build
```

### Running the Application

```bash
./gradlew :application:bootRun
```

The application will be available at http://localhost:8080

## Storage

By default, stories are stored as JSON files in a local directory. The storage path can be configured using the property:

```
org.ledger.storage.path
```

## Development

### Adding a New Domain Feature

1. Define the domain entity in the `core` module
2. Implement any required repositories or use cases
3. Create DTOs for serialization if needed
4. Implement the repository adapter in the `application` module
5. Add controller endpoints for the new feature
6. Update the frontend components to support the feature

### Testing

The project uses JUnit 5 and AssertJ for testing. Each module contains its own tests.

```bash
./gradlew test
```

## Technology Stack

- **Backend**
  - Kotlin
  - Spring Boot
  - Spring WebFlux
  - kotlinx-serialization

- **Frontend**
  - Angular
  - TypeScript
  - Angular Material (likely)

- **Build Tools**
  - Gradle (Kotlin DSL)
  - Node Gradle plugin

## Project Conventions

- Domain-driven design principles are used throughout the codebase
- The core domain is platform-agnostic (using Kotlin Multiplatform)
- Repository interfaces are defined in the domain, with implementations in the infrastructure
- DTOs are used for serialization and API communication
