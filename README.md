# Temperature Converter

## 1. Project Overview and Objectives

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

## 2. Technology Stack and Dependencies

| Area | Technology |
|---|---|
| Programming language | Java |
| Runtime | JDK 17 or later |
| User interface | JavaFX |
| Database | MariaDB |
| Database connectivity | JDBC |
| Database driver | MariaDB Connector/J |
| Query language | SQL |
| Build tool | Maven, if configured in the project |
| Development environment | IntelliJ IDEA or another Java IDE |
| Version control | Git and GitHub |

The application uses JDBC to communicate with MariaDB. Prepared statements are used for database operations involving supplied values, and try-with-resources statements help ensure that database resources are closed correctly.

## 3. Design and Development Methodology

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

### Temperature Conversion Logic

The application converts the source temperature to Celsius and then converts Celsius to the requested target unit.

The formulas are:

**Fahrenheit to Celsius**

```text
C = (F - 32) × 5 / 9
```

**Celsius to Fahrenheit**

```text
F = (C × 9 / 5) + 32
```

**Kelvin to Celsius**

```text
C = K - 273.15
```

**Celsius to Kelvin**

```text
K = C + 273.15
```

If the source and target units are identical, the original temperature value is returned.

The `isExtremeTemperature()` method considers a temperature extreme when it is below -40°C or above 50°C.

### Database Access

`TemperatureUnitDAO` provides functionality to retrieve all supported temperature units and find a unit by its ID.

`TempRecordDAO` provides functionality to insert conversion records and retrieve saved records in descending ID order.

`DBConnection` manages database connectivity and database initialization.

The database initialization process creates the tables if they do not exist and inserts the default temperature units using `INSERT IGNORE`.

### Development Methodology

Development is organized around separate application components:

1. Implement the conversion formulas.
2. Create the model classes.
3. Implement database connection and initialization.
4. Implement the DAO classes.
5. Build the JavaFX interface.
6. Connect the interface to the conversion and database layers.
7. Perform manual tests for conversion accuracy, database functionality, and error handling.

## 4. Functional Testing

Testing should cover the conversion logic, user interface, database operations, and error handling.

The test cases below describe the expected behavior. They should only be marked as passed after the application has been executed and the results verified.

### TemperatureConverter Testing

| Test case | Input | Expected result |
|---|---|---|
| Celsius to Fahrenheit | 0°C | 32.00°F |
| Celsius to Kelvin | 0°C | 273.15 K |
| Fahrenheit to Celsius | 32°F | 0.00°C |
| Kelvin to Celsius | 273.15 K | 0.00°C |
| Same-unit conversion | 25°C to Celsius | 25.00°C |
| Negative temperature | -40°C to Fahrenheit | -40.00°F |
| Extreme temperature | 60°C | Extreme |
| Non-extreme temperature | 20°C | Not extreme |
| Invalid numeric input | `abc` | An appropriate error message |

### Database DAO Testing

| Test case | Expected result |
|---|---|
| Database initialization | Required tables are created |
| Retrieve all temperature units | Three default units are returned |
| Find unit by ID | The matching unit is returned |
| Find unknown unit | The appropriate error or missing-unit result is handled |
| Save conversion record | The record is inserted successfully |
| Retrieve conversion records | Saved records are returned in descending ID order |
| Foreign key constraints | References to nonexistent units are rejected |
| Database connection failure | An appropriate database error is reported |

### Manual Testing Procedure

1. Start the MariaDB server.
2. Execute the SQL initialization script.
3. Configure the database credentials.
4. Launch the JavaFX application.
5. Verify that the source and destination dropdown menus contain the supported units.
6. Enter a temperature and select the source and target units.
7. Click **Convert** and compare the result with the expected value.
8. Click **Save Record** and verify that the conversion is stored.
9. Click **Check Extreme Temperature** and verify the classification.
10. Click **Test Database** and confirm that the database connection works.
11. Enter invalid text and verify that an error message appears.
12. Test the application with the database unavailable and verify that errors are handled appropriately.

### Test Results

The following table should be updated after testing.

| Test category | Result |
|---|---|
| Temperature conversion | Not tested |
| Invalid input handling | Not tested |
| Extreme-temperature detection | Not tested |
| Database initialization | Not tested |
| Temperature unit retrieval | Not tested |
| Saving conversion records | Not tested |
| Database connectivity | Not tested |

No automated testing framework is included in the described implementation. JUnit can be introduced to test the conversion logic and database operations automatically.

## 5. Setup and Execution Instructions

### Prerequisites

Install and configure the following:

- JDK 17 or later.
- JavaFX SDK or correctly configured JavaFX dependencies.
- MariaDB server.
- MariaDB JDBC driver.
- IntelliJ IDEA or another Java IDE.
- Maven, if the project is configured to use it.

Verify the Java and Maven installations:

```bash
java -version
mvn -version
```

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

### Build and Compile

If Maven is configured in the project, run the following commands from the repository root:

```bash
mvn clean compile
```

This command compiles the project and checks for compilation errors.

### Run the JavaFX Application

If the project contains the appropriate Maven JavaFX plugin configuration, run:

```bash
mvn javafx:run
```

Alternatively, configure the JDK, JavaFX libraries, and MariaDB JDBC driver in your IDE, then run the main application class, such as `org.example.Main` or `org.example.App`, depending on the project structure.

### Using the Application

1. Launch the application.
2. Enter a temperature value.
3. Choose the source unit.
4. Choose the target unit.
5. Click **Convert** to display the result.
6. Click **Save Record** to save the conversion in MariaDB.
7. Click **Check Extreme Temperature** to classify the temperature.
8. Click **Test Database** to check connectivity.

## 6. Known Limitations

- Automated unit tests are not included in the described implementation.
- `TemperatureConverterView` is empty; the actual GUI is implemented in `App`.
- The DAO can retrieve saved conversion records, but the current GUI does not display conversion history.
- Numeric input validation may not reject `NaN`, infinity, or physically invalid negative Kelvin values.
- The Java database defaults may not match the credentials created by the SQL initialization script.
- The application requires a running MariaDB server and correctly initialized tables for database operations.
- Maven execution depends on the project having the necessary JavaFX plugin and dependency configuration.

## 7. Future Improvements

Possible improvements include:

- Adding automated JUnit tests.
- Displaying conversion history in the graphical interface.
- Rejecting non-finite numeric values and negative Kelvin temperatures.
- Improving database error reporting.
- Separating the GUI, controller, and business logic more completely.
- Using a secure external configuration for database credentials.
- Adding automated build and deployment workflows with Jenkins and Docker.

## 8. Conclusion

Temperature Converter demonstrates Java programming, JavaFX interface development, temperature conversion formulas, object-oriented design, JDBC connectivity, MariaDB database operations, and exception handling.

The application is designed to convert temperatures between Celsius, Fahrenheit, and Kelvin, identify extreme temperatures, and save conversion records in a relational database.

Further development can improve input validation, automated testing, conversion history, and deployment automation.

## 9. GitHub Repository

[OTP1_inclass1_assignment_Khaled_Marai](https://github.com/Khaleddamascene/OTP1_inclass1_assignment_Khaleddamascene)
