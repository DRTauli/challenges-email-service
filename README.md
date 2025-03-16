## Spring Boot Email Service

##  Overview
This is a Spring Boot Java application designed for sending emails using various email service providers, 
including Mailgun, SendGrid, and Mailersend.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Building the Application](#building-the-application)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [Testing](#testing)
- [Logging](#logging)
- [ToDo](#todo)

## Prerequisites
Before you begin, ensure you have the following installed:
- Java 11 or higher
- Maven 3.6 or higher (for building the application)
- An IDE (like IntelliJ IDEA, Eclipse, or Spring Tool Suite) for development (optional)

## Building the Application
To build the application, follow these steps:

1. Clone the repository:

```
   git clone https://github.com/DRTauli/challenges-email-service.git
   cd email-service
```

2. Build the application using Maven:

```
   mvn clean install
```

This command compiles the code and packages the application into a JAR file located in the target directory.

## Running the Application
After building the application, you can run it using the following command:
java -jar target/email-service-0.0.1.jar

## Application Properties
Before running the application, configure your email service properties in src/main/resources/application.properties:

```
    mailgun.apiBaseUrl=https://api.mailgun.net/v3
    mailgun.fromEmail=<YOUR_SENDER_EMAIL>
    mailgun.apiKey=<YOUR_API_KEY>
    mailgun.domain=<YOUR_DOMAIN>
    
    sendgrid.apiBaseUrl=https://api.sendgrid.com/v3/mail/send
    sendgrid.fromEmail=<YOUR_SENDER_EMAIL>
    sendgrid.apiKey=<YOUR_API_KEY>
    
    mailersend.apiBaseUrl=https://api.mailersend.com/v1/email
    mailersend.fromEmail=<YOUR_SENDER_EMAIL>
    mailersend.apiKey=<YOUR_API_KEY>
```

Replace the placeholders with your actual API keys and domains.

## Usage
To use the application, you can make HTTP POST requests to /api/emails/send with the required parameters. 
An example request would look like this:

``` Example
POST /api/emails/send HTTP/1.1
Host: your-app-url
Content-Type: application/x-www-form-urlencoded

subject=Test Subject&body=Hello World&recipient=recipient@example.com&cc=cc@example.com
```

Example using CURL:

```bash
curl -X POST "http://localhost:8080/api/emails/send" \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "subject=Test Subject" \
-d "body=Hello, this is a test email." \
-d "recipient=recipient@example.com" \
-d "cc=cc@example.com,cc2@example.com,cc3@example.com" \
-d "bcc=bcc@example.com"
```

## Testing
You can run tests using Maven. To execute all test cases, use the following command:
```
mvn test
```
The current test cases are general input validations including the fail over mechanism for the email sending.

Currently my MailGun and SendGrid playtest accounts are not working so only mailersend API is being tested for successful email sending.

## Logging
The application uses SLF4J for logging. You can configure logging levels in the application.properties file. 
For example, to set the logging level for the com.siteminder.email package:
logging.level.com.siteminder.email=DEBUG

## TODO
- Setup MailGun and SendGrid configurations or look for other alternatives API like MailerSend.
- Implement HTML email body support.
- Enhance input validation (e.g., check if the recipient's email address has an active status or is not on a suppression list).
- Integrate a logging framework for better debugging and monitoring.
- Create a user interface for easier interaction with the email service.
- more...