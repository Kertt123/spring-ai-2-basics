# Spring AI 2 Basics with OpenAI

This project demonstrates the capabilities of the Spring AI framework, showcasing how to integrate and utilize OpenAI's models for various AI-powered tasks.

## Features

This project includes examples of the following Spring AI features:

*   **Chat Completion:** Engage in conversational AI using OpenAI's chat models. The application supports maintaining conversation history through the use of `ChatMemory` advisors.
*   **Image Generation:** Dynamically generate images from text prompts using the DALL-E model.
*   **Image Comprehension:** Analyze and describe images provided via URL, local path, or direct upload.
*   **Audio Transcription:** Convert audio files (MP4) into text.
*   **Text-to-Speech:** Generate audio (MPEG) from text.

## Getting Started

### Prerequisites

*   Java 25
*   Maven
*   An OpenAI API key

### Configuration

1.  Add your OpenAI API key to the `src/main/resources/application.properties` file:

    ```properties
    spring.ai.openai.api-key=<YOUR_API_KEY>
    spring.ai.openai.chat.options.model=gpt-4o
    spring.ai.openai.image.options.model=dall-e-3
    ```

### Running the Application

1.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
2.  Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```
The application will be available at `http://localhost:8080`.

## API Endpoints

### Chat
*   `POST /chat/text`: Get a text completion from the model.

### Image
*   `POST /image/generateImage`: Generate an image from a text prompt.
*   `POST /image/textWithImageUrl`: Analyze an image from a URL.
*   `POST /image/textWithImagePath`: Analyze an image from a local path.
*   `POST /image/textWithImage`: Analyze an uploaded image.

### Audio
*   `POST /audio/transcribe`: Transcribe an audio file.
*   `POST /audio/generateAudio`: Generate an audio file from text.
