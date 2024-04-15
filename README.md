# Task Manager App
A web application built using React and Spring Boot for managing tasks. Users can create, delete, and update tasks. Additionally, the app authenticates with Google accounts to add tasks to Google Calendar.

## Installation
1. Clone this repository:
   `git clone https://github.com/your-username/task-manager-app.git` 
2. cd task-manager

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

- Java Development Kit (JDK)
- Node.js and npm

### Installation

1. **Spring Boot Application**:
    - Navigate to the root directory of the Java Spring Boot application.
    - Open the `com/task_manager/task/TaskApplication.java` file.
    - Run the application:
      ```
      ./mvnw spring-boot:run
      ```
    - The server will start, typically on `http://localhost:8080`.

2. **React Application**:
    - Change directory to the React app's pipeline:
      ```
      cd pipeline/reactapp
      ```
    - Install the necessary npm packages:
      ```
      npm install
      ```
    - Start the React development server:
      ```
      npm start
      ```
    - The application will be available at `http://localhost:3000`.

### Google Calendar Integration

Ensure you have set up your Google API credentials and added them to your environment variables to enable task synchronization with Google Calendar.

### Usage

Once both the Spring Boot and React applications are running, you can create, update, and delete tasks through the user interface. Authentication with a Google account is required to add tasks to your Google Calendar.
