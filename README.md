# Spring AI 2 Basics with EPAM DIAL

This project demonstrates the capabilities of the Spring AI framework, showcasing how to integrate and utilize EPAM
DIAL's models for various AI-powered tasks.

## Features

This project includes examples of the following Spring AI 2 features:

*   **Chat Completion:** Engage in conversational AI using EPAM DIAL's chat models. The application supports maintaining
    conversation history in a PostgreSQL database using `JdbcChatMemoryRepository`.
*   **Structured Output:** Convert natural language into structured data (POJOs), for example, to get a list of movie
    recommendations in a specific format.
*   **Image Generation:** Dynamically generate images from text prompts using the DALL-E model.
*   **Image Comprehension:** Analyze and describe images provided via URL, local path, or direct upload.

## Getting Started

### Prerequisites

*   Java 25
*   Maven
*   Docker and Docker Compose
*   An EPAM DIAL API key

### Configuration

1.  Set the `DIAL_API_KEY` environment variable with your EPAM DIAL API key. The application uses this environment
    variable to authenticate with the DIAL service.

2.  The `src/main/resources/application.properties` file is configured to use the EPAM DIAL endpoint and connect to a
    local PostgreSQL database:

    ```properties
    spring.ai.azure.openai.api-key=${DIAL_API_KEY}
    spring.ai.azure.openai.endpoint=https://ai-proxy.lab.epam.com
    
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
*   `POST /chat/movie`: Get a structured movie recommendation based on a text prompt.

### Image

*   `POST /image/generateImage`: Generate an image from a text prompt.
*   `POST /image/textWithImageUrl`: Analyze an image from a URL.
*   `POST /image/textWithImagePath`: Analyze an image from a local path.
*   `POST /image/textWithImage`: Analyze an uploaded image.

