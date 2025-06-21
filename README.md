# DirectDrop - Real-Time File & Message Sharing

## Introduction

DirectDrop is an instant messaging service that enables real-time communication between users on the same network and across different networks. The backend provides a robust API for user management, message handling, file sharing, and real-time notifications through WebSocket connections.

This project serves as the server-side component of DirectDrop, handling message routing, file storage, and chat room management. It's designed for developers who want to build secure, scalable messaging applications with support for both local network and internet-based communication.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [API Overview](#api-overview)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **User Management**: Create, update, and manage user profiles with unique usernames
- **Real-time Messaging**: Send and receive messages instantly using WebSocket connections
- **Network-based Chat Rooms**: Automatically group users by IP address for local network communication
- **Cross-network Messaging**: Enable communication between users on different networks
- **File Sharing**: Upload and share files with support for multiple attachments
- **Message Search**: Search through message history with keyword-based filtering
- **Pagination**: Efficient message retrieval with configurable page sizes
- **Cloud Storage**: Secure file storage using Cloudinary integration
- **RESTful API**: Comprehensive REST endpoints for all messaging operations
- **Data Validation**: Input validation and error handling for robust operations

## Tech Stack

### Backend Framework

- **Spring Boot 3.4.3**: Java-based framework for building web applications
- **Spring Web**: RESTful web services and MVC framework
- **Spring Data MongoDB**: Data access layer for MongoDB
- **Spring WebSocket**: Real-time bidirectional communication

### Database

- **MongoDB**: NoSQL document database for flexible data storage

### Cloud Services

- **Cloudinary**: Cloud-based file storage and management service

### Development Tools

- **Maven**: Build automation and dependency management
- **Lombok**: Reduces boilerplate code with annotations
- **Spring Boot DevTools**: Development-time features and auto-restart

### Java Version

- **Java 17**: Modern Java features and performance improvements

## Project Structure

```
DirectDrop-backend/
├── mvnw                          # Maven wrapper script for Unix/Linux
├── mvnw.cmd                      # Maven wrapper script for Windows
├── pom.xml                       # Maven project configuration and dependencies
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/aktic/directdropbackend/
│   │   │       ├── config/                       # Application configuration classes
│   │   │       │   ├── CloudinaryConfig.java     # Cloudinary service configuration
│   │   │       │   ├── CorsConfig.java           # Cross-origin resource sharing setup
│   │   │       │   ├── DataInitializerConfig.java # Database initialization settings
│   │   │       │   ├── StorageInitializerConfig.java # File storage configuration
│   │   │       │   └── WebSocketConfig.java      # WebSocket connection settings
│   │   │       ├── constant/                     # Application constants and enums
│   │   │       ├── controller/                   # REST API endpoint handlers
│   │   │       │   ├── chatRoom/                 # Chat room management endpoints
│   │   │       │   ├── message/                  # Message handling endpoints
│   │   │       │   └── user/                     # User management endpoints
│   │   │       ├── DirectDropBackendApplication.java # Main application entry point
│   │   │       ├── exception/                    # Custom exception handling
│   │   │       ├── filter/                       # HTTP request/response filters
│   │   │       ├── model/                        # Data models and entities
│   │   │       │   ├── entity/                   # Database entity classes
│   │   │       │   │   ├── ChatRoom.java         # Chat room data structure
│   │   │       │   │   ├── Message.java          # Message data structure
│   │   │       │   │   └── User.java             # User data structure
│   │   │       │   ├── enums/                    # Enumeration types
│   │   │       │   ├── request/                  # API request data models
│   │   │       │   └── response/                 # API response data models
│   │   │       ├── repository/                   # Data access layer
│   │   │       │   ├── search/                   # Search functionality implementation
│   │   │       │   ├── ChatRoomRepository.java   # Chat room database operations
│   │   │       │   ├── MessageRepository.java    # Message database operations
│   │   │       │   └── UserRepository.java       # User database operations
│   │   │       ├── service/                      # Business logic layer
│   │   │       │   ├── chatRoom/                 # Chat room business logic
│   │   │       │   ├── fileStorage/              # File handling and storage logic
│   │   │       │   ├── ipService/                # IP address management
│   │   │       │   ├── message/                  # Message processing logic
│   │   │       │   ├── socket/                   # WebSocket communication logic
│   │   │       │   └── user/                     # User management logic
│   │   │       └── util/                         # Utility classes and helpers
│   │   └── resources/                            # Application configuration files
│   │       ├── application.yml                   # Main application configuration
│   │       ├── application-dev.yml               # Development environment settings
│   │       ├── application-prod.yml              # Production environment settings
│   │       ├── static/                           # Static web resources
│   │       └── templates/                        # Template files
│   └── test/                                     # Unit and integration tests
├── target/                                       # Compiled application files
└── uploads/                                      # Local file upload directory
```

### Key Components Explained

**Controllers**: Handle HTTP requests and responses, providing REST API endpoints for client applications to interact with the backend services.

**Services**: Contain the business logic for processing user requests, managing data, and coordinating between different components of the application.

**Repositories**: Provide data access methods for interacting with the MongoDB database, handling CRUD operations for users, messages, and chat rooms.

**Models**: Define the data structures used throughout the application, including database entities, request/response objects, and enumeration types.

**Configuration**: Contains setup classes for various services like Cloudinary, WebSocket, CORS, and database initialization.

## Installation

### Prerequisites

Before running DirectDrop Backend, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6** or higher
- **MongoDB 4.4** or higher
- **Cloudinary Account** (for file storage)

### Step-by-Step Setup

1. **Clone the Repository**

   ```bash
   git clone <repository-url>
   cd DirectDrop-backend
   ```

2. **Install MongoDB**

   - Download and install MongoDB from [mongodb.com](https://www.mongodb.com/try/download/community)
   - Start MongoDB service
   - Create a database named `DirectDrop`

3. **Configure Cloudinary**

   - Sign up for a free account at [cloudinary.com](https://cloudinary.com)
   - Get your Cloud Name, API Key, and API Secret
   - Add these credentials to your environment variables or configuration files

4. **Configure Environment Variables**
   Create a `.env` file in the project root or set environment variables:

   ```bash
   CLOUDINARY_CLOUD_NAME=your_cloud_name
   CLOUDINARY_API_KEY=your_api_key
   CLOUDINARY_API_SECRET=your_api_secret
   ```

5. **Build the Project**

   ```bash
   mvn clean install
   ```

6. **Run the Application**

   ```bash
   mvn spring-boot:run
   ```

   Alternatively, you can run the JAR file:

   ```bash
   java -jar target/DirectDrop-backend-0.0.1-SNAPSHOT.jar
   ```

### Development Setup

For development, the application will run on `http://localhost:8080` by default. The development profile includes:

- Hot reload enabled
- Detailed logging
- MongoDB connection to local instance
- File upload size limit of 10MB

## Usage

### Starting the Application

1. **Development Mode**

   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. **Production Mode**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

### API Endpoints

The application provides the following main API endpoints:

- **User Management**: `/api/user/*`
- **Message Handling**: `/api/message/*`
- **Chat Room Management**: `/api/chatroom/*`

### WebSocket Connection

For real-time messaging, connect to the WebSocket endpoint:

```
ws://localhost:8080/ws
```

### File Upload

The application supports file uploads up to 10MB per file. Files are stored in Cloudinary and accessible via secure URLs.

## API Overview

### User Management

- `PATCH /api/user/update/{userId}` - Update user information
- `GET /api/user/find-user/{userId}` - Search for users by username or IP address

### Messaging

- `POST /api/message/same-network/send-message` - Send message to users on the same network
- `POST /api/message/across-network/send-message` - Send message to users on different networks
- `GET /api/message/same-network/get-messages/{userId}` - Retrieve messages from same network
- `GET /api/message/across-network/get-messages/{userId}` - Retrieve messages from different networks

### Chat Rooms

- Automatic creation based on IP addresses
- Support for multiple users per chat room
- Real-time message broadcasting

## Architecture

### System Design

DirectDrop Backend follows a clean architecture pattern:

1. **Presentation Layer**: REST controllers handle HTTP requests and responses
2. **Business Logic Layer**: Services contain application logic and coordinate operations
3. **Data Access Layer**: Repositories manage database interactions
4. **Data Layer**: MongoDB stores application data

### Key Features

- **Microservices-ready**: Modular design allows for easy scaling and maintenance
- **Real-time Communication**: WebSocket support for instant messaging
- **File Management**: Cloudinary integration for secure file storage
- **Search Capability**: Full-text search across messages
- **Pagination**: Efficient data retrieval for large datasets

### Security Considerations

- Input validation on all endpoints
- File type and size restrictions
- CORS configuration for cross-origin requests
- Secure file storage with Cloudinary

## Contributing

We welcome contributions to DirectDrop Backend! Here's how you can help:

### Development Process

1. **Fork the Repository**

   - Create your own fork of the project
   - Clone it to your local machine

2. **Create a Feature Branch**

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Your Changes**

   - Follow the existing code style and conventions
   - Add appropriate tests for new functionality
   - Update documentation as needed

4. **Test Your Changes**

   ```bash
   mvn test
   mvn spring-boot:run
   ```

5. **Submit a Pull Request**
   - Provide a clear description of your changes
   - Include any relevant issue numbers
   - Ensure all tests pass

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Maintain consistent indentation
- Write unit tests for new features

### Reporting Issues

When reporting bugs or requesting features:

1. Check existing issues to avoid duplicates
2. Provide detailed steps to reproduce the problem
3. Include relevant error messages and logs
4. Specify your environment (OS, Java version, etc.)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

### Project Maintainer

- **Author**: Azizullah Khan
- **Email**: aziz.bin.aman16@gmail.com

### Support

For technical support or questions:

- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation for common solutions

### Acknowledgments

- Spring Boot team for the excellent framework
- MongoDB communities
- All contributors and testers

### Getting Help

- **Documentation**: This README provides comprehensive setup and usage instructions
- **API Reference**: Check the controller classes for detailed endpoint documentation
- **Community**: Join our community discussions for help and collaboration

---

**Note**: This is a backend service that requires a frontend application to provide a complete user experience. The API endpoints are designed to be consumed by web or mobile applications.
