Temperature Converter — Individual In-Class Assignment

1. Assignment Description

Problem Statement

The objective of this assignment is to develop a temperature converter application that converts temperatures between Celsius, Fahrenheit, and Kelvin. The application also stores conversion records in a MariaDB database and identifies extreme temperatures.

Key Requirements

Convert temperatures between Celsius, Fahrenheit, and Kelvin.

Provide a graphical user interface (GUI) using JavaFX.

Store temperature conversion records in MariaDB.

Retrieve supported temperature units using Data Access Objects (DAOs).

Identify extreme temperatures using predefined thresholds.

Initialize database tables and insert default temperature units.

Handle invalid user input and database errors.

2. Technologies and Tools

Technology

Purpose

Java

Main programming language

JavaFX

Graphical user interface

JDBC

Database connectivity

MariaDB

Database management

SQL

Database and table creation

Maven (if configured)

Dependency management and build automation

IntelliJ IDEA

Development and debugging

Git and GitHub

Version control and submission

3. Project Architecture

The application uses a layered architecture that separates the user interface, conversion logic, database access, and data models.

App: Main JavaFX application and event handling.

TemperatureConverter: Temperature conversion formulas and extreme-temperature detection.

TemperatureConverterView: View class, currently empty in the provided implementation.

TemperatureUnit: Model representing a temperature unit.

TempRecord: Model representing a saved conversion.

TemperatureUnitDAO: Retrieves temperature units from the database.

TempRecordDAO: Saves and retrieves conversion records.

DBConnection: Establishes database connections and initializes the schema.


4. Temperature Conversion

The application supports conversions between Celsius, Fahrenheit, and Kelvin.

Conversion formulas

Fahrenheit to Celsius

C = (F - 32) × 5 / 9

Celsius to Fahrenheit

F = (C × 9 / 5) + 32

Kelvin to Celsius

C = K - 273.15

Celsius to Kelvin

K = C + 273.15

The application converts the source temperature to Celsius and then converts it to the target unit. If both units are the same, the original value is returned.

Extreme-temperature detection

A temperature is considered extreme if it is below -40°C or above 50°C.



6. Database Setup

Prerequisites

Before starting, make sure that:

MariaDB is installed and running.

You have permission to create a database and database user.

A MariaDB client or database management tool is available.

6.1 Create the SQL file

Create a directory named sql in the project root and create a file named database.sql.

Paste the following script into the file:

CREATE DATABASE IF NOT EXISTS temperature_converter;

CREATE USER IF NOT EXISTS 'temperature_user'@'localhost'
IDENTIFIED BY 'temperature_password';

GRANT ALL PRIVILEGES ON temperature_converter.*
TO 'temperature_user'@'localhost';

FLUSH PRIVILEGES;

USE temperature_converter;

CREATE TABLE IF NOT EXISTS temperature_units (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(30) NOT NULL UNIQUE,
symbol VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS temperature_records (
id INT PRIMARY KEY AUTO_INCREMENT,
value DOUBLE NOT NULL,
unit_id INT NOT NULL,
converted_value DOUBLE NOT NULL,
converted_unit_id INT NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (unit_id) REFERENCES temperature_units(id),
FOREIGN KEY (converted_unit_id) REFERENCES temperature_units(id)
);

INSERT IGNORE INTO temperature_units (id, name, symbol)
VALUES
(1, 'Celsius', '°C'),
(2, 'Fahrenheit', '°F'),
(3, 'Kelvin', 'K');




6.2 Execute the SQL script

Start the MariaDB server.

Open your MariaDB client or database management tool.

Execute the contents of sql/database.sql.

Verify that the temperature_converter database has been created.

Confirm that both tables exist and that the three default temperature units have been inserted.

The SQL script creates a database user named temperature_user with the password temperature_password.

Note: These are development credentials. Do not use them for a publicly accessible or production database.

7. Database Configuration

The Java application should connect using the same credentials created by the SQL script.

Configure the following environment variables:

DB_URL=jdbc:mariadb://localhost:3306/temperature_converter
DB_USER=temperature_user
DB_PASSWORD=temperature_password

If environment variables are not configured, the application may use default credentials defined in DBConnection.java. Make sure those defaults match your database configuration.

Do not commit real passwords or sensitive credentials to GitHub.

8. How to Run the Application

8.1 Prerequisites

Install and configure:

JDK 17 or later.

JavaFX SDK or the required JavaFX dependencies.

MariaDB server.

MariaDB JDBC driver.

IntelliJ IDEA or another compatible Java IDE.

Maven, if the project uses Maven.

8.2 Run using Maven

If the project contains a correctly configured pom.xml, open a terminal in the project root and run:

mvn clean compile
mvn javafx:run

These commands require the JavaFX Maven plugin and dependencies to be configured in the project.

If Maven is not configured, set up JavaFX and the MariaDB JDBC driver in your IDE, then run the main application class, such as org.example.Main or org.example.App, depending on the project structure.



7. Conclusion

This assignment implements a JavaFX temperature converter integrated with a MariaDB database. It demonstrates object-oriented programming, temperature conversion formulas, GUI event handling, JDBC database access, SQL table relationships, prepared statements, and exception handling.

The application supports conversions between Celsius, Fahrenheit, and Kelvin, extreme-temperature detection, and saving conversion records. Further improvements could include displaying conversion history, adding automated tests, improving input validation, and separating the GUI from the application controller.

8. Repository

GitHub repository for submission:

OTP1_inclass1_assignment_Khaled_Marai