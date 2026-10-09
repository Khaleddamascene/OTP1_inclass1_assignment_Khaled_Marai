# Temperature Converter

##  Project Overview and Objectives

Temperature Converter is a Java desktop application for converting temperatures between Celsius, Fahrenheit, and Kelvin. It provides a JavaFX graphical user interface and uses a MariaDB relational database to store conversion records.

The application allows users to enter a temperature, select the source and target units, perform a conversion, save the result, and check whether a temperature is extreme.

The main objectives are to:

- Implement accurate temperature conversion formulas.
- Provide a simple and user-friendly graphical interface.
- Store conversion records in a MariaDB database.
- Retrieve supported temperature units through Data Access Objects (DAOs).
- Identify extreme temperatures using predefined thresholds.
- Initialize database tables and insert default temperature units.
- Handle invalid input and database errors appropriately.

## Technology Stack and Dependencies

| Area | Technology |
|---|---|
| Programming language | Java |
| Build tool | Maven|
| Runtime | JDK 17 or later |
| User interface | JavaFX |
| Database | MariaDB |
| Database connectivity | JDBC |
| Database driver | MariaDB Connector/J |
| Query language | SQL |
| Containerization | Docker |
| Continuous integration | Jenkins |
| Code coverage  | JaCoCo |
| Unit testing framework | JUnit Jupiter |
| Build and dependency management| Maven |
| Development environment | IntelliJ IDEA |
| Version control | Git and GitHub |


The application uses JDBC to communicate with MariaDB. Prepared statements are used for database operations involving supplied values, and try-with-resources statements help ensure that database resources are closed correctly.

## Design and Development Methodology

### Architecture

The application follows a layered architecture that separates the user interface, conversion logic, database access, and data models.

**Presentation layer:** `App` extends the JavaFX application functionality and manages the window, input controls, unit selection, conversion actions, and status messages.

**View layer:** `TemperatureConverterView` is intended to represent the graphical view, but it is currently empty. The actual graphical interface is implemented in `App`.

**Business logic layer:** `TemperatureConverter` implements the temperature conversion formulas and extreme-temperature detection.

**Data-access layer:** `TemperatureUnitDAO` retrieves supported temperature units, while `TempRecordDAO` saves and retrieves conversion records.

**Infrastructure layer:** `DBConnection` establishes database connections and initializes the database schema.

**Model layer:** `TemperatureUnit` represents a temperature unit, while `TempRecord` represents a saved conversion record.

This separation makes the conversion logic easier to maintain and allows database operations to be managed independently from the graphical interface.

### UML and ER Design

The logical component relationships are:

```text
App
 ├── TemperatureConverter
 ├── TemperatureUnitDAO
 │    └── DBConnection
 └── TempRecordDAO
      └── DBConnection

TemperatureUnitDAO
 └── TemperatureUnit

TempRecordDAO
 └── TempRecord
```


The relational database contains two tables.

**temperature_units**

| Column | Description |
|---|---|
| `id` | Primary key |
| `name` | Unique temperature unit name |
| `symbol` | Unit symbol |

**temperature_records**

| Column | Description |
|---|---|
| `id` | Primary key |
| `value` | Original temperature value |
| `unit_id` | Foreign key referencing the original unit |
| `converted_value` | Converted temperature value |
| `converted_unit_id` | Foreign key referencing the destination unit |
| `created_at` | Timestamp of record creation |

The `temperature_units` table stores Celsius, Fahrenheit, and Kelvin. The `temperature_records` table references this table twice: once for the source unit and once for the destination unit.

### Database Access

`TemperatureUnitDAO` provides functionality to retrieve all supported temperature units and find a unit by its ID.

`TempRecordDAO` provides functionality to insert conversion records and retrieve saved records in descending ID order.

`DBConnection` manages database connectivity and database initialization.

The database initialization process creates the tables if they do not exist and inserts the default temperature units using `INSERT IGNORE`.



### Configure MariaDB

The application uses a database named `temperature_converter`.

Create a directory named `sql` in the project root and create a file named `database.sql`.

Add the following SQL script:

```sql
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
```

Execute this script using a MariaDB client or database management tool.

The script creates the database, creates a database user, grants database permissions, creates both tables, and inserts the three default temperature units.

### Configure Database Credentials

The `DBConnection` class reads the following environment variables:

```ini
DB_URL=jdbc:mariadb://localhost:3306/temperature_converter
DB_USER=temperature_user
DB_PASSWORD=temperature_password
```

Configure these variables in your environment or IDE run configuration.

If the environment variables are not set, the provided Java implementation uses its default connection settings. Make sure the URL, username, and password match your MariaDB configuration.

The credentials above are intended for local development only. Do not commit sensitive database credentials to GitHub.



## Automated Testing

Install and configure the following:

- Mockito: supports mocking dependencies.
- JavaFX SDK or correctly configured JavaFX dependencies.
- H2 Database: provides an alternative database for tests.
- JUnit Jupiter: supports unit tests.
- JaCoCo: collects code coverage data and generates reports.
- Maven Surefire Plugin: executes tests during the Maven test phase

To execute the available automated tests, run:
```bash
mvn test
```

To generate the configured JaCoCo report, run:

```bash
mvn verify
```
The coverage report is normally generated at:

target/site/jacoco/index.html



### Build and Compile

Compile the application:

```bash
mvn clean package
```


Run tests only

```bash
mvn test
```

This command compiles the project and checks for compilation errors.

### Run the JavaFX Application

If the project contains the appropriate Maven JavaFX plugin configuration, run:

```bash
mvn javafx:run
```


### Run with Docker

You can run the Temperature Converter application using a prebuilt Docker image from Docker Hub or build the image locally.

Option 1: Run the Image from Docker Hub

Pull the latest image:
```bash
docker pull khlaledmarai/temperature_converter:latest
```

Since this application uses JavaFX for its graphical user interface (GUI), Windows users must configure an X server before running the container.

Run the application on Windows with an X server available:
```bash
docker run --rm \
-e DISPLAY=host.docker.internal:0.0 \
khlaledmarai/temperature_converter:latest
```

Option 2: Build and Run Locally

Make sure Docker Desktop is running, then open a terminal in the project root directory. 

1. Build the Docker image
```bash
docker build -t temperature-converter .
```

2. Run the container

```bash
docker run --rm temperature-converter
```
Note: If the application requires a graphical display, configure the appropriate display settings for your operating system before running the container.

Configure the GUI on Windows

To display the JavaFX application when running Docker on Windows, you need an X server, such as VcXsrv or Xming.

Follow these steps:

Install and start VcXsrv or Xming on Windows.

Configure the X server to accept connections from the Docker container.

Make sure the display number matches the DISPLAY environment variable.

Run the Docker container using the command provided in Option 1.

The environment variable DISPLAY=host.docker.internal:0.0 tells the application to connect to the X server on the Windows host. This hostname is supported by Docker Desktop in typical Windows configurations.

If the GUI does not appear, check the following:

The X server is running.

The display number is correct.

The Windows firewall is not blocking the connection.

The X server's access controls permit the required connection.

The container includes the necessary Java, JavaFX, and X11 libraries.

For security, avoid allowing unrestricted X server connections except when necessary for testing


### Using the Application

1. Launch the application.
2. Enter a temperature value.
3. Choose the source unit.
4. Choose the target unit.
5. Click **Convert** to display the result.
6. Click **Save Record** to save the conversion in MariaDB.
7. Click **Check Extreme Temperature** to classify the temperature.
8. Click **Test Database** to check connectivity.


##  Future Improvements

Possible improvements include:

- Adding automated JUnit tests.
- Displaying conversion history in the graphical interface.
- Rejecting non-finite numeric values and negative Kelvin temperatures.
- Improving database error reporting.
- Separating the GUI, controller, and business logic more completely.
- Using a secure external configuration for database credentials.
- Adding automated build and deployment workflows with Jenkins and Docker.

##  Conclusion

Temperature Converter demonstrates Java programming, JavaFX interface development, temperature conversion formulas, object-oriented design, JDBC connectivity, MariaDB database operations, and exception handling.

The application is designed to convert temperatures between Celsius, Fahrenheit, and Kelvin, identify extreme temperatures, and save conversion records in a relational database.

Further development can improve input validation, automated testing, conversion history, and deployment automation.




### Contact Information:
Khaled Marai.
```
maraim@metropolia.fi
```


