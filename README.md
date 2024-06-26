# Task Manager App
A web application built using React and Spring Boot for managing tasks. Users can create, delete, and update tasks. Additionally, the app authenticates with Google accounts to add tasks to Google Calendar.

## Installation
1. Clone this repository:
   `git clone https://github.com/MohamedNasser8/Task_Manager` 
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
      cd front
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

## Screenshots

### 1. Google Account Integration

![Google Account Integration](/images/google_Access.png)

- **Figure 1:** Integrating a Google account for task synchronization. Ensure that the necessary services (such as Calendar) are enabled in your Google API console.

### 2. Creating a Task Form

#### Phase 1: Email and Title

![Creating a Task - Phase 1](/images/create_task_form.png)

- **Figure 2:** The first phase of creating a task involves entering the email address and task title.

#### Phase 2: Description, Start Date, and End Date

![Creating a Task - Phase 2](/images/create_task_form_2.png)

- **Figure 3:** In the second phase, users provide a task description, start date, and end date.

#### Phase 3: Confirmation

![Confirm](/images/confirm_creation_info.png)


### 3. Main Page

![Main Page](images/added_task.png)

- **Figure 4:** The main page displays all tasks. Tasks synced with Google Calendar are highlighted in green, while unsynced tasks are shown in red.

![Main Page](images/task_not_synced.png)
