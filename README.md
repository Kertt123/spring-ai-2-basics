# Spring AI 2 Basics with EPAM DIAL

This project demonstrates the capabilities of the Spring AI framework, showcasing how to integrate and utilize EPAM
DIAL's models for various AI-powered tasks.

## Features

This project includes examples of the following Spring AI features:

* **Chat Completion:** Engage in conversational AI using EPAM DIAL's chat models. The application supports maintaining
  conversation history through the use of `ChatMemory` advisors.
* **Image Generation:** Dynamically generate images from text prompts using the DALL-E model.
* **Image Comprehension:** Analyze and describe images provided via URL, local path, or direct upload.

## Getting Started

### Prerequisites

* Java 25
* Maven
* An EPAM DIAL API key

### Configuration

1. Set the `DIAL_API_KEY` environment variable with your EPAM DIAL API key. The application uses this environment
   variable to authenticate with the DIAL service.

2. The `src/main/resources/application.properties` file is configured to use the EPAM DIAL endpoint:

   ```properties
   spring.ai.azure.openai.api-key=${DIAL_API_KEY}
   spring.ai.azure.openai.endpoint=https://ai-proxy.lab.epam.com
   ```

### Running the Application

1. Build the project using Maven:
   ```bash
   mvn clean install
   ```
2. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`.

## API Endpoints

### Chat

* `POST /chat/text`: Get a text completion from the model.

### Image

* `POST /image/generateImage`: Generate an image from a text prompt.
* `POST /image/textWithImageUrl`: Analyze an image from a URL.
* `POST /image/textWithImagePath`: Analyze an image from a local path.
* `POST /image/textWithImage`: Analyze an uploaded image.

