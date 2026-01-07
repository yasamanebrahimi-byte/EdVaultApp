# EdVault

A comprehensive JavaFX-based educational management system for managing student profiles, programming languages, and collaborative comments.

## Overview

EdVault is a desktop application designed to streamline the management of student information, track programming language proficiency, and facilitate collaborative feedback through a comment system. Built with JavaFX, it provides an intuitive GUI for educators and administrators to maintain and organize educational data efficiently.

## Features

- **Student Profile Management**: Create, view, edit, and manage detailed student profiles
- **Programming Language Tracking**: Maintain a database of programming languages and track student proficiency
- **Comment System**: Add and view collaborative comments on student profiles
- **Search Functionality**: Quickly search for students and other resources
- **Reports View**: Generate and view reports on student data
- **Data Persistence**: Store student and programming language information in JSON format

## Technology Stack

- **Language**: Java 11+
- **Framework**: JavaFX 21.0.6
- **Build Tool**: Maven
- **Data Format**: JSON
- **Testing**: JUnit 5

## Project Structure

```
EdVault/
├── src/main/
│   ├── java/cs151/
│   │   ├── application/
│   │   │   └── Main.java                    # Application entry point
│   │   ├── controller/
│   │   │   ├── CommentController.java
│   │   │   ├── ProgrammingLanguageController.java
│   │   │   ├── SearchController.java
│   │   │   └── StudentProfileController.java
│   │   ├── model/
│   │   │   ├── Comment.java
│   │   │   ├── Student.java
│   │   │   ├── ProgrammingLanguageRepository.java
│   │   │   └── StudentRepository.java
│   │   ├── util/
│   │   │   ├── AppUtils.java
│   │   │   ├── Database.java
│   │   │   ├── UICreator.java
│   │   │   └── ValidationUtils.java
│   │   └── view/
│   │       ├── HomePageView.java
│   │       ├── NewStudentProfileView.java
│   │       ├── OneStudentProfileView.java
│   │       ├── EditStudentProfileView.java
│   │       ├── AddProgrammingLanguageView.java
│   │       ├── AllAddedProgrammingLanguagesView.java
│   │       ├── AddCommentView.java
│   │       ├── AllCommentsView.java
│   │       ├── SearchView.java
│   │       ├── SearchResultView.java
│   │       └── ReportsView.java
│   └── data/
│       ├── students.json
│       └── languages.txt
├── pom.xml
└── README.md
```

## Architecture

EdVault follows the **Model-View-Controller (MVC)** design pattern:

- **Model**: Contains data classes (`Student`, `Comment`) and repository classes for data management
- **View**: JavaFX views that handle the user interface and user interactions
- **Controller**: Manages business logic and coordinates between views and models
- **Utility**: Helper classes for database operations, UI creation, validation, and general utilities

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Installation

1. Clone or download the project:

   ```bash
   cd EdVault
   ```

2. Build the project using Maven:

   ```bash
   mvn clean install
   ```

3. Run the application:

   ```bash
   mvn javafx:run
   ```

   Or compile and run directly:

   ```bash
   mvn clean package
   java -jar target/EdVault-1.0-SNAPSHOT.jar
   ```

## Usage

### Home Page

- Launch the application to see the welcome page
- Access all main features from the navigation menu

### Student Management

- **Add New Student**: Create a new student profile with personal information
- **View Student**: Search and view existing student profiles
- **Edit Student**: Modify student information
- **Delete Student**: Remove a student from the system

### Programming Languages

- **Add Language**: Add new programming languages to the database
- **View Languages**: Browse all available programming languages
- **Manage Proficiency**: Track which students have proficiency in specific languages

### Comments

- **Add Comment**: Leave feedback on student profiles
- **View Comments**: View all comments associated with a student
- **Track Feedback**: Monitor collaborative feedback and comments

### Search

- Use the search functionality to quickly find students by name, ID, or other attributes
- View search results with direct access to student profiles

### Reports

- Generate reports on student information
- View analytics and summaries of student data

## Data Files

- **students.json**: Stores all student profile information in JSON format
- **languages.txt**: Contains the list of available programming languages

## Dependencies

### Runtime

- `javafx-controls` (21.0.6)
- `javafx-fxml` (21.0.6)
- `org.json` (20240303)

### Testing

- `junit-jupiter-api` (5.12.1)
- `junit-jupiter-engine` (5.12.1)

## Build Information

- **Source Encoding**: UTF-8
- **Maven Compiler**: Configured for Java compilation
- **Artifact ID**: EdVault
- **Version**: 1.0-SNAPSHOT

---
