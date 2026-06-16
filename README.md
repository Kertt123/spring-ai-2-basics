# Spring AI 2 Basics with Anthropic

This project demonstrates the capabilities of the Spring AI framework, showcasing how to integrate and use
Anthropic models for various AI-powered tasks.

## Features

This project includes examples of the following Spring AI 2 features:

*   **Chat Completion:** Engage in conversational AI using Anthropic chat models. The application supports maintaining
    conversation history in a PostgreSQL database using `JdbcChatMemoryRepository`.
*   **Structured Output:** Convert natural language into structured data (POJOs), for example, to get a list of movie
    recommendations in a specific format.
*   **Streaming Responses:** Stream model output using Server-Sent Events.
*   **Image Comprehension (Multimodal):** Analyze and describe images provided via URL, local path, or direct upload.

## Getting Started

### Prerequisites

*   Java 25
*   Maven
*   Docker and Docker Compose
*   An Anthropic API key

### Configuration

1.  Set the `ANTHROPIC_API_KEY` environment variable with your Anthropic API key.

2.  The `src/main/resources/application.properties` file is configured to use Anthropic and connect to a local
    PostgreSQL database:

    ```properties
    spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
    
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    spring.jpa.hibernate.ddl-auto=create
    ```

### Running the Application

1.  Start the PostgreSQL database using Docker Compose:
    ```bash
    docker-compose up -d
    ```

2.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
3.  Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```

The application will be available at `http://localhost:8080`.

## API Endpoints

### Chat

*   `POST /chat/text`: Get a text completion from the model.
*   `POST /chat/textWithMemory`: Get a text completion with persistent conversation memory.
*   `POST /chat/textStream`: Stream a text completion (SSE).
*   `POST /chat/movieRecommendation`: Get a structured movie recommendation based on a text prompt.

### Image

*   `POST /image/textWithImageUrl`: Analyze an image from a URL.
*   `POST /image/textWithImagePath`: Analyze an image from a local path.
*   `POST /image/textWithImage`: Analyze an uploaded image.

